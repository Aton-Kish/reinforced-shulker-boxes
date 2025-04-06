package atonkish.reinfshulker.gametest.testcase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
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
import atonkish.reinfshulker.gametest.util.MockServerPlayerHelper;
import atonkish.reinfshulker.item.ModItems;

public class AdvancementDefaultTests implements CustomTestMethodInvoker {
    private static final Map<DyeColor, Item> SHULKER_BOX_MAP = new LinkedHashMap<>() {
        {
            put((DyeColor) null, Items.SHULKER_BOX);
            put(DyeColor.WHITE, Items.WHITE_SHULKER_BOX);
            put(DyeColor.ORANGE, Items.ORANGE_SHULKER_BOX);
            put(DyeColor.MAGENTA, Items.MAGENTA_SHULKER_BOX);
            put(DyeColor.LIGHT_BLUE, Items.LIGHT_BLUE_SHULKER_BOX);
            put(DyeColor.YELLOW, Items.YELLOW_SHULKER_BOX);
            put(DyeColor.LIME, Items.LIME_SHULKER_BOX);
            put(DyeColor.PINK, Items.PINK_SHULKER_BOX);
            put(DyeColor.GRAY, Items.GRAY_SHULKER_BOX);
            put(DyeColor.LIGHT_GRAY, Items.LIGHT_GRAY_SHULKER_BOX);
            put(DyeColor.CYAN, Items.CYAN_SHULKER_BOX);
            put(DyeColor.PURPLE, Items.PURPLE_SHULKER_BOX);
            put(DyeColor.BLUE, Items.BLUE_SHULKER_BOX);
            put(DyeColor.BROWN, Items.BROWN_SHULKER_BOX);
            put(DyeColor.GREEN, Items.GREEN_SHULKER_BOX);
            put(DyeColor.RED, Items.RED_SHULKER_BOX);
            put(DyeColor.BLACK, Items.BLACK_SHULKER_BOX);
        }
    };

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

