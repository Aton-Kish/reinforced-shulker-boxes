package atonkish.reinfshulker.client.render.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.ShulkerEntityModel;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Direction;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.block.entity.ReinforcedShulkerBoxBlockEntity;
import atonkish.reinfshulker.client.render.ModTexturedRenderLayers;

@Environment(EnvType.CLIENT)
public class ReinforcedShulkerBoxBlockEntityRenderer implements BlockEntityRenderer<ReinforcedShulkerBoxBlockEntity> {
    private final ShulkerEntityModel<?> model;

    public ReinforcedShulkerBoxBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.model = new ShulkerEntityModel<>(ctx.getLayerModelPart(EntityModelLayers.SHULKER));
    }

    public void render(ReinforcedShulkerBoxBlockEntity reinforcedShulkerBoxBlockEntity, float tickDelta,
            MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Direction direction = Direction.UP;
        if (reinforcedShulkerBoxBlockEntity.hasWorld()) {
            BlockState blockState = reinforcedShulkerBoxBlockEntity.getWorld()
                    .getBlockState(reinforcedShulkerBoxBlockEntity.getPos());
            if (blockState.getBlock() instanceof ShulkerBoxBlock) {
                direction = (Direction) blockState.get(ShulkerBoxBlock.FACING);
            }
        }

        DyeColor color = reinforcedShulkerBoxBlockEntity.getColor();
        ReinforcingMaterial material = reinforcedShulkerBoxBlockEntity.getMaterial();
        SpriteIdentifier spriteIdentifier2;
        if (color == null) {
            spriteIdentifier2 = ModTexturedRenderLayers.REINFORCED_SHULKER_TEXTURE_ID_MAP.get(material);
        } else {
            spriteIdentifier2 = ModTexturedRenderLayers.COLORED_REINFORCED_SHULKER_BOXES_TEXTURES_MAP.get(material)
                    .get(color.getId());
        }

        matrices.push();
        matrices.translate(0.5D, 0.5D, 0.5D);
        float g = 0.9995F;
        matrices.scale(g, g, g);
        matrices.multiply(direction.getRotationQuaternion());
        matrices.scale(1.0F, -1.0F, -1.0F);
        matrices.translate(0.0D, -1.0D, 0.0D);
        ModelPart modelPart = this.model.getLid();
        modelPart.setPivot(0.0F, 24.0F - reinforcedShulkerBoxBlockEntity.getAnimationProgress(tickDelta) * 0.5F * 16.0F,
                0.0F);
        modelPart.yaw = 270.0F * reinforcedShulkerBoxBlockEntity.getAnimationProgress(tickDelta) * 0.017453292F;
        VertexConsumer vertexConsumer = spriteIdentifier2.getVertexConsumer(vertexConsumers,
                RenderLayer::getEntityCutoutNoCull);
        this.model.render(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        matrices.pop();
    }
}
