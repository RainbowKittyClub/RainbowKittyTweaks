package club.rainbowkitty.tweaks.rule;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * A performance-oriented caching system for game rule values across server worlds.
 * This class provides efficient access to game rule values by caching computed values
 * per world, avoiding repeated game rule lookups during gameplay.
 * 
 * <p>The cache maintains a registry of game rule providers and stores computed values
 * on a per-world basis. When game rules change, the affected worlds can have their
 * caches refreshed to ensure consistency.
 * 
 * <p>This class is thread-safe for registration operations but cache access should
 * be performed on the server thread.
 */
public final class GameRuleCache {
    private GameRuleCache() {}

    // Registration of keys -> provider(ServerWorld -> value)
    private static final Map<String, RegisteredValue<?>> REGISTRY = new LinkedHashMap<>();

    // Per-world cache of key -> value
    private static final Object2ObjectMap<ServerWorld, Map<String, Object>> CACHE = new Object2ObjectOpenHashMap<>();

    /**
     * Initializes the cache for all worlds on the given server.
     * This should be called during server startup to populate initial cache values.
     * 
     * @param server The minecraft server instance
     */
    public static void init(MinecraftServer server) {
        server.getWorlds().forEach(GameRuleCache::refreshWorld);
    }

    /**
     * Refreshes the cache for all worlds on the given server.
     * This should be called when game rules have been modified globally.
     * 
     * @param server The minecraft server instance
     */
    public static void refreshAll(MinecraftServer server) {
        server.getWorlds().forEach(GameRuleCache::refreshWorld);
    }

    /**
     * Refreshes the cache for a specific world by recomputing all registered values.
     * This should be called when game rules have changed for a particular world.
     * 
     * @param world The server world to refresh cache for
     */
    public static void refreshWorld(ServerWorld world) {
        Map<String, Object> map = new LinkedHashMap<>(REGISTRY.size());
        for (Map.Entry<String, RegisteredValue<?>> e : REGISTRY.entrySet()) {
            map.put(e.getKey(), e.getValue().compute(world));
        }
        CACHE.put(world, map);
    }

    /**
     * Registers a game rule provider for caching.
     * The provider function will be called to compute the current value for each world.
     * Registration is ignored if a rule with the same key is already registered.
     * 
     * @param <T> The type of value the rule provides
     * @param key The unique key for this game rule
     * @param type The class type of the rule value
     * @param provider A function that computes the rule value for a given world
     */
    public static synchronized <T> void register(String key, Class<T> type, Function<ServerWorld, T> provider) {
        if (REGISTRY.containsKey(key)) return; // ignore duplicate
        REGISTRY.put(key, new RegisteredValue<>(type, provider));
    }

    /**
     * Gets a cached game rule value for the specified world and key.
     * If the world's cache is not initialized, it will be refreshed automatically.
     * If the value is not found or is of the wrong type, the default value is returned.
     * 
     * @param <T> The expected type of the rule value
     * @param world The server world to get the value for
     * @param key The game rule key
     * @param type The expected class type of the value
     * @param defaultValue The default value to return if lookup fails
     * @return The cached rule value or the default value
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(ServerWorld world, String key, Class<T> type, T defaultValue) {
        Map<String, Object> map = CACHE.get(world);
        if (map == null) {
            refreshWorld(world);
            map = CACHE.get(world);
            if (map == null) return defaultValue;
        }
        Object val = map.get(key);
        if (val == null) return defaultValue;
        if (!type.isInstance(val)) return defaultValue;
        return (T) val;
    }

    /**
     * Convenience method to get a boolean game rule value.
     * 
     * @param world The server world to get the value for
     * @param key The game rule key
     * @param defaultValue The default value to return if lookup fails
     * @return The cached boolean value or the default value
     */
    public static boolean getBoolean(ServerWorld world, String key, boolean defaultValue) {
        Boolean v = get(world, key, Boolean.class, null);
        return v != null ? v : defaultValue;
    }

    /**
     * Convenience method to get an integer game rule value.
     * 
     * @param world The server world to get the value for
     * @param key The game rule key
     * @param defaultValue The default value to return if lookup fails
     * @return The cached integer value or the default value
     */
    public static int getInt(ServerWorld world, String key, int defaultValue) {
        Integer v = get(world, key, Integer.class, null);
        return v != null ? v : defaultValue;
    }

    /**
     * Internal record that holds type information and a provider function
     * for computing game rule values.
     * 
     * @param <T> The type of value this registration provides
     * @param type The class type of the rule value
     * @param provider The function that computes the rule value
     */
    private record RegisteredValue<T>(Class<T> type, Function<ServerWorld, T> provider) {
        /**
         * Computes the rule value for the given world using the registered provider.
         * 
         * @param world The server world to compute the value for
         * @return The computed rule value
         */
        T compute(ServerWorld world) { return provider.apply(world); }
    }
}
