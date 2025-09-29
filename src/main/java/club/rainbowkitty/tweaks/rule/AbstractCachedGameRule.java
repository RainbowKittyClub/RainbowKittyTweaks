package club.rainbowkitty.tweaks.rule;

import net.minecraft.world.GameRules;

/**
 * Abstract base class for cached game rules that provides common functionality
 * for managing game rule metadata and default behavior.
 * 
 * @param <T> The type of value this game rule stores (Boolean, Integer, etc.)
 */
public abstract class AbstractCachedGameRule<T> {
    protected GameRules.Key<?> gameRuleKey;
    protected final Class<T> returnType;
    protected final T defaultValue;

    /**
     * Creates a new abstract cached game rule with the specified metadata.
     * 
     * @param key The game rule key name
     * @param type The class type of the rule value
     * @param defaultValue The default value for the game rule
     */
    public AbstractCachedGameRule(String key, Class<T> type, T defaultValue) {
        this.returnType = type;
        this.defaultValue = defaultValue;
    }

    /**
     * Gets the key name of this game rule.
     * 
     * @return The game rule key string
     */
    public String getKey() {
        return gameRuleKey != null ? gameRuleKey.getName() : null;
    }

    /**
     * Gets the type class of this game rule's value.
     * 
     * @return The class type of the rule value
     */
    public Class<T> getReturnType() {
        return returnType;
    }

    /**
     * Gets the default value for this game rule.
     * 
     * @return The default value used when the rule is not set
     */
    public T getDefaultValue() {
        return defaultValue;
    }

    /**
     * Gets the current value of this game rule for the specified world.
     * This is the default implementation that uses the game rule cache.
     * Concrete implementations may override this for specialized behavior.
     * 
     * @param world The server world to get the rule value for
     * @return The current value of this game rule
     */
    public T getValue(net.minecraft.server.world.ServerWorld world) {
        return GameRuleCache.get(world, this.getKey(), returnType, defaultValue);
    }
}
