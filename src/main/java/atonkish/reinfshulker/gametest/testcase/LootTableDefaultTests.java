package atonkish.reinfshulker.gametest.testcase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.TestContext;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;

import atonkish.reinfcore.util.ReinforcingMaterials;

import atonkish.reinfchest.ReinforcedChestsMod;

import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;
import atonkish.reinfshulker.block.ModBlocks;
import atonkish.reinfshulker.gametest.util.MockServerPlayerHelper;

public class LootTableDefaultTests implements CustomTestMethodInvoker {
    public void invokeTestMethod(TestContext context, Method method) {
        try {
            method.invoke(this, context);
        } catch (InvocationTargetException e) {
            // Ensure that any GameTestException are propagated without wrapping
            if (e.getTargetException() instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }

            throw new RuntimeException("Failed to invoke test method", e);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to invoke test method", e);
        }
    }

    private void test(TestContext context, Block shulkerBoxBlock, Item tool, boolean shouldDrop) {
        String testName = String.format("%s %s %s",
                ReinforcedChestsMod.MOD_ID,
                this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[2].getMethodName())
                .replace(" ", "_");

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
    }

    //
    // Copper Shulker Box
    //

    @GameTest(maxTicks = 1000)
    public void breakCopperShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get((DyeColor) null),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakWhiteCopperShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.WHITE),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakOrangeCopperShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.ORANGE),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakMagentaCopperShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.MAGENTA),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightBlueCopperShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIGHT_BLUE),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakYellowCopperShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.YELLOW),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLimeCopperShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIME),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPinkCopperShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.PINK),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGrayCopperShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.GRAY),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightGrayCopperShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIGHT_GRAY),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakCyanCopperShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.CYAN),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPurpleCopperShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.PURPLE),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlueCopperShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BLUE),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBrownCopperShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BROWN),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGreenCopperShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.GREEN),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakRedCopperShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.RED),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlackCopperShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BLACK),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakCopperShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get((DyeColor) null),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakWhiteCopperShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.WHITE),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakOrangeCopperShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.ORANGE),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakMagentaCopperShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.MAGENTA),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightBlueCopperShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIGHT_BLUE),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakYellowCopperShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.YELLOW),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLimeCopperShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIME),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPinkCopperShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.PINK),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGrayCopperShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.GRAY),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightGrayCopperShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIGHT_GRAY),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakCyanCopperShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.CYAN),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPurpleCopperShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.PURPLE),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlueCopperShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BLUE),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBrownCopperShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BROWN),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGreenCopperShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.GREEN),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakRedCopperShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.RED),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlackCopperShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BLACK),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakCopperShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get((DyeColor) null),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakWhiteCopperShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.WHITE),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakOrangeCopperShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.ORANGE),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakMagentaCopperShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.MAGENTA),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightBlueCopperShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIGHT_BLUE),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakYellowCopperShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.YELLOW),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLimeCopperShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIME),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPinkCopperShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.PINK),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGrayCopperShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.GRAY),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightGrayCopperShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIGHT_GRAY),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakCyanCopperShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.CYAN),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPurpleCopperShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.PURPLE),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlueCopperShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BLUE),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBrownCopperShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BROWN),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGreenCopperShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.GREEN),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakRedCopperShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.RED),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlackCopperShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BLACK),
                Items.AIR,
                true);
    }

    //
    // Iron Shulker Box
    //

    @GameTest(maxTicks = 1000)
    public void breakIronShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get((DyeColor) null),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakWhiteIronShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.WHITE),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakOrangeIronShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.ORANGE),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakMagentaIronShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.MAGENTA),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightBlueIronShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIGHT_BLUE),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakYellowIronShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.YELLOW),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLimeIronShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIME),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPinkIronShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.PINK),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGrayIronShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.GRAY),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightGrayIronShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIGHT_GRAY),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakCyanIronShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.CYAN),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPurpleIronShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.PURPLE),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlueIronShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BLUE),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBrownIronShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BROWN),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGreenIronShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.GREEN),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakRedIronShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.RED),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlackIronShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BLACK),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakIronShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get((DyeColor) null),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakWhiteIronShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.WHITE),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakOrangeIronShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.ORANGE),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakMagentaIronShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.MAGENTA),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightBlueIronShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIGHT_BLUE),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakYellowIronShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.YELLOW),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLimeIronShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIME),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPinkIronShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.PINK),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGrayIronShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.GRAY),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightGrayIronShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIGHT_GRAY),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakCyanIronShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.CYAN),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPurpleIronShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.PURPLE),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlueIronShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BLUE),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBrownIronShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BROWN),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGreenIronShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.GREEN),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakRedIronShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.RED),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlackIronShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BLACK),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakIronShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get((DyeColor) null),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakWhiteIronShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.WHITE),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakOrangeIronShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.ORANGE),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakMagentaIronShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.MAGENTA),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightBlueIronShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIGHT_BLUE),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakYellowIronShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.YELLOW),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLimeIronShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIME),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPinkIronShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.PINK),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGrayIronShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.GRAY),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightGrayIronShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIGHT_GRAY),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakCyanIronShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.CYAN),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPurpleIronShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.PURPLE),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlueIronShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BLUE),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBrownIronShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BROWN),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGreenIronShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.GREEN),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakRedIronShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.RED),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlackIronShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BLACK),
                Items.AIR,
                true);
    }

    //
    // Gold Shulker Box
    //

    @GameTest(maxTicks = 1000)
    public void breakGoldShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get((DyeColor) null),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakWhiteGoldShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.WHITE),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakOrangeGoldShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.ORANGE),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakMagentaGoldShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.MAGENTA),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightBlueGoldShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIGHT_BLUE),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakYellowGoldShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.YELLOW),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLimeGoldShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIME),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPinkGoldShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.PINK),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGrayGoldShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.GRAY),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightGrayGoldShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIGHT_GRAY),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakCyanGoldShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.CYAN),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPurpleGoldShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.PURPLE),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlueGoldShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BLUE),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBrownGoldShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BROWN),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGreenGoldShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.GREEN),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakRedGoldShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.RED),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlackGoldShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BLACK),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGoldShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get((DyeColor) null),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakWhiteGoldShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.WHITE),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakOrangeGoldShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.ORANGE),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakMagentaGoldShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.MAGENTA),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightBlueGoldShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIGHT_BLUE),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakYellowGoldShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.YELLOW),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLimeGoldShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIME),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPinkGoldShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.PINK),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGrayGoldShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.GRAY),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightGrayGoldShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIGHT_GRAY),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakCyanGoldShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.CYAN),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPurpleGoldShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.PURPLE),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlueGoldShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BLUE),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBrownGoldShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BROWN),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGreenGoldShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.GREEN),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakRedGoldShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.RED),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlackGoldShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BLACK),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGoldShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get((DyeColor) null),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakWhiteGoldShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.WHITE),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakOrangeGoldShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.ORANGE),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakMagentaGoldShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.MAGENTA),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightBlueGoldShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIGHT_BLUE),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakYellowGoldShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.YELLOW),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLimeGoldShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIME),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPinkGoldShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.PINK),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGrayGoldShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.GRAY),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightGrayGoldShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIGHT_GRAY),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakCyanGoldShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.CYAN),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPurpleGoldShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.PURPLE),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlueGoldShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BLUE),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBrownGoldShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BROWN),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGreenGoldShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.GREEN),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakRedGoldShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.RED),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlackGoldShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BLACK),
                Items.AIR,
                true);
    }

    //
    // Diamond Shulker Box
    //

    @GameTest(maxTicks = 1000)
    public void breakDiamondShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get((DyeColor) null),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakWhiteDiamondShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.WHITE),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakOrangeDiamondShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.ORANGE),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakMagentaDiamondShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.MAGENTA),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightBlueDiamondShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIGHT_BLUE),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakYellowDiamondShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.YELLOW),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLimeDiamondShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIME),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPinkDiamondShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.PINK),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGrayDiamondShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.GRAY),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightGrayDiamondShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIGHT_GRAY),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakCyanDiamondShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.CYAN),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPurpleDiamondShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.PURPLE),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlueDiamondShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BLUE),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBrownDiamondShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BROWN),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGreenDiamondShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.GREEN),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakRedDiamondShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.RED),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlackDiamondShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BLACK),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakDiamondShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get((DyeColor) null),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakWhiteDiamondShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.WHITE),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakOrangeDiamondShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.ORANGE),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakMagentaDiamondShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.MAGENTA),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightBlueDiamondShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIGHT_BLUE),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakYellowDiamondShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.YELLOW),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLimeDiamondShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIME),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPinkDiamondShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.PINK),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGrayDiamondShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.GRAY),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightGrayDiamondShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIGHT_GRAY),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakCyanDiamondShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.CYAN),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPurpleDiamondShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.PURPLE),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlueDiamondShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BLUE),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBrownDiamondShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BROWN),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGreenDiamondShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.GREEN),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakRedDiamondShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.RED),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlackDiamondShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BLACK),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakDiamondShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get((DyeColor) null),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakWhiteDiamondShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.WHITE),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakOrangeDiamondShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.ORANGE),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakMagentaDiamondShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.MAGENTA),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightBlueDiamondShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIGHT_BLUE),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakYellowDiamondShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.YELLOW),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLimeDiamondShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIME),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPinkDiamondShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.PINK),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGrayDiamondShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.GRAY),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightGrayDiamondShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIGHT_GRAY),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakCyanDiamondShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.CYAN),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPurpleDiamondShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.PURPLE),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlueDiamondShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BLUE),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBrownDiamondShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BROWN),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGreenDiamondShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.GREEN),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakRedDiamondShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.RED),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlackDiamondShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BLACK),
                Items.AIR,
                true);
    }

    //
    // Netherite Shulker Box
    //

    @GameTest(maxTicks = 1000)
    public void breakNetheriteShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get((DyeColor) null),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakWhiteNetheriteShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.WHITE),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakOrangeNetheriteShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.ORANGE),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakMagentaNetheriteShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.MAGENTA),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightBlueNetheriteShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.LIGHT_BLUE),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakYellowNetheriteShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.YELLOW),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLimeNetheriteShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.LIME),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPinkNetheriteShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.PINK),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGrayNetheriteShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.GRAY),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightGrayNetheriteShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.LIGHT_GRAY),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakCyanNetheriteShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.CYAN),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPurpleNetheriteShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.PURPLE),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlueNetheriteShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.BLUE),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBrownNetheriteShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.BROWN),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGreenNetheriteShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.GREEN),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakRedNetheriteShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.RED),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlackNetheriteShulkerBoxWithNetheritePickaxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.BLACK),
                Items.NETHERITE_PICKAXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakNetheriteShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get((DyeColor) null),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakWhiteNetheriteShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.WHITE),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakOrangeNetheriteShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.ORANGE),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakMagentaNetheriteShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.MAGENTA),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightBlueNetheriteShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.LIGHT_BLUE),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakYellowNetheriteShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.YELLOW),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLimeNetheriteShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.LIME),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPinkNetheriteShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.PINK),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGrayNetheriteShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.GRAY),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightGrayNetheriteShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.LIGHT_GRAY),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakCyanNetheriteShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.CYAN),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPurpleNetheriteShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.PURPLE),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlueNetheriteShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.BLUE),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBrownNetheriteShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.BROWN),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGreenNetheriteShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.GREEN),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakRedNetheriteShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.RED),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlackNetheriteShulkerBoxWithNetheriteAxe(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.BLACK),
                Items.NETHERITE_AXE,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakNetheriteShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get((DyeColor) null),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakWhiteNetheriteShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.WHITE),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakOrangeNetheriteShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.ORANGE),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakMagentaNetheriteShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.MAGENTA),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightBlueNetheriteShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.LIGHT_BLUE),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakYellowNetheriteShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.YELLOW),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLimeNetheriteShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.LIME),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPinkNetheriteShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.PINK),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGrayNetheriteShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.GRAY),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakLightGrayNetheriteShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.LIGHT_GRAY),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakCyanNetheriteShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.CYAN),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakPurpleNetheriteShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.PURPLE),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlueNetheriteShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.BLUE),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBrownNetheriteShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.BROWN),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakGreenNetheriteShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.GREEN),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakRedNetheriteShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.RED),
                Items.AIR,
                true);
    }

    @GameTest(maxTicks = 1000)
    public void breakBlackNetheriteShulkerBoxWithoutTools(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.BLACK),
                Items.AIR,
                true);
    }
}
