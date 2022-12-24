package atonkish.reinfshulker.block;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import atonkish.reinfcore.util.ReinforcingMaterial;

public class ModBlocks {
    public static final Map<ReinforcingMaterial, Map<DyeColor, Block>> REINFORCED_SHULKER_BOX_MAP = new LinkedHashMap<>();
    public static final Map<ReinforcingMaterial, Map<DyeColor, Block.Settings>> REINFORCED_SHULKER_BOX_SETTINGS_MAP = new LinkedHashMap<>();

    public static Block registerMaterialDyeColor(String namespace, ReinforcingMaterial material, DyeColor color,
            Block.Settings settings) {
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
            String id = color == null
                    ? material.getName() + "_shulker_box"
                    : color.getName() + "_" + material.getName() + "_shulker_box";
            Block block = ModBlocks.register(namespace, id, new ReinforcedShulkerBoxBlock(material, color, settings));
            REINFORCED_SHULKER_BOX_MAP.get(material).put(color, block);
        }

        return REINFORCED_SHULKER_BOX_MAP.get(material).get(color);
    }

    private static Block register(String namespace, String id, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(namespace, id), block);
    }
}
