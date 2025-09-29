package club.rainbowkitty.tweaks.mixin;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.CommandBlockExecutor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import club.rainbowkitty.tweaks.module.CommandBlockToggle;

@Mixin(CommandBlockExecutor.class)
public class CommandBlockExecutorMixin {
    
    /**
     * Intercepts command block execution attempts and cancels them if disabled by game rules.
     * 
     * <p>See {@link CommandBlockToggle#onCommandBlockExecute} for implementation details.
     * 
     * @param world The world in which the command block is attempting to execute
     * @param cir The callback info returnable that can be used to cancel execution
     */
    @Inject(method = "execute", at = @At("HEAD"), cancellable = true)
    private void rainbowkittytweaks$cancelCommandBlockExecution(World world, CallbackInfoReturnable<Boolean> cir) {
        if (!(world instanceof ServerWorld)) return;
        CommandBlockToggle.onCommandBlockExecute((CommandBlockExecutor) (Object) this, world, cir);
    }
}
