package atonkish.reinfshulker;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.util.DyeColor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import atonkish.reinfcore.api.ReinforcedCoreModInitializer;
import atonkish.reinfcore.api.ReinforcedCoreRegistry;
import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfshulker.api.ReinforcedShulkerBoxesModInitializer;
import atonkish.reinfshulker.api.ReinforcedShulkerBoxesRegistry;
import atonkish.reinfshulker.block.cauldron.ModCauldronBehavior;
import atonkish.reinfshulker.block.dispenser.ModDispenserBehavior;
import atonkish.reinfshulker.recipe.ModRecipeSerializer;
import atonkish.reinfshulker.util.ReinforcingMaterialSettings;

public class ReinforcedShulkerBoxesMod implements ModInitializer, ReinforcedCoreModInitializer {
	public static final String MOD_ID = "reinfshulker";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static boolean IS_REINFCHEST_LOADED = false;

	@Override
	public void onInitialize() {
		IS_REINFCHEST_LOADED = FabricLoader.getInstance().isModLoaded(ReinforcedChestsMod.MOD_ID);
	}

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
			ReinforcedShulkerBoxesRegistry.registerMaterialCleanStat(MOD_ID, material);
			ReinforcedShulkerBoxesRegistry.registerMaterialOpenStat(MOD_ID, material);

			// Blocks
			ReinforcedShulkerBoxesRegistry.registerMaterialDyeColorBlock(MOD_ID, material, (DyeColor) null,
					materialSettings.getBlockSettings());
			for (DyeColor color : DyeColor.values()) {
				ReinforcedShulkerBoxesRegistry.registerMaterialDyeColorBlock(MOD_ID, material, color,
						materialSettings.getColorBlockSettings(color));
			}
			ReinforcedShulkerBoxesRegistry.registerMaterialBlockEntityType(MOD_ID, material);

			// Items
			ReinforcedShulkerBoxesRegistry.registerMaterialDyeColorItem(MOD_ID, material, (DyeColor) null,
					materialSettings.getItemSettings());
			for (DyeColor color : DyeColor.values()) {
				ReinforcedShulkerBoxesRegistry.registerMaterialDyeColorItem(MOD_ID, material, color,
						materialSettings.getItemSettings());
			}
		}

		// Item Group Icon
		ReinforcedShulkerBoxesRegistry.registerMaterialDyeColorItemGroupIcon(MOD_ID,
				ReinforcingMaterialSettings.NETHERITE.getMaterial(), (DyeColor) null);
	}
}
