package atonkish.reinfshulker.item;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import atonkish.reinfcore.item.ItemGroupInterface;
import atonkish.reinfcore.item.ModItemGroup;
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
            Item item = register(
                    new BlockItem(ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(material).get(color),
                            REINFORCED_SHULKER_BOX_SETTINGS_MAP.get(material).get(color)));
            REINFORCED_SHULKER_BOX_MAP.get(material).put(color, item);
        }

        return REINFORCED_SHULKER_BOX_MAP.get(material).get(color);
    }

    public static void registerMaterialDyeColorItemGroupIcon(ReinforcingMaterial material, DyeColor color) {
        Item item = REINFORCED_SHULKER_BOX_MAP.get(material).get(color);
        ((ItemGroupInterface) ModItemGroup.REINFORCED_STORAGE).setIcon(item);
    }

    private static Item register(BlockItem item) {
        return register(item.getBlock(), (Item) item);
    }

    protected static Item register(Block block, Item item) {
        return register(Registry.BLOCK.getId(block), item);
    }

    private static Item register(Identifier id, Item item) {
        if (item instanceof BlockItem) {
            ((BlockItem) item).appendBlocks(Item.BLOCK_ITEMS, item);
        }

        return Registry.register(Registry.ITEM, id, item);
    }
}
