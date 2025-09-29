package club.rainbowkitty.tweaks.mixin;

import club.rainbowkitty.tweaks.module.FireArrowsLightFire;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends ProjectileEntity {
    
    /**
     * Required constructor for extending ProjectileEntity.
     * This constructor is never called directly but is required for mixin compilation.
     * 
     * @param entityType The type of projectile entity
     * @param world The world the entity exists in
     */
    protected PersistentProjectileEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * Injects fire ignition logic when arrows hit blocks.
     * 
     * <p>This method is called after the original {@code onBlockHit} method completes.
     * See {@link FireArrowsLightFire#onArrowBlockHit} for implementation details.
     * 
     * @param hit The block hit result containing impact information
     * @param ci The callback info from the mixin injection
     */
    @Inject(method = "onBlockHit", at = @At("TAIL"))
    private void rainbowkittytweaks$igniteOnFireArrowHit(BlockHitResult hit, CallbackInfo ci) {
        if (hit.getType() == HitResult.Type.MISS) return;
        PersistentProjectileEntity self = (PersistentProjectileEntity) (Object) this;
        if (!(self.getWorld() instanceof ServerWorld world)) return;

        FireArrowsLightFire.onArrowBlockHit(self, world, hit);
    }
}