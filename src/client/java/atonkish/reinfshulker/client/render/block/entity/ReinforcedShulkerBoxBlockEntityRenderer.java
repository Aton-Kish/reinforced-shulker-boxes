package atonkish.reinfshulker.client.render.block.entity;

import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import atonkish.reinfcore.util.ReinforcingMaterial;

import atonkish.reinfshulker.block.entity.ReinforcedShulkerBoxBlockEntity;
import atonkish.reinfshulker.client.render.ModTexturedRenderLayers;

@Environment(EnvType.CLIENT)
public class ReinforcedShulkerBoxBlockEntityRenderer implements BlockEntityRenderer<ReinforcedShulkerBoxBlockEntity> {
    private final ShulkerBoxBlockModel model;

    public ReinforcedShulkerBoxBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this(ctx.getLoadedEntityModels());
    }

    public ReinforcedShulkerBoxBlockEntityRenderer(LoadedEntityModels models) {
        this.model = new ShulkerBoxBlockModel(models.getModelPart(EntityModelLayers.SHULKER_BOX));
    }

    @Override
    public void render(ReinforcedShulkerBoxBlockEntity shulkerBoxBlockEntity, float tickDelta, MatrixStack matrixStack,
            VertexConsumerProvider vertexConsumerProvider, int light, int overlay, Vec3d vec3d) {
        Direction direction = (Direction) shulkerBoxBlockEntity.getCachedState().get(ShulkerBoxBlock.FACING,
                Direction.UP);
        DyeColor color = shulkerBoxBlockEntity.getColor();
        ReinforcingMaterial material = shulkerBoxBlockEntity.getMaterial();
        SpriteIdentifier spriteIdentifier;
        if (color == null) {
            spriteIdentifier = ModTexturedRenderLayers.REINFORCED_SHULKER_TEXTURE_ID_MAP.get(material);
        } else {
            spriteIdentifier = ModTexturedRenderLayers.COLORED_REINFORCED_SHULKER_BOXES_TEXTURES_MAP.get(material)
                    .get(color.getIndex());
        }

        float openness = shulkerBoxBlockEntity.getAnimationProgress(tickDelta);
        this.render(matrixStack, vertexConsumerProvider, light, overlay, direction, openness, spriteIdentifier);
    }

    public void render(
            MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay,
            Direction facing, float openness, SpriteIdentifier textureId) {
        matrixStack.push();
        matrixStack.translate(0.5F, 0.5F, 0.5F);
        float f = 0.9995F;
        matrixStack.scale(f, f, f);
        matrixStack.multiply(facing.getRotationQuaternion());
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        matrixStack.translate(0.0F, -1.0F, 0.0F);
        this.model.animateLid(openness);
        VertexConsumer vertexConsumer = textureId.getVertexConsumer(vertexConsumerProvider, this.model::getLayer);
        this.model.render(matrixStack, vertexConsumer, light, overlay);
        matrixStack.pop();
    }

    @Environment(EnvType.CLIENT)
    static class ShulkerBoxBlockModel extends Model {
        private final ModelPart lid;

        public ShulkerBoxBlockModel(ModelPart root) {
            super(root, RenderLayer::getEntityCutoutNoCull);
            this.lid = root.getChild("lid");
        }

        public void animateLid(float openness) {
            this.lid.setOrigin(0.0F, 24.0F - openness * 0.5F * 16.0F, 0.0F);
            this.lid.yaw = 270.0F * openness * (float) (Math.PI / 180.0);
        }
    }
}
