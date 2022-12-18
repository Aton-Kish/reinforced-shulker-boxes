package atonkish.reinfshulker.client.render;

import com.google.common.collect.ImmutableList;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import atonkish.reinfcore.util.ReinforcingMaterial;

@Environment(EnvType.CLIENT)
public class ModTexturedRenderLayers {
    public static final Map<ReinforcingMaterial, SpriteIdentifier> REINFORCED_SHULKER_TEXTURE_ID_MAP = new LinkedHashMap<>();
    public static final Map<ReinforcingMaterial, List<SpriteIdentifier>> COLORED_REINFORCED_SHULKER_BOXES_TEXTURES_MAP = new LinkedHashMap<>();

    public static SpriteIdentifier registerMaterialDefaultSprite(String namespace, ReinforcingMaterial material) {
        if (!REINFORCED_SHULKER_TEXTURE_ID_MAP.containsKey(material)) {
            SpriteIdentifier identifier = new SpriteIdentifier(TexturedRenderLayers.SHULKER_BOXES_ATLAS_TEXTURE,
                    new Identifier(namespace, "entity/reinforced_shulker/" + material.getName() + "/shulker"));
            REINFORCED_SHULKER_TEXTURE_ID_MAP.put(material, identifier);
        }

        return REINFORCED_SHULKER_TEXTURE_ID_MAP.get(material);
    }

    public static List<SpriteIdentifier> registerMaterialColoringSprites(String namespace,
            ReinforcingMaterial material) {
        if (!COLORED_REINFORCED_SHULKER_BOXES_TEXTURES_MAP.containsKey(material)) {
            List<SpriteIdentifier> identifiers = Stream.of(DyeColor.values()).map((color) -> {
                return new SpriteIdentifier(TexturedRenderLayers.SHULKER_BOXES_ATLAS_TEXTURE,
                        new Identifier(namespace,
                                "entity/reinforced_shulker/" + material.getName() + "/shulker_" + color.getName()));
            }).collect(ImmutableList.toImmutableList());
            COLORED_REINFORCED_SHULKER_BOXES_TEXTURES_MAP.put(material, identifiers);
        }

        return COLORED_REINFORCED_SHULKER_BOXES_TEXTURES_MAP.get(material);
    }
}
