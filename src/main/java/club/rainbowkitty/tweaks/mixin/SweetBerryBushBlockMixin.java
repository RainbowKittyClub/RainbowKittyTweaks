package club.rainbowkitty.tweaks.mixin;

import club.rainbowkitty.tweaks.module.LegArmorProtectsFromBerryThorns;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SweetBerryBushBlock.class)
public class SweetBerryBushBlockMixin {
    
    /**
     * Wraps the entity damage operation to provide armor-based protection against berry bush thorns.
     * 
     * <p>This method intercepts damage calls from berry bush collisions and evaluates whether
     * the target entity should be protected from damage. The damage call is skipped based on
     * the result of {@link LegArmorProtectsFromBerryThorns#shouldCancelDamage}.
     * 
     * @param targetEntity The entity that collided with the berry bush
     * @param world The server world where the collision occurred
     * @param source The damage source (berry bush thorns)
     * @param amount The amount of damage that would be dealt
     * @param original The original damage operation that would normally execute
     * @return {@code false} if damage was prevented by armor, otherwise the result of normal damage processing
     */
    @WrapOperation(
            method = "onEntityCollision",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z"
            )
    )
    private boolean rainbowkittytweaks$preventBerryThornDamageIfWearingLegArmor(Entity targetEntity, ServerWorld world, DamageSource source, float amount, Operation<Boolean> original) {
        return LegArmorProtectsFromBerryThorns.shouldCancelDamage(targetEntity, world)
                ? false
                : original.call(targetEntity, world, source, amount);
    }
}