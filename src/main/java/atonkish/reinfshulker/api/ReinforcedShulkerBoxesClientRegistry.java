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
    public static Identifier registerMaterialAtlasTexture(String namespace, ReinforcingMaterial material) {
        return ModTexturedRenderLayers.registerMaterialAtlasTexture(namespace, material);
    }

    public static RenderLayer registerMaterialRenderLayer(String namespace, ReinforcingMaterial material) {
        return ModTexturedRenderLayers.registerMaterialRenderLayer(material);
    }

    public static SpriteIdentifier registerMaterialDefaultSprite(String namespace, ReinforcingMaterial material) {
        return ModTexturedRenderLayers.registerMaterialDefaultSprite(namespace, material);
    }

    public static List<SpriteIdentifier> registerMaterialColoringSprites(String namespace,
            ReinforcingMaterial material) {
        return ModTexturedRenderLayers.registerMaterialColoringSprites(namespace, material);
    }
}
