package atonkish.reinfshulker.client.render.block.entity;

import java.util.Set;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.block.entity.state.ShulkerBoxBlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteHolder;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import atonkish.reinfcore.util.ReinforcingMaterial;

import atonkish.reinfshulker.block.ReinforcedShulkerBoxBlock;
import atonkish.reinfshulker.block.entity.ReinforcedShulkerBoxBlockEntity;
import atonkish.reinfshulker.client.render.ModTexturedRenderLayers;

@Environment(EnvType.CLIENT)
public class ReinforcedShulkerBoxBlockEntityRenderer
        implements BlockEntityRenderer<ReinforcedShulkerBoxBlockEntity, ShulkerBoxBlockEntityRenderState> {
    private final SpriteHolder spriteHolder;
    private final ShulkerBoxBlockModel model;

    public ReinforcedShulkerBoxBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.spriteHolder = context.spriteHolder();
        this.model = new ShulkerBoxBlockModel(context.getLayerModelPart(EntityModelLayers.SHULKER_BOX));
    }

    public ShulkerBoxBlockEntityRenderState createRenderState() {
        return new ShulkerBoxBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(ReinforcedShulkerBoxBlockEntity shulkerBoxBlockEntity,
            ShulkerBoxBlockEntityRenderState shulkerBoxBlockEntityRenderState, float tickProgress, Vec3d vec3d,
            @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlayCommand) {
        BlockEntityRenderState.updateBlockEntityRenderState(shulkerBoxBlockEntity, shulkerBoxBlockEntityRenderState,
                crumblingOverlayCommand);
        shulkerBoxBlockEntityRenderState.facing = (Direction) shulkerBoxBlockEntity.getCachedState()
                .get(ShulkerBoxBlock.FACING, Direction.UP);
        shulkerBoxBlockEntityRenderState.dyeColor = shulkerBoxBlockEntity.getColor();
        shulkerBoxBlockEntityRenderState.animationProgress = shulkerBoxBlockEntity.getAnimationProgress(tickProgress);
    }

    @Override
    public void render(ShulkerBoxBlockEntityRenderState shulkerBoxBlockEntityRenderState, MatrixStack matrixStack,
            OrderedRenderCommandQueue orderedRenderCommandQueue, CameraRenderState cameraRenderState) {
        DyeColor color = shulkerBoxBlockEntityRenderState.dyeColor;
        ReinforcingMaterial material = ((ReinforcedShulkerBoxBlock) shulkerBoxBlockEntityRenderState.blockState
                .getBlock()).getMaterial();
        SpriteIdentifier spriteIdentifier = color == null
                ? ModTexturedRenderLayers.REINFORCED_SHULKER_TEXTURE_ID_MAP.get(material)
                : ModTexturedRenderLayers.COLORED_REINFORCED_SHULKER_BOXES_TEXTURES_MAP.get(material)
                        .get(color.getIndex());

        this.render(matrixStack, orderedRenderCommandQueue, shulkerBoxBlockEntityRenderState.lightmapCoordinates,
                OverlayTexture.DEFAULT_UV, shulkerBoxBlockEntityRenderState.facing,
                shulkerBoxBlockEntityRenderState.animationProgress, shulkerBoxBlockEntityRenderState.crumblingOverlay,
                spriteIdentifier, 0);
    }

    public void render(MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, int light,
            int overlay, Direction facing,
            float openness, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlayCommand,
            SpriteIdentifier spriteIdentifier, int i) {
        matrixStack.push();
        this.setTransforms(matrixStack, facing, openness);
        RenderLayer renderLayer = spriteIdentifier.getRenderLayer(this.model::getLayer);
        Sprite sprite = this.spriteHolder.getSprite(spriteIdentifier);
        orderedRenderCommandQueue.submitModel(this.model, openness, matrixStack, renderLayer,
                light, overlay, -1, sprite, i, crumblingOverlayCommand);
        matrixStack.pop();
    }

    private void setTransforms(MatrixStack matrixStack, Direction facing, float openness) {
        matrixStack.translate(0.5F, 0.5F, 0.5F);
        float f = 0.9995F;
        matrixStack.scale(f, f, f);
        matrixStack.multiply(facing.getRotationQuaternion());
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        matrixStack.translate(0.0F, -1.0F, 0.0F);
        this.model.setAngles(openness);
    }

    public void collectVertices(Direction facing, float openness, Set<Vector3f> vertices) {
        MatrixStack matrixStack = new MatrixStack();
        this.setTransforms(matrixStack, facing, openness);
        this.model.getRootPart().collectVertices(matrixStack, vertices);
    }

    @Environment(EnvType.CLIENT)
    static class ShulkerBoxBlockModel extends Model<Float> {
        private final ModelPart lid;

        public ShulkerBoxBlockModel(ModelPart root) {
            super(root, RenderLayer::getEntityCutoutNoCull);
            this.lid = root.getChild("lid");
        }

        public void setAngles(Float openness) {
            super.setAngles(openness);
            this.lid.setOrigin(0.0F, 24.0F - openness * 0.5F * 16.0F, 0.0F);
            this.lid.yaw = 270.0F * openness * (float) (Math.PI / 180.0);
        }
    }
}
