package atonkish.reinfshulker.gametest.testcase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.state.property.Properties;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
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
import atonkish.reinfshulker.item.ModItems;
import atonkish.reinfshulker.stat.ModStats;

public class CauldronBehaviorDefaultTests implements CustomTestMethodInvoker {
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
        context.setBlockState(blockPos, Blocks.WATER_CAULDRON
                .getDefaultState().with(Properties.LEVEL_3, 3));

        ServerPlayerEntity player = MockServerPlayerHelper.spawn(context,
                GameMode.SURVIVAL, Vec3d.of(blockPos.south(4)));
        player.setStackInHand(Hand.MAIN_HAND, new ItemStack(shulkerBoxBlock.asItem()));

        Stat<Identifier> stat = Stats.CUSTOM
                .getOrCreateStat(
                        ModStats.CLEAN_REINFORCED_SHULKER_BOX_MAP.get(shulkerBoxBlock.getMaterial()));

        // Act
        CompletableFuture<Void> futurePartialAct1 = new CompletableFuture<>();
        CompletableFuture<Void> futurePartialAct2 = new CompletableFuture<>();

        Map<String, Integer> statMap = new HashMap<String, Integer>();
        String statMapKeyBeforeCleaning = "beforeCleaning";
        String statMapKeyAfterCleaning = "afterCleaning";

        long tickOrigin = 0;
        context.runAtTick(tickOrigin, () -> {
            statMap.put(statMapKeyBeforeCleaning, player.getStatHandler().getStat(stat));

            context.useBlock(blockPos, player);

            futurePartialAct1.complete(null);
        });

        long tickShulkerBoxCleaning = 1;
        context.runAtTick(tickShulkerBoxCleaning, () -> {
            statMap.put(statMapKeyAfterCleaning, player.getStatHandler().getStat(stat));

            futurePartialAct2.complete(null);
        });

        // Assert
        CompletableFuture.allOf(futurePartialAct1, futurePartialAct2).thenRun(() -> {
            try {
                context.assertEquals(
                        player.getMainHandStack().getItem(), ModItems.REINFORCED_SHULKER_BOX_MAP
                                .get(shulkerBoxBlock.getMaterial()).get((DyeColor) null),
                        Text.of("main hand item"));
                context.assertEquals(context.getBlockState(blockPos).get(Properties.LEVEL_3), 2,
                        Text.of("fluid level"));
                context.assertEquals(
                        statMap.get(statMapKeyAfterCleaning) - statMap.get(statMapKeyBeforeCleaning), 1,
                        Text.of(String.format("diff %s value", stat.getName())));
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
    public void cleanWhiteCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.WHITE));
    }

