package atonkish.reinfshulker.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.block.Block;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import atonkish.reinfcore.gametest.TestFunction;
import atonkish.reinfcore.util.ReinforcingMaterials;
import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;
import atonkish.reinfshulker.block.ModBlocks;
import atonkish.reinfshulker.gametest.util.TestIdentifier;

public class InventoryTests {
  private static final String TEST_ENVIRONMENT_DEFAULT =
      String.format("%s:inventory/default", ReinforcedShulkerBoxesMod.MOD_ID);
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
                InventoryTests.createTest(
                    String.format("%s inventory size", block.getName().getString()), block, 45));
          }

          // Iron Shulker Box
          for (Block block :
              ModBlocks.REINFORCED_SHULKER_BOX_MAP
                  .get(ReinforcingMaterials.MAP.get("iron"))
                  .values()) {
            add(
                InventoryTests.createTest(
                    String.format("%s inventory size", block.getName().getString()), block, 54));
          }

          // Gold Shulker Box
          for (Block block :
              ModBlocks.REINFORCED_SHULKER_BOX_MAP
                  .get(ReinforcingMaterials.MAP.get("gold"))
                  .values()) {
            add(
                InventoryTests.createTest(
                    String.format("%s inventory size", block.getName().getString()), block, 81));
          }

          // Diamond Shulker Box
          for (Block block :
              ModBlocks.REINFORCED_SHULKER_BOX_MAP
                  .get(ReinforcingMaterials.MAP.get("diamond"))
                  .values()) {
            add(
                InventoryTests.createTest(
                    String.format("%s inventory size", block.getName().getString()), block, 108));
          }

          // Netherite Shulker Box
          for (Block block :
              ModBlocks.REINFORCED_SHULKER_BOX_MAP
                  .get(ReinforcingMaterials.MAP.get("netherite"))
                  .values()) {
            add(
                InventoryTests.createTest(
                    String.format("%s inventory size", block.getName().getString()), block, 108));
          }
        }
      };

  private static TestFunction createTest(String name, Block shulkerBoxBlock, int size) {
    Identifier testIdentifier =
        TestIdentifier.of(ReinforcedShulkerBoxesMod.MOD_ID, InventoryTests.class, name);

    return new TestFunction(
        testIdentifier,
        InventoryTests.TEST_ENVIRONMENT_DEFAULT,
        InventoryTests.TEST_STRUCTURE_EMPTY,
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
          context.setBlockState(blockPos, shulkerBoxBlock);

          // Act
          ShulkerBoxBlockEntity entity =
              context.getBlockEntity(blockPos, ShulkerBoxBlockEntity.class);

          // Assert
          try {
            context.assertEquals(
                entity.size(),
                size,
                Text.of(String.format("%s inventory size", shulkerBoxBlock.getName().getString())));
          } catch (Exception e) {
            ReinforcedShulkerBoxesMod.LOGGER.error("[{}] {}", testIdentifier, e.getMessage());
            throw e;
          }

          context.complete();
        });
  }
}
