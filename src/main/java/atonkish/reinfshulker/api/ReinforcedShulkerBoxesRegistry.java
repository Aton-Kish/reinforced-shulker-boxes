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

public class ReinforcedChestsRegistry {
    public static Identifier registerMaterialCleanStat(ReinforcingMaterial material) {
        return ModStats.registerMaterialClean(material);
    }

    public static Identifier registerMaterialOpenStat(ReinforcingMaterial material) {
        return ModStats.registerMaterialOpen(material);
    }

    public static Block registerMaterialDyeColorBlock(ReinforcingMaterial material, DyeColor color,
            Block.Settings settings) {
        return ModBlocks.registerMaterialDyeColor(material, color, settings);
    }

    public static BlockEntityType<ReinforcedShulkerBoxBlockEntity> registerMaterialBlockEntityType(
            ReinforcingMaterial material) {
        return ModBlockEntityType.registerMaterial(material);
    }

    public static Item registerMaterialDyeColorItem(ReinforcingMaterial material, DyeColor color,
            Item.Settings settings) {
        return ModItems.registerMaterialDyeColor(material, color, settings);
    }

    public static void registerMaterialDyeColorItemGroupIcon(ReinforcingMaterial material, DyeColor color) {
        ModItems.registerMaterialDyeColorItemGroupIcon(material, color);
    }
}
