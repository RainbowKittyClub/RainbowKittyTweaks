package club.rainbowkitty.tweaks.module;

import club.rainbowkitty.tweaks.rule.CachedBooleanRule;
import static club.rainbowkitty.tweaks.Main.LOGGER;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.CommandBlockExecutor;

/**
 * Module to toggle command block execution via a game rule.
 * When enabled, command blocks can execute commands as normal.
 * When disabled, command blocks will not execute any commands.
 * Controlled via the {@code commandBlocksEnabled} gamerule.
 * 
 * See {@link club.rainbowkitty.tweaks.mixin.CommandBlockExecutorMixin} for implementation details.
 */
public final class CommandBlockToggle {
    private static final CommandBlockToggle INSTANCE = new CommandBlockToggle();
    private static boolean initialized = false;

    public static final CachedBooleanRule ALLOW_COMMAND_BLOCK_EXECUTION = new CachedBooleanRule(
        "commandBlocksEnabled",
        true
    );

    private CommandBlockToggle() {}

    public static void init() {
        if (initialized) return;
        initialized = true;
    }

    public static CommandBlockToggle getInstance() {
        return INSTANCE;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    /**
     * Handles the logic for command block execution attempts.
     * If the relevant game rule is disabled, it prevents execution
     * and logs a warning with the command block's position.
     *
     * @param commandBlockExecutor The command block executor instance
     * @param world The world in which the command block is attempting to execute
     * @param cir The callback info returnable that can be used to cancel execution
     */
    public static void onCommandBlockExecute(
        net.minecraft.world.CommandBlockExecutor commandBlockExecutor,
        net.minecraft.world.World world,
        org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<Boolean> cir
    ) {
        if (isInitialized()) {
            if (world instanceof ServerWorld sw) {
            // Respect gamerule: commandBlocksEnabled
                if (ALLOW_COMMAND_BLOCK_EXECUTION.getValue(sw) == false) {
                    var exec = (CommandBlockExecutor) (Object) commandBlockExecutor;
                    var pos = exec.getPos();
                    LOGGER.warn("Command block at {},{},{} in {} unable to execute. Gamerule commandBlocksEnabled is false.",
                        pos.getX(), pos.getY(), pos.getZ(), sw.getRegistryKey().getValue());
                        exec.setSuccessCount(0);
                        cir.setReturnValue(false);
                }
            }
        }
    }
}