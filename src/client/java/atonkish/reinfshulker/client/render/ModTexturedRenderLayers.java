package atonkish.reinfshulker.client.render;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import com.google.common.collect.ImmutableList;

import atonkish.reinfcore.util.ReinforcingMaterial;

@Environment(EnvType.CLIENT)
public class ModTexturedRenderLayers {
  public static final Map<ReinforcingMaterial, SpriteId> REINFORCED_SHULKER_TEXTURE_ID_MAP =
      new LinkedHashMap<>();
  public static final Map<ReinforcingMaterial, List<SpriteId>>
      COLORED_REINFORCED_SHULKER_BOXES_TEXTURES_MAP = new LinkedHashMap<>();

  public static SpriteId registerMaterialDefaultSprite(
      String namespace, ReinforcingMaterial material) {
    if (!REINFORCED_SHULKER_TEXTURE_ID_MAP.containsKey(material)) {
      SpriteId spriteId =
          new SpriteId(
              Sheets.SHULKER_SHEET,
              Identifier.fromNamespaceAndPath(
                  "minecraft", String.format("entity/shulker/%s/shulker", material.getName())));
      REINFORCED_SHULKER_TEXTURE_ID_MAP.put(material, spriteId);
    }

    return REINFORCED_SHULKER_TEXTURE_ID_MAP.get(material);
  }

  public static List<SpriteId> registerMaterialColoringSprites(
      String namespace, ReinforcingMaterial material) {
    if (!COLORED_REINFORCED_SHULKER_BOXES_TEXTURES_MAP.containsKey(material)) {
      List<SpriteId> spriteIds =
          Stream.of(DyeColor.values())
              .map(
                  color ->
                      new SpriteId(
                          Sheets.SHULKER_SHEET,
                          Identifier.fromNamespaceAndPath(
                              "minecraft",
                              String.format(
                                  "entity/shulker/%s/shulker_%s",
                                  material.getName(), color.getName()))))
              .collect(ImmutableList.toImmutableList());
      COLORED_REINFORCED_SHULKER_BOXES_TEXTURES_MAP.put(material, spriteIds);
    }

    return COLORED_REINFORCED_SHULKER_BOXES_TEXTURES_MAP.get(material);
  }
}
