package atonkish.reinfshulker.block;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import atonkish.reinfcore.util.ReinforcingMaterial;

public class ModBlocks {
  public static final Map<ReinforcingMaterial, Map<DyeColor, Block>> REINFORCED_SHULKER_BOX_MAP =
      new LinkedHashMap<>();
  public static final Map<ReinforcingMaterial, Map<DyeColor, Block.Settings>>
      REINFORCED_SHULKER_BOX_SETTINGS_MAP = new LinkedHashMap<>();

  public static Block registerMaterialDyeColor(
      String namespace, ReinforcingMaterial material, DyeColor color, Block.Settings settings) {
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
      String id =
          color == null
              ? material.getName() + "_shulker_box"
              : color.getId() + "_" + material.getName() + "_shulker_box";
      Block block =
          ModBlocks.register(
              Identifier.of(namespace, id),
              (abstractBlockSettings) ->
                  new ReinforcedShulkerBoxBlock(material, color, abstractBlockSettings),
              REINFORCED_SHULKER_BOX_SETTINGS_MAP.get(material).get(color));
      REINFORCED_SHULKER_BOX_MAP.get(material).put(color, block);
    }

    return REINFORCED_SHULKER_BOX_MAP.get(material).get(color);
  }

  private static Block register(
      RegistryKey<Block> key,
      Function<AbstractBlock.Settings, Block> factory,
      AbstractBlock.Settings settings) {
    Block block = factory.apply(settings.registryKey(key));
    return Registry.register(Registries.BLOCK, key, block);
  }

  private static Block register(
      Identifier id,
      Function<AbstractBlock.Settings, Block> factory,
      AbstractBlock.Settings settings) {
    return register(keyOf(id), factory, settings);
  }

  private static RegistryKey<Block> keyOf(Identifier id) {
    return RegistryKey.of(RegistryKeys.BLOCK, id);
  }
}
