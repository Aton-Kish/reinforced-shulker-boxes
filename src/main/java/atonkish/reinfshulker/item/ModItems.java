package atonkish.reinfshulker.item;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.DyeColor;

import atonkish.reinfcore.item.ModItemGroup;
import atonkish.reinfcore.item.ModItemGroups;
import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.block.ModBlocks;

public class ModItems {
    public static final Map<ReinforcingMaterial, Map<DyeColor, Item>> REINFORCED_SHULKER_BOX_MAP = new LinkedHashMap<>();
    public static final Map<ReinforcingMaterial, Map<DyeColor, Item.Settings>> REINFORCED_SHULKER_BOX_SETTINGS_MAP = new LinkedHashMap<>();

    public static Item registerMaterialDyeColor(ReinforcingMaterial material, DyeColor color,
            Item.Settings settings) {
        if (!REINFORCED_SHULKER_BOX_SETTINGS_MAP.containsKey(material)) {
            REINFORCED_SHULKER_BOX_SETTINGS_MAP.put(material, new LinkedHashMap<>());
        }

        if (!REINFORCED_SHULKER_BOX_MAP.containsKey(material)) {
            REINFORCED_SHULKER_BOX_MAP.put(material, new LinkedHashMap<>());
        }

        if (!REINFORCED_SHULKER_BOX_SETTINGS_MAP.get(material).containsKey(color)) {
            REINFORCED_SHULKER_BOX_SETTINGS_MAP.get(material).put(color, settings);
        }

        if (!REINFORCED_SHULKER_BOX_MAP.get(material).containsKey(color)) {
            Item item = ModItems.register(ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(material).get(color),
                    REINFORCED_SHULKER_BOX_SETTINGS_MAP.get(material).get(color));
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register(content -> content.add(item));
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> content.add(item));
            ItemGroupEvents.modifyEntriesEvent(ModItemGroups.REINFORCED_STORAGE).register(content -> content.add(item));
            REINFORCED_SHULKER_BOX_MAP.get(material).put(color, item);
        }

        return REINFORCED_SHULKER_BOX_MAP.get(material).get(color);
    }

    public static void registerMaterialDyeColorItemGroupIcon(ReinforcingMaterial material, DyeColor color) {
        Item item = REINFORCED_SHULKER_BOX_MAP.get(material).get(color);
        ModItemGroup.setIcon(Registries.ITEM_GROUP.get(ModItemGroups.REINFORCED_STORAGE), item);
    }

    private static RegistryKey<Item> keyOf(RegistryKey<Block> blockKey) {
        return RegistryKey.of(RegistryKeys.ITEM, blockKey.getValue());
    }

    public static Item register(Block block, Item.Settings settings) {
        return register(block, BlockItem::new, settings);
    }

    private static Item register(Block block, BiFunction<Block, Item.Settings, Item> factory, Item.Settings settings) {
        return register(
                keyOf(block.getRegistryEntry().registryKey()),
                itemSettings -> (Item) factory.apply(block, itemSettings), settings.useBlockPrefixedTranslationKey());
    }

    private static Item register(RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings) {
        Item item = factory.apply(settings.registryKey(key));
        if (item instanceof BlockItem blockItem) {
            blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
        }

        return Registry.register(Registries.ITEM, key, item);
    }
}
