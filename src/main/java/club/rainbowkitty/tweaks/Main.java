package club.rainbowkitty.tweaks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import club.rainbowkitty.tweaks.rule.GameRuleCache;
import club.rainbowkitty.tweaks.module.CommandBlockToggle;
import club.rainbowkitty.tweaks.module.FireArrowsLightFire;
import club.rainbowkitty.tweaks.module.GiveAllRecipesAtJoin;
import club.rainbowkitty.tweaks.module.IronGolemPassiveRegen;
import club.rainbowkitty.tweaks.module.LegArmorProtectsFromBerryThorns;

public class Main implements ModInitializer {
    public static final String MOD_ID = "rainbowkittytweaks";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // Register built-in datapacks
        BuiltinDataPacks.register();

        // Initialize gamerule cache after the server starts (worlds are available)
        ServerLifecycleEvents.SERVER_STARTED.register(server -> GameRuleCache.init(server));

        // Ensure modules are initialized (register their rules early)
        CommandBlockToggle.init();
        FireArrowsLightFire.init();
        GiveAllRecipesAtJoin.init();
        LegArmorProtectsFromBerryThorns.init();
        IronGolemPassiveRegen.init();

        LOGGER.info("RainbowKittyTweaks initialized!");
    }
}