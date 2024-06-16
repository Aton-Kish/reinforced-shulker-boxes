package atonkish.reinfshulker.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.recipe.input.SmithingRecipeInput;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.StructureTestUtil;
import net.minecraft.test.TestFunction;
import net.minecraft.util.DyeColor;

import atonkish.reinfcore.util.ReinforcingMaterials;
import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;
import atonkish.reinfshulker.item.ModItems;

public class RecipeTests {
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

    private static final String BATCH_ID = String.format("%s:RecipeBatch",
            ReinforcedShulkerBoxesMod.MOD_ID);

    public static final Collection<TestFunction> TEST_FUNCTIONS = new ArrayList<>() {
        {
            // Copper Shulker Box
            for (DyeColor color : SHULKER_BOX_MAP.keySet()) {
                ItemStack baseShulkerBox = new ItemStack(SHULKER_BOX_MAP.get(color));
                baseShulkerBox.set(DataComponentTypes.CONTAINER,
                        ContainerComponent.fromStacks(List.of(new ItemStack(Items.DIRT))));
                ItemStack material = new ItemStack(Items.COPPER_INGOT);
                ItemStack shulkerBox = new ItemStack(
                        ModItems.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("copper")).get(color));
                shulkerBox.set(DataComponentTypes.CONTAINER,
                        ContainerComponent.fromStacks(List.of(new ItemStack(Items.DIRT))));

                add(RecipeTests.createTest(
                        String.format("Craft %s", baseShulkerBox.getItem().getName().getString()),
                        RecipeType.CRAFTING,
                        CraftingRecipeInput.create(3, 3, List.of(
                                material, material, material,
                                material, baseShulkerBox, material,
                                material, material, material)),
                        shulkerBox));
            }

            for (DyeColor baseColor : SHULKER_BOX_MAP.keySet()) {
                for (DyeColor dyeColor : DyeColor.values()) {
                    ItemStack baseShulkerBox = new ItemStack(
                            ModItems.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("copper"))
                                    .get(baseColor));
                    baseShulkerBox.set(DataComponentTypes.CONTAINER,
                            ContainerComponent.fromStacks(List.of(new ItemStack(Items.DIRT))));
                    ItemStack dye = new ItemStack(DyeItem.byColor(dyeColor));
                    ItemStack dyedShulkerBox = new ItemStack(
                            ModItems.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("copper"))
                                    .get(dyeColor));
                    dyedShulkerBox.set(DataComponentTypes.CONTAINER,
                            ContainerComponent.fromStacks(List.of(new ItemStack(Items.DIRT))));

                    add(RecipeTests.createTest(
                            String.format("Coloring from %s to %s",
                                    baseShulkerBox.getItem().getName().getString(),
                                    dyedShulkerBox.getItem().getName().getString()),
                            RecipeType.CRAFTING,
                            CraftingRecipeInput.create(2, 2, List.of(
                                    baseShulkerBox, dye,
                                    ItemStack.EMPTY, ItemStack.EMPTY)),
                            dyedShulkerBox));
                }
            }

            {
                ItemStack chest = new ItemStack(
                        atonkish.reinfchest.item.ModItems.REINFORCED_CHEST_MAP
                                .get(ReinforcingMaterials.MAP.get("copper")));
                ItemStack shell = new ItemStack(Items.SHULKER_SHELL);
                ItemStack shulkerBox = new ItemStack(
                        ModItems.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("copper"))
                                .get((DyeColor) null));

                add(RecipeTests.createTest(
                        String.format("Craft %s from %s",
                                shulkerBox.getItem().getName().getString(),
                                chest.getItem().getName().getString()),
                        RecipeType.CRAFTING,
                        CraftingRecipeInput.create(3, 3, List.of(
                                shell, ItemStack.EMPTY, ItemStack.EMPTY,
                                chest, ItemStack.EMPTY, ItemStack.EMPTY,
                                shell, ItemStack.EMPTY, ItemStack.EMPTY)),
                        shulkerBox));
            }

            // Iron Shulker Box
            for (DyeColor color : SHULKER_BOX_MAP.keySet()) {
                ItemStack baseShulkerBox = new ItemStack(
                        ModItems.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("copper")).get(color));
                baseShulkerBox.set(DataComponentTypes.CONTAINER,
                        ContainerComponent.fromStacks(List.of(new ItemStack(Items.DIRT))));
                ItemStack material = new ItemStack(Items.IRON_INGOT);
                ItemStack shulkerBox = new ItemStack(
                        ModItems.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("iron")).get(color));
                shulkerBox.set(DataComponentTypes.CONTAINER,
                        ContainerComponent.fromStacks(List.of(new ItemStack(Items.DIRT))));

                add(RecipeTests.createTest(
                        String.format("Craft %s", baseShulkerBox.getItem().getName().getString()),
                        RecipeType.CRAFTING,
                        CraftingRecipeInput.create(3, 3, List.of(
                                material, material, material,
                                material, baseShulkerBox, material,
                                material, material, material)),
                        shulkerBox));
            }

            for (DyeColor baseColor : SHULKER_BOX_MAP.keySet()) {
                for (DyeColor dyeColor : DyeColor.values()) {
                    ItemStack baseShulkerBox = new ItemStack(
                            ModItems.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("iron"))
                                    .get(baseColor));
                    baseShulkerBox.set(DataComponentTypes.CONTAINER,
                            ContainerComponent.fromStacks(List.of(new ItemStack(Items.DIRT))));
                    ItemStack dye = new ItemStack(DyeItem.byColor(dyeColor));
                    ItemStack dyedShulkerBox = new ItemStack(
                            ModItems.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("iron"))
                                    .get(dyeColor));
                    dyedShulkerBox.set(DataComponentTypes.CONTAINER,
                            ContainerComponent.fromStacks(List.of(new ItemStack(Items.DIRT))));

                    add(RecipeTests.createTest(
                            String.format("Coloring from %s to %s",
                                    baseShulkerBox.getItem().getName().getString(),
                                    dyedShulkerBox.getItem().getName().getString()),
                            RecipeType.CRAFTING,
                            CraftingRecipeInput.create(2, 2, List.of(
                                    baseShulkerBox, dye,
                                    ItemStack.EMPTY, ItemStack.EMPTY)),
                            dyedShulkerBox));
                }
            }

            {
                ItemStack chest = new ItemStack(
                        atonkish.reinfchest.item.ModItems.REINFORCED_CHEST_MAP
                                .get(ReinforcingMaterials.MAP.get("iron")));
                ItemStack shell = new ItemStack(Items.SHULKER_SHELL);
                ItemStack shulkerBox = new ItemStack(
                        ModItems.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("iron"))
                                .get((DyeColor) null));

                add(RecipeTests.createTest(
                        String.format("Craft %s from %s",
                                shulkerBox.getItem().getName().getString(),
                                chest.getItem().getName().getString()),
                        RecipeType.CRAFTING,
                        CraftingRecipeInput.create(3, 3, List.of(
                                shell, ItemStack.EMPTY, ItemStack.EMPTY,
                                chest, ItemStack.EMPTY, ItemStack.EMPTY,
                                shell, ItemStack.EMPTY, ItemStack.EMPTY)),
                        shulkerBox));
            }

            // Gold Shulker Box
            for (DyeColor color : SHULKER_BOX_MAP.keySet()) {
                ItemStack baseShulkerBox = new ItemStack(
                        ModItems.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("iron")).get(color));
                baseShulkerBox.set(DataComponentTypes.CONTAINER,
                        ContainerComponent.fromStacks(List.of(new ItemStack(Items.DIRT))));
                ItemStack material = new ItemStack(Items.GOLD_INGOT);
                ItemStack shulkerBox = new ItemStack(
                        ModItems.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("gold")).get(color));
                shulkerBox.set(DataComponentTypes.CONTAINER,
                        ContainerComponent.fromStacks(List.of(new ItemStack(Items.DIRT))));

                add(RecipeTests.createTest(
                        String.format("Craft %s", baseShulkerBox.getItem().getName().getString()),
                        RecipeType.CRAFTING,
                        CraftingRecipeInput.create(3, 3, List.of(
                                material, material, material,
                                material, baseShulkerBox, material,
                                material, material, material)),
                        shulkerBox));
            }

            for (DyeColor baseColor : SHULKER_BOX_MAP.keySet()) {
                for (DyeColor dyeColor : DyeColor.values()) {
                    ItemStack baseShulkerBox = new ItemStack(
                            ModItems.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("gold"))
                                    .get(baseColor));
                    baseShulkerBox.set(DataComponentTypes.CONTAINER,
                            ContainerComponent.fromStacks(List.of(new ItemStack(Items.DIRT))));
                    ItemStack dye = new ItemStack(DyeItem.byColor(dyeColor));
                    ItemStack dyedShulkerBox = new ItemStack(
                            ModItems.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("gold"))
                                    .get(dyeColor));
                    dyedShulkerBox.set(DataComponentTypes.CONTAINER,
                            ContainerComponent.fromStacks(List.of(new ItemStack(Items.DIRT))));

                    add(RecipeTests.createTest(
                            String.format("Coloring from %s to %s",
                                    baseShulkerBox.getItem().getName().getString(),
                                    dyedShulkerBox.getItem().getName().getString()),
                            RecipeType.CRAFTING,
                            CraftingRecipeInput.create(2, 2, List.of(
                                    baseShulkerBox, dye,
                                    ItemStack.EMPTY, ItemStack.EMPTY)),
                            dyedShulkerBox));
                }
            }

            {
                ItemStack chest = new ItemStack(
                        atonkish.reinfchest.item.ModItems.REINFORCED_CHEST_MAP
                                .get(ReinforcingMaterials.MAP.get("gold")));
                ItemStack shell = new ItemStack(Items.SHULKER_SHELL);
                ItemStack shulkerBox = new ItemStack(
                        ModItems.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("gold"))
                                .get((DyeColor) null));

                add(RecipeTests.createTest(
                        String.format("Craft %s from %s",
                                shulkerBox.getItem().getName().getString(),
                                chest.getItem().getName().getString()),
                        RecipeType.CRAFTING,
                        CraftingRecipeInput.create(3, 3, List.of(
                                shell, ItemStack.EMPTY, ItemStack.EMPTY,
                                chest, ItemStack.EMPTY, ItemStack.EMPTY,
                                shell, ItemStack.EMPTY, ItemStack.EMPTY)),
                        shulkerBox));
            }

            // Diamond Shulker Box
            for (DyeColor color : SHULKER_BOX_MAP.keySet()) {
                ItemStack baseShulkerBox = new ItemStack(
                        ModItems.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("gold")).get(color));
                baseShulkerBox.set(DataComponentTypes.CONTAINER,
                        ContainerComponent.fromStacks(List.of(new ItemStack(Items.DIRT))));
                ItemStack material = new ItemStack(Items.DIAMOND);
                ItemStack shulkerBox = new ItemStack(
                        ModItems.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("diamond")).get(color));
                shulkerBox.set(DataComponentTypes.CONTAINER,
                        ContainerComponent.fromStacks(List.of(new ItemStack(Items.DIRT))));

                add(RecipeTests.createTest(
                        String.format("Craft %s", baseShulkerBox.getItem().getName().getString()),
                        RecipeType.CRAFTING,
                        CraftingRecipeInput.create(3, 3, List.of(
                                material, material, material,
                                material, baseShulkerBox, material,
                                material, material, material)),
                        shulkerBox));
            }

            for (DyeColor baseColor : SHULKER_BOX_MAP.keySet()) {
                for (DyeColor dyeColor : DyeColor.values()) {
                    ItemStack baseShulkerBox = new ItemStack(
                            ModItems.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("diamond"))
                                    .get(baseColor));
                    baseShulkerBox.set(DataComponentTypes.CONTAINER,
                            ContainerComponent.fromStacks(List.of(new ItemStack(Items.DIRT))));
                    ItemStack dye = new ItemStack(DyeItem.byColor(dyeColor));
                    ItemStack dyedShulkerBox = new ItemStack(
                            ModItems.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("diamond"))
                                    .get(dyeColor));
                    dyedShulkerBox.set(DataComponentTypes.CONTAINER,
                            ContainerComponent.fromStacks(List.of(new ItemStack(Items.DIRT))));

                    add(RecipeTests.createTest(
                            String.format("Coloring from %s to %s",
                                    baseShulkerBox.getItem().getName().getString(),
                                    dyedShulkerBox.getItem().getName().getString()),
                            RecipeType.CRAFTING,
                            CraftingRecipeInput.create(2, 2, List.of(
                                    baseShulkerBox, dye,
                                    ItemStack.EMPTY, ItemStack.EMPTY)),
                            dyedShulkerBox));
                }
            }

            {
                ItemStack chest = new ItemStack(
                        atonkish.reinfchest.item.ModItems.REINFORCED_CHEST_MAP
                                .get(ReinforcingMaterials.MAP.get("diamond")));
                ItemStack shell = new ItemStack(Items.SHULKER_SHELL);
                ItemStack shulkerBox = new ItemStack(
                        ModItems.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("diamond"))
                                .get((DyeColor) null));

                add(RecipeTests.createTest(
                        String.format("Craft %s from %s",
                                shulkerBox.getItem().getName().getString(),
                                chest.getItem().getName().getString()),
                        RecipeType.CRAFTING,
                        CraftingRecipeInput.create(3, 3, List.of(
                                shell, ItemStack.EMPTY, ItemStack.EMPTY,
                                chest, ItemStack.EMPTY, ItemStack.EMPTY,
                                shell, ItemStack.EMPTY, ItemStack.EMPTY)),
                        shulkerBox));
            }

            // Netherite Shulker Box
            for (DyeColor color : SHULKER_BOX_MAP.keySet()) {
                ItemStack template = new ItemStack(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
                ItemStack baseShulkerBox = new ItemStack(
                        ModItems.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("diamond")).get(color));
                baseShulkerBox.set(DataComponentTypes.CONTAINER,
                        ContainerComponent.fromStacks(List.of(new ItemStack(Items.DIRT))));
                ItemStack material = new ItemStack(Items.NETHERITE_INGOT);
                ItemStack shulkerBox = new ItemStack(
                        ModItems.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("netherite")).get(color));
                shulkerBox.set(DataComponentTypes.CONTAINER,
                        ContainerComponent.fromStacks(List.of(new ItemStack(Items.DIRT))));

                add(RecipeTests.createTest(
                        String.format("Smithing %s", baseShulkerBox.getItem().getName().getString()),
                        RecipeType.SMITHING,
                        new SmithingRecipeInput(template, baseShulkerBox, material),
                        shulkerBox));
            }

            for (DyeColor baseColor : SHULKER_BOX_MAP.keySet()) {
                for (DyeColor dyeColor : DyeColor.values()) {
                    ItemStack baseShulkerBox = new ItemStack(
                            ModItems.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("netherite"))
                                    .get(baseColor));
                    baseShulkerBox.set(DataComponentTypes.CONTAINER,
                            ContainerComponent.fromStacks(List.of(new ItemStack(Items.DIRT))));
                    ItemStack dye = new ItemStack(DyeItem.byColor(dyeColor));
                    ItemStack dyedShulkerBox = new ItemStack(
                            ModItems.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("netherite"))
                                    .get(dyeColor));
                    dyedShulkerBox.set(DataComponentTypes.CONTAINER,
                            ContainerComponent.fromStacks(List.of(new ItemStack(Items.DIRT))));

                    add(RecipeTests.createTest(
                            String.format("Coloring from %s to %s",
                                    baseShulkerBox.getItem().getName().getString(),
                                    dyedShulkerBox.getItem().getName().getString()),
                            RecipeType.CRAFTING,
                            CraftingRecipeInput.create(2, 2, List.of(
                                    baseShulkerBox, dye,
                                    ItemStack.EMPTY, ItemStack.EMPTY)),
                            dyedShulkerBox));
                }
            }

            {
                ItemStack chest = new ItemStack(
                        atonkish.reinfchest.item.ModItems.REINFORCED_CHEST_MAP
                                .get(ReinforcingMaterials.MAP.get("netherite")));
                ItemStack shell = new ItemStack(Items.SHULKER_SHELL);
                ItemStack shulkerBox = new ItemStack(
                        ModItems.REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterials.MAP.get("netherite"))
                                .get((DyeColor) null));

                add(RecipeTests.createTest(
                        String.format("Craft %s from %s",
                                shulkerBox.getItem().getName().getString(),
                                chest.getItem().getName().getString()),
                        RecipeType.CRAFTING,
                        CraftingRecipeInput.create(3, 3, List.of(
                                shell, ItemStack.EMPTY, ItemStack.EMPTY,
                                chest, ItemStack.EMPTY, ItemStack.EMPTY,
                                shell, ItemStack.EMPTY, ItemStack.EMPTY)),
                        shulkerBox));
            }
        }
    };

    private static <I extends RecipeInput, T extends Recipe<I>> TestFunction createTest(String name,
            RecipeType<T> type, I input, ItemStack expected) {
        String testName = String.format("%s %s %s",
                ReinforcedShulkerBoxesMod.MOD_ID,
                RecipeTests.class.getSimpleName(),
                name)
                .replace(" ", "_");

        return new TestFunction(
                RecipeTests.BATCH_ID,
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
                    ServerWorld world = context.getWorld();
                    RecipeManager recipeManager = world.getRecipeManager();
                    DynamicRegistryManager registryManager = world.getRegistryManager();
                    T recipe = recipeManager.getFirstMatch(type, input, world).orElseThrow().value();

                    // Act
                    ItemStack actual = recipe.craft(input, registryManager);

                    // Assert
                    try {
                        context.assertTrue(ItemStack.areEqual(actual, expected),
                                "Recipe result differs from expected.");
                    } catch (Exception e) {
                        ReinforcedShulkerBoxesMod.LOGGER.error("[{}] {}", testName, e.getMessage());
                        throw e;
                    }

                    context.complete();
                });
    }
}
