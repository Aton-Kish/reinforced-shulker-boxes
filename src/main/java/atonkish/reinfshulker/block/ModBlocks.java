package atonkish.reinfshulker.block;

import java.util.HashMap;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;

public class ModBlocks {
    public static final HashMap<ReinforcingMaterial, HashMap<DyeColor, Block>> REINFORCED_SHULKER_BOX_MAP;

    public static void init() {
    }

    private static Block register(String id, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(ReinforcedShulkerBoxesMod.MOD_ID, id), block);
    }

    private static FabricBlockSettings createMaterialSettings(ReinforcingMaterial material) {
        AbstractBlock.ContextPredicate contextPredicate = (state, world, pos) -> {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (!(blockEntity instanceof ShulkerBoxBlockEntity)) {
                return true;
            } else {
                ShulkerBoxBlockEntity shulkerBoxBlockEntity = (ShulkerBoxBlockEntity) blockEntity;
                return shulkerBoxBlockEntity.suffocates();
            }
        };
        FabricBlockSettings settings = FabricBlockSettings.of(Material.SHULKER_BOX).breakByTool(FabricToolTags.PICKAXES)
                .dynamicBounds().nonOpaque().suffocates(contextPredicate).blockVision(contextPredicate);
        switch (material) {
            case COPPER:
                settings = settings.strength(2.0F, 6.0F).sounds(BlockSoundGroup.COPPER);
                break;
            case IRON:
            case GOLD:
            case DIAMOND:
                settings = settings.strength(2.0F, 6.0F).sounds(BlockSoundGroup.METAL);
                break;
            case NETHERITE:
                settings = settings.strength(2.0F, 1200.0F).sounds(BlockSoundGroup.NETHERITE);
                break;
            default:
                settings = settings.strength(2.0F);
        }
        return settings;
    }

    static {
        REINFORCED_SHULKER_BOX_MAP = new HashMap<>();
        for (ReinforcingMaterial material : ReinforcingMaterial.values()) {
            HashMap<DyeColor, Block> materialShulkerBoxMap = new HashMap<>();
            // Non-Color
            Block nonColoredBlock = register(material.getName() + "_shulker_box",
                    new ReinforcedShulkerBoxBlock(material, (DyeColor) null, createMaterialSettings(material)));
            materialShulkerBoxMap.put((DyeColor) null, nonColoredBlock);
            // Color
            for (DyeColor color : DyeColor.values()) {
                Block coloredBlock = register(color.getName() + "_" + material.getName() + "_shulker_box",
                        new ReinforcedShulkerBoxBlock(material, color, createMaterialSettings(material)));
                materialShulkerBoxMap.put(color, coloredBlock);
            }
            REINFORCED_SHULKER_BOX_MAP.put(material, materialShulkerBoxMap);
        }
    }
}
