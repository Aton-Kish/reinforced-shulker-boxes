package atonkish.reinfshulker.block;

import java.util.LinkedHashMap;

import net.minecraft.block.Block;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;

public class ModBlocks {
    public static final LinkedHashMap<ReinforcingMaterial, LinkedHashMap<DyeColor, Block>> REINFORCED_SHULKER_BOX_MAP = new LinkedHashMap<>();
    public static final LinkedHashMap<ReinforcingMaterial, LinkedHashMap<DyeColor, Block.Settings>> REINFORCED_SHULKER_BOX_SETTINGS_MAP = new LinkedHashMap<>();

    public static Block registerMaterialDyeColor(ReinforcingMaterial material, DyeColor color,
            Block.Settings settings) {
        if (!REINFORCED_SHULKER_BOX_SETTINGS_MAP.containsKey(material)) {
            REINFORCED_SHULKER_BOX_SETTINGS_MAP.put(material, new LinkedHashMap<>());
        }
        REINFORCED_SHULKER_BOX_SETTINGS_MAP.get(material).put(color, settings);

        String id = color == null
                ? material.getName() + "_shulker_box"
                : color.getName() + "_" + material.getName() + "_shulker_box";
        Block block = register(id, new ReinforcedShulkerBoxBlock(material, color, settings));

        if (!REINFORCED_SHULKER_BOX_MAP.containsKey(material)) {
            REINFORCED_SHULKER_BOX_MAP.put(material, new LinkedHashMap<>());
        }
        REINFORCED_SHULKER_BOX_MAP.get(material).put(color, block);

        return block;
    }

    private static Block register(String id, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(ReinforcedShulkerBoxesMod.MOD_ID, id), block);
    }
}
