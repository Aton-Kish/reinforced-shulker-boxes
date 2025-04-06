package atonkish.reinfshulker.gametest.testcase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.test.TestContext;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;

import atonkish.reinfcore.util.ReinforcingMaterials;

import atonkish.reinfchest.ReinforcedChestsMod;

import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;
import atonkish.reinfshulker.block.ModBlocks;

public class DispenserBehaviorDefaultTests implements CustomTestMethodInvoker {
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

    private void test(TestContext context, Block shulkerBoxBlock) {
        String testName = String.format("%s %s %s",
                ReinforcedChestsMod.MOD_ID,
                this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[2].getMethodName())
                .replace(" ", "_");

        // Arrange
        BlockPos blockPos = BlockPos.ORIGIN;
        context.setBlockState(blockPos, Blocks.DISPENSER
                .getDefaultState().with(DispenserBlock.FACING, Direction.SOUTH));

        DispenserBlockEntity entity = context.getBlockEntity(blockPos, DispenserBlockEntity.class);
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
    }

    //
    // Copper Shulker Box
    //

    @GameTest
    public void dispenseCopperShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get((DyeColor) null));
    }

    @GameTest
    public void dispenseWhiteCopperShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.WHITE));
    }

    @GameTest
    public void dispenseOrangeCopperShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.ORANGE));
    }

    @GameTest
    public void dispenseMagentaCopperShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.MAGENTA));
    }

    @GameTest
    public void dispenseLightBlueCopperShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIGHT_BLUE));
    }

    @GameTest
    public void dispenseYellowCopperShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.YELLOW));
    }

    @GameTest
    public void dispenseLimeCopperShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIME));
    }

    @GameTest
    public void dispensePinkCopperShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.PINK));
    }

    @GameTest
    public void dispenseGrayCopperShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.GRAY));
    }

    @GameTest
    public void dispenseLightGrayCopperShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIGHT_GRAY));
    }

    @GameTest
    public void dispenseCyanCopperShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.CYAN));
    }

    @GameTest
    public void dispensePurpleCopperShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.PURPLE));
    }

    @GameTest
    public void dispenseBlueCopperShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BLUE));
    }

    @GameTest
    public void dispenseBrownCopperShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BROWN));
    }

    @GameTest
    public void dispenseGreenCopperShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.GREEN));
    }

    @GameTest
    public void dispenseRedCopperShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.RED));
    }

    @GameTest
    public void dispenseBlackCopperShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BLACK));
    }

    //
    // Iron Shulker Box
    //

    @GameTest
    public void dispenseIronShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get((DyeColor) null));
    }

    @GameTest
    public void dispenseWhiteIronShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.WHITE));
    }

    @GameTest
    public void dispenseOrangeIronShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.ORANGE));
    }

    @GameTest
    public void dispenseMagentaIronShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.MAGENTA));
    }

    @GameTest
    public void dispenseLightBlueIronShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIGHT_BLUE));
    }

    @GameTest
    public void dispenseYellowIronShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.YELLOW));
    }

    @GameTest
    public void dispenseLimeIronShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIME));
    }

    @GameTest
    public void dispensePinkIronShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.PINK));
    }

    @GameTest
    public void dispenseGrayIronShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.GRAY));
    }

    @GameTest
    public void dispenseLightGrayIronShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIGHT_GRAY));
    }

    @GameTest
    public void dispenseCyanIronShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.CYAN));
    }

    @GameTest
    public void dispensePurpleIronShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.PURPLE));
    }

    @GameTest
    public void dispenseBlueIronShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BLUE));
    }

    @GameTest
    public void dispenseBrownIronShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BROWN));
    }

    @GameTest
    public void dispenseGreenIronShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.GREEN));
    }

    @GameTest
    public void dispenseRedIronShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.RED));
    }

    @GameTest
    public void dispenseBlackIronShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BLACK));
    }

    //
    // Gold Shulker Box
    //

    @GameTest
    public void dispenseGoldShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get((DyeColor) null));
    }

    @GameTest
    public void dispenseWhiteGoldShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.WHITE));
    }

    @GameTest
    public void dispenseOrangeGoldShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.ORANGE));
    }

    @GameTest
    public void dispenseMagentaGoldShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.MAGENTA));
    }

    @GameTest
    public void dispenseLightBlueGoldShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIGHT_BLUE));
    }

    @GameTest
    public void dispenseYellowGoldShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.YELLOW));
    }

    @GameTest
    public void dispenseLimeGoldShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIME));
    }

    @GameTest
    public void dispensePinkGoldShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.PINK));
    }

    @GameTest
    public void dispenseGrayGoldShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.GRAY));
    }

    @GameTest
    public void dispenseLightGrayGoldShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIGHT_GRAY));
    }

    @GameTest
    public void dispenseCyanGoldShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.CYAN));
    }

    @GameTest
    public void dispensePurpleGoldShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.PURPLE));
    }

    @GameTest
    public void dispenseBlueGoldShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BLUE));
    }

    @GameTest
    public void dispenseBrownGoldShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BROWN));
    }

    @GameTest
    public void dispenseGreenGoldShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.GREEN));
    }

    @GameTest
    public void dispenseRedGoldShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.RED));
    }

    @GameTest
    public void dispenseBlackGoldShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BLACK));
    }

    //
    // Diamond Shulker Box
    //

    @GameTest
    public void dispenseDiamondShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get((DyeColor) null));
    }

    @GameTest
    public void dispenseWhiteDiamondShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.WHITE));
    }

    @GameTest
    public void dispenseOrangeDiamondShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.ORANGE));
    }

    @GameTest
    public void dispenseMagentaDiamondShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.MAGENTA));
    }

    @GameTest
    public void dispenseLightBlueDiamondShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIGHT_BLUE));
    }

    @GameTest
    public void dispenseYellowDiamondShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.YELLOW));
    }

    @GameTest
    public void dispenseLimeDiamondShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIME));
    }

    @GameTest
    public void dispensePinkDiamondShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.PINK));
    }

    @GameTest
    public void dispenseGrayDiamondShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.GRAY));
    }

    @GameTest
    public void dispenseLightGrayDiamondShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIGHT_GRAY));
    }

    @GameTest
    public void dispenseCyanDiamondShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.CYAN));
    }

    @GameTest
    public void dispensePurpleDiamondShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.PURPLE));
    }

    @GameTest
    public void dispenseBlueDiamondShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BLUE));
    }

    @GameTest
    public void dispenseBrownDiamondShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BROWN));
    }

    @GameTest
    public void dispenseGreenDiamondShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.GREEN));
    }

    @GameTest
    public void dispenseRedDiamondShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.RED));
    }

    @GameTest
    public void dispenseBlackDiamondShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BLACK));
    }

    //
    // Netherite Shulker Box
    //

    @GameTest
    public void dispenseNetheriteShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get((DyeColor) null));
    }

    @GameTest
    public void dispenseWhiteNetheriteShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.WHITE));
    }

    @GameTest
    public void dispenseOrangeNetheriteShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.ORANGE));
    }

    @GameTest
    public void dispenseMagentaNetheriteShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.MAGENTA));
    }

    @GameTest
    public void dispenseLightBlueNetheriteShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.LIGHT_BLUE));
    }

    @GameTest
    public void dispenseYellowNetheriteShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.YELLOW));
    }

    @GameTest
    public void dispenseLimeNetheriteShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.LIME));
    }

    @GameTest
    public void dispensePinkNetheriteShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.PINK));
    }

    @GameTest
    public void dispenseGrayNetheriteShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.GRAY));
    }

    @GameTest
    public void dispenseLightGrayNetheriteShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.LIGHT_GRAY));
    }

    @GameTest
    public void dispenseCyanNetheriteShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.CYAN));
    }

    @GameTest
    public void dispensePurpleNetheriteShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.PURPLE));
    }

    @GameTest
    public void dispenseBlueNetheriteShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.BLUE));
    }

    @GameTest
    public void dispenseBrownNetheriteShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.BROWN));
    }

    @GameTest
    public void dispenseGreenNetheriteShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.GREEN));
    }

    @GameTest
    public void dispenseRedNetheriteShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.RED));
    }

    @GameTest
    public void dispenseBlackNetheriteShulkerBox(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.BLACK));
    }
}
