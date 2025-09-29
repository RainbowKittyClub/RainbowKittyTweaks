package club.rainbowkitty.tweaks.module;

import club.rainbowkitty.tweaks.rule.CachedBooleanRule;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

/**
 * Module to automatically unlock all recipes for players when they join (spawn).
 * Controlled via the {@code giveAllRecipes} gamerule.
 *
 * See mixin {@link club.rainbowkitty.tweaks.mixin.ServerPlayerEntityMixin} for behavior.
 */
public final class GiveAllRecipesAtJoin {
    private static final GiveAllRecipesAtJoin INSTANCE = new GiveAllRecipesAtJoin();
    private static boolean initialized = false;

    public static final CachedBooleanRule ALLOW_GIVE_ALL_RECIPES = new CachedBooleanRule(
        "giveAllRecipes",
        true
    );

    private GiveAllRecipesAtJoin() {}

    public static void init() {
        if (initialized) return;
        initialized = true;
    }

    public static GiveAllRecipesAtJoin getInstance() {
        return INSTANCE;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static boolean tryGiveAllRecipes(ServerPlayerEntity player, ServerWorld world) {
        // Unlock all recipes by getting them from the server's recipe manager
        var server = player.getServer();
        if (server != null) {
            // Respect gamerule: giveAllRecipes
            if (!GiveAllRecipesAtJoin.ALLOW_GIVE_ALL_RECIPES.getValue(world)) return false;
            var recipeManager = server.getRecipeManager();
            var allRecipes = recipeManager.values();
            
            // Unlock all recipes for the player
            player.unlockRecipes(allRecipes);
        }
        return true;
    }
}
