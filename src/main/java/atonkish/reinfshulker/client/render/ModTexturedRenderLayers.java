package atonkish.reinfshulker.client.render;

import com.google.common.collect.ImmutableList;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import atonkish.reinfcore.util.ReinforcingMaterial;

@Environment(EnvType.CLIENT)
public class ModTexturedRenderLayers {
    public static final Identifier SHULKER_BOXES_ATLAS_TEXTURE = new Identifier("textures/atlas/shulker_boxes.png");
    public static final Map<ReinforcingMaterial, Identifier> REINFORCED_SHULKER_BOXES_ATLAS_TEXTURE_MAP = new LinkedHashMap<>();
    private static final Map<ReinforcingMaterial, RenderLayer> REINFORCED_SHULKER_BOXES_RENDER_LAYER_MAP = new LinkedHashMap<>();
    public static final Map<ReinforcingMaterial, SpriteIdentifier> REINFORCED_SHULKER_TEXTURE_ID_MAP = new LinkedHashMap<>();
    public static final Map<ReinforcingMaterial, List<SpriteIdentifier>> COLORED_REINFORCED_SHULKER_BOXES_TEXTURES_MAP = new LinkedHashMap<>();

    public static Identifier registerMaterialAtlasTexture(String namespace, ReinforcingMaterial material) {
        if (!REINFORCED_SHULKER_BOXES_ATLAS_TEXTURE_MAP.containsKey(material)) {
            Identifier identifier = new Identifier(namespace,
                    "textures/atlas/" + material.getName() + "_shulker_boxes.png");

            ClientSpriteRegistryCallback.event(identifier).register((atlasTexture, registry) -> {
                String nonColorPath = "entity/reinforced_shulker/" + material.getName() + "/shulker";
                registry.register(new Identifier(namespace, nonColorPath));
                for (DyeColor color : DyeColor.values()) {
                    String colorPath = nonColorPath + "_" + color.getName();
                    registry.register(new Identifier(namespace, colorPath));
                }
            });

            REINFORCED_SHULKER_BOXES_ATLAS_TEXTURE_MAP.put(material, identifier);
        }

        return REINFORCED_SHULKER_BOXES_ATLAS_TEXTURE_MAP.get(material);
    }

    public static RenderLayer registerMaterialRenderLayer(ReinforcingMaterial material) {
        if (!REINFORCED_SHULKER_BOXES_RENDER_LAYER_MAP.containsKey(material)) {
            RenderLayer renderLayer = RenderLayer
                    .getEntityCutout(REINFORCED_SHULKER_BOXES_ATLAS_TEXTURE_MAP.get(material));
            REINFORCED_SHULKER_BOXES_RENDER_LAYER_MAP.put(material, renderLayer);
        }

        return REINFORCED_SHULKER_BOXES_RENDER_LAYER_MAP.get(material);
    }

    public static SpriteIdentifier registerMaterialDefaultSprite(String namespace, ReinforcingMaterial material) {
        if (!REINFORCED_SHULKER_TEXTURE_ID_MAP.containsKey(material)) {
            SpriteIdentifier identifier = new SpriteIdentifier(
                    REINFORCED_SHULKER_BOXES_ATLAS_TEXTURE_MAP.get(material),
                    new Identifier(namespace, "entity/reinforced_shulker/" + material.getName() + "/shulker"));
            REINFORCED_SHULKER_TEXTURE_ID_MAP.put(material, identifier);
        }

        return REINFORCED_SHULKER_TEXTURE_ID_MAP.get(material);
    }

    public static List<SpriteIdentifier> registerMaterialColoringSprites(String namespace,
            ReinforcingMaterial material) {
        if (!COLORED_REINFORCED_SHULKER_BOXES_TEXTURES_MAP.containsKey(material)) {
            List<SpriteIdentifier> identifiers = Stream.of(DyeColor.values()).map((color) -> {
                return new SpriteIdentifier(REINFORCED_SHULKER_BOXES_ATLAS_TEXTURE_MAP.get(material),
                        new Identifier(namespace,
                                "entity/reinforced_shulker/" + material.getName() + "/shulker_" + color.getName()));
            }).collect(ImmutableList.toImmutableList());
            COLORED_REINFORCED_SHULKER_BOXES_TEXTURES_MAP.put(material, identifiers);
        }

        return COLORED_REINFORCED_SHULKER_BOXES_TEXTURES_MAP.get(material);
    }

    public static RenderLayer getReinforcedShulkerBoxes(ReinforcingMaterial material) {
        return REINFORCED_SHULKER_BOXES_RENDER_LAYER_MAP.get(material);
    }
}
