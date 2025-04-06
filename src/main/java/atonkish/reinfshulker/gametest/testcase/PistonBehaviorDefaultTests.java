package atonkish.reinfshulker.gametest.testcase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.test.TestContext;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;

import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;

import atonkish.reinfcore.util.ReinforcingMaterials;

import atonkish.reinfchest.ReinforcedChestsMod;

import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;
import atonkish.reinfshulker.block.ModBlocks;
import atonkish.reinfshulker.block.ReinforcedShulkerBoxBlock;

public class PistonBehaviorDefaultTests implements CustomTestMethodInvoker {
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
        context.setBlockState(blockPos, shulkerBoxBlock);
        context.setBlockState(blockPos.south(1), Blocks.PISTON);

        // Act
        CompletableFuture<Void> futurePartialAct1 = new CompletableFuture<>();
        CompletableFuture<Void> futurePartialAct2 = new CompletableFuture<>();

        long tickOrigin = 0;
        context.runAtTick(tickOrigin, () -> {
            context.putAndRemoveRedstoneBlock(blockPos.south(1).up(1), 1);

            futurePartialAct1.complete(null);
        });

        long tickShulkerBoxBreaking = 2;
        context.runAtTick(tickShulkerBoxBreaking, () -> {
            futurePartialAct2.complete(null);
        });

        // Assert
        CompletableFuture.allOf(futurePartialAct1, futurePartialAct2).thenRun(() -> {
            try {
                context.expectBlock(Blocks.AIR, blockPos);
                context.expectItemAt(shulkerBoxBlock.asItem(), blockPos, 1);
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

    @GameTest(maxTicks = 100)
    public void pistonBreaksCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get((DyeColor) null));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksWhiteCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.WHITE));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksOrangeCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.ORANGE));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksMagentaCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.MAGENTA));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksLightBlueCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIGHT_BLUE));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksYellowCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.YELLOW));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksLimeCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIME));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksPinkCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.PINK));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksGrayCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.GRAY));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksLightGrayCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIGHT_GRAY));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksCyanCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.CYAN));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksPurpleCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.PURPLE));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksBlueCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BLUE));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksBrownCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BROWN));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksGreenCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.GREEN));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksRedCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.RED));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksBlackCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BLACK));
    }

    //
    // Iron Shulker Box
    //

    @GameTest(maxTicks = 100)
    public void pistonBreaksIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get((DyeColor) null));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksWhiteIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.WHITE));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksOrangeIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.ORANGE));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksMagentaIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.MAGENTA));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksLightBlueIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIGHT_BLUE));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksYellowIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.YELLOW));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksLimeIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIME));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksPinkIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.PINK));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksGrayIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.GRAY));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksLightGrayIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIGHT_GRAY));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksCyanIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.CYAN));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksPurpleIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.PURPLE));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksBlueIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BLUE));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksBrownIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BROWN));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksGreenIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.GREEN));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksRedIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.RED));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksBlackIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BLACK));
    }

    //
    // Gold Shulker Box
    //

    @GameTest(maxTicks = 100)
    public void pistonBreaksGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get((DyeColor) null));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksWhiteGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.WHITE));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksOrangeGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.ORANGE));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksMagentaGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.MAGENTA));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksLightBlueGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIGHT_BLUE));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksYellowGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.YELLOW));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksLimeGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIME));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksPinkGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.PINK));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksGrayGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.GRAY));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksLightGrayGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIGHT_GRAY));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksCyanGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.CYAN));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksPurpleGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.PURPLE));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksBlueGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BLUE));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksBrownGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BROWN));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksGreenGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.GREEN));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksRedGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.RED));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksBlackGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BLACK));
    }

    //
    // Diamond Shulker Box
    //

    @GameTest(maxTicks = 100)
    public void pistonBreaksDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get((DyeColor) null));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksWhiteDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.WHITE));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksOrangeDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.ORANGE));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksMagentaDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.MAGENTA));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksLightBlueDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIGHT_BLUE));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksYellowDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.YELLOW));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksLimeDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIME));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksPinkDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.PINK));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksGrayDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.GRAY));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksLightGrayDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIGHT_GRAY));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksCyanDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.CYAN));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksPurpleDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.PURPLE));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksBlueDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BLUE));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksBrownDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BROWN));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksGreenDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.GREEN));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksRedDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.RED));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksBlackDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BLACK));
    }

    //
    // Netherite Shulker Box
    //

    @GameTest(maxTicks = 100)
    public void pistonBreaksNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get((DyeColor) null));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksWhiteNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.WHITE));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksOrangeNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.ORANGE));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksMagentaNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.MAGENTA));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksLightBlueNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.LIGHT_BLUE));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksYellowNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.YELLOW));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksLimeNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.LIME));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksPinkNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.PINK));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksGrayNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.GRAY));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksLightGrayNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.LIGHT_GRAY));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksCyanNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.CYAN));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksPurpleNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.PURPLE));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksBlueNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.BLUE));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksBrownNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.BROWN));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksGreenNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.GREEN));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksRedNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.RED));
    }

    @GameTest(maxTicks = 100)
    public void pistonBreaksBlackNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.BLACK));
    }
}