    private void test(TestContext context, Item item, Identifier advancementId) {
        String testName = String.format("%s %s %s",
                ReinforcedChestsMod.MOD_ID,
                this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[2].getMethodName())
                .replace(" ", "_");

        // Arrange
        ServerPlayerEntity player = MockServerPlayerHelper.spawn(context,
                GameMode.SURVIVAL, Vec3d.of(BlockPos.ORIGIN));
        AdvancementEntry entry = context.getWorld().getServer().getAdvancementLoader().get(advancementId);
        AdvancementProgress progress = player.getAdvancementTracker().getProgress(entry);

        // Act
        CompletableFuture<Void> futurePartialAct1 = new CompletableFuture<>();
        CompletableFuture<Void> futurePartialAct2 = new CompletableFuture<>();

        Map<String, Boolean> progressMap = new HashMap<String, Boolean>();
        String progressMapKeyBeforeHavingItem = "beforeHavingItem";
        String progressMapKeyAfterHavingItem = "afterHavingItem";

        long tickOrigin = 0;
        context.runAtTick(tickOrigin, () -> {
            progressMap.put(progressMapKeyBeforeHavingItem, progress.isDone());

            player.giveItemStack(new ItemStack(item));

            futurePartialAct1.complete(null);
        });

        long tickObtained = 1;
        context.runAtTick(tickObtained, () -> {
            progressMap.put(progressMapKeyAfterHavingItem, progress.isDone());

            futurePartialAct2.complete(null);
        });

        // Assert
        CompletableFuture.allOf(futurePartialAct1, futurePartialAct2).thenRun(() -> {
            try {
                context.assertFalse(
                        progressMap.get(progressMapKeyBeforeHavingItem),
                        Text.of(String.format(
                                "Expected that advancement %s has not been done yet, but it has been already done.",
                                entry)));
                context.assertTrue(
                        progressMap.get(progressMapKeyAfterHavingItem),
                        Text.of(String.format(
                                "Expected that advancement %s has been done, but it has not been done yet.",
                                entry)));
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
    public void obtainCopperShulkerBoxRecipeAdvancementByHavingShulkerBox(TestContext context) {
        test(context,
                SHULKER_BOX_MAP.get((DyeColor) null),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/copper_shulker_box"));
    }

    @GameTest
    public void obtainCopperShulkerBoxRecipeAdvancementByHavingWhiteShulkerBox(TestContext context) {
        test(context,
                SHULKER_BOX_MAP.get(DyeColor.WHITE),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/copper_shulker_box"));
    }

    @GameTest
    public void obtainCopperShulkerBoxRecipeAdvancementByHavingOrangeShulkerBox(TestContext context) {
        test(context,
                SHULKER_BOX_MAP.get(DyeColor.ORANGE),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/copper_shulker_box"));
    }

    @GameTest
    public void obtainCopperShulkerBoxRecipeAdvancementByHavingMagentaShulkerBox(TestContext context) {
        test(context,
                SHULKER_BOX_MAP.get(DyeColor.MAGENTA),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/copper_shulker_box"));
    }

    @GameTest
    public void obtainCopperShulkerBoxRecipeAdvancementByHavingLightBlueShulkerBox(TestContext context) {
        test(context,
                SHULKER_BOX_MAP.get(DyeColor.LIGHT_BLUE),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/copper_shulker_box"));
    }

    @GameTest
    public void obtainCopperShulkerBoxRecipeAdvancementByHavingYellowShulkerBox(TestContext context) {
        test(context,
                SHULKER_BOX_MAP.get(DyeColor.YELLOW),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/copper_shulker_box"));
    }

    @GameTest
    public void obtainCopperShulkerBoxRecipeAdvancementByHavingLimeShulkerBox(TestContext context) {
        test(context,
                SHULKER_BOX_MAP.get(DyeColor.LIME),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/copper_shulker_box"));
    }

    @GameTest
    public void obtainCopperShulkerBoxRecipeAdvancementByHavingPinkShulkerBox(TestContext context) {
        test(context,
                SHULKER_BOX_MAP.get(DyeColor.PINK),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/copper_shulker_box"));
    }

    @GameTest
    public void obtainCopperShulkerBoxRecipeAdvancementByHavingGrayShulkerBox(TestContext context) {
        test(context,
                SHULKER_BOX_MAP.get(DyeColor.GRAY),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/copper_shulker_box"));
    }

    @GameTest
    public void obtainCopperShulkerBoxRecipeAdvancementByHavingLightGrayShulkerBox(TestContext context) {
        test(context,
                SHULKER_BOX_MAP.get(DyeColor.LIGHT_GRAY),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/copper_shulker_box"));
    }

    @GameTest
    public void obtainCopperShulkerBoxRecipeAdvancementByHavingCyanShulkerBox(TestContext context) {
        test(context,
                SHULKER_BOX_MAP.get(DyeColor.CYAN),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/copper_shulker_box"));
    }

    @GameTest
    public void obtainCopperShulkerBoxRecipeAdvancementByHavingPurpleShulkerBox(TestContext context) {
        test(context,
                SHULKER_BOX_MAP.get(DyeColor.PURPLE),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/copper_shulker_box"));
    }

    @GameTest
    public void obtainCopperShulkerBoxRecipeAdvancementByHavingBlueShulkerBox(TestContext context) {
        test(context,
                SHULKER_BOX_MAP.get(DyeColor.BLUE),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/copper_shulker_box"));
    }

    @GameTest
    public void obtainCopperShulkerBoxRecipeAdvancementByHavingBrownShulkerBox(TestContext context) {
        test(context,
                SHULKER_BOX_MAP.get(DyeColor.BROWN),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/copper_shulker_box"));
    }

    @GameTest
    public void obtainCopperShulkerBoxRecipeAdvancementByHavingGreenShulkerBox(TestContext context) {
        test(context,
                SHULKER_BOX_MAP.get(DyeColor.GREEN),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/copper_shulker_box"));
    }

    @GameTest
    public void obtainCopperShulkerBoxRecipeAdvancementByHavingRedShulkerBox(TestContext context) {
        test(context,
                SHULKER_BOX_MAP.get(DyeColor.RED),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/copper_shulker_box"));
    }

    @GameTest
    public void obtainCopperShulkerBoxRecipeAdvancementByHavingBlackShulkerBox(TestContext context) {
        test(context,
                SHULKER_BOX_MAP.get(DyeColor.BLACK),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/copper_shulker_box"));
    }

    @GameTest
    public void obtainCopperShulkerBoxRecipeAdvancementByHavingCopperIngot(TestContext context) {
        test(context,
                Items.COPPER_INGOT,
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/copper_shulker_box"));
    }

    @GameTest
    public void obtainCopperShulkerBoxRecipeAdvancementByHavingCopperChest(TestContext context) {
        test(context,
                atonkish.reinfchest.item.ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("copper")),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID,
                        "recipes/decorations/copper_shulker_box_from_copper_chest"));
    }

    //
    // Iron Shulker Box
    //

    @GameTest
    public void obtainIronShulkerBoxRecipeAdvancementByHavingCopperShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get((DyeColor) null),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/iron_shulker_box"));
    }

    @GameTest
    public void obtainIronShulkerBoxRecipeAdvancementByHavingWhiteCopperShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.WHITE),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/iron_shulker_box"));
    }

    @GameTest
    public void obtainIronShulkerBoxRecipeAdvancementByHavingOrangeCopperShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.ORANGE),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/iron_shulker_box"));
    }

    @GameTest
    public void obtainIronShulkerBoxRecipeAdvancementByHavingMagentaCopperShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.MAGENTA),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/iron_shulker_box"));
    }

    @GameTest
    public void obtainIronShulkerBoxRecipeAdvancementByHavingLightBlueCopperShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIGHT_BLUE),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/iron_shulker_box"));
    }

    @GameTest
    public void obtainIronShulkerBoxRecipeAdvancementByHavingYellowCopperShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.YELLOW),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/iron_shulker_box"));
    }

    @GameTest
    public void obtainIronShulkerBoxRecipeAdvancementByHavingLimeCopperShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIME),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/iron_shulker_box"));
    }

    @GameTest
    public void obtainIronShulkerBoxRecipeAdvancementByHavingPinkCopperShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.PINK),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/iron_shulker_box"));
    }

    @GameTest
    public void obtainIronShulkerBoxRecipeAdvancementByHavingGrayCopperShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.GRAY),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/iron_shulker_box"));
    }

    @GameTest
    public void obtainIronShulkerBoxRecipeAdvancementByHavingLightGrayCopperShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.LIGHT_GRAY),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/iron_shulker_box"));
    }

    @GameTest
    public void obtainIronShulkerBoxRecipeAdvancementByHavingCyanCopperShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.CYAN),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/iron_shulker_box"));
    }

    @GameTest
    public void obtainIronShulkerBoxRecipeAdvancementByHavingPurpleCopperShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.PURPLE),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/iron_shulker_box"));
    }

    @GameTest
    public void obtainIronShulkerBoxRecipeAdvancementByHavingBlueCopperShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BLUE),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/iron_shulker_box"));
    }

    @GameTest
    public void obtainIronShulkerBoxRecipeAdvancementByHavingBrownCopperShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BROWN),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/iron_shulker_box"));
    }

    @GameTest
    public void obtainIronShulkerBoxRecipeAdvancementByHavingGreenCopperShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.GREEN),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/iron_shulker_box"));
    }

    @GameTest
    public void obtainIronShulkerBoxRecipeAdvancementByHavingRedCopperShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.RED),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/iron_shulker_box"));
    }

    @GameTest
    public void obtainIronShulkerBoxRecipeAdvancementByHavingBlackCopperShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(DyeColor.BLACK),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/iron_shulker_box"));
    }

    @GameTest
    public void obtainIronShulkerBoxRecipeAdvancementByHavingIronIngot(TestContext context) {
        test(context,
                Items.IRON_INGOT,
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/iron_shulker_box"));
    }

    @GameTest
    public void obtainIronShulkerBoxRecipeAdvancementByHavingIronChest(TestContext context) {
        test(context,
                atonkish.reinfchest.item.ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("iron")),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID,
                        "recipes/decorations/iron_shulker_box_from_iron_chest"));
    }

    //
    // Gold Shulker Box
    //

    @GameTest
    public void obtainGoldShulkerBoxRecipeAdvancementByHavingIronShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get((DyeColor) null),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/gold_shulker_box"));
    }

    @GameTest
    public void obtainGoldShulkerBoxRecipeAdvancementByHavingWhiteIronShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.WHITE),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/gold_shulker_box"));
    }

    @GameTest
    public void obtainGoldShulkerBoxRecipeAdvancementByHavingOrangeIronShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.ORANGE),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/gold_shulker_box"));
    }

    @GameTest
    public void obtainGoldShulkerBoxRecipeAdvancementByHavingMagentaIronShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.MAGENTA),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/gold_shulker_box"));
    }

    @GameTest
    public void obtainGoldShulkerBoxRecipeAdvancementByHavingLightBlueIronShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIGHT_BLUE),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/gold_shulker_box"));
    }

    @GameTest
    public void obtainGoldShulkerBoxRecipeAdvancementByHavingYellowIronShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.YELLOW),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/gold_shulker_box"));
    }

    @GameTest
    public void obtainGoldShulkerBoxRecipeAdvancementByHavingLimeIronShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIME),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/gold_shulker_box"));
    }

    @GameTest
    public void obtainGoldShulkerBoxRecipeAdvancementByHavingPinkIronShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.PINK),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/gold_shulker_box"));
    }

    @GameTest
    public void obtainGoldShulkerBoxRecipeAdvancementByHavingGrayIronShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.GRAY),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/gold_shulker_box"));
    }

    @GameTest
    public void obtainGoldShulkerBoxRecipeAdvancementByHavingLightGrayIronShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.LIGHT_GRAY),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/gold_shulker_box"));
    }

    @GameTest
    public void obtainGoldShulkerBoxRecipeAdvancementByHavingCyanIronShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.CYAN),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/gold_shulker_box"));
    }

    @GameTest
    public void obtainGoldShulkerBoxRecipeAdvancementByHavingPurpleIronShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.PURPLE),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/gold_shulker_box"));
    }

    @GameTest
    public void obtainGoldShulkerBoxRecipeAdvancementByHavingBlueIronShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BLUE),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/gold_shulker_box"));
    }

    @GameTest
    public void obtainGoldShulkerBoxRecipeAdvancementByHavingBrownIronShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BROWN),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/gold_shulker_box"));
    }

    @GameTest
    public void obtainGoldShulkerBoxRecipeAdvancementByHavingGreenIronShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.GREEN),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/gold_shulker_box"));
    }

    @GameTest
    public void obtainGoldShulkerBoxRecipeAdvancementByHavingRedIronShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.RED),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/gold_shulker_box"));
    }

    @GameTest
    public void obtainGoldShulkerBoxRecipeAdvancementByHavingBlackIronShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(DyeColor.BLACK),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/gold_shulker_box"));
    }

    @GameTest
    public void obtainGoldShulkerBoxRecipeAdvancementByHavingGoldIngot(TestContext context) {
        test(context,
                Items.GOLD_INGOT,
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/gold_shulker_box"));
    }

    @GameTest
    public void obtainGoldShulkerBoxRecipeAdvancementByHavingGoldChest(TestContext context) {
        test(context,
                atonkish.reinfchest.item.ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("gold")),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID,
                        "recipes/decorations/gold_shulker_box_from_gold_chest"));
    }

    //
    // Diamond Shulker Box
    //

    @GameTest
    public void obtainDiamondShulkerBoxRecipeAdvancementByHavingGoldShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get((DyeColor) null),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/diamond_shulker_box"));
    }

    @GameTest
    public void obtainDiamondShulkerBoxRecipeAdvancementByHavingWhiteGoldShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.WHITE),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/diamond_shulker_box"));
    }

    @GameTest
    public void obtainDiamondShulkerBoxRecipeAdvancementByHavingOrangeGoldShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.ORANGE),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/diamond_shulker_box"));
    }

    @GameTest
    public void obtainDiamondShulkerBoxRecipeAdvancementByHavingMagentaGoldShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.MAGENTA),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/diamond_shulker_box"));
    }

    @GameTest
    public void obtainDiamondShulkerBoxRecipeAdvancementByHavingLightBlueGoldShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIGHT_BLUE),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/diamond_shulker_box"));
    }

    @GameTest
    public void obtainDiamondShulkerBoxRecipeAdvancementByHavingYellowGoldShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.YELLOW),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/diamond_shulker_box"));
    }

    @GameTest
    public void obtainDiamondShulkerBoxRecipeAdvancementByHavingLimeGoldShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIME),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/diamond_shulker_box"));
    }

    @GameTest
    public void obtainDiamondShulkerBoxRecipeAdvancementByHavingPinkGoldShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.PINK),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/diamond_shulker_box"));
    }

    @GameTest
    public void obtainDiamondShulkerBoxRecipeAdvancementByHavingGrayGoldShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.GRAY),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/diamond_shulker_box"));
    }

    @GameTest
    public void obtainDiamondShulkerBoxRecipeAdvancementByHavingLightGrayGoldShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.LIGHT_GRAY),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/diamond_shulker_box"));
    }

    @GameTest
    public void obtainDiamondShulkerBoxRecipeAdvancementByHavingCyanGoldShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.CYAN),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/diamond_shulker_box"));
    }

    @GameTest
    public void obtainDiamondShulkerBoxRecipeAdvancementByHavingPurpleGoldShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.PURPLE),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/diamond_shulker_box"));
    }

    @GameTest
    public void obtainDiamondShulkerBoxRecipeAdvancementByHavingBlueGoldShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BLUE),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/diamond_shulker_box"));
    }

    @GameTest
    public void obtainDiamondShulkerBoxRecipeAdvancementByHavingBrownGoldShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BROWN),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/diamond_shulker_box"));
    }

    @GameTest
    public void obtainDiamondShulkerBoxRecipeAdvancementByHavingGreenGoldShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.GREEN),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/diamond_shulker_box"));
    }

    @GameTest
    public void obtainDiamondShulkerBoxRecipeAdvancementByHavingRedGoldShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.RED),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/diamond_shulker_box"));
    }

    @GameTest
    public void obtainDiamondShulkerBoxRecipeAdvancementByHavingBlackGoldShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(DyeColor.BLACK),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/diamond_shulker_box"));
    }

    @GameTest
    public void obtainDiamondShulkerBoxRecipeAdvancementByHavingDiamondIngot(TestContext context) {
        test(context,
                Items.DIAMOND,
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/diamond_shulker_box"));
    }

    @GameTest
    public void obtainDiamondShulkerBoxRecipeAdvancementByHavingDiamondChest(TestContext context) {
        test(context,
                atonkish.reinfchest.item.ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("diamond")),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID,
                        "recipes/decorations/diamond_shulker_box_from_diamond_chest"));
    }

    //
    // Netherite Shulker Box
    //

    @GameTest
    public void obtainNetheriteShulkerBoxRecipeAdvancementByHavingDiamondShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get((DyeColor) null),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/netherite_shulker_box_smithing"));
    }

    @GameTest
    public void obtainNetheriteShulkerBoxRecipeAdvancementByHavingWhiteDiamondShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.WHITE),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/netherite_shulker_box_smithing"));
    }

    @GameTest
    public void obtainNetheriteShulkerBoxRecipeAdvancementByHavingOrangeDiamondShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.ORANGE),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/netherite_shulker_box_smithing"));
    }

    @GameTest
    public void obtainNetheriteShulkerBoxRecipeAdvancementByHavingMagentaDiamondShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.MAGENTA),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/netherite_shulker_box_smithing"));
    }

    @GameTest
    public void obtainNetheriteShulkerBoxRecipeAdvancementByHavingLightBlueDiamondShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIGHT_BLUE),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/netherite_shulker_box_smithing"));
    }

    @GameTest
    public void obtainNetheriteShulkerBoxRecipeAdvancementByHavingYellowDiamondShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.YELLOW),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/netherite_shulker_box_smithing"));
    }

    @GameTest
    public void obtainNetheriteShulkerBoxRecipeAdvancementByHavingLimeDiamondShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIME),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/netherite_shulker_box_smithing"));
    }

    @GameTest
    public void obtainNetheriteShulkerBoxRecipeAdvancementByHavingPinkDiamondShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.PINK),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/netherite_shulker_box_smithing"));
    }

    @GameTest
    public void obtainNetheriteShulkerBoxRecipeAdvancementByHavingGrayDiamondShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.GRAY),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/netherite_shulker_box_smithing"));
    }

    @GameTest
    public void obtainNetheriteShulkerBoxRecipeAdvancementByHavingLightGrayDiamondShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.LIGHT_GRAY),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/netherite_shulker_box_smithing"));
    }

    @GameTest
    public void obtainNetheriteShulkerBoxRecipeAdvancementByHavingCyanDiamondShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.CYAN),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/netherite_shulker_box_smithing"));
    }

    @GameTest
    public void obtainNetheriteShulkerBoxRecipeAdvancementByHavingPurpleDiamondShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.PURPLE),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/netherite_shulker_box_smithing"));
    }

    @GameTest
    public void obtainNetheriteShulkerBoxRecipeAdvancementByHavingBlueDiamondShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BLUE),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/netherite_shulker_box_smithing"));
    }

    @GameTest
    public void obtainNetheriteShulkerBoxRecipeAdvancementByHavingBrownDiamondShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BROWN),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/netherite_shulker_box_smithing"));
    }

    @GameTest
    public void obtainNetheriteShulkerBoxRecipeAdvancementByHavingGreenDiamondShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.GREEN),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/netherite_shulker_box_smithing"));
    }

    @GameTest
    public void obtainNetheriteShulkerBoxRecipeAdvancementByHavingRedDiamondShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.RED),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/netherite_shulker_box_smithing"));
    }

    @GameTest
    public void obtainNetheriteShulkerBoxRecipeAdvancementByHavingBlackDiamondShulkerBox(TestContext context) {
        test(context,
                ModItems.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(DyeColor.BLACK),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/netherite_shulker_box_smithing"));
    }

    @GameTest
    public void obtainNetheriteShulkerBoxRecipeAdvancementByHavingNetheriteIngot(TestContext context) {
        test(context,
                Items.NETHERITE_INGOT,
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID, "recipes/decorations/netherite_shulker_box_smithing"));
    }

    @GameTest
    public void obtainNetheriteShulkerBoxRecipeAdvancementByHavingNetheriteChest(TestContext context) {
        test(context,
                atonkish.reinfchest.item.ModItems.REINFORCED_CHEST_MAP.get(ReinforcingMaterials.MAP.get("netherite")),
                Identifier.of(ReinforcedShulkerBoxesMod.MOD_ID,
                        "recipes/decorations/netherite_shulker_box_from_netherite_chest"));
    }
}
