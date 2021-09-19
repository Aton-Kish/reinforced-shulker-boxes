package atonkish.reinfshulker.item;

import java.util.HashMap;

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
    public static final HashMap<ReinforcingMaterial, HashMap<DyeColor, Item>> REINFORCED_SHULKER_BOX_MAP;

    public static void init() {
        Item iconItem = REINFORCED_SHULKER_BOX_MAP.get(ReinforcingMaterial.NETHERITE).get((DyeColor) null);
        ((ItemGroupInterface) ModItemGroup.REINFORCED_STORAGE).setIcon(iconItem);
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

    private static Item.Settings createMaterialSettings(ReinforcingMaterial material) {
        Item.Settings settings = new Item.Settings().group(ModItemGroup.REINFORCED_STORAGE);
        switch (material) {
            default:
            case COPPER:
            case IRON:
            case GOLD:
            case DIAMOND:
                break;
            case NETHERITE:
                settings = settings.fireproof();
                break;
        }
        return settings;
    }

    static {
        REINFORCED_SHULKER_BOX_MAP = new HashMap<>();
        for (ReinforcingMaterial material : ReinforcingMaterial.values()) {
            HashMap<DyeColor, Item> materialShulkerBoxMap = new HashMap<>();
            // Non-Color
            Item nonColoredItem = register(
                    new BlockItem(ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(material).get((DyeColor) null),
                            createMaterialSettings(material)));
            materialShulkerBoxMap.put((DyeColor) null, nonColoredItem);
            // Color
            for (DyeColor color : DyeColor.values()) {
                Item coloredItem = register(new BlockItem(ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(material).get(color),
                        createMaterialSettings(material)));
                materialShulkerBoxMap.put(color, coloredItem);
            }
            REINFORCED_SHULKER_BOX_MAP.put(material, materialShulkerBoxMap);
        }
    }
}
