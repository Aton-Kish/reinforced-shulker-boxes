package atonkish.reinfshulker;

import java.util.HashMap;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;

import atonkish.reinfcore.api.ReinforcedCoreClientModInitializer;
import atonkish.reinfcore.api.ReinforcedCoreClientRegistry;
import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.block.ModBlocks;
import atonkish.reinfshulker.block.ReinforcedShulkerBoxBlock;
import atonkish.reinfshulker.block.entity.ModBlockEntityType;
import atonkish.reinfshulker.block.entity.ReinforcedShulkerBoxBlockEntity;
import atonkish.reinfshulker.client.render.block.entity.ReinforcedShulkerBoxBlockEntityRenderer;

@Environment(EnvType.CLIENT)
public class ReinforcedShulkerBoxesClientMod implements ReinforcedCoreClientModInitializer {
	@Override
	public void onInitializeReinforcedCoreClient() {
		// init Reinforced Core
		initializeReinforcedCoreClient();

		// Block Entity Renderer
		registerBlockEntityRenderer();

		// Item Renderer
		registerBuiltinItemRenderer();
	}

	private static void initializeReinforcedCoreClient() {
		for (ReinforcingMaterial material : ReinforcedShulkerBoxesMod.MATERIALS) {
			// Reinforced Storage Screen
			ReinforcedCoreClientRegistry.registerShulkerBoxScreen(material);
		}
	}

	private static void registerBlockEntityRenderer() {
		for (BlockEntityType<ReinforcedShulkerBoxBlockEntity> blockEntityType : ModBlockEntityType.REINFORCED_SHULKER_BOX_MAP
				.values()) {
			BlockEntityRendererRegistry.register(blockEntityType, ReinforcedShulkerBoxBlockEntityRenderer::new);
		}
	}

	private static void registerBuiltinItemRenderer() {
		for (HashMap<DyeColor, Block> materialShulkerBoxMap : ModBlocks.REINFORCED_SHULKER_BOX_MAP.values()) {
			for (Block block : materialShulkerBoxMap.values()) {
				ReinforcingMaterial material = ((ReinforcedShulkerBoxBlock) block).getMaterial();
				BuiltinItemRendererRegistry.INSTANCE.register(block, (ItemStack stack, ModelTransformation.Mode mode,
						MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) -> {
					BlockEntity blockEntity = new ReinforcedShulkerBoxBlockEntity(material, BlockPos.ORIGIN,
							block.getDefaultState());
					MinecraftClient.getInstance().getBlockEntityRenderDispatcher().renderEntity(blockEntity, matrices,
							vertexConsumers, light, overlay);
				});
			}
		}
	}
}
