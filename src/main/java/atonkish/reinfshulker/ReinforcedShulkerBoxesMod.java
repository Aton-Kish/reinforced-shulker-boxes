package atonkish.reinfshulker;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.DyeColor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import atonkish.reinfcore.api.ReinforcedCoreModInitializer;
import atonkish.reinfcore.api.ReinforcedCoreRegistry;
import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.api.ReinforcedShulkerBoxesModInitializer;
import atonkish.reinfshulker.api.ReinforcedShulkerBoxesRegistry;
import atonkish.reinfshulker.block.cauldron.ModCauldronBehavior;
import atonkish.reinfshulker.block.dispenser.ModDispenserBehavior;
import atonkish.reinfshulker.recipe.ModRecipeSerializer;
import atonkish.reinfshulker.util.ReinforcingMaterialSettings;

public class ReinforcedShulkerBoxesMod implements ReinforcedCoreModInitializer {
	public static final String MOD_ID = "reinfshulker";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	@Override
	public void onInitializeReinforcedCore() {
		// init Reinforced Core
		initializeReinforcedCore();

		// init Reinforced Shulker Boxes
		initializeReinforcedShulkerBoxes();

		// entrypoint: "reinfshulker"
		FabricLoader.getInstance()
				.getEntrypoints(MOD_ID, ReinforcedShulkerBoxesModInitializer.class)
				.forEach(ReinforcedShulkerBoxesModInitializer::onInitializeReinforcedShulkerBoxes);

		// Recipe Serializer
		ModRecipeSerializer.init();

		// Block Entity Behaviors
		ModCauldronBehavior.init();
		ModDispenserBehavior.init();
	}

	private static void initializeReinforcedCore() {
		for (ReinforcingMaterialSettings materialSettings : ReinforcingMaterialSettings.values()) {
			ReinforcingMaterial material = materialSettings.getMaterial();

			// Reinforced Storage Screen Model
			ReinforcedCoreRegistry.registerMaterialSingleBlockScreenModel(material);

			// Reinforced Storage Screen Handler
			ReinforcedCoreRegistry.registerMaterialShulkerBoxScreenHandler(material);
		}
	}

	private static void initializeReinforcedShulkerBoxes() {
		for (ReinforcingMaterialSettings materialSettings : ReinforcingMaterialSettings.values()) {
			ReinforcingMaterial material = materialSettings.getMaterial();

			// Stats
			ReinforcedShulkerBoxesRegistry.registerMaterialCleanStat(material);
			ReinforcedShulkerBoxesRegistry.registerMaterialOpenStat(material);

			// Blocks
			ReinforcedShulkerBoxesRegistry.registerMaterialDyeColorBlock(material, (DyeColor) null,
					materialSettings.getBlockSettings());
			for (DyeColor color : DyeColor.values()) {
				ReinforcedShulkerBoxesRegistry.registerMaterialDyeColorBlock(material, color,
						materialSettings.getBlockSettings());
			}
			ReinforcedShulkerBoxesRegistry.registerMaterialBlockEntityType(material);

			// Items
			ReinforcedShulkerBoxesRegistry.registerMaterialDyeColorItem(material, (DyeColor) null,
					materialSettings.getItemSettings());
			for (DyeColor color : DyeColor.values()) {
				ReinforcedShulkerBoxesRegistry.registerMaterialDyeColorItem(material, color,
						materialSettings.getItemSettings());
			}
		}

		// Item Group Icon
		ReinforcedShulkerBoxesRegistry.registerMaterialDyeColorItemGroupIcon(
				ReinforcingMaterialSettings.NETHERITE.getMaterial(), (DyeColor) null);
	}
}
