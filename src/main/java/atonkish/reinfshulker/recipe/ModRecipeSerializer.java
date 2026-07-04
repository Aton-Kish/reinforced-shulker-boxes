package atonkish.reinfshulker.recipe;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;

public class ModRecipeSerializer {
  public static final RecipeSerializer<ReinforcedShulkerBoxCraftingRecipe> REINFORCED_SHULKER_BOX;

  public static void init() {}

  private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(
      String id, S serializer) {
    Identifier identifier = Identifier.fromNamespaceAndPath(ReinforcedShulkerBoxesMod.MOD_ID, id);
    return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, identifier, serializer);
  }

  static {
    REINFORCED_SHULKER_BOX =
        register(
            "crafting_special_reinforcedshulkerbox",
            new RecipeSerializer<>(
                ReinforcedShulkerBoxCraftingRecipe.Serializer.CODEC,
                ReinforcedShulkerBoxCraftingRecipe.Serializer.PACKET_CODEC));
  }
}
