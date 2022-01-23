package atonkish.reinfshulker.client.render;

import com.google.common.collect.ImmutableList;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Stream;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;

@Environment(EnvType.CLIENT)
public class ModTexturedRenderLayers {
    public static final Identifier SHULKER_BOXES_ATLAS_TEXTURE = new Identifier("textures/atlas/shulker_boxes.png");
    public static final LinkedHashMap<ReinforcingMaterial, Identifier> REINFORCED_SHULKER_BOXES_ATLAS_TEXTURE_MAP = new LinkedHashMap<>();
    private static final LinkedHashMap<ReinforcingMaterial, RenderLayer> REINFORCED_SHULKER_BOXES_RENDER_LAYER_MAP = new LinkedHashMap<>();
    public static final LinkedHashMap<ReinforcingMaterial, SpriteIdentifier> REINFORCED_SHULKER_TEXTURE_ID_MAP = new LinkedHashMap<>();
    public static final LinkedHashMap<ReinforcingMaterial, List<SpriteIdentifier>> COLORED_REINFORCED_SHULKER_BOXES_TEXTURES_MAP = new LinkedHashMap<>();

    public static Identifier registerMaterialAtlasTexture(ReinforcingMaterial material) {
        Identifier identifier = new Identifier(ReinforcedShulkerBoxesMod.MOD_ID,
                "textures/atlas/" + material.getName() + "_shulker_boxes.png");

        REINFORCED_SHULKER_BOXES_ATLAS_TEXTURE_MAP.put(material, identifier);

        return identifier;
    }

    public static RenderLayer registerMaterialRenderLayer(ReinforcingMaterial material) {
        RenderLayer renderLayer = RenderLayer.getEntityCutout(REINFORCED_SHULKER_BOXES_ATLAS_TEXTURE_MAP.get(material));

        REINFORCED_SHULKER_BOXES_RENDER_LAYER_MAP.put(material, renderLayer);

        return renderLayer;
    }

    public static SpriteIdentifier registerMaterialDefaultSprite(ReinforcingMaterial material) {
        SpriteIdentifier identifier = new SpriteIdentifier(
                REINFORCED_SHULKER_BOXES_ATLAS_TEXTURE_MAP.get(material), new Identifier(
                        ReinforcedShulkerBoxesMod.MOD_ID,
                        "entity/reinforced_shulker/" + material.getName() + "/shulker"));

        REINFORCED_SHULKER_TEXTURE_ID_MAP.put(material, identifier);

        return identifier;
    }

    public static List<SpriteIdentifier> registerMaterialColoringSprites(ReinforcingMaterial material) {
        List<SpriteIdentifier> identifiers = Stream.of(DyeColor.values()).map((color) -> {
            return new SpriteIdentifier(REINFORCED_SHULKER_BOXES_ATLAS_TEXTURE_MAP.get(material),
                    new Identifier(ReinforcedShulkerBoxesMod.MOD_ID,
                            "entity/reinforced_shulker/" + material.getName() + "/shulker_" + color.getName()));
        }).collect(ImmutableList.toImmutableList());

        COLORED_REINFORCED_SHULKER_BOXES_TEXTURES_MAP.put(material, identifiers);

        return identifiers;
    }

    public static RenderLayer getReinforcedShulkerBoxes(ReinforcingMaterial material) {
        return REINFORCED_SHULKER_BOXES_RENDER_LAYER_MAP.get(material);
    }
}
