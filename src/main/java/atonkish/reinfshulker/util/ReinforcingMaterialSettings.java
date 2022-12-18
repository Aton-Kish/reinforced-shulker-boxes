package atonkish.reinfshulker.util;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.sound.BlockSoundGroup;

import atonkish.reinfcore.api.ReinforcedCoreRegistry;
import atonkish.reinfcore.util.ReinforcingMaterial;

public enum ReinforcingMaterialSettings {
    COPPER(ReinforcedCoreRegistry.registerReinforcingMaterial("copper", 45, Items.COPPER_INGOT),
            FabricBlockSettings.of(Material.SHULKER_BOX).strength(2.0F, 6.0F).sounds(BlockSoundGroup.COPPER),
            new Item.Settings().maxCount(1)),
    IRON(ReinforcedCoreRegistry.registerReinforcingMaterial("iron", 54, Items.IRON_INGOT),
            FabricBlockSettings.of(Material.SHULKER_BOX).strength(2.0F, 6.0F).sounds(BlockSoundGroup.METAL),
            new Item.Settings().maxCount(1)),
    GOLD(ReinforcedCoreRegistry.registerReinforcingMaterial("gold", 81, Items.GOLD_INGOT),
            FabricBlockSettings.of(Material.SHULKER_BOX).strength(2.0F, 6.0F).sounds(BlockSoundGroup.METAL),
            new Item.Settings().maxCount(1)),
    DIAMOND(ReinforcedCoreRegistry.registerReinforcingMaterial("diamond", 108, Items.DIAMOND),
            FabricBlockSettings.of(Material.SHULKER_BOX).strength(2.0F, 6.0F).sounds(BlockSoundGroup.METAL),
            new Item.Settings().maxCount(1)),
    NETHERITE(ReinforcedCoreRegistry.registerReinforcingMaterial("netherite", 108, Items.NETHERITE_INGOT),
            FabricBlockSettings.of(Material.SHULKER_BOX).strength(2.0F, 1200.0F).sounds(BlockSoundGroup.NETHERITE),
            new Item.Settings().maxCount(1).fireproof());

    private final ReinforcingMaterial material;
    private final Block.Settings blockSettings;
    private final Item.Settings itemSettings;

    private ReinforcingMaterialSettings(ReinforcingMaterial material, Block.Settings blockSettings,
            Item.Settings itemSettings) {
        AbstractBlock.ContextPredicate contextPredicate = (state, world, pos) -> {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (!(blockEntity instanceof ShulkerBoxBlockEntity)) {
                return true;
            } else {
                ShulkerBoxBlockEntity shulkerBoxBlockEntity = (ShulkerBoxBlockEntity) blockEntity;
                return shulkerBoxBlockEntity.suffocates();
            }
        };

        this.material = material;
        this.blockSettings = blockSettings.dynamicBounds().nonOpaque()
                .suffocates(contextPredicate).blockVision(contextPredicate);
        this.itemSettings = itemSettings;
    }

    public ReinforcingMaterial getMaterial() {
        return this.material;
    }

    public Block.Settings getBlockSettings() {
        return this.blockSettings;
    }

    public Item.Settings getItemSettings() {
        return this.itemSettings;
    }
}
