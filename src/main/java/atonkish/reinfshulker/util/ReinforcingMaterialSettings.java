package atonkish.reinfshulker.util;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;

import atonkish.reinfcore.api.ReinforcedCoreRegistry;
import atonkish.reinfcore.util.ReinforcingMaterial;

public enum ReinforcingMaterialSettings {
    COPPER(ReinforcedCoreRegistry.registerReinforcingMaterial("copper", 45, Items.COPPER_INGOT),
            FabricBlockSettings
                    .create()
                    .strength(2.0F, 6.0F)
                    .sounds(BlockSoundGroup.COPPER),
            new Item.Settings()),
    IRON(ReinforcedCoreRegistry.registerReinforcingMaterial("iron", 54, Items.IRON_INGOT),
            FabricBlockSettings
                    .create()
                    .instrument(Instrument.IRON_XYLOPHONE)
                    .strength(2.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL),
            new Item.Settings()),
    GOLD(ReinforcedCoreRegistry.registerReinforcingMaterial("gold", 81, Items.GOLD_INGOT),
            FabricBlockSettings
                    .create()
                    .instrument(Instrument.BELL)
                    .strength(2.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL),
            new Item.Settings()),
    DIAMOND(ReinforcedCoreRegistry.registerReinforcingMaterial("diamond", 108, Items.DIAMOND),
            FabricBlockSettings
                    .create()
                    .strength(2.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL),
            new Item.Settings()),
    NETHERITE(ReinforcedCoreRegistry.registerReinforcingMaterial("netherite", 108, Items.NETHERITE_INGOT),
            FabricBlockSettings
                    .create()
                    .strength(2.0F, 1200.0F)
                    .sounds(BlockSoundGroup.NETHERITE),
            new Item.Settings().fireproof());

    private final ReinforcingMaterial material;
    private final Block.Settings blockSettings;
    private final Item.Settings itemSettings;

    private ReinforcingMaterialSettings(ReinforcingMaterial material, Block.Settings blockSettings,
            Item.Settings itemSettings) {
        AbstractBlock.ContextPredicate contextPredicate = (state, world, pos) -> {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (!(blockEntity instanceof ShulkerBoxBlockEntity)) {
                return true;
            }
            ShulkerBoxBlockEntity shulkerBoxBlockEntity = (ShulkerBoxBlockEntity) blockEntity;
            return shulkerBoxBlockEntity.suffocates();
        };

        this.material = material;
        this.blockSettings = blockSettings.solid().dynamicBounds().nonOpaque()
                .suffocates(contextPredicate).blockVision(contextPredicate)
                .pistonBehavior(PistonBehavior.DESTROY).solidBlock(Blocks::always);
        this.itemSettings = itemSettings.maxCount(1);
    }

    public ReinforcingMaterial getMaterial() {
        return this.material;
    }

    public Block.Settings getBlockSettings() {
        return this.blockSettings.mapColor(MapColor.PURPLE);
    }

    public Block.Settings getColorBlockSettings(DyeColor color) {
        MapColor mapColor = switch (color) {
            case PURPLE -> MapColor.TERRACOTTA_PURPLE;
            default -> color.getMapColor();
        };

        return this.blockSettings.mapColor(mapColor);
    }

    public Item.Settings getItemSettings() {
        return this.itemSettings;
    }
}
