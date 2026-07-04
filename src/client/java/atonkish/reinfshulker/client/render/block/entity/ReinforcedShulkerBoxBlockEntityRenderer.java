package atonkish.reinfshulker.client.render.block.entity;

import java.util.function.Consumer;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.ShulkerBoxRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.phys.Vec3;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionfc;
import org.joml.Vector3fc;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.block.entity.ReinforcedShulkerBoxBlockEntity;
import atonkish.reinfshulker.client.render.ModTexturedRenderLayers;

@Environment(EnvType.CLIENT)
public class ReinforcedShulkerBoxBlockEntityRenderer
    implements BlockEntityRenderer<
        ReinforcedShulkerBoxBlockEntity,
        ReinforcedShulkerBoxBlockEntityRenderer.ReinforcedShulkerBoxRenderState> {
  private final SpriteGetter sprites;
  private final ShulkerBoxBlockModel model;

  public ReinforcedShulkerBoxBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    this.sprites = context.sprites();
    this.model =
        new ShulkerBoxBlockModel(context.entityModelSet().bakeLayer(ModelLayers.SHULKER_BOX));
  }

  @Override
  public ReinforcedShulkerBoxRenderState createRenderState() {
    return new ReinforcedShulkerBoxRenderState();
  }

  @Override
  public void extractRenderState(
      ReinforcedShulkerBoxBlockEntity shulkerBoxBlockEntity,
      ReinforcedShulkerBoxRenderState state,
      float tickProgress,
      Vec3 cameraPosition,
      ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
    BlockEntityRenderer.super.extractRenderState(
        shulkerBoxBlockEntity, state, tickProgress, cameraPosition, breakProgress);
    state.direction =
        (Direction)
            shulkerBoxBlockEntity
                .getBlockState()
                .getValueOrElse(ShulkerBoxBlock.FACING, Direction.UP);
    state.color = shulkerBoxBlockEntity.getColor();
    state.progress = shulkerBoxBlockEntity.getProgress(tickProgress);
    state.material = shulkerBoxBlockEntity.getMaterial();
  }

  @Override
  public void submit(
      ReinforcedShulkerBoxRenderState state,
      PoseStack poseStack,
      SubmitNodeCollector submitNodeCollector,
      CameraRenderState cameraRenderState) {
    DyeColor color = state.color;
    ReinforcingMaterial material = state.material;

    SpriteId spriteIdentifier =
        color == null
            ? ModTexturedRenderLayers.REINFORCED_SHULKER_TEXTURE_ID_MAP.get(material)
            : ModTexturedRenderLayers.COLORED_REINFORCED_SHULKER_BOXES_TEXTURES_MAP
                .get(material)
                .get(color.getId());

    this.submit(
        poseStack,
        submitNodeCollector,
        state.lightCoords,
        OverlayTexture.NO_OVERLAY,
        state.direction,
        state.progress,
        state.breakProgress,
        spriteIdentifier,
        0);
  }

  public void submit(
      PoseStack poseStack,
      SubmitNodeCollector submitNodeCollector,
      int lightCoords,
      int overlayCoords,
      Direction direction,
      float progress,
      ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress,
      SpriteId sprite,
      int outlineColor) {
    poseStack.pushPose();
    this.prepareModel(poseStack, direction, progress);

    submitNodeCollector.submitModel(
        this.model,
        progress,
        poseStack,
        lightCoords,
        overlayCoords,
        -1,
        sprite,
        this.sprites,
        outlineColor,
        breakProgress);

    poseStack.popPose();
  }

  private void prepareModel(PoseStack poseStack, Direction direction, float progress) {
    poseStack.translate(0.5F, 0.5F, 0.5F);
    float scale = 0.9995F;
    poseStack.scale(scale, scale, scale);
    poseStack.mulPose((Quaternionfc) direction.getRotation());
    poseStack.scale(1.0F, -1.0F, -1.0F);
    poseStack.translate(0.0F, -1.0F, 0.0F);
    this.model.setupAnim(progress);
  }

  public void getExtents(Direction direction, float progress, Consumer<Vector3fc> consumer) {
    PoseStack poseStack = new PoseStack();
    this.prepareModel(poseStack, direction, progress);
    this.model.root().getExtentsForGui(poseStack, consumer);
  }

  @Environment(EnvType.CLIENT)
  static class ShulkerBoxBlockModel extends Model {
    private final ModelPart lid;

    public ShulkerBoxBlockModel(ModelPart root) {
      super(root, id -> RenderTypes.entityCutout((Identifier) id));
      this.lid = root.getChild("lid");
    }

    public void setupAnim(Float progress) {
      super.setupAnim(progress);
      this.lid.setPos(0.0F, 24.0F - progress * 0.5F * 16.0F, 0.0F);
      this.lid.yRot = 270.0F * progress * (float) (Math.PI / 180.0);
    }
  }

  @Environment(EnvType.CLIENT)
  public static class ReinforcedShulkerBoxRenderState extends ShulkerBoxRenderState {
    ReinforcingMaterial material;
  }
}
