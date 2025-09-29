package club.rainbowkitty.tweaks;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Optional;

public final class BuiltinDataPacks {
    private BuiltinDataPacks() {}

        public static final Identifier CHEAPER_RAILS = Identifier.of(Main.MOD_ID, "cheaper_rails");
        public static final Identifier CHEAPER_MAPS = Identifier.of(Main.MOD_ID, "cheaper_maps");
        public static final Identifier CRAFTABLE_NAME_TAG = Identifier.of(Main.MOD_ID, "craftable_name_tag");
        public static final Identifier FARMERSDELIGHT_ROPE_COMPAT = Identifier.of(Main.MOD_ID, "farmersdelight_rope_compat");

    public static void register() {
        Optional<ModContainer> self = FabricLoader.getInstance().getModContainer(Main.MOD_ID);
        if (self.isEmpty()) return;
        ModContainer container = self.get();

        // Register as built-in SERVER_DATA packs, enabled by default
        ResourceManagerHelper.registerBuiltinResourcePack(
            CHEAPER_RAILS, container,
            Text.literal("RainbowKittyTweaks: cheaper rails"),
            ResourcePackActivationType.DEFAULT_ENABLED
        );
        ResourceManagerHelper.registerBuiltinResourcePack(
            CHEAPER_MAPS, container,
            Text.literal("RainbowKittyTweaks: cheaper maps"),
            ResourcePackActivationType.DEFAULT_ENABLED
        );
        ResourceManagerHelper.registerBuiltinResourcePack(
            CRAFTABLE_NAME_TAG, container,
            Text.literal("RainbowKittyTweaks: craftable name tag"),
            ResourcePackActivationType.DEFAULT_ENABLED
        );
        ResourceManagerHelper.registerBuiltinResourcePack(
            FARMERSDELIGHT_ROPE_COMPAT, container,
            Text.literal("RainbowKittyTweaks: add Farmers' Delight rope to rope tag"),
            ResourcePackActivationType.NORMAL
        );
    }
}
