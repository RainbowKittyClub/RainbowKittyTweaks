package club.rainbowkitty.tweaks.module;

import club.rainbowkitty.tweaks.rule.CachedIntRule;
import club.rainbowkitty.tweaks.state.MobRegenState;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;

/**
 * Module to provide passive regeneration for iron golems via a game rule.
 * When enabled, iron golems will slowly regenerate health over time if they
 * have taken damage, with configurable regeneration rate and delay.
 * Controlled via the {@code ironGolemRegenTicks} and {@code ironGolemRegenDelay} gamerules.
 * 
 * The regeneration process follows these steps:
 * <ol>
 *   <li>Check if enough time has passed since last damage</li>
 *   <li>If delay period has passed, increment regeneration counter</li>
 *   <li>When counter reaches the configured rate, heal 1 health point</li>
 *   <li>Reset counter and update last health tracking</li>
 * </ol>
 */
public final class IronGolemPassiveRegen {
    private static final IronGolemPassiveRegen INSTANCE = new IronGolemPassiveRegen();
    private static boolean initialized = false; 

    public static final CachedIntRule IRON_GOLEM_REGENERATION_RATE = new CachedIntRule(
        "ironGolemRegenTicks",
        60,
        0,
        Integer.MAX_VALUE
    );
    public static final CachedIntRule IRON_GOLEM_REGENERATION_DELAY = new CachedIntRule(
        "ironGolemRegenDelay",
        200,
        0,
        Integer.MAX_VALUE
    );

    private IronGolemPassiveRegen() {}

    public static void init() {
        if (initialized) return;
        initialized = true;
    }

    public static IronGolemPassiveRegen getInstance() {
        return INSTANCE;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static void ironGolemRegenTick(MobEntity self, ServerWorld world, MobRegenState state) {
        int ticksPerHeal = IronGolemPassiveRegen.IRON_GOLEM_REGENERATION_RATE.getValue(world);
        if (ticksPerHeal <= 0) return;

        long regenDelay = IronGolemPassiveRegen.IRON_GOLEM_REGENERATION_DELAY.getValue(world);

        // Update time since last damage: reset on hurt frames, otherwise increment and persist.
        LivingEntity living = (LivingEntity) self;
        if (living.hurtTime > 0) {
            state.setTimeSinceDamage(0L);
        } else {
            state.setTimeSinceDamage(state.getTimeSinceDamage() + 1);
        }

        // If we're still in the damage delay period, reset regen counter and skip healing
        if (state.getTimeSinceDamage() < regenDelay) {
            state.setRegenCounter(0);
            return;
        }

        // Handle regeneration if below max health
        if (self.isAlive() && self.getHealth() < self.getMaxHealth()) {
            int c = state.getRegenCounter() + 1;
            if (c >= ticksPerHeal) {
                state.setRegenCounter(0);
                self.heal(1.0F);
            } else {
                state.setRegenCounter(c);
            }
        } else {
            state.setRegenCounter(0);
        }
    }
}