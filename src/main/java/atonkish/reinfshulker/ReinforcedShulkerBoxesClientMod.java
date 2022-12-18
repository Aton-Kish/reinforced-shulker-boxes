package atonkish.reinfshulker;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import atonkish.reinfcore.api.ReinforcedCoreClientModInitializer;
import atonkish.reinfcore.api.ReinforcedCoreClientRegistry;
import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.api.ReinforcedShulkerBoxesClientModInitializer;
import atonkish.reinfshulker.api.ReinforcedShulkerBoxesClientRegistry;
import atonkish.reinfshulker.block.ModBlocks;
import atonkish.reinfshulker.block.entity.ModBlockEntityType;
import atonkish.reinfshulker.block.entity.ReinforcedShulkerBoxBlockEntity;
import atonkish.reinfshulker.client.render.block.entity.ReinforcedShulkerBoxBlockEntityRenderer;
import atonkish.reinfshulker.util.ReinforcingMaterialSettings;

@Environment(EnvType.CLIENT)
public class ReinforcedShulkerBoxesClientMod implements ReinforcedCoreClientModInitializer {
	@Override
	public void onInitializeReinforcedCoreClient() {
		// init Reinforced Core
		initializeReinforcedCoreClient();

		// init Reinforced Shulker Boxes
		initializeReinforcedShulkerBoxesClient();

		// entrypoint: "reinfshulkerclient"
		FabricLoader.getInstance()
				.getEntrypoints(ReinforcedShulkerBoxesMod.MOD_ID + "client",
						ReinforcedShulkerBoxesClientModInitializer.class)
				.forEach(ReinforcedShulkerBoxesClientModInitializer::onInitializeReinforcedShulkerBoxesClient);
	}

	private static void initializeReinforcedCoreClient() {
		for (ReinforcingMaterialSettings materialSettings : ReinforcingMaterialSettings.values()) {
			ReinforcingMaterial material = materialSettings.getMaterial();

			// Reinforced Storage Screen
			ReinforcedCoreClientRegistry.registerMaterialShulkerBoxScreen(material);
		}
	}

	private static void initializeReinforcedShulkerBoxesClient() {
		for (ReinforcingMaterialSettings materialSettings : ReinforcingMaterialSettings.values()) {
			ReinforcingMaterial material = materialSettings.getMaterial();

			// Textured Render Layers
			ReinforcedShulkerBoxesClientRegistry.registerMaterialDefaultSprite(ReinforcedShulkerBoxesMod.MOD_ID,
					material);
			ReinforcedShulkerBoxesClientRegistry.registerMaterialColoringSprites(ReinforcedShulkerBoxesMod.MOD_ID,
					material);

			// Block Entity Renderer
			BlockEntityRendererRegistry
					.register(ModBlockEntityType.REINFORCED_SHULKER_BOX_MAP.get(material),
							ReinforcedShulkerBoxBlockEntityRenderer::new);

			// Item Renderer
			for (Block block : ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(material).values()) {
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
