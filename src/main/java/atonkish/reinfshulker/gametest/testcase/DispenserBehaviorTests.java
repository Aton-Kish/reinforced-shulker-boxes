package atonkish.reinfshulker.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import atonkish.reinfcore.gametest.TestFunction;
import atonkish.reinfcore.util.ReinforcingMaterials;
import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;
import atonkish.reinfshulker.block.ModBlocks;
import atonkish.reinfshulker.gametest.util.TestIdentifier;

public class DispenserBehaviorTests {
  private static final String TEST_ENVIRONMENT_DEFAULT =
      String.format("%s:dispenser_behavior/default", ReinforcedShulkerBoxesMod.MOD_ID);
  private static final String TEST_STRUCTURE_EMPTY = "fabric-gametest-api-v1:empty";

  public static final Collection<TestFunction> TEST_FUNCTIONS =
      new ArrayList<>() {
        {
          // Copper Shulker Box
          for (Block block :
              ModBlocks.REINFORCED_SHULKER_BOX_MAP
                  .get(ReinforcingMaterials.MAP.get("copper"))
                  .values()) {
            add(
                DispenserBehaviorTests.createTest(
                    String.format("Dispense %s", block.getName().getString()), block));
          }

          // Iron Shulker Box
          for (Block block :
              ModBlocks.REINFORCED_SHULKER_BOX_MAP
                  .get(ReinforcingMaterials.MAP.get("iron"))
                  .values()) {
            add(
                DispenserBehaviorTests.createTest(
                    String.format("Dispense %s", block.getName().getString()), block));
          }

          // Gold Shulker Box
          for (Block block :
              ModBlocks.REINFORCED_SHULKER_BOX_MAP
                  .get(ReinforcingMaterials.MAP.get("gold"))
                  .values()) {
            add(
                DispenserBehaviorTests.createTest(
                    String.format("Dispense %s", block.getName().getString()), block));
          }

          // Diamond Shulker Box
          for (Block block :
              ModBlocks.REINFORCED_SHULKER_BOX_MAP
                  .get(ReinforcingMaterials.MAP.get("diamond"))
                  .values()) {
            add(
                DispenserBehaviorTests.createTest(
                    String.format("Dispense %s", block.getName().getString()), block));
          }

          // Netherite Shulker Box
          for (Block block :
              ModBlocks.REINFORCED_SHULKER_BOX_MAP
                  .get(ReinforcingMaterials.MAP.get("netherite"))
                  .values()) {
            add(
                DispenserBehaviorTests.createTest(
                    String.format("Dispense %s", block.getName().getString()), block));
          }
        }
      };

  private static TestFunction createTest(String name, Block shulkerBoxBlock) {
    Identifier testIdentifier =
        TestIdentifier.of(ReinforcedShulkerBoxesMod.MOD_ID, DispenserBehaviorTests.class, name);

    return new TestFunction(
        testIdentifier,
        DispenserBehaviorTests.TEST_ENVIRONMENT_DEFAULT,
        DispenserBehaviorTests.TEST_STRUCTURE_EMPTY,
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
          BlockPos blockPos = BlockPos.ORIGIN;
          context.setBlockState(
              blockPos,
              Blocks.DISPENSER.getDefaultState().with(DispenserBlock.FACING, Direction.SOUTH));

          DispenserBlockEntity entity =
              context.getBlockEntity(blockPos, DispenserBlockEntity.class);
          entity.setStack(0, new ItemStack(shulkerBoxBlock.asItem()));

          // Act
          CompletableFuture<Void> futurePartialAct1 = new CompletableFuture<>();
          CompletableFuture<Void> futurePartialAct2 = new CompletableFuture<>();

          long tickOrigin = 0;
          context.runAtTick(
              tickOrigin,
              () -> {
                context.putAndRemoveRedstoneBlock(blockPos.up(1), 0);

                futurePartialAct1.complete(null);
              });

          long tickShulkerBoxPlaced = 4;
          context.runAtTick(
              tickShulkerBoxPlaced,
              () -> {
                futurePartialAct2.complete(null);
              });

          // Assert
          CompletableFuture.allOf(futurePartialAct1, futurePartialAct2)
              .thenRun(
                  () -> {
                    try {
                      context.expectBlock(shulkerBoxBlock, blockPos.south(1));
                    } catch (Exception e) {
                      ReinforcedShulkerBoxesMod.LOGGER.error(
                          "[{}] {}", testIdentifier, e.getMessage());
                      throw e;
                    }

                    context.complete();
                  });
        });
  }
}
