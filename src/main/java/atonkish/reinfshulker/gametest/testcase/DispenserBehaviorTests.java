package atonkish.reinfshulker.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.test.StructureTestUtil;
import net.minecraft.test.TestFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import atonkish.reinfcore.util.ReinforcingMaterials;
import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;
import atonkish.reinfshulker.block.ModBlocks;

public class DispenserBehaviorTests {
    private static final String BATCH_ID = String.format("%s:DispenserBehaviorBatch",
            ReinforcedShulkerBoxesMod.MOD_ID);

    public static final Collection<TestFunction> TEST_FUNCTIONS = new ArrayList<>() {
        {
            // Copper Shulker Box
            for (Block block : ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("copper"))
                    .values()) {
                add(DispenserBehaviorTests.createTest(
                        String.format("Dispense %s", block.getName().getString()), block));
            }

            // Iron Shulker Box
            for (Block block : ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("iron"))
                    .values()) {
                add(DispenserBehaviorTests.createTest(
                        String.format("Dispense %s", block.getName().getString()), block));
            }

            // Gold Shulker Box
            for (Block block : ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("gold"))
                    .values()) {
                add(DispenserBehaviorTests.createTest(
                        String.format("Dispense %s", block.getName().getString()), block));
            }

            // Diamond Shulker Box
            for (Block block : ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("diamond"))
                    .values()) {
                add(DispenserBehaviorTests.createTest(
                        String.format("Dispense %s", block.getName().getString()), block));
            }

            // Netherite Shulker Box
            for (Block block : ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("netherite"))
                    .values()) {
                add(DispenserBehaviorTests.createTest(
                        String.format("Dispense %s", block.getName().getString()), block));
            }
        }
    };

    private static TestFunction createTest(String name, Block shulkerBoxBlock) {
        String testName = String.format("%s %s %s",
                ReinforcedShulkerBoxesMod.MOD_ID,
                DispenserBehaviorTests.class.getSimpleName(),
                name)
                .replace(" ", "_");

        return new TestFunction(
                DispenserBehaviorTests.BATCH_ID,
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
                    context.setBlockState(blockPos, Blocks.DISPENSER
                            .getDefaultState().with(DispenserBlock.FACING, Direction.SOUTH));

                    DispenserBlockEntity entity = (DispenserBlockEntity) context.getBlockEntity(blockPos);
                    entity.setStack(0, new ItemStack(shulkerBoxBlock.asItem()));

                    // Act
                    CompletableFuture<Void> futurePartialAct1 = new CompletableFuture<>();
                    CompletableFuture<Void> futurePartialAct2 = new CompletableFuture<>();

                    long tickOrigin = 0;
                    context.runAtTick(tickOrigin, () -> {
                        context.putAndRemoveRedstoneBlock(blockPos.up(1), 0);

                        futurePartialAct1.complete(null);
                    });

                    long tickShulkerBoxPlaced = 4;
                    context.runAtTick(tickShulkerBoxPlaced, () -> {
                        futurePartialAct2.complete(null);
                    });

                    // Assert
                    CompletableFuture.allOf(futurePartialAct1, futurePartialAct2).thenRun(() -> {
                        try {
                            context.expectBlock(shulkerBoxBlock, blockPos.south(1));
                        } catch (Exception e) {
                            ReinforcedShulkerBoxesMod.LOGGER.error("[{}] {}", testName, e.getMessage());
                            throw e;
                        }

                        context.complete();
                    });
                });
    }
}
