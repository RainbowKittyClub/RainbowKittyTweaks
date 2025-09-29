package club.rainbowkitty.tweaks.module;

import club.rainbowkitty.tweaks.Main;
import club.rainbowkitty.tweaks.rule.CachedBooleanRule;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

/**
 * Module to toggle whether arrows on fire can start fires when they hit a block.
 * Controlled via the {@code fireArrowsLightFire} gamerule.
 *
 * See mixin {@link club.rainbowkitty.tweaks.mixin.PersistentProjectileEntityMixin} for behavior.
 */
public final class FireArrowsLightFire {
    private static final FireArrowsLightFire INSTANCE = new FireArrowsLightFire();
    private static boolean initialized = false;

    public static final CachedBooleanRule ALLOW_FIRE_ARROWS_LIGHT_FIRE = new CachedBooleanRule(
        "fireArrowsLightFire",
        true
    );

    private FireArrowsLightFire() {}

    public static void init() {
        if (initialized) return;
        initialized = true;
    }

    public static FireArrowsLightFire getInstance() {
        return INSTANCE;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    /**
     * Handles the logic for when a fire arrow hits a block.
     * If the arrow is on fire and the relevant game rule is enabled,
     * it attempts to ignite the block at the hit position using
     * flint and steel-like behavior.
     *
     * @param projectile The projectile entity (arrow) that hit the block
     * @param hitResult The result of the block hit, containing position and side information
     */
    public static void onArrowBlockHit(PersistentProjectileEntity projectile, ServerWorld world, BlockHitResult hitResult) {
        if (projectile == null || hitResult == null) return;

        if (!projectile.isOnFire()) return;
        if (!ALLOW_FIRE_ARROWS_LIGHT_FIRE.getValue(world)) return;

        Entity owner = projectile.getOwner();
        ActionResult result = useFlintAndSteelLike(world, hitResult, owner);

        Main.LOGGER.debug(
            "Fire arrow hit: pos={}, side={}, owner={}, result={}",
            hitResult.getBlockPos(), hitResult.getSide(), owner, result
        );
    }

    // Simulates the use of a flint and steel at the location of the arrow hit so that fire placement is logged by Ledger
    // and other mods that hook into Item#useOnBlock can respond appropriately.
    private static ActionResult useFlintAndSteelLike(ServerWorld world, BlockHitResult originalHit, @Nullable Entity owner) {
        BlockPos blockPos = originalHit.getBlockPos();
        BlockHitResult phantomHit = new BlockHitResult(
            originalHit.getPos(),
            originalHit.getSide(),
            blockPos,
            originalHit.isInsideBlock()
        );

        ItemStack phantomStack = new ItemStack(Items.FLINT_AND_STEEL);
        PlayerEntity playerOwner = owner instanceof PlayerEntity ? (PlayerEntity) owner : null;

        ItemUsageContext context = new ItemUsageContext(world, playerOwner, Hand.MAIN_HAND, phantomStack, phantomHit);
        return Items.FLINT_AND_STEEL.useOnBlock(context);
    }
}