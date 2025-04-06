package atonkish.reinfshulker.gametest.testcase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.block.Block;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;

import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;

import atonkish.reinfcore.util.ReinforcingMaterials;

import atonkish.reinfchest.ReinforcedChestsMod;

import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;
import atonkish.reinfshulker.block.ModBlocks;

public class InventoryDefaultTests implements CustomTestMethodInvoker {
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

    private void test(TestContext context, Block shulkerBoxBlock, int size) {
        String testName = String.format("%s %s %s",
                ReinforcedChestsMod.MOD_ID,
                this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[2].getMethodName())
                .replace(" ", "_");

        // Arrange
        BlockPos blockPos = BlockPos.ORIGIN;
        context.setBlockState(blockPos, shulkerBoxBlock);

        // Act
        ShulkerBoxBlockEntity entity = context.getBlockEntity(blockPos, ShulkerBoxBlockEntity.class);

        // Assert
        try {
            context.assertEquals(entity.size(), size,
                    Text.of(String.format(
                            "%s inventory size",
                            shulkerBoxBlock.getName().getString())));
        } catch (Exception e) {
            ReinforcedShulkerBoxesMod.LOGGER.error("[{}] {}", testName, e.getMessage());
            throw e;
        }

        context.complete();
    }

    //
    // Copper Shulker Box
    //

