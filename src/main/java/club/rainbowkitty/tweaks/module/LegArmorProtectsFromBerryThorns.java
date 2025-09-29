package club.rainbowkitty.tweaks.module;

import club.rainbowkitty.tweaks.rule.CachedBooleanRule;

/**
 * Module to toggle whether leg armor prevents damage from sweet berry bush thorns.
 * Controlled via the {@code legArmorProtectsFromBerryThorns} gamerule.
 *
 * See mixin {@link club.rainbowkitty.tweaks.mixin.SweetBerryBushBlockMixin} for behavior.
 */
public final class LegArmorProtectsFromBerryThorns {
    private static final LegArmorProtectsFromBerryThorns INSTANCE = new LegArmorProtectsFromBerryThorns();
    private static boolean initialized = false;

    public static final CachedBooleanRule ALLOW_LEG_ARMOR_PROTECTS_FROM_BERRY_THORNS = new CachedBooleanRule(
        "legArmorProtectsFromBerryThorns",
        true
    );

    private LegArmorProtectsFromBerryThorns() {}

    public static void init() {
        if (initialized) return;
        initialized = true;
    }

    public static LegArmorProtectsFromBerryThorns getInstance() {
        return INSTANCE;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    /**
     * Evaluate whether berry bush thorn damage should be cancelled for the given entity in the
     * provided world.
     *
     * @param targetEntity the entity that would take damage
     * @param world the server world where the event originates
     * @return true if damage should be cancelled, false otherwise
     */
    public static boolean shouldCancelDamage(net.minecraft.entity.Entity targetEntity, net.minecraft.server.world.ServerWorld world) {
        if (ALLOW_LEG_ARMOR_PROTECTS_FROM_BERRY_THORNS.getValue(world)) {
            // Players with leg armor are protected
            if (targetEntity instanceof net.minecraft.entity.player.PlayerEntity player) {
                return player.getEquippedStack(net.minecraft.entity.EquipmentSlot.LEGS).isIn(net.minecraft.registry.tag.ItemTags.LEG_ARMOR);
            }

            // Horses with body armor are protected
            if (targetEntity instanceof net.minecraft.entity.passive.HorseEntity horse) {
                return !horse.getEquippedStack(net.minecraft.entity.EquipmentSlot.BODY).isEmpty();
            }

            // Wolves with body armor are protected
            if (targetEntity instanceof net.minecraft.entity.passive.WolfEntity wolf) {
                return !wolf.getEquippedStack(net.minecraft.entity.EquipmentSlot.BODY).isEmpty();
            }

            // Other mobs with leg armor are protected
            if (targetEntity instanceof net.minecraft.entity.mob.MobEntity mob) {
                return mob.getEquippedStack(net.minecraft.entity.EquipmentSlot.LEGS).isIn(net.minecraft.registry.tag.ItemTags.LEG_ARMOR);
            }
        }

        // Protection disabled or entity not wearing appropriate armor - proceed with normal damage
        return false;
    }
}
