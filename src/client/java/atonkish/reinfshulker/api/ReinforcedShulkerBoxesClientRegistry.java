package atonkish.reinfshulker.api;

import java.util.List;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.client.render.ModTexturedRenderLayers;

@Environment(EnvType.CLIENT)
public class ReinforcedShulkerBoxesClientRegistry {
  @Deprecated
  public static Identifier registerMaterialAtlasTexture(
      String namespace, ReinforcingMaterial material) {
    return Sheets.SHULKER_SHEET;
  }

  @Deprecated
  public static RenderType registerMaterialRenderLayer(
      String namespace, ReinforcingMaterial material) {
    return RenderTypes.entityCutout(Sheets.SHULKER_SHEET);
  }

  public static SpriteId registerMaterialDefaultSprite(
      String namespace, ReinforcingMaterial material) {
    return ModTexturedRenderLayers.registerMaterialDefaultSprite(namespace, material);
  }

  public static List<SpriteId> registerMaterialColoringSprites(
      String namespace, ReinforcingMaterial material) {
    return ModTexturedRenderLayers.registerMaterialColoringSprites(namespace, material);
  }
}
