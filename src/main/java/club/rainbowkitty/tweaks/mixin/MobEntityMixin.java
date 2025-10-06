package club.rainbowkitty.tweaks.mixin;

import club.rainbowkitty.tweaks.module.IronGolemPassiveRegen;
import club.rainbowkitty.tweaks.state.MobRegenState;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public class MobEntityMixin {

    @Unique
    private MobRegenState rainbowkittyTweaks$mobRegenState = new MobRegenState(0L, 0);

    /**
     * Implements passive regeneration logic during the mob's tick cycle.
     * 
     * <p>This method currently only affects iron golems.
     * See {@link IronGolemPassiveRegen#ironGolemRegenTick} for details.
     * 
     * @param ci The callback info from the mixin injection
     */
    @Inject(method = "tick", at = @At("TAIL"))
    private void rainbowkittytweaks$ironGolemRegenEffect(CallbackInfo ci) {
        MobEntity self = (MobEntity) (Object) this;        
        if (!(self.getEntityWorld() instanceof ServerWorld world)) return;
        
        // Only apply regeneration to iron golems for now
        if ((self instanceof IronGolemEntity)) IronGolemPassiveRegen.ironGolemRegenTick(self, world, this.rainbowkittyTweaks$mobRegenState);
    }
}
