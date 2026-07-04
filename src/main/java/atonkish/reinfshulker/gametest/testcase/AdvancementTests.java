package atonkish.reinfshulker.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.Vec3;

import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfcore.gametest.TestFunction;
import atonkish.reinfcore.util.ReinforcingMaterials;
import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;
import atonkish.reinfshulker.gametest.util.MockServerPlayerHelper;
import atonkish.reinfshulker.gametest.util.TestIdentifier;
import atonkish.reinfshulker.item.ModItems;

public class AdvancementTests {
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
      String.format("%s:advancement/default", ReinforcedShulkerBoxesMod.MOD_ID);
  private static final String TEST_STRUCTURE_EMPTY = "fabric-gametest-api-v1:empty";

  public static final Collection<TestFunction> TEST_FUNCTIONS =
      new ArrayList<>() {
        {
          // Copper Shulker Box
          for (Item item : SHULKER_BOX_MAP.values()) {
            add(
                AdvancementTests.createTest(
                    String.format(
                        "Obtain Copper Shulker Box recipe advancement by having %s",
                        item.getName().getString()),
                    item,
                    Identifier.fromNamespaceAndPath(
                        ReinforcedShulkerBoxesMod.MOD_ID,
                        "recipes/decorations/copper_shulker_box")));
          }
          add(
              AdvancementTests.createTest(
                  "Obtain Copper Shulker Box recipe advancement by having Copper Ingot",
                  Items.COPPER_INGOT,
                  Identifier.fromNamespaceAndPath(
                      ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/copper_shulker_box")));
          add(
              AdvancementTests.createTest(
                  "Obtain Copper Shulker Box recipe advancement by having Reinforced Copper Chest",
                  atonkish.reinfchest.item.ModItems.REINFORCED_CHEST_MAP.get(
                      ReinforcingMaterials.MAP.get("copper")),
                  Identifier.fromNamespaceAndPath(
                      ReinforcedShulkerBoxesMod.MOD_ID,
                      "recipes/decorations/copper_shulker_box_from_reinforced_copper_chest")));
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
            add(
                AdvancementTests.createTest(
                    String.format(
                        "Obtain Copper Shulker Box recipe advancement by having %s",
                        item.getName().getString()),
                    item,
                    Identifier.fromNamespaceAndPath(
                        ReinforcedChestsMod.MOD_ID,
                        "recipes/decorations/iron_chest_from_copper_chests")));
          }

          // Iron Shulker Box
          for (Item item :
              ModItems.REINFORCED_SHULKER_BOX_MAP
                  .get(ReinforcingMaterials.MAP.get("copper"))
                  .values()) {
            add(
                AdvancementTests.createTest(
                    String.format(
                        "Obtain Iron Shulker Box recipe advancement by having %s",
                        item.getName().getString()),
                    item,
                    Identifier.fromNamespaceAndPath(
                        ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/iron_shulker_box")));
          }
          add(
              AdvancementTests.createTest(
                  "Obtain Iron Shulker Box recipe advancement by having Iron Ingot",
                  Items.IRON_INGOT,
                  Identifier.fromNamespaceAndPath(
                      ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/iron_shulker_box")));
          add(
              AdvancementTests.createTest(
                  "Obtain Iron Shulker Box recipe advancement by having Iron Chest",
                  atonkish.reinfchest.item.ModItems.REINFORCED_CHEST_MAP.get(
                      ReinforcingMaterials.MAP.get("iron")),
                  Identifier.fromNamespaceAndPath(
                      ReinforcedShulkerBoxesMod.MOD_ID,
                      "recipes/decorations/iron_shulker_box_from_iron_chest")));

          // Gold Shulker Box
          for (Item item :
              ModItems.REINFORCED_SHULKER_BOX_MAP
                  .get(ReinforcingMaterials.MAP.get("iron"))
                  .values()) {
            add(
                AdvancementTests.createTest(
                    String.format(
                        "Obtain Gold Shulker Box recipe advancement by having %s",
                        item.getName().getString()),
                    item,
                    Identifier.fromNamespaceAndPath(
                        ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/gold_shulker_box")));
          }
          add(
              AdvancementTests.createTest(
                  "Obtain Gold Shulker Box recipe advancement by having Gold Ingot",
                  Items.GOLD_INGOT,
                  Identifier.fromNamespaceAndPath(
                      ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/gold_shulker_box")));
          add(
              AdvancementTests.createTest(
                  "Obtain Gold Shulker Box recipe advancement by having Gold Chest",
                  atonkish.reinfchest.item.ModItems.REINFORCED_CHEST_MAP.get(
                      ReinforcingMaterials.MAP.get("gold")),
                  Identifier.fromNamespaceAndPath(
                      ReinforcedShulkerBoxesMod.MOD_ID,
                      "recipes/decorations/gold_shulker_box_from_gold_chest")));

          // Diamond Shulker Box
          for (Item item :
              ModItems.REINFORCED_SHULKER_BOX_MAP
                  .get(ReinforcingMaterials.MAP.get("gold"))
                  .values()) {
            add(
                AdvancementTests.createTest(
                    String.format(
                        "Obtain Diamond Shulker Box recipe advancement by having %s",
                        item.getName().getString()),
                    item,
                    Identifier.fromNamespaceAndPath(
                        ReinforcedShulkerBoxesMod.MOD_ID,
                        "recipes/decorations/diamond_shulker_box")));
          }
          add(
              AdvancementTests.createTest(
                  "Obtain Diamond Shulker Box recipe advancement by having Diamond Ingot",
                  Items.DIAMOND,
                  Identifier.fromNamespaceAndPath(
                      ReinforcedShulkerBoxesMod.MOD_ID,
                      "recipes/decorations/diamond_shulker_box")));
          add(
              AdvancementTests.createTest(
                  "Obtain Diamond Shulker Box recipe advancement by having Diamond Chest",
                  atonkish.reinfchest.item.ModItems.REINFORCED_CHEST_MAP.get(
                      ReinforcingMaterials.MAP.get("diamond")),
                  Identifier.fromNamespaceAndPath(
                      ReinforcedShulkerBoxesMod.MOD_ID,
                      "recipes/decorations/diamond_shulker_box_from_diamond_chest")));

          // Netherite Shulker Box
          for (Item item :
              ModItems.REINFORCED_SHULKER_BOX_MAP
                  .get(ReinforcingMaterials.MAP.get("diamond"))
                  .values()) {
            add(
                AdvancementTests.createTest(
                    String.format(
                        "Obtain Netherite Shulker Box recipe advancement by having %s",
                        item.getName().getString()),
                    item,
                    Identifier.fromNamespaceAndPath(
                        ReinforcedShulkerBoxesMod.MOD_ID,
                        "recipes/decorations/netherite_shulker_box_smithing")));
          }
          add(
              AdvancementTests.createTest(
                  "Obtain Netherite Shulker Box recipe advancement by having Netherite Ingot",
                  Items.NETHERITE_INGOT,
                  Identifier.fromNamespaceAndPath(
                      ReinforcedShulkerBoxesMod.MOD_ID,
                      "recipes/decorations/netherite_shulker_box_smithing")));
          add(
              AdvancementTests.createTest(
                  "Obtain Netherite Shulker Box recipe advancement by having Netherite Chest",
                  atonkish.reinfchest.item.ModItems.REINFORCED_CHEST_MAP.get(
                      ReinforcingMaterials.MAP.get("netherite")),
                  Identifier.fromNamespaceAndPath(
                      ReinforcedShulkerBoxesMod.MOD_ID,
                      "recipes/decorations/netherite_shulker_box_from_netherite_chest")));
        }
      };

  private static TestFunction createTest(String name, Item item, Identifier advancementId) {
    Identifier testIdentifier =
        TestIdentifier.of(ReinforcedShulkerBoxesMod.MOD_ID, AdvancementTests.class, name);

    return new TestFunction(
        testIdentifier,
        AdvancementTests.TEST_ENVIRONMENT_DEFAULT,
        AdvancementTests.TEST_STRUCTURE_EMPTY,
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
          ServerPlayer player =
              MockServerPlayerHelper.spawn(
                  context, GameType.SURVIVAL, Vec3.atLowerCornerOf(BlockPos.ZERO));
          AdvancementHolder entry =
              context.getLevel().getServer().getAdvancements().get(advancementId);
          AdvancementProgress progress = player.getAdvancements().getOrStartProgress(entry);

          // Act
          CompletableFuture<Void> futurePartialAct1 = new CompletableFuture<>();
          CompletableFuture<Void> futurePartialAct2 = new CompletableFuture<>();

          Map<String, Boolean> progressMap = new HashMap<String, Boolean>();
          String progressMapKeyBeforeHavingItem = "beforeHavingItem";
          String progressMapKeyAfterHavingItem = "afterHavingItem";

          long tickOrigin = 0;
          context.runAtTickTime(
              tickOrigin,
              () -> {
                progressMap.put(progressMapKeyBeforeHavingItem, progress.isDone());

                player.addItem(new ItemStack(item));

                futurePartialAct1.complete(null);
              });

          long tickObtained = 1;
          context.runAtTickTime(
              tickObtained,
              () -> {
                progressMap.put(progressMapKeyAfterHavingItem, progress.isDone());

                futurePartialAct2.complete(null);
              });

          // Assert
          CompletableFuture.allOf(futurePartialAct1, futurePartialAct2)
              .thenRun(
                  () -> {
                    try {
                      context.assertFalse(
                          progressMap.get(progressMapKeyBeforeHavingItem),
                          Component.nullToEmpty(
                              String.format(
                                  "Expected that advancement %s has not been done yet, but it has been already done.",
                                  entry)));
                      context.assertTrue(
                          progressMap.get(progressMapKeyAfterHavingItem),
                          Component.nullToEmpty(
                              String.format(
                                  "Expected that advancement %s has been done, but it has not been done yet.",
                                  entry)));
                    } catch (Exception e) {
                      ReinforcedShulkerBoxesMod.LOGGER.error(
                          "[{}] {}", testIdentifier, e.getMessage());
                      throw e;
                    } finally {
                      MockServerPlayerHelper.destroy(context, player);
                    }

                    context.succeed();
                  });
        });
  }
}
