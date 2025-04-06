package atonkish.reinfshulker.gametest.testcase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
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

public class PiglinDefaultTests implements CustomTestMethodInvoker {
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

        ServerPlayerEntity player = MockServerPlayerHelper.spawn(context,
                GameMode.SURVIVAL, Vec3d.of(blockPos.south(4)));
        player.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));

        PiglinEntity piglin = context.spawnMob(EntityType.PIGLIN, blockPos.east(1));

        // Act
        CompletableFuture<Void> futurePartialAct1 = new CompletableFuture<>();
        CompletableFuture<Void> futurePartialAct2 = new CompletableFuture<>();

        Map<String, Boolean> angryAtMap = new HashMap<String, Boolean>();
        String angryAtMapKeyBeforeAngryAtPlayer = "beforeAngryAtPlayer";
        String angryAtMapKeyAfterAngryAtPlayer = "afterAngryAtPlayer";

        long tickChestOpen = 20;
        context.runAtTick(tickChestOpen, () -> {
            angryAtMap.put(angryAtMapKeyBeforeAngryAtPlayer,
                    piglin.getBrain().hasMemoryModuleWithValue(MemoryModuleType.ANGRY_AT,
                            player.getUuid()));

            context.useBlock(blockPos, player);

            futurePartialAct1.complete(null);
        });

        long tickAngryAtPlayer = 21;
        context.runAtTick(tickAngryAtPlayer, () -> {
            angryAtMap.put(angryAtMapKeyAfterAngryAtPlayer,
                    piglin.getBrain().hasMemoryModuleWithValue(MemoryModuleType.ANGRY_AT,
                            player.getUuid()));

            futurePartialAct2.complete(null);
        });

        // Assert
        CompletableFuture.allOf(futurePartialAct1, futurePartialAct2).thenRun(() -> {
            try {
                context.assertFalse(angryAtMap.get(angryAtMapKeyBeforeAngryAtPlayer),
                        Text.of("Expected that the piglin is not angry at player, but it has been already angry."));
                context.assertTrue(angryAtMap.get(angryAtMapKeyAfterAngryAtPlayer),
                        Text.of("Expected that the piglin is angry at player, but it has not been angry yet."));
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

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get((DyeColor) null));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningWhiteCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.WHITE));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningOrangeCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.ORANGE));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningMagentaCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.MAGENTA));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningLightBlueCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIGHT_BLUE));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningYellowCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.YELLOW));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningLimeCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIME));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningPinkCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.PINK));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningGrayCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.GRAY));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningLightGrayCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIGHT_GRAY));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningCyanCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.CYAN));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningPurpleCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.PURPLE));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningBlueCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BLUE));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningBrownCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BROWN));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningGreenCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.GREEN));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningRedCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.RED));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningBlackCopperShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BLACK));
    }

    //
    // Iron Shulker Box
    //

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get((DyeColor) null));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningWhiteIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.WHITE));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningOrangeIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.ORANGE));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningMagentaIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.MAGENTA));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningLightBlueIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIGHT_BLUE));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningYellowIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.YELLOW));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningLimeIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIME));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningPinkIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.PINK));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningGrayIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.GRAY));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningLightGrayIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIGHT_GRAY));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningCyanIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.CYAN));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningPurpleIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.PURPLE));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningBlueIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BLUE));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningBrownIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BROWN));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningGreenIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.GREEN));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningRedIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.RED));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningBlackIronShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BLACK));
    }

    //
    // Gold Shulker Box
    //

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get((DyeColor) null));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningWhiteGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.WHITE));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningOrangeGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.ORANGE));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningMagentaGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.MAGENTA));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningLightBlueGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIGHT_BLUE));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningYellowGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.YELLOW));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningLimeGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIME));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningPinkGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.PINK));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningGrayGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.GRAY));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningLightGrayGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIGHT_GRAY));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningCyanGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.CYAN));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningPurpleGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.PURPLE));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningBlueGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BLUE));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningBrownGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BROWN));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningGreenGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.GREEN));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningRedGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.RED));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningBlackGoldShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BLACK));
    }

    //
    // Diamond Shulker Box
    //

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get((DyeColor) null));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningWhiteDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.WHITE));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningOrangeDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.ORANGE));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningMagentaDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.MAGENTA));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningLightBlueDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIGHT_BLUE));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningYellowDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.YELLOW));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningLimeDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIME));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningPinkDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.PINK));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningGrayDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.GRAY));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningLightGrayDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIGHT_GRAY));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningCyanDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.CYAN));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningPurpleDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.PURPLE));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningBlueDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BLUE));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningBrownDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BROWN));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningGreenDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.GREEN));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningRedDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.RED));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningBlackDiamondShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BLACK));
    }

    //
    // Netherite Shulker Box
    //

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get((DyeColor) null));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningWhiteNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.WHITE));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningOrangeNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.ORANGE));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningMagentaNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.MAGENTA));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningLightBlueNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.LIGHT_BLUE));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningYellowNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.YELLOW));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningLimeNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.LIME));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningPinkNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.PINK));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningGrayNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.GRAY));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningLightGrayNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.LIGHT_GRAY));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningCyanNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.CYAN));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningPurpleNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.PURPLE));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningBlueNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.BLUE));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningBrownNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.BROWN));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningGreenNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.GREEN));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningRedNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.RED));
    }

    @GameTest(maxTicks = 100)
    public void piglinGetAngryAfterOpeningBlackNetheriteShulkerBox(TestContext context) {
        test(context,
                (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.BLACK));
    }
}
