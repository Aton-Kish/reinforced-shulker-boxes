package atonkish.reinfshulker.gametest.testcase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;

import atonkish.reinfcore.util.ReinforcingMaterials;

import atonkish.reinfchest.ReinforcedChestsMod;

import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;
import atonkish.reinfshulker.block.ModBlocks;
import atonkish.reinfshulker.block.ReinforcedShulkerBoxBlock;
import atonkish.reinfshulker.gametest.util.MockServerPlayerHelper;
import atonkish.reinfshulker.stat.ModStats;

public class OpenDefaultTests implements CustomTestMethodInvoker {
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

    private void test(TestContext context, ReinforcedShulkerBoxBlock shulkerBoxBlock) {
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

        Stat<Identifier> stat = Stats.CUSTOM
                .getOrCreateStat(
                        ModStats.OPEN_REINFORCED_SHULKER_BOX_MAP.get(shulkerBoxBlock.getMaterial()));

        // Act
        CompletableFuture<Void> futurePartialAct1 = new CompletableFuture<>();
        CompletableFuture<Void> futurePartialAct2 = new CompletableFuture<>();

        Map<String, Integer> statMap = new HashMap<String, Integer>();
        String statMapKeyBeforeOpening = "beforeOpening";
        String statMapKeyAfterOpening = "afterOpening";

        long tickOrigin = 0;
        context.runAtTick(tickOrigin, () -> {
            statMap.put(statMapKeyBeforeOpening, player.getStatHandler().getStat(stat));

            context.useBlock(blockPos, player);

            futurePartialAct1.complete(null);
        });

        long tickShulkerBoxOpening = 1;
        context.runAtTick(tickShulkerBoxOpening, () -> {
            statMap.put(statMapKeyAfterOpening, player.getStatHandler().getStat(stat));

            futurePartialAct2.complete(null);
        });

        // Assert
        CompletableFuture.allOf(futurePartialAct1, futurePartialAct2).thenRun(() -> {
            try {
                context.assertEquals(
                        statMap.get(statMapKeyAfterOpening) - statMap.get(statMapKeyBeforeOpening), 1,
                        Text.of(String.format(
                                "diff %s value",
                                stat.getName())));
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

    @GameTest
    public void openCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get((DyeColor) null));
    }

    @GameTest
    public void openWhiteCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.WHITE));
    }

    @GameTest
    public void openOrangeCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.ORANGE));
    }

    @GameTest
    public void openMagentaCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.MAGENTA));
    }

    @GameTest
    public void openLightBlueCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIGHT_BLUE));
    }

    @GameTest
    public void openYellowCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.YELLOW));
    }

    @GameTest
    public void openLimeCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIME));
    }

    @GameTest
    public void openPinkCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.PINK));
    }

    @GameTest
    public void openGrayCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.GRAY));
    }

    @GameTest
    public void openLightGrayCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIGHT_GRAY));
    }

    @GameTest
    public void openCyanCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.CYAN));
    }

    @GameTest
    public void openPurpleCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.PURPLE));
    }

    @GameTest
    public void openBlueCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BLUE));
    }

    @GameTest
    public void openBrownCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BROWN));
    }

    @GameTest
    public void openGreenCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.GREEN));
    }

    @GameTest
    public void openRedCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.RED));
    }

    @GameTest
    public void openBlackCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BLACK));
    }

    //
    // Iron Shulker Box
    //

    @GameTest
    public void openIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get((DyeColor) null));
    }

    @GameTest
    public void openWhiteIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.WHITE));
    }

    @GameTest
    public void openOrangeIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.ORANGE));
    }

    @GameTest
    public void openMagentaIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.MAGENTA));
    }

    @GameTest
    public void openLightBlueIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIGHT_BLUE));
    }

    @GameTest
    public void openYellowIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.YELLOW));
    }

    @GameTest
    public void openLimeIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIME));
    }

    @GameTest
    public void openPinkIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.PINK));
    }

    @GameTest
    public void openGrayIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.GRAY));
    }

    @GameTest
    public void openLightGrayIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIGHT_GRAY));
    }

    @GameTest
    public void openCyanIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.CYAN));
    }

    @GameTest
    public void openPurpleIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.PURPLE));
    }

    @GameTest
    public void openBlueIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BLUE));
    }

    @GameTest
    public void openBrownIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BROWN));
    }

    @GameTest
    public void openGreenIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.GREEN));
    }

    @GameTest
    public void openRedIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.RED));
    }

    @GameTest
    public void openBlackIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BLACK));
    }

    //
    // Gold Shulker Box
    //

    @GameTest
    public void openGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get((DyeColor) null));
    }

    @GameTest
    public void openWhiteGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.WHITE));
    }

    @GameTest
    public void openOrangeGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.ORANGE));
    }

    @GameTest
    public void openMagentaGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.MAGENTA));
    }

    @GameTest
    public void openLightBlueGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIGHT_BLUE));
    }

    @GameTest
    public void openYellowGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.YELLOW));
    }

    @GameTest
    public void openLimeGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIME));
    }

    @GameTest
    public void openPinkGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.PINK));
    }

    @GameTest
    public void openGrayGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.GRAY));
    }

    @GameTest
    public void openLightGrayGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIGHT_GRAY));
    }

    @GameTest
    public void openCyanGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.CYAN));
    }

    @GameTest
    public void openPurpleGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.PURPLE));
    }

    @GameTest
    public void openBlueGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BLUE));
    }

    @GameTest
    public void openBrownGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BROWN));
    }

    @GameTest
    public void openGreenGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.GREEN));
    }

    @GameTest
    public void openRedGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.RED));
    }

    @GameTest
    public void openBlackGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BLACK));
    }

    //
    // Diamond Shulker Box
    //

    @GameTest
    public void openDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get((DyeColor) null));
    }

    @GameTest
    public void openWhiteDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.WHITE));
    }

    @GameTest
    public void openOrangeDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.ORANGE));
    }

    @GameTest
    public void openMagentaDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.MAGENTA));
    }

    @GameTest
    public void openLightBlueDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIGHT_BLUE));
    }

    @GameTest
    public void openYellowDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.YELLOW));
    }

    @GameTest
    public void openLimeDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIME));
    }

    @GameTest
    public void openPinkDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.PINK));
    }

    @GameTest
    public void openGrayDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.GRAY));
    }

    @GameTest
    public void openLightGrayDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIGHT_GRAY));
    }

    @GameTest
    public void openCyanDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.CYAN));
    }

    @GameTest
    public void openPurpleDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.PURPLE));
    }

    @GameTest
    public void openBlueDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BLUE));
    }

    @GameTest
    public void openBrownDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BROWN));
    }

    @GameTest
    public void openGreenDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.GREEN));
    }

    @GameTest
    public void openRedDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.RED));
    }

    @GameTest
    public void openBlackDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BLACK));
    }

    //
    // Netherite Shulker Box
    //

    @GameTest
    public void openNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get((DyeColor) null));
    }

    @GameTest
    public void openWhiteNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.WHITE));
    }

    @GameTest
    public void openOrangeNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.ORANGE));
    }

    @GameTest
    public void openMagentaNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.MAGENTA));
    }

    @GameTest
    public void openLightBlueNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.LIGHT_BLUE));
    }

    @GameTest
    public void openYellowNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.YELLOW));
    }

    @GameTest
    public void openLimeNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.LIME));
    }

    @GameTest
    public void openPinkNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.PINK));
    }

    @GameTest
    public void openGrayNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.GRAY));
    }

    @GameTest
    public void openLightGrayNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.LIGHT_GRAY));
    }

    @GameTest
    public void openCyanNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.CYAN));
    }

    @GameTest
    public void openPurpleNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.PURPLE));
    }

    @GameTest
    public void openBlueNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.BLUE));
    }

    @GameTest
    public void openBrownNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.BROWN));
    }

    @GameTest
    public void openGreenNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.GREEN));
    }

    @GameTest
    public void openRedNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.RED));
    }

    @GameTest
    public void openBlackNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.BLACK));
    }
}