    @GameTest
    public void copperShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get((DyeColor) null),
                45);
    }

    @GameTest
    public void whiteCopperShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.WHITE),
                45);
    }

    @GameTest
    public void orangeCopperShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.ORANGE),
                45);
    }

    @GameTest
    public void magentaCopperShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.MAGENTA),
                45);
    }

    @GameTest
    public void lightBlueCopperShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIGHT_BLUE),
                45);
    }

    @GameTest
    public void yellowCopperShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.YELLOW),
                45);
    }

    @GameTest
    public void limeCopperShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIME),
                45);
    }

    @GameTest
    public void pinkCopperShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.PINK),
                45);
    }

    @GameTest
    public void grayCopperShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.GRAY),
                45);
    }

    @GameTest
    public void lightGrayCopperShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIGHT_GRAY),
                45);
    }

    @GameTest
    public void cyanCopperShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.CYAN),
                45);
    }

    @GameTest
    public void purpleCopperShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.PURPLE),
                45);
    }

    @GameTest
    public void blueCopperShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BLUE),
                45);
    }

    @GameTest
    public void brownCopperShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BROWN),
                45);
    }

    @GameTest
    public void greenCopperShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.GREEN),
                45);
    }

    @GameTest
    public void redCopperShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.RED),
                45);
    }

    @GameTest
    public void blackCopperShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BLACK),
                45);
    }

    //
    // Iron Shulker Box
    //

    @GameTest
    public void ironShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get((DyeColor) null),
                54);
    }

    @GameTest
    public void whiteIronShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.WHITE),
                54);
    }

    @GameTest
    public void orangeIronShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.ORANGE),
                54);
    }

    @GameTest
    public void magentaIronShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.MAGENTA),
                54);
    }

    @GameTest
    public void lightBlueIronShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIGHT_BLUE),
                54);
    }

    @GameTest
    public void yellowIronShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.YELLOW),
                54);
    }

    @GameTest
    public void limeIronShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIME),
                54);
    }

    @GameTest
    public void pinkIronShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.PINK),
                54);
    }

    @GameTest
    public void grayIronShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.GRAY),
                54);
    }

    @GameTest
    public void lightGrayIronShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIGHT_GRAY),
                54);
    }

    @GameTest
    public void cyanIronShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.CYAN),
                54);
    }

    @GameTest
    public void purpleIronShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.PURPLE),
                54);
    }

    @GameTest
    public void blueIronShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BLUE),
                54);
    }

    @GameTest
    public void brownIronShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BROWN),
                54);
    }

    @GameTest
    public void greenIronShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.GREEN),
                54);
    }

    @GameTest
    public void redIronShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.RED),
                54);
    }

    @GameTest
    public void blackIronShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BLACK),
                54);
    }

    //
    // Gold Shulker Box
    //

    @GameTest
    public void goldShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get((DyeColor) null),
                81);
    }

    @GameTest
    public void whiteGoldShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.WHITE),
                81);
    }

    @GameTest
    public void orangeGoldShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.ORANGE),
                81);
    }

    @GameTest
    public void magentaGoldShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.MAGENTA),
                81);
    }

    @GameTest
    public void lightBlueGoldShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIGHT_BLUE),
                81);
    }

    @GameTest
    public void yellowGoldShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.YELLOW),
                81);
    }

    @GameTest
    public void limeGoldShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIME),
                81);
    }

    @GameTest
    public void pinkGoldShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.PINK),
                81);
    }

    @GameTest
    public void grayGoldShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.GRAY),
                81);
    }

    @GameTest
    public void lightGrayGoldShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIGHT_GRAY),
                81);
    }

    @GameTest
    public void cyanGoldShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.CYAN),
                81);
    }

    @GameTest
    public void purpleGoldShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.PURPLE),
                81);
    }

    @GameTest
    public void blueGoldShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BLUE),
                81);
    }

    @GameTest
    public void brownGoldShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BROWN),
                81);
    }

    @GameTest
    public void greenGoldShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.GREEN),
                81);
    }

    @GameTest
    public void redGoldShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.RED),
                81);
    }

    @GameTest
    public void blackGoldShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BLACK),
                81);
    }

    //
    // Diamond Shulker Box
    //

    @GameTest
    public void diamondShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get((DyeColor) null),
                108);
    }

    @GameTest
    public void whiteDiamondShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.WHITE),
                108);
    }

    @GameTest
    public void orangeDiamondShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.ORANGE),
                108);
    }

    @GameTest
    public void magentaDiamondShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.MAGENTA),
                108);
    }

    @GameTest
    public void lightBlueDiamondShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIGHT_BLUE),
                108);
    }

    @GameTest
    public void yellowDiamondShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.YELLOW),
                108);
    }

    @GameTest
    public void limeDiamondShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIME),
                108);
    }

    @GameTest
    public void pinkDiamondShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.PINK),
                108);
    }

    @GameTest
    public void grayDiamondShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.GRAY),
                108);
    }

    @GameTest
    public void lightGrayDiamondShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIGHT_GRAY),
                108);
    }

    @GameTest
    public void cyanDiamondShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.CYAN),
                108);
    }

    @GameTest
    public void purpleDiamondShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.PURPLE),
                108);
    }

    @GameTest
    public void blueDiamondShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BLUE),
                108);
    }

    @GameTest
    public void brownDiamondShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BROWN),
                108);
    }

    @GameTest
    public void greenDiamondShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.GREEN),
                108);
    }

    @GameTest
    public void redDiamondShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.RED),
                108);
    }

    @GameTest
    public void blackDiamondShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BLACK),
                108);
    }

    //
    // Netherite Shulker Box
    //

    @GameTest
    public void netheriteShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get((DyeColor) null),
                108);
    }

    @GameTest
    public void whiteNetheriteShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.WHITE),
                108);
    }

    @GameTest
    public void orangeNetheriteShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.ORANGE),
                108);
    }

    @GameTest
    public void magentaNetheriteShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.MAGENTA),
                108);
    }

    @GameTest
    public void lightBlueNetheriteShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.LIGHT_BLUE),
                108);
    }

    @GameTest
    public void yellowNetheriteShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.YELLOW),
                108);
    }

    @GameTest
    public void limeNetheriteShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.LIME),
                108);
    }

    @GameTest
    public void pinkNetheriteShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.PINK),
                108);
    }

    @GameTest
    public void grayNetheriteShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.GRAY),
                108);
    }

    @GameTest
    public void lightGrayNetheriteShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.LIGHT_GRAY),
                108);
    }

    @GameTest
    public void cyanNetheriteShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.CYAN),
                108);
    }

    @GameTest
    public void purpleNetheriteShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.PURPLE),
                108);
    }

    @GameTest
    public void blueNetheriteShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.BLUE),
                108);
    }

    @GameTest
    public void brownNetheriteShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.BROWN),
                108);
    }

    @GameTest
    public void greenNetheriteShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.GREEN),
                108);
    }

    @GameTest
    public void redNetheriteShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.RED),
                108);
    }

    @GameTest
    public void blackNetheriteShulkerBoxInventorySize(TestContext context) {
        test(context,
                ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(DyeColor.BLACK),
                108);
    }
}
