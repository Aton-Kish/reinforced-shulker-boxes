package atonkish.reinfshulker.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

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
                    Identifier.of(
                        ReinforcedShulkerBoxesMod.MOD_ID,
                        "recipes/decorations/copper_shulker_box")));
          }
          add(
              AdvancementTests.createTest(
                  "Obtain Copper Shulker Box recipe advancement by having Copper Ingot",
                  Items.COPPER_INGOT,
                  Identifier.of(
                      ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/copper_shulker_box")));
          add(
              AdvancementTests.createTest(
                  "Obtain Copper Shulker Box recipe advancement by having Reinforced Copper Chest",
                  atonkish.reinfchest.item.ModItems.REINFORCED_CHEST_MAP.get(
                      ReinforcingMaterials.MAP.get("copper")),
                  Identifier.of(
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
                    Identifier.of(
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
                    Identifier.of(
                        ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/iron_shulker_box")));
          }
          add(
              AdvancementTests.createTest(
                  "Obtain Iron Shulker Box recipe advancement by having Iron Ingot",
                  Items.IRON_INGOT,
                  Identifier.of(
                      ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/iron_shulker_box")));
          add(
              AdvancementTests.createTest(
                  "Obtain Iron Shulker Box recipe advancement by having Iron Chest",
                  atonkish.reinfchest.item.ModItems.REINFORCED_CHEST_MAP.get(
                      ReinforcingMaterials.MAP.get("iron")),
                  Identifier.of(
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
                    Identifier.of(
                        ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/gold_shulker_box")));
          }
          add(
              AdvancementTests.createTest(
                  "Obtain Gold Shulker Box recipe advancement by having Gold Ingot",
                  Items.GOLD_INGOT,
                  Identifier.of(
                      ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/gold_shulker_box")));
          add(
              AdvancementTests.createTest(
                  "Obtain Gold Shulker Box recipe advancement by having Gold Chest",
                  atonkish.reinfchest.item.ModItems.REINFORCED_CHEST_MAP.get(
                      ReinforcingMaterials.MAP.get("gold")),
                  Identifier.of(
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
                    Identifier.of(
                        ReinforcedShulkerBoxesMod.MOD_ID,
                        "recipes/decorations/diamond_shulker_box")));
          }
          add(
              AdvancementTests.createTest(
                  "Obtain Diamond Shulker Box recipe advancement by having Diamond Ingot",
                  Items.DIAMOND,
                  Identifier.of(
                      ReinforcedShulkerBoxesMod.MOD_ID,
                      "recipes/decorations/diamond_shulker_box")));
          add(
              AdvancementTests.createTest(
                  "Obtain Diamond Shulker Box recipe advancement by having Diamond Chest",
                  atonkish.reinfchest.item.ModItems.REINFORCED_CHEST_MAP.get(
                      ReinforcingMaterials.MAP.get("diamond")),
                  Identifier.of(
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
                    Identifier.of(
                        ReinforcedShulkerBoxesMod.MOD_ID,
                        "recipes/decorations/netherite_shulker_box_smithing")));
          }
          add(
              AdvancementTests.createTest(
                  "Obtain Netherite Shulker Box recipe advancement by having Netherite Ingot",
                  Items.NETHERITE_INGOT,
                  Identifier.of(
                      ReinforcedShulkerBoxesMod.MOD_ID,
                      "recipes/decorations/netherite_shulker_box_smithing")));
          add(
              AdvancementTests.createTest(
                  "Obtain Netherite Shulker Box recipe advancement by having Netherite Chest",
                  atonkish.reinfchest.item.ModItems.REINFORCED_CHEST_MAP.get(
                      ReinforcingMaterials.MAP.get("netherite")),
                  Identifier.of(
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
        BlockRotation.NONE,
        false,
        1,
        1,
        false,
        (context) -> {
          // Arrange
          ServerPlayerEntity player =
              MockServerPlayerHelper.spawn(context, GameMode.SURVIVAL, Vec3d.of(BlockPos.ORIGIN));
          AdvancementEntry entry =
              context.getWorld().getServer().getAdvancementLoader().get(advancementId);
          AdvancementProgress progress = player.getAdvancementTracker().getProgress(entry);

          // Act
          CompletableFuture<Void> futurePartialAct1 = new CompletableFuture<>();
          CompletableFuture<Void> futurePartialAct2 = new CompletableFuture<>();

          Map<String, Boolean> progressMap = new HashMap<String, Boolean>();
          String progressMapKeyBeforeHavingItem = "beforeHavingItem";
          String progressMapKeyAfterHavingItem = "afterHavingItem";

          long tickOrigin = 0;
          context.runAtTick(
              tickOrigin,
              () -> {
                progressMap.put(progressMapKeyBeforeHavingItem, progress.isDone());

                player.giveItemStack(new ItemStack(item));

                futurePartialAct1.complete(null);
              });

          long tickObtained = 1;
          context.runAtTick(
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
                          Text.of(
                              String.format(
                                  "Expected that advancement %s has not been done yet, but it has been already done.",
                                  entry)));
                      context.assertTrue(
                          progressMap.get(progressMapKeyAfterHavingItem),
                          Text.of(
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

                    context.complete();
                  });
        });
  }
}
