package atonkish.reinfshulker.api;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.block.ModBlocks;
import atonkish.reinfshulker.block.entity.ModBlockEntityType;
import atonkish.reinfshulker.block.entity.ReinforcedShulkerBoxBlockEntity;
import atonkish.reinfshulker.item.ModItems;
import atonkish.reinfshulker.stat.ModStats;

public class ReinforcedShulkerBoxesRegistry {
    public static Identifier registerMaterialCleanStat(String namespace, ReinforcingMaterial material) {
        return ModStats.registerMaterialClean(namespace, material);
    }

    public static Identifier registerMaterialOpenStat(String namespace, ReinforcingMaterial material) {
        return ModStats.registerMaterialOpen(namespace, material);
    }

    public static Block registerMaterialDyeColorBlock(String namespace, ReinforcingMaterial material, DyeColor color,
            Block.Settings settings) {
        return ModBlocks.registerMaterialDyeColor(namespace, material, color, settings);
    }

    public static BlockEntityType<ReinforcedShulkerBoxBlockEntity> registerMaterialBlockEntityType(String namespace,
            ReinforcingMaterial material) {
        return ModBlockEntityType.registerMaterial(namespace, material);
    }

    public static Item registerMaterialDyeColorItem(String namespace, ReinforcingMaterial material, DyeColor color,
            Item.Settings settings) {
        return ModItems.registerMaterialDyeColor(material, color, settings);
    }

    public static void registerMaterialDyeColorItemGroupIcon(String namespace, ReinforcingMaterial material,
            DyeColor color) {
        ModItems.registerMaterialDyeColorItemGroupIcon(material, color);
    }
}
