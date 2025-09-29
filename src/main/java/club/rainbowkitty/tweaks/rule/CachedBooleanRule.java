package club.rainbowkitty.tweaks.rule;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;

/**
 * A cached boolean game rule that provides efficient access to boolean game rule values.
 * This class automatically handles game rule registration, caching, and cache invalidation
 * when the rule value changes.
 * 
 * <p>The rule is registered in the MISC category and automatically refreshes the cache
 * across all server worlds when its value is modified.
 */
public class CachedBooleanRule extends AbstractCachedGameRule<Boolean> {
   
    /**
     * @param key The key as used in `/gamerule` commands
     * @param defaultValue The default value for the game rule
     */
    public CachedBooleanRule(String key, Boolean defaultValue) {
        super(key, Boolean.class, defaultValue);
        GameRules.Key<GameRules.BooleanRule> booleanRuleKey = GameRuleRegistry.register(key, GameRules.Category.MISC, GameRuleFactory.createBooleanRule(defaultValue, (server, rule) -> {
            if (server != null) {
                for (ServerWorld world : server.getWorlds()) {
                    GameRuleCache.refreshWorld(world);
                }
            }
        }));
        this.gameRuleKey = booleanRuleKey;
        GameRuleCache.register(key, Boolean.class, w -> w.getGameRules().getBoolean(booleanRuleKey));
    }
}
