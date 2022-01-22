package atonkish.reinfshulker.client.render;

import com.google.common.collect.ImmutableList;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfcore.util.ReinforcingMaterials;
import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;

@Environment(EnvType.CLIENT)
public class ModTexturedRenderLayers {
    public static final Identifier SHULKER_BOXES_ATLAS_TEXTURE = new Identifier("textures/atlas/shulker_boxes.png");
    public static final HashMap<ReinforcingMaterial, Identifier> REINFORCED_SHULKER_BOXES_ATLAS_TEXTURE_MAP;
    private static final HashMap<ReinforcingMaterial, RenderLayer> REINFORCED_SHULKER_BOXES_RENDER_LAYER_MAP;
    public static final HashMap<ReinforcingMaterial, SpriteIdentifier> REINFORCED_SHULKER_TEXTURE_ID_MAP;
    public static final HashMap<ReinforcingMaterial, List<SpriteIdentifier>> COLORED_REINFORCED_SHULKER_BOXES_TEXTURES_MAP;

    public static RenderLayer getReinforcedShulkerBoxes(ReinforcingMaterial material) {
        return REINFORCED_SHULKER_BOXES_RENDER_LAYER_MAP.get(material);
    }

    static {
        REINFORCED_SHULKER_BOXES_ATLAS_TEXTURE_MAP = new HashMap<>();
        REINFORCED_SHULKER_BOXES_RENDER_LAYER_MAP = new HashMap<>();
        REINFORCED_SHULKER_TEXTURE_ID_MAP = new HashMap<>();
        COLORED_REINFORCED_SHULKER_BOXES_TEXTURES_MAP = new HashMap<>();
        for (ReinforcingMaterial material : ReinforcingMaterials.MAP.values()) {
            // Atlas Texture
            Identifier atlasTexture = new Identifier(ReinforcedShulkerBoxesMod.MOD_ID,
                    "textures/atlas/" + material.getName() + "_shulker_boxes.png");
            REINFORCED_SHULKER_BOXES_ATLAS_TEXTURE_MAP.put(material, atlasTexture);

            // Render Layer
            RenderLayer renderLayer = RenderLayer.getEntityCutout(atlasTexture);
            REINFORCED_SHULKER_BOXES_RENDER_LAYER_MAP.put(material, renderLayer);

            // Sprite Identifiers
            // Non-Color
            SpriteIdentifier nonColoredTextureId = new SpriteIdentifier(
                    REINFORCED_SHULKER_BOXES_ATLAS_TEXTURE_MAP.get(material), new Identifier(
                            ReinforcedShulkerBoxesMod.MOD_ID,
                            "entity/reinforced_shulker/" + material.getName() + "/shulker"));
            REINFORCED_SHULKER_TEXTURE_ID_MAP.put(material, nonColoredTextureId);
            // Color
            List<SpriteIdentifier> coloredTextureIds = Stream.of(DyeColor.values()).map((color) -> {
                return new SpriteIdentifier(REINFORCED_SHULKER_BOXES_ATLAS_TEXTURE_MAP.get(material),
                        new Identifier(ReinforcedShulkerBoxesMod.MOD_ID,
                                "entity/reinforced_shulker/" + material.getName() + "/shulker_" + color));
            }).collect(ImmutableList.toImmutableList());
            COLORED_REINFORCED_SHULKER_BOXES_TEXTURES_MAP.put(material, coloredTextureIds);
        }
    }
}
