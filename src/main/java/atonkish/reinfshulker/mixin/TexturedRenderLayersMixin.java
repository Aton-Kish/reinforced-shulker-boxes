package atonkish.reinfshulker.mixin;

import java.util.function.Consumer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.block.ModBlocks;
import atonkish.reinfshulker.client.render.ModTexturedRenderLayers;

@Environment(EnvType.CLIENT)
@Mixin(TexturedRenderLayers.class)
public class TexturedRenderLayersMixin {
    @Inject(at = @At("HEAD"), method = "addDefaultTextures")
    private static void addDefaultTextures(Consumer<SpriteIdentifier> adder, CallbackInfo info) {
        for (ReinforcingMaterial material : ModBlocks.REINFORCED_SHULKER_BOX_MAP.keySet()) {
            adder.accept(ModTexturedRenderLayers.REINFORCED_SHULKER_TEXTURE_ID_MAP.get(material));
            ModTexturedRenderLayers.COLORED_REINFORCED_SHULKER_BOXES_TEXTURES_MAP.get(material).forEach(adder);
        }
    }
}
