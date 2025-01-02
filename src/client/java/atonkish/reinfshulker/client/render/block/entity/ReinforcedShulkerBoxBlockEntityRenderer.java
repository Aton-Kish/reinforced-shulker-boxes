package atonkish.reinfshulker.client.render.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.Objects;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Direction;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.block.entity.ReinforcedShulkerBoxBlockEntity;
import atonkish.reinfshulker.client.render.ModTexturedRenderLayers;

@Environment(EnvType.CLIENT)
public class ReinforcedShulkerBoxBlockEntityRenderer implements BlockEntityRenderer<ReinforcedShulkerBoxBlockEntity> {
    private final ShulkerBoxBlockModel model;

    public ReinforcedShulkerBoxBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.model = new ShulkerBoxBlockModel(ctx.getLayerModelPart(EntityModelLayers.SHULKER));
    }

    @Override
    public void render(ReinforcedShulkerBoxBlockEntity shulkerBoxBlockEntity, float tickDelta, MatrixStack matrixStack,
            VertexConsumerProvider vertexConsumerProvider, int lignt, int overlay) {
        Direction direction = Direction.UP;
        if (shulkerBoxBlockEntity.hasWorld()) {
            BlockState blockState = shulkerBoxBlockEntity.getWorld().getBlockState(shulkerBoxBlockEntity.getPos());
            if (blockState.getBlock() instanceof ShulkerBoxBlock) {
                direction = (Direction) blockState.get(ShulkerBoxBlock.FACING);
            }
        }

        DyeColor color = shulkerBoxBlockEntity.getColor();
        ReinforcingMaterial material = shulkerBoxBlockEntity.getMaterial();
        SpriteIdentifier spriteIdentifier;
        if (color == null) {
            spriteIdentifier = ModTexturedRenderLayers.REINFORCED_SHULKER_TEXTURE_ID_MAP.get(material);
        } else {
            spriteIdentifier = ModTexturedRenderLayers.COLORED_REINFORCED_SHULKER_BOXES_TEXTURES_MAP.get(material)
                    .get(color.getId());
        }

        matrixStack.push();
        matrixStack.translate(0.5F, 0.5F, 0.5F);
        float g = 0.9995F;
        matrixStack.scale(g, g, g);
        matrixStack.multiply(direction.getRotationQuaternion());
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        matrixStack.translate(0.0F, -1.0F, 0.0F);
        this.model.animateLid(shulkerBoxBlockEntity, tickDelta);
        Objects.requireNonNull(this.model);
        VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumerProvider,
                this.model::getLayer);
        this.model.render(matrixStack, vertexConsumer, lignt, overlay);
        matrixStack.pop();
    }

    // NOTE: it was re-implemented because an error occurs at the start of the game
    // when attempting to access ShulkerBoxBlockModel using accesswidener.
    class ShulkerBoxBlockModel extends Model {
        private final ModelPart lid;

        public ShulkerBoxBlockModel(ModelPart root) {
            super(root, RenderLayer::getEntityCutoutNoCull);
            this.lid = root.getChild("lid");
        }

        public void animateLid(ShulkerBoxBlockEntity blockEntity, float delta) {
            this.lid.setPivot(0.0F, 24.0F - blockEntity.getAnimationProgress(delta) * 0.5F * 16.0F, 0.0F);
            this.lid.yaw = 270.0F * blockEntity.getAnimationProgress(delta) * 0.017453292F;
        }
    }
}