    @GameTest
    public void cleanOrangeCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.ORANGE));
    }

    @GameTest
    public void cleanMagentaCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.MAGENTA));
    }

    @GameTest
    public void cleanLightBlueCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIGHT_BLUE));
    }

    @GameTest
    public void cleanYellowCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.YELLOW));
    }

    @GameTest
    public void cleanLimeCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIME));
    }

    @GameTest
    public void cleanPinkCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.PINK));
    }

    @GameTest
    public void cleanGrayCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.GRAY));
    }

    @GameTest
    public void cleanLightGrayCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIGHT_GRAY));
    }

    @GameTest
    public void cleanCyanCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.CYAN));
    }

    @GameTest
    public void cleanPurpleCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.PURPLE));
    }

    @GameTest
    public void cleanBlueCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BLUE));
    }

    @GameTest
    public void cleanBrownCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BROWN));
    }

    @GameTest
    public void cleanGreenCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.GREEN));
    }

    @GameTest
    public void cleanRedCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.RED));
    }

    @GameTest
    public void cleanBlackCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BLACK));
    }

    //
    // Iron Shulker Box
    //

    @GameTest
    public void cleanWhiteIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.WHITE));
    }

    @GameTest
    public void cleanOrangeIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.ORANGE));
    }

    @GameTest
    public void cleanMagentaIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.MAGENTA));
    }

    @GameTest
    public void cleanLightBlueIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIGHT_BLUE));
    }

    @GameTest
    public void cleanYellowIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.YELLOW));
    }

    @GameTest
    public void cleanLimeIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIME));
    }

    @GameTest
    public void cleanPinkIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.PINK));
    }

    @GameTest
    public void cleanGrayIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.GRAY));
    }

    @GameTest
    public void cleanLightGrayIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIGHT_GRAY));
    }

    @GameTest
    public void cleanCyanIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.CYAN));
    }

    @GameTest
    public void cleanPurpleIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.PURPLE));
    }

    @GameTest
    public void cleanBlueIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BLUE));
    }

    @GameTest
    public void cleanBrownIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BROWN));
    }

    @GameTest
    public void cleanGreenIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.GREEN));
    }

    @GameTest
    public void cleanRedIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.RED));
    }

    @GameTest
    public void cleanBlackIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BLACK));
    }

    //
    // Gold Shulker Box
    //

    @GameTest
    public void cleanWhiteGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.WHITE));
    }

    @GameTest
    public void cleanOrangeGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.ORANGE));
    }

    @GameTest
    public void cleanMagentaGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.MAGENTA));
    }

    @GameTest
    public void cleanLightBlueGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIGHT_BLUE));
    }

    @GameTest
    public void cleanYellowGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.YELLOW));
    }

    @GameTest
    public void cleanLimeGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIME));
    }

    @GameTest
    public void cleanPinkGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.PINK));
    }

    @GameTest
    public void cleanGrayGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.GRAY));
    }

    @GameTest
    public void cleanLightGrayGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIGHT_GRAY));
    }

    @GameTest
    public void cleanCyanGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.CYAN));
    }

    @GameTest
    public void cleanPurpleGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.PURPLE));
    }

    @GameTest
    public void cleanBlueGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BLUE));
    }

    @GameTest
    public void cleanBrownGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BROWN));
    }

    @GameTest
    public void cleanGreenGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.GREEN));
    }

    @GameTest
    public void cleanRedGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.RED));
    }

    @GameTest
    public void cleanBlackGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BLACK));
    }

    //
    // Diamond Shulker Box
    //

    @GameTest
    public void cleanWhiteDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.WHITE));
    }

    @GameTest
    public void cleanOrangeDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.ORANGE));
    }

    @GameTest
    public void cleanMagentaDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.MAGENTA));
    }

    @GameTest
    public void cleanLightBlueDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIGHT_BLUE));
    }

    @GameTest
    public void cleanYellowDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.YELLOW));
    }

    @GameTest
    public void cleanLimeDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIME));
    }

    @GameTest
    public void cleanPinkDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.PINK));
    }

    @GameTest
    public void cleanGrayDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.GRAY));
    }

    @GameTest
    public void cleanLightGrayDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIGHT_GRAY));
    }

    @GameTest
    public void cleanCyanDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.CYAN));
    }

    @GameTest
    public void cleanPurpleDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.PURPLE));
    }

    @GameTest
    public void cleanBlueDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BLUE));
    }

    @GameTest
    public void cleanBrownDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BROWN));
    }

    @GameTest
    public void cleanGreenDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.GREEN));
    }

    @GameTest
    public void cleanRedDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.RED));
    }

    @GameTest
    public void cleanBlackDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BLACK));
    }

    //
    // Netherite Shulker Box
    //

    @GameTest
    public void cleanWhiteNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.WHITE));
    }

    @GameTest
    public void cleanOrangeNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.ORANGE));
    }

    @GameTest
    public void cleanMagentaNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.MAGENTA));
    }

    @GameTest
    public void cleanLightBlueNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.LIGHT_BLUE));
    }

    @GameTest
    public void cleanYellowNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.YELLOW));
    }

    @GameTest
    public void cleanLimeNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.LIME));
    }

    @GameTest
    public void cleanPinkNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.PINK));
    }

    @GameTest
    public void cleanGrayNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.GRAY));
    }

    @GameTest
    public void cleanLightGrayNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.LIGHT_GRAY));
    }

    @GameTest
    public void cleanCyanNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.CYAN));
    }

    @GameTest
    public void cleanPurpleNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.PURPLE));
    }

    @GameTest
    public void cleanBlueNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.BLUE));
    }

    @GameTest
    public void cleanBrownNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.BROWN));
    }

    @GameTest
    public void cleanGreenNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.GREEN));
    }

    @GameTest
    public void cleanRedNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.RED));
    }

    @GameTest
    public void cleanBlackNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.BLACK));
    }
}
