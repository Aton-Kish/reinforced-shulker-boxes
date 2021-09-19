package atonkish.reinfshulker.block.dispenser;

import java.util.HashMap;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.BlockPlacementDispenserBehavior;
import net.minecraft.item.Item;
import net.minecraft.util.DyeColor;

import atonkish.reinfshulker.item.ModItems;

public interface ModDispenserBehavior {
    public static void init() {
        for (HashMap<DyeColor, Item> materialShulkerBoxMap : ModItems.REINFORCED_SHULKER_BOX_MAP.values()) {
            for (Item item : materialShulkerBoxMap.values()) {
                DispenserBlock.registerBehavior(item, new BlockPlacementDispenserBehavior());
            }
        }
    }
}
