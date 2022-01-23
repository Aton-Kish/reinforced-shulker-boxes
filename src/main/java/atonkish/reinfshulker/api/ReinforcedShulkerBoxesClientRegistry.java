package atonkish.reinfshulker.api;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.client.render.ModTexturedRenderLayers;

@Environment(EnvType.CLIENT)
public class ReinforcedShulkerBoxesClientRegistry {
    public static Identifier registerMaterialAtlasTexture(ReinforcingMaterial material) {
        return ModTexturedRenderLayers.registerMaterialAtlasTexture(material);
    }

    public static RenderLayer registerMaterialRenderLayer(ReinforcingMaterial material) {
        return ModTexturedRenderLayers.registerMaterialRenderLayer(material);
    }

    public static SpriteIdentifier registerMaterialDefaultSprite(ReinforcingMaterial material) {
        return ModTexturedRenderLayers.registerMaterialDefaultSprite(material);
    }

    public static List<SpriteIdentifier> registerMaterialColoringSprites(ReinforcingMaterial material) {
        return ModTexturedRenderLayers.registerMaterialColoringSprites(material);
    }
}
