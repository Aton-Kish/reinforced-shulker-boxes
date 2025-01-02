package atonkish.reinfshulker.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.StructureTestUtil;
import net.minecraft.test.TestFunction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import atonkish.reinfcore.util.ReinforcingMaterials;
import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;
import atonkish.reinfshulker.block.ModBlocks;
import atonkish.reinfshulker.gametest.util.MockServerPlayerHelper;

public class LootTableTests {
    private static final String BATCH_ID = String.format("%s:LootTableBatch",
            ReinforcedShulkerBoxesMod.MOD_ID);

    public static final Collection<TestFunction> TEST_FUNCTIONS = new ArrayList<>() {
        {
            // Copper Shulker Box
            for (Block block : ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("copper"))
                    .values()) {
                add(LootTableTests.createTest(
                        String.format("Break %s with Netherite Pickaxe", block.getName().getString()),
                        block,
                        Items.NETHERITE_PICKAXE,
                        true));
                add(LootTableTests.createTest(
                        String.format("Break %s with Netherite Axe", block.getName().getString()),
                        block,
                        Items.NETHERITE_AXE,
                        true));
                add(LootTableTests.createTest(
                        String.format("Break %s without tools", block.getName().getString()),
                        block,
                        Items.AIR,
                        true));
            }

            // Iron Shulker Box
            for (Block block : ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("iron"))
                    .values()) {
                add(LootTableTests.createTest(
                        String.format("Break %s with Netherite Pickaxe", block.getName().getString()),
                        block,
                        Items.NETHERITE_PICKAXE,
                        true));
                add(LootTableTests.createTest(
                        String.format("Break %s with Netherite Axe", block.getName().getString()),
                        block,
                        Items.NETHERITE_AXE,
                        true));
                add(LootTableTests.createTest(
                        String.format("Break %s without tools", block.getName().getString()),
                        block,
                        Items.AIR,
                        true));
            }

            // Gold Shulker Box
            for (Block block : ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("gold"))
                    .values()) {
                add(LootTableTests.createTest(
                        String.format("Break %s with Netherite Pickaxe", block.getName().getString()),
                        block,
                        Items.NETHERITE_PICKAXE,
                        true));
                add(LootTableTests.createTest(
                        String.format("Break %s with Netherite Axe", block.getName().getString()),
                        block,
                        Items.NETHERITE_AXE,
                        true));
                add(LootTableTests.createTest(
                        String.format("Break %s without tools", block.getName().getString()),
                        block,
                        Items.AIR,
                        true));
            }

            // Diamond Shulker Box
            for (Block block : ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("diamond"))
                    .values()) {
                add(LootTableTests.createTest(
                        String.format("Break %s with Netherite Pickaxe", block.getName().getString()),
                        block,
                        Items.NETHERITE_PICKAXE,
                        true));
                add(LootTableTests.createTest(
                        String.format("Break %s with Netherite Axe", block.getName().getString()),
                        block,
                        Items.NETHERITE_AXE,
                        true));
                add(LootTableTests.createTest(
                        String.format("Break %s without tools", block.getName().getString()),
                        block,
                        Items.AIR,
                        true));
            }

            // Netherite Shulker Box
            for (Block block : ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("netherite"))
                    .values()) {
                add(LootTableTests.createTest(
                        String.format("Break %s with Netherite Pickaxe", block.getName().getString()),
                        block,
                        Items.NETHERITE_PICKAXE,
                        true));
                add(LootTableTests.createTest(
                        String.format("Break %s with Netherite Axe", block.getName().getString()),
                        block,
                        Items.NETHERITE_AXE,
                        true));
                add(LootTableTests.createTest(
                        String.format("Break %s without tools", block.getName().getString()),
                        block,
                        Items.AIR,
                        true));
            }
        }
    };

    private static TestFunction createTest(String name, Block shulkerBoxBlock, Item tool, boolean shouldDrop) {
        String testName = String.format("%s %s %s",
                ReinforcedShulkerBoxesMod.MOD_ID,
                LootTableTests.class.getSimpleName(),
                name)
                .replace(" ", "_");

        return new TestFunction(
                LootTableTests.BATCH_ID,
                testName,
                FabricGameTest.EMPTY_STRUCTURE,
                StructureTestUtil.getRotation(0),
                1000,
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

                    ServerPlayerEntity player = MockServerPlayerHelper.spawn(context,
                            GameMode.SURVIVAL, Vec3d.of(blockPos.south(4)));
                    player.setStackInHand(Hand.MAIN_HAND, new ItemStack(tool));

                    // Act
                    CompletableFuture<Void> futurePartialAct1 = new CompletableFuture<>();
                    CompletableFuture<Void> futurePartialAct2 = new CompletableFuture<>();

                    long tickOrigin = 0;
                    context.runAtTick(tickOrigin, () -> {
                        player.interactionManager.processBlockBreakingAction(
                                context.getAbsolutePos(blockPos), PlayerActionC2SPacket.Action.START_DESTROY_BLOCK,
                                Direction.NORTH, context.getWorld().getHeight(), 0);

                        futurePartialAct1.complete(null);
                    });

                    long tickBlockBreaking = (long) Math.ceil(
                            1.0D / context.getBlockState(blockPos).calcBlockBreakingDelta(player,
                                    context.getWorld(), blockPos));
                    context.runAtTick(tickBlockBreaking, () -> {
                        player.interactionManager.processBlockBreakingAction(
                                context.getAbsolutePos(blockPos),
                                PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK,
                                Direction.NORTH, context.getWorld().getHeight(), 0);

                        futurePartialAct2.complete(null);
                    });

                    ReinforcedShulkerBoxesMod.LOGGER.info("[{}] {} can be mined in {} ticks by {}",
                            testName,
                            shulkerBoxBlock.getName().getString(),
                            tickBlockBreaking,
                            tool.getName().getString());

                    // Assert
                    CompletableFuture.allOf(futurePartialAct1, futurePartialAct2).thenRun(() -> {
                        try {
                            context.expectBlock(Blocks.AIR, blockPos);
                            context.expectItemsAt(shulkerBoxBlock.asItem(), blockPos, 1, shouldDrop ? 1 : 0);
                        } catch (Exception e) {
                            ReinforcedShulkerBoxesMod.LOGGER.error("[{}] {}", testName, e.getMessage());
                            throw e;
                        } finally {
                            MockServerPlayerHelper.destroy(context, player);
                        }

                        context.complete();
                    });
                });
    }
}
