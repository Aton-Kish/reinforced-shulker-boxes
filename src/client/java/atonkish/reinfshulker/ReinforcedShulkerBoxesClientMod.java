package atonkish.reinfshulker;

import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

import atonkish.reinfcore.api.ReinforcedCoreClientModInitializer;
import atonkish.reinfcore.api.ReinforcedCoreClientRegistry;
import atonkish.reinfcore.util.ReinforcingMaterial;

import atonkish.reinfshulker.api.ReinforcedShulkerBoxesClientModInitializer;
import atonkish.reinfshulker.api.ReinforcedShulkerBoxesClientRegistry;
import atonkish.reinfshulker.block.entity.ModBlockEntityType;
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

		// entrypoint: "reinfshulker-client"
		FabricLoader.getInstance()
				.getEntrypoints(String.format("%s-client", ReinforcedShulkerBoxesMod.MOD_ID),
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
			BlockEntityRendererFactories
					.register(ModBlockEntityType.REINFORCED_SHULKER_BOX_MAP.get(material),
							ReinforcedShulkerBoxBlockEntityRenderer::new);
		}
	}
}
