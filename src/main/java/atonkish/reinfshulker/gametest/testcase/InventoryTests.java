package atonkish.reinfshulker.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;

import net.minecraft.block.Block;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.test.StructureTestUtil;
import net.minecraft.test.TestFunction;
import net.minecraft.util.math.BlockPos;

import atonkish.reinfcore.util.ReinforcingMaterials;
import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;
import atonkish.reinfshulker.block.ModBlocks;

public class InventoryTests {
    private static final String BATCH_ID = String.format("%s:InventoryBatch",
            ReinforcedShulkerBoxesMod.MOD_ID);

    public static final Collection<TestFunction> TEST_FUNCTIONS = new ArrayList<>() {
        {
            // Copper Shulker Box
            for (Block block : ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("copper"))
                    .values()) {
                add(InventoryTests.createTest(
                        String.format("%s inventory size", block.getName().getString()),
                        block,
                        45));
            }

            // Iron Shulker Box
            for (Block block : ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("iron"))
                    .values()) {
                add(InventoryTests.createTest(
                        String.format("%s inventory size", block.getName().getString()),
                        block,
                        54));
            }

            // Gold Shulker Box
            for (Block block : ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("gold"))
                    .values()) {
                add(InventoryTests.createTest(
                        String.format("%s inventory size", block.getName().getString()),
                        block,
                        81));
            }

            // Diamond Shulker Box
            for (Block block : ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("diamond"))
                    .values()) {
                add(InventoryTests.createTest(
                        String.format("%s inventory size", block.getName().getString()),
                        block,
                        108));
            }

            // Netherite Shulker Box
            for (Block block : ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("netherite"))
                    .values()) {
                add(InventoryTests.createTest(
                        String.format("%s inventory size", block.getName().getString()),
                        block,
                        108));
            }
        }
    };

    private static TestFunction createTest(String name, Block shulkerBoxBlock, int size) {
        String testName = String.format("%s %s %s",
                ReinforcedShulkerBoxesMod.MOD_ID,
                InventoryTests.class.getSimpleName(),
                name)
                .replace(" ", "_");
        return new TestFunction(
                InventoryTests.BATCH_ID,
                testName,
                FabricGameTest.EMPTY_STRUCTURE,
                StructureTestUtil.getRotation(0),
                100,
                0L,
                true,
                false,
                1,
                1,
                false,
                (context) -> {
                    // Arrange
                    BlockPos blockPos = BlockPos.ORIGIN;
                    context.setBlockState(blockPos, shulkerBoxBlock);

                    // Act
                    ShulkerBoxBlockEntity entity = (ShulkerBoxBlockEntity) context.getBlockEntity(blockPos);

                    // Assert
                    try {
                        context.assertEquals(entity.size(), size,
                                String.format("%s inventory size", shulkerBoxBlock.getName().getString()));
                    } catch (Exception e) {
                        ReinforcedShulkerBoxesMod.LOGGER.error("[{}] {}", testName, e.getMessage());
                        throw e;
                    }

                    context.complete();
                });
    }
}
