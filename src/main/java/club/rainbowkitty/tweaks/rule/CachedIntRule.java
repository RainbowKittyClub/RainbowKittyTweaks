package club.rainbowkitty.tweaks.rule;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;

/**
 * A cached integer game rule that provides efficient access to integer game rule values.
 * This class automatically handles game rule registration, caching, and cache invalidation
 * when the rule value changes.
 * 
 * <p>The rule is registered in the MISC category and automatically refreshes the cache
 * across all server worlds when its value is modified. Supports optional minimum and
 * maximum value constraints.
 */
public class CachedIntRule extends AbstractCachedGameRule<Integer> {
    
    /**
     * Creates a new cached integer game rule with the specified key and default value.
     * Uses the full integer range (Integer.MIN_VALUE to Integer.MAX_VALUE) as constraints.
     * 
     * @param key The game rule key name
     * @param defaultValue The default value for the game rule
     */
    public CachedIntRule(String key, Integer defaultValue) {
        this(key, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Creates a new cached integer game rule with the specified key, default value,
     * and value constraints.
     * 
     * @param key The game rule key name
     * @param defaultValue The default value for the game rule
     * @param min The minimum allowed value (inclusive)
     * @param max The maximum allowed value (inclusive)
     */
    public CachedIntRule(String key, Integer defaultValue, int min, int max) {
        super(key, Integer.class, defaultValue);
        GameRules.Key<GameRules.IntRule> intRuleKey = GameRuleRegistry.register(key, GameRules.Category.MISC, GameRuleFactory.createIntRule(defaultValue, min, max, (server, rule) -> {
            if (server != null) {
                for (ServerWorld world : server.getWorlds()) {
                    GameRuleCache.refreshWorld(world);
                }
            }
        }));
        this.gameRuleKey = intRuleKey;
        GameRuleCache.register(key, Integer.class, w -> w.getGameRules().getInt(intRuleKey));
    }
}
