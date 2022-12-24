package atonkish.reinfshulker.recipe;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;

public class ModRecipeSerializer {
    public static final RecipeSerializer<ReinforcedShulkerBoxCraftingRecipe> REINFORCED_SHULKER_BOX;
    public static final SpecialRecipeSerializer<ReinforcedShulkerBoxColoringRecipe> REINFORCED_SHULKER_BOX_COLORING;

    public static void init() {
    }

    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
        Identifier identifier = new Identifier(ReinforcedShulkerBoxesMod.MOD_ID, id);
        return Registry.register(Registries.RECIPE_SERIALIZER, identifier, serializer);
    }

    static {
        REINFORCED_SHULKER_BOX = register("crafting_special_reinforcedshulkerbox",
                new ReinforcedShulkerBoxCraftingRecipe.Serializer());
        REINFORCED_SHULKER_BOX_COLORING = register("crafting_special_reinforcedshulkerboxcoloring",
                new SpecialRecipeSerializer<>(ReinforcedShulkerBoxColoringRecipe::new));
    }
}
