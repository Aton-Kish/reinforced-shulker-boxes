package atonkish.reinfshulker.stat;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

import atonkish.reinfcore.util.ReinforcingMaterial;

public class ModStats {
  public static final Map<ReinforcingMaterial, Identifier> CLEAN_REINFORCED_SHULKER_BOX_MAP =
      new LinkedHashMap<>();
  public static final Map<ReinforcingMaterial, Identifier> OPEN_REINFORCED_SHULKER_BOX_MAP =
      new LinkedHashMap<>();

  public static Identifier registerMaterialClean(String namespace, ReinforcingMaterial material) {
    if (!CLEAN_REINFORCED_SHULKER_BOX_MAP.containsKey(material)) {
      String id = "clean_" + material.getName() + "_shulker_box";
      Identifier identifier = register(namespace, id, StatFormatter.DEFAULT);
      CLEAN_REINFORCED_SHULKER_BOX_MAP.put(material, identifier);
    }

    return CLEAN_REINFORCED_SHULKER_BOX_MAP.get(material);
  }

  public static Identifier registerMaterialOpen(String namespace, ReinforcingMaterial material) {
    if (!OPEN_REINFORCED_SHULKER_BOX_MAP.containsKey(material)) {
      String id = "open_" + material.getName() + "_shulker_box";
      Identifier identifier = register(namespace, id, StatFormatter.DEFAULT);
      OPEN_REINFORCED_SHULKER_BOX_MAP.put(material, identifier);
    }

    return OPEN_REINFORCED_SHULKER_BOX_MAP.get(material);
  }

  private static Identifier register(String namespace, String id, StatFormatter formatter) {
    Identifier identifier = Identifier.fromNamespaceAndPath(namespace, id);
    Registry.register(BuiltInRegistries.CUSTOM_STAT, id, identifier);
    Stats.CUSTOM.get(identifier, formatter);
    return identifier;
  }
}
