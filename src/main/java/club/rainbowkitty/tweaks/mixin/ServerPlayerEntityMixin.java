package club.rainbowkitty.tweaks.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import club.rainbowkitty.tweaks.module.GiveAllRecipesAtJoin;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    /**
     * Injects into the player spawn process. Called after the original {@code onSpawn} method completes.
     * 
     * <p>If the player has not yet received all recipes, this method attempts to unlock all recipes
     * for the player by calling {@link GiveAllRecipesAtJoin#tryGiveAllRecipes
     * 
     * @param ci The callback info from the mixin injection
     */
    @Inject(method = "onSpawn", at = @At("TAIL"))
    private void rainbowkittytweaks$unlockAllRecipesAtLogin(CallbackInfo ci) {
        ServerPlayerEntity self = (ServerPlayerEntity) (Object) this;
        if (!(self.getEntityWorld() instanceof ServerWorld world)) return;

        GiveAllRecipesAtJoin.tryGiveAllRecipes(self, world);
    }
}