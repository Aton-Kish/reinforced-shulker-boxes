package atonkish.reinfshulker.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.level.block.Rotation;

import atonkish.reinfcore.gametest.TestFunction;
import atonkish.reinfcore.util.ReinforcingMaterials;
import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;
import atonkish.reinfshulker.gametest.util.TestIdentifier;
import atonkish.reinfshulker.item.ModItems;

public class RecipeTests {
  private static final Map<DyeColor, Item> SHULKER_BOX_MAP =
      new LinkedHashMap<>() {
        {
          put((DyeColor) null, Items.SHULKER_BOX);
          put(DyeColor.WHITE, Items.WHITE_SHULKER_BOX);
          put(DyeColor.ORANGE, Items.ORANGE_SHULKER_BOX);
          put(DyeColor.MAGENTA, Items.MAGENTA_SHULKER_BOX);
          put(DyeColor.LIGHT_BLUE, Items.LIGHT_BLUE_SHULKER_BOX);
          put(DyeColor.YELLOW, Items.YELLOW_SHULKER_BOX);
          put(DyeColor.LIME, Items.LIME_SHULKER_BOX);
          put(DyeColor.PINK, Items.PINK_SHULKER_BOX);
          put(DyeColor.GRAY, Items.GRAY_SHULKER_BOX);
          put(DyeColor.LIGHT_GRAY, Items.LIGHT_GRAY_SHULKER_BOX);
          put(DyeColor.CYAN, Items.CYAN_SHULKER_BOX);
          put(DyeColor.PURPLE, Items.PURPLE_SHULKER_BOX);
          put(DyeColor.BLUE, Items.BLUE_SHULKER_BOX);
          put(DyeColor.BROWN, Items.BROWN_SHULKER_BOX);
          put(DyeColor.GREEN, Items.GREEN_SHULKER_BOX);
          put(DyeColor.RED, Items.RED_SHULKER_BOX);
          put(DyeColor.BLACK, Items.BLACK_SHULKER_BOX);
        }
      };

  private static final String TEST_ENVIRONMENT_DEFAULT =
      String.format("%s:recipe/default", ReinforcedShulkerBoxesMod.MOD_ID);
  private static final String TEST_STRUCTURE_EMPTY = "fabric-gametest-api-v1:empty";

  public static final Collection<TestFunction> TEST_FUNCTIONS =
      new ArrayList<>() {
        {
          // Copper Shulker Box
          for (DyeColor color : SHULKER_BOX_MAP.keySet()) {
            ItemStack baseShulkerBox = new ItemStack(SHULKER_BOX_MAP.get(color));
            baseShulkerBox.set(
                DataComponents.CONTAINER,
                ItemContainerContents.fromItems(List.of(new ItemStack(Items.DIRT))));
            ItemStack material = new ItemStack(Items.COPPER_INGOT);
            ItemStack shulkerBox =
                new ItemStack(
                    ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(color));
            shulkerBox.set(
                DataComponents.CONTAINER,
                ItemContainerContents.fromItems(List.of(new ItemStack(Items.DIRT))));

            add(
                RecipeTests.createTest(
                    String.format("Craft %s", baseShulkerBox.getItem().getName().getString()),
                    RecipeType.CRAFTING,
                    CraftingInput.of(
                        3,
                        3,
                        List.of(
                            material,
                            material,
                            material,
                            material,
                            baseShulkerBox,
                            material,
                            material,
                            material,
                            material)),
                    shulkerBox));
          }

          for (DyeColor baseColor : SHULKER_BOX_MAP.keySet()) {
            for (DyeColor dyeColor : DyeColor.values()) {
              if (dyeColor.equals(baseColor)) {
                continue;
              }

              ItemStack baseShulkerBox =
                  new ItemStack(
                      ModItems.REINFORCED_SHULKER_BOX_MAP
                          .get(ReinforcingMaterials.MAP.get("copper"))
                          .get(baseColor));
              baseShulkerBox.set(
                  DataComponents.CONTAINER,
                  ItemContainerContents.fromItems(List.of(new ItemStack(Items.DIRT))));
              ItemStack dye = new ItemStack(DyeItem.byColor(dyeColor));
              ItemStack dyedShulkerBox =
                  new ItemStack(
                      ModItems.REINFORCED_SHULKER_BOX_MAP
                          .get(ReinforcingMaterials.MAP.get("copper"))
                          .get(dyeColor));
              dyedShulkerBox.set(
                  DataComponents.CONTAINER,
                  ItemContainerContents.fromItems(List.of(new ItemStack(Items.DIRT))));

              add(
                  RecipeTests.createTest(
                      String.format(
                          "Coloring from %s to %s",
                          baseShulkerBox.getItem().getName().getString(),
                          dyedShulkerBox.getItem().getName().getString()),
                      RecipeType.CRAFTING,
                      CraftingInput.of(
                          2, 2, List.of(baseShulkerBox, dye, ItemStack.EMPTY, ItemStack.EMPTY)),
                      dyedShulkerBox));
            }
          }

          {
            // from Modded Copper Chest (for backward compatible)
            ItemStack chest =
                new ItemStack(
                    atonkish.reinfchest.item.ModItems.REINFORCED_CHEST_MAP.get(
                        ReinforcingMaterials.MAP.get("copper")));
            ItemStack shell = new ItemStack(Items.SHULKER_SHELL);
            ItemStack shulkerBox =
                new ItemStack(
                    ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get((DyeColor) null));

            add(
                RecipeTests.createTest(
                    "Craft Copper Shulker Box from Reinforced Copper Chest",
                    RecipeType.CRAFTING,
                    CraftingInput.of(
                        3,
                        3,
                        List.of(
                            shell,
                            ItemStack.EMPTY,
                            ItemStack.EMPTY,
                            chest,
                            ItemStack.EMPTY,
                            ItemStack.EMPTY,
                            shell,
                            ItemStack.EMPTY,
                            ItemStack.EMPTY)),
                    shulkerBox));
          }

          {
            // from Copper Chests
            for (Item item :
                List.of(
                    Items.COPPER_CHEST,
                    Items.EXPOSED_COPPER_CHEST,
                    Items.WEATHERED_COPPER_CHEST,
                    Items.OXIDIZED_COPPER_CHEST,
                    Items.WAXED_COPPER_CHEST,
                    Items.WAXED_EXPOSED_COPPER_CHEST,
                    Items.WAXED_WEATHERED_COPPER_CHEST,
                    Items.WAXED_OXIDIZED_COPPER_CHEST)) {
              ItemStack chest = new ItemStack(item);
              ItemStack shell = new ItemStack(Items.SHULKER_SHELL);
              ItemStack shulkerBox =
                  new ItemStack(
                      ModItems.REINFORCED_SHULKER_BOX_MAP
                          .get(ReinforcingMaterials.MAP.get("copper"))
                          .get((DyeColor) null));

              add(
                  RecipeTests.createTest(
                      String.format(
                          "Craft Copper Shulker Box from %s",
                          chest.getItem().getName().getString()),
                      RecipeType.CRAFTING,
                      CraftingInput.of(
                          3,
                          3,
                          List.of(
                              shell,
                              ItemStack.EMPTY,
                              ItemStack.EMPTY,
                              chest,
                              ItemStack.EMPTY,
                              ItemStack.EMPTY,
                              shell,
                              ItemStack.EMPTY,
                              ItemStack.EMPTY)),
                      shulkerBox));
            }
          }

          // Iron Shulker Box
          for (DyeColor color : SHULKER_BOX_MAP.keySet()) {
            ItemStack baseShulkerBox =
                new ItemStack(
                    ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(color));
            baseShulkerBox.set(
                DataComponents.CONTAINER,
                ItemContainerContents.fromItems(List.of(new ItemStack(Items.DIRT))));
            ItemStack material = new ItemStack(Items.IRON_INGOT);
            ItemStack shulkerBox =
                new ItemStack(
                    ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(color));
            shulkerBox.set(
                DataComponents.CONTAINER,
                ItemContainerContents.fromItems(List.of(new ItemStack(Items.DIRT))));

            add(
                RecipeTests.createTest(
                    String.format("Craft %s", baseShulkerBox.getItem().getName().getString()),
                    RecipeType.CRAFTING,
                    CraftingInput.of(
                        3,
                        3,
                        List.of(
                            material,
                            material,
                            material,
                            material,
                            baseShulkerBox,
                            material,
                            material,
                            material,
                            material)),
                    shulkerBox));
          }

          for (DyeColor baseColor : SHULKER_BOX_MAP.keySet()) {
            for (DyeColor dyeColor : DyeColor.values()) {
              if (dyeColor.equals(baseColor)) {
                continue;
              }

              ItemStack baseShulkerBox =
                  new ItemStack(
                      ModItems.REINFORCED_SHULKER_BOX_MAP
                          .get(ReinforcingMaterials.MAP.get("iron"))
                          .get(baseColor));
              baseShulkerBox.set(
                  DataComponents.CONTAINER,
                  ItemContainerContents.fromItems(List.of(new ItemStack(Items.DIRT))));
              ItemStack dye = new ItemStack(DyeItem.byColor(dyeColor));
              ItemStack dyedShulkerBox =
                  new ItemStack(
                      ModItems.REINFORCED_SHULKER_BOX_MAP
                          .get(ReinforcingMaterials.MAP.get("iron"))
                          .get(dyeColor));
              dyedShulkerBox.set(
                  DataComponents.CONTAINER,
                  ItemContainerContents.fromItems(List.of(new ItemStack(Items.DIRT))));

              add(
                  RecipeTests.createTest(
                      String.format(
                          "Coloring from %s to %s",
                          baseShulkerBox.getItem().getName().getString(),
                          dyedShulkerBox.getItem().getName().getString()),
                      RecipeType.CRAFTING,
                      CraftingInput.of(
                          2, 2, List.of(baseShulkerBox, dye, ItemStack.EMPTY, ItemStack.EMPTY)),
                      dyedShulkerBox));
            }
          }

          {
            ItemStack chest =
                new ItemStack(
                    atonkish.reinfchest.item.ModItems.REINFORCED_CHEST_MAP.get(
                        ReinforcingMaterials.MAP.get("iron")));
            ItemStack shell = new ItemStack(Items.SHULKER_SHELL);
            ItemStack shulkerBox =
                new ItemStack(
                    ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get((DyeColor) null));

            add(
                RecipeTests.createTest(
                    String.format(
                        "Craft %s from %s",
                        shulkerBox.getItem().getName().getString(),
                        chest.getItem().getName().getString()),
                    RecipeType.CRAFTING,
                    CraftingInput.of(
                        3,
                        3,
                        List.of(
                            shell,
                            ItemStack.EMPTY,
                            ItemStack.EMPTY,
                            chest,
                            ItemStack.EMPTY,
                            ItemStack.EMPTY,
                            shell,
                            ItemStack.EMPTY,
                            ItemStack.EMPTY)),
                    shulkerBox));
          }

          // Gold Shulker Box
          for (DyeColor color : SHULKER_BOX_MAP.keySet()) {
            ItemStack baseShulkerBox =
                new ItemStack(
                    ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(color));
            baseShulkerBox.set(
                DataComponents.CONTAINER,
                ItemContainerContents.fromItems(List.of(new ItemStack(Items.DIRT))));
            ItemStack material = new ItemStack(Items.GOLD_INGOT);
            ItemStack shulkerBox =
                new ItemStack(
                    ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(color));
            shulkerBox.set(
                DataComponents.CONTAINER,
                ItemContainerContents.fromItems(List.of(new ItemStack(Items.DIRT))));

            add(
                RecipeTests.createTest(
                    String.format("Craft %s", baseShulkerBox.getItem().getName().getString()),
                    RecipeType.CRAFTING,
                    CraftingInput.of(
                        3,
                        3,
                        List.of(
                            material,
                            material,
                            material,
                            material,
                            baseShulkerBox,
                            material,
                            material,
                            material,
                            material)),
                    shulkerBox));
          }

          for (DyeColor baseColor : SHULKER_BOX_MAP.keySet()) {
            for (DyeColor dyeColor : DyeColor.values()) {
              if (dyeColor.equals(baseColor)) {
                continue;
              }

              ItemStack baseShulkerBox =
                  new ItemStack(
                      ModItems.REINFORCED_SHULKER_BOX_MAP
                          .get(ReinforcingMaterials.MAP.get("gold"))
                          .get(baseColor));
              baseShulkerBox.set(
                  DataComponents.CONTAINER,
                  ItemContainerContents.fromItems(List.of(new ItemStack(Items.DIRT))));
              ItemStack dye = new ItemStack(DyeItem.byColor(dyeColor));
              ItemStack dyedShulkerBox =
                  new ItemStack(
                      ModItems.REINFORCED_SHULKER_BOX_MAP
                          .get(ReinforcingMaterials.MAP.get("gold"))
                          .get(dyeColor));
              dyedShulkerBox.set(
                  DataComponents.CONTAINER,
                  ItemContainerContents.fromItems(List.of(new ItemStack(Items.DIRT))));

              add(
                  RecipeTests.createTest(
                      String.format(
                          "Coloring from %s to %s",
                          baseShulkerBox.getItem().getName().getString(),
                          dyedShulkerBox.getItem().getName().getString()),
                      RecipeType.CRAFTING,
                      CraftingInput.of(
                          2, 2, List.of(baseShulkerBox, dye, ItemStack.EMPTY, ItemStack.EMPTY)),
                      dyedShulkerBox));
            }
          }

          {
            ItemStack chest =
                new ItemStack(
                    atonkish.reinfchest.item.ModItems.REINFORCED_CHEST_MAP.get(
                        ReinforcingMaterials.MAP.get("gold")));
            ItemStack shell = new ItemStack(Items.SHULKER_SHELL);
            ItemStack shulkerBox =
                new ItemStack(
                    ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get((DyeColor) null));

            add(
                RecipeTests.createTest(
                    String.format(
                        "Craft %s from %s",
                        shulkerBox.getItem().getName().getString(),
                        chest.getItem().getName().getString()),
                    RecipeType.CRAFTING,
                    CraftingInput.of(
                        3,
                        3,
                        List.of(
                            shell,
                            ItemStack.EMPTY,
                            ItemStack.EMPTY,
                            chest,
                            ItemStack.EMPTY,
                            ItemStack.EMPTY,
                            shell,
                            ItemStack.EMPTY,
                            ItemStack.EMPTY)),
                    shulkerBox));
          }

          // Diamond Shulker Box
          for (DyeColor color : SHULKER_BOX_MAP.keySet()) {
            ItemStack baseShulkerBox =
                new ItemStack(
                    ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(color));
            baseShulkerBox.set(
                DataComponents.CONTAINER,
                ItemContainerContents.fromItems(List.of(new ItemStack(Items.DIRT))));
            ItemStack material = new ItemStack(Items.DIAMOND);
            ItemStack shulkerBox =
                new ItemStack(
                    ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(color));
            shulkerBox.set(
                DataComponents.CONTAINER,
                ItemContainerContents.fromItems(List.of(new ItemStack(Items.DIRT))));

            add(
                RecipeTests.createTest(
                    String.format("Craft %s", baseShulkerBox.getItem().getName().getString()),
                    RecipeType.CRAFTING,
                    CraftingInput.of(
                        3,
                        3,
                        List.of(
                            material,
                            material,
                            material,
                            material,
                            baseShulkerBox,
                            material,
                            material,
                            material,
                            material)),
                    shulkerBox));
          }

          for (DyeColor baseColor : SHULKER_BOX_MAP.keySet()) {
            for (DyeColor dyeColor : DyeColor.values()) {
              if (dyeColor.equals(baseColor)) {
                continue;
              }

              ItemStack baseShulkerBox =
                  new ItemStack(
                      ModItems.REINFORCED_SHULKER_BOX_MAP
                          .get(ReinforcingMaterials.MAP.get("diamond"))
                          .get(baseColor));
              baseShulkerBox.set(
                  DataComponents.CONTAINER,
                  ItemContainerContents.fromItems(List.of(new ItemStack(Items.DIRT))));
              ItemStack dye = new ItemStack(DyeItem.byColor(dyeColor));
              ItemStack dyedShulkerBox =
                  new ItemStack(
                      ModItems.REINFORCED_SHULKER_BOX_MAP
                          .get(ReinforcingMaterials.MAP.get("diamond"))
                          .get(dyeColor));
              dyedShulkerBox.set(
                  DataComponents.CONTAINER,
                  ItemContainerContents.fromItems(List.of(new ItemStack(Items.DIRT))));

              add(
                  RecipeTests.createTest(
                      String.format(
                          "Coloring from %s to %s",
                          baseShulkerBox.getItem().getName().getString(),
                          dyedShulkerBox.getItem().getName().getString()),
                      RecipeType.CRAFTING,
                      CraftingInput.of(
                          2, 2, List.of(baseShulkerBox, dye, ItemStack.EMPTY, ItemStack.EMPTY)),
                      dyedShulkerBox));
            }
          }

          {
            ItemStack chest =
                new ItemStack(
                    atonkish.reinfchest.item.ModItems.REINFORCED_CHEST_MAP.get(
                        ReinforcingMaterials.MAP.get("diamond")));
            ItemStack shell = new ItemStack(Items.SHULKER_SHELL);
            ItemStack shulkerBox =
                new ItemStack(
                    ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get((DyeColor) null));

            add(
                RecipeTests.createTest(
                    String.format(
                        "Craft %s from %s",
                        shulkerBox.getItem().getName().getString(),
                        chest.getItem().getName().getString()),
                    RecipeType.CRAFTING,
                    CraftingInput.of(
                        3,
                        3,
                        List.of(
                            shell,
                            ItemStack.EMPTY,
                            ItemStack.EMPTY,
                            chest,
                            ItemStack.EMPTY,
                            ItemStack.EMPTY,
                            shell,
                            ItemStack.EMPTY,
                            ItemStack.EMPTY)),
                    shulkerBox));
          }

          // Netherite Shulker Box
          for (DyeColor color : SHULKER_BOX_MAP.keySet()) {
            ItemStack template = new ItemStack(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
            ItemStack baseShulkerBox =
                new ItemStack(
                    ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(color));
            baseShulkerBox.set(
                DataComponents.CONTAINER,
                ItemContainerContents.fromItems(List.of(new ItemStack(Items.DIRT))));
            ItemStack material = new ItemStack(Items.NETHERITE_INGOT);
            ItemStack shulkerBox =
                new ItemStack(
                    ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(color));
            shulkerBox.set(
                DataComponents.CONTAINER,
                ItemContainerContents.fromItems(List.of(new ItemStack(Items.DIRT))));

            add(
                RecipeTests.createTest(
                    String.format("Smithing %s", baseShulkerBox.getItem().getName().getString()),
                    RecipeType.SMITHING,
                    new SmithingRecipeInput(template, baseShulkerBox, material),
                    shulkerBox));
          }

          for (DyeColor baseColor : SHULKER_BOX_MAP.keySet()) {
            for (DyeColor dyeColor : DyeColor.values()) {
              if (dyeColor.equals(baseColor)) {
                continue;
              }

              ItemStack baseShulkerBox =
                  new ItemStack(
                      ModItems.REINFORCED_SHULKER_BOX_MAP
                          .get(ReinforcingMaterials.MAP.get("netherite"))
                          .get(baseColor));
              baseShulkerBox.set(
                  DataComponents.CONTAINER,
                  ItemContainerContents.fromItems(List.of(new ItemStack(Items.DIRT))));
              ItemStack dye = new ItemStack(DyeItem.byColor(dyeColor));
              ItemStack dyedShulkerBox =
                  new ItemStack(
                      ModItems.REINFORCED_SHULKER_BOX_MAP
                          .get(ReinforcingMaterials.MAP.get("netherite"))
                          .get(dyeColor));
              dyedShulkerBox.set(
                  DataComponents.CONTAINER,
                  ItemContainerContents.fromItems(List.of(new ItemStack(Items.DIRT))));

              add(
                  RecipeTests.createTest(
                      String.format(
                          "Coloring from %s to %s",
                          baseShulkerBox.getItem().getName().getString(),
                          dyedShulkerBox.getItem().getName().getString()),
                      RecipeType.CRAFTING,
                      CraftingInput.of(
                          2, 2, List.of(baseShulkerBox, dye, ItemStack.EMPTY, ItemStack.EMPTY)),
                      dyedShulkerBox));
            }
          }

          {
            ItemStack chest =
                new ItemStack(
                    atonkish.reinfchest.item.ModItems.REINFORCED_CHEST_MAP.get(
                        ReinforcingMaterials.MAP.get("netherite")));
            ItemStack shell = new ItemStack(Items.SHULKER_SHELL);
            ItemStack shulkerBox =
                new ItemStack(
                    ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get((DyeColor) null));

            add(
                RecipeTests.createTest(
                    String.format(
                        "Craft %s from %s",
                        shulkerBox.getItem().getName().getString(),
                        chest.getItem().getName().getString()),
                    RecipeType.CRAFTING,
                    CraftingInput.of(
                        3,
                        3,
                        List.of(
                            shell,
                            ItemStack.EMPTY,
                            ItemStack.EMPTY,
                            chest,
                            ItemStack.EMPTY,
                            ItemStack.EMPTY,
                            shell,
                            ItemStack.EMPTY,
                            ItemStack.EMPTY)),
                    shulkerBox));
          }
        }
      };

  private static <I extends RecipeInput, T extends Recipe<I>> TestFunction createTest(
      String name, RecipeType<T> type, I input, ItemStack expected) {
    Identifier testIdentifier =
        TestIdentifier.of(ReinforcedShulkerBoxesMod.MOD_ID, RecipeTests.class, name);

    return new TestFunction(
        testIdentifier,
        RecipeTests.TEST_ENVIRONMENT_DEFAULT,
        RecipeTests.TEST_STRUCTURE_EMPTY,
        20,
        0,
        true,
        Rotation.NONE,
        false,
        1,
        1,
        false,
        (context) -> {
          // Arrange
          ServerLevel world = context.getLevel();
          RecipeManager recipeManager = world.recipeAccess();
          RegistryAccess registryManager = world.registryAccess();
          T recipe = recipeManager.getRecipeFor(type, input, world).orElseThrow().value();

          // Act
          ItemStack actual = recipe.assemble(input, registryManager);

          // Assert
          try {
            context.assertTrue(
                ItemStack.matches(actual, expected),
                Component.nullToEmpty("Recipe result differs from expected."));
          } catch (Exception e) {
            ReinforcedShulkerBoxesMod.LOGGER.error("[{}] {}", testIdentifier, e.getMessage());
            throw e;
          }

          context.succeed();
        });
  }
}
