package atonkish.reinfshulker.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import atonkish.reinfcore.gametest.TestFunction;
import atonkish.reinfcore.util.ReinforcingMaterials;
import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;
import atonkish.reinfshulker.block.ModBlocks;
import atonkish.reinfshulker.block.ReinforcedShulkerBoxBlock;
import atonkish.reinfshulker.gametest.util.MockServerPlayerHelper;
import atonkish.reinfshulker.gametest.util.TestIdentifier;
import atonkish.reinfshulker.item.ModItems;
import atonkish.reinfshulker.stat.ModStats;

public class CauldronBehaviorTests {
  private static final String TEST_ENVIRONMENT_DEFAULT =
      String.format("%s:cauldron_behavior/default", ReinforcedShulkerBoxesMod.MOD_ID);
  private static final String TEST_STRUCTURE_EMPTY = "fabric-gametest-api-v1:empty";

  public static final Collection<TestFunction> TEST_FUNCTIONS =
      new ArrayList<>() {
        {
          // Copper Shulker Box
          for (DyeColor color : DyeColor.values()) {
            ReinforcedShulkerBoxBlock shulkerBoxBlock =
                (ReinforcedShulkerBoxBlock)
                    ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(color);

            add(
                CauldronBehaviorTests.createTest(
                    String.format("Clean %s", shulkerBoxBlock.getName().getString()),
                    shulkerBoxBlock));
          }

          // Iron Shulker Box
          for (DyeColor color : DyeColor.values()) {
            ReinforcedShulkerBoxBlock shulkerBoxBlock =
                (ReinforcedShulkerBoxBlock)
                    ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(color);

            add(
                CauldronBehaviorTests.createTest(
                    String.format("Clean %s", shulkerBoxBlock.getName().getString()),
                    shulkerBoxBlock));
          }

          // Gold Shulker Box
          for (DyeColor color : DyeColor.values()) {
            ReinforcedShulkerBoxBlock shulkerBoxBlock =
                (ReinforcedShulkerBoxBlock)
                    ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(color);

            add(
                CauldronBehaviorTests.createTest(
                    String.format("Clean %s", shulkerBoxBlock.getName().getString()),
                    shulkerBoxBlock));
          }

          // Diamond Shulker Box
          for (DyeColor color : DyeColor.values()) {
            ReinforcedShulkerBoxBlock shulkerBoxBlock =
                (ReinforcedShulkerBoxBlock)
                    ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(color);

            add(
                CauldronBehaviorTests.createTest(
                    String.format("Clean %s", shulkerBoxBlock.getName().getString()),
                    shulkerBoxBlock));
          }

          // Netherite Shulker Box
          for (DyeColor color : DyeColor.values()) {
            ReinforcedShulkerBoxBlock shulkerBoxBlock =
                (ReinforcedShulkerBoxBlock)
                    ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(color);

            add(
                CauldronBehaviorTests.createTest(
                    String.format("Clean %s", shulkerBoxBlock.getName().getString()),
                    shulkerBoxBlock));
          }
        }
      };

  private static TestFunction createTest(String name, ReinforcedShulkerBoxBlock shulkerBoxBlock) {
    Identifier testIdentifier =
        TestIdentifier.of(ReinforcedShulkerBoxesMod.MOD_ID, CauldronBehaviorTests.class, name);

    return new TestFunction(
        testIdentifier,
        CauldronBehaviorTests.TEST_ENVIRONMENT_DEFAULT,
        CauldronBehaviorTests.TEST_STRUCTURE_EMPTY,
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
          BlockPos blockPos = BlockPos.ZERO;
          context.setBlock(
              blockPos,
              Blocks.WATER_CAULDRON
                  .defaultBlockState()
                  .setValue(BlockStateProperties.LEVEL_CAULDRON, 3));

          ServerPlayer player =
              MockServerPlayerHelper.spawn(
                  context, GameType.SURVIVAL, Vec3.atLowerCornerOf(blockPos.south(4)));
          player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(shulkerBoxBlock.asItem()));

          Stat<Identifier> stat =
              Stats.CUSTOM.get(
                  ModStats.CLEAN_REINFORCED_SHULKER_BOX_MAP.get(shulkerBoxBlock.getMaterial()));

          // Act
          CompletableFuture<Void> futurePartialAct1 = new CompletableFuture<>();
          CompletableFuture<Void> futurePartialAct2 = new CompletableFuture<>();

          Map<String, Integer> statMap = new HashMap<String, Integer>();
          String statMapKeyBeforeCleaning = "beforeCleaning";
          String statMapKeyAfterCleaning = "afterCleaning";

          long tickOrigin = 0;
          context.runAtTickTime(
              tickOrigin,
              () -> {
                statMap.put(statMapKeyBeforeCleaning, player.getStats().getValue(stat));

                context.useBlock(blockPos, player);

                futurePartialAct1.complete(null);
              });

          long tickShulkerBoxCleaning = 1;
          context.runAtTickTime(
              tickShulkerBoxCleaning,
              () -> {
                statMap.put(statMapKeyAfterCleaning, player.getStats().getValue(stat));

                futurePartialAct2.complete(null);
              });

          // Assert
          CompletableFuture.allOf(futurePartialAct1, futurePartialAct2)
              .thenRun(
                  () -> {
                    try {
                      context.assertValueEqual(
                          player.getMainHandItem().getItem(),
                          ModItems.REINFORCED_SHULKER_BOX_MAP
                              .get(shulkerBoxBlock.getMaterial())
                              .get((DyeColor) null),
                          Component.nullToEmpty("main hand item"));
                      context.assertValueEqual(
                          context
                              .getBlockState(blockPos)
                              .getValue(BlockStateProperties.LEVEL_CAULDRON),
                          2,
                          Component.nullToEmpty("fluid level"));
                      context.assertValueEqual(
                          statMap.get(statMapKeyAfterCleaning)
                              - statMap.get(statMapKeyBeforeCleaning),
                          1,
                          Component.nullToEmpty(String.format("diff %s value", stat.getName())));
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
