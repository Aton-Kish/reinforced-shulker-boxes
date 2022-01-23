package atonkish.reinfshulker;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Items;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import atonkish.reinfcore.api.ReinforcedCoreModInitializer;
import atonkish.reinfcore.api.ReinforcedCoreRegistry;
import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.api.ReinforcedShulkerBoxesModInitializer;
import atonkish.reinfshulker.block.cauldron.ModCauldronBehavior;
import atonkish.reinfshulker.block.dispenser.ModDispenserBehavior;
import atonkish.reinfshulker.recipe.ModRecipeSerializer;

public class ReinforcedShulkerBoxesMod implements ReinforcedCoreModInitializer {
	public static final String MOD_ID = "reinfshulker";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static final ReinforcingMaterial[] MATERIALS = new ReinforcingMaterial[] {
			new ReinforcingMaterial("copper", 45, Items.COPPER_INGOT),
			new ReinforcingMaterial("iron", 54, Items.IRON_INGOT),
			new ReinforcingMaterial("gold", 81, Items.GOLD_INGOT),
			new ReinforcingMaterial("diamond", 108, Items.DIAMOND),
			new ReinforcingMaterial("netherite", 108, Items.NETHERITE_INGOT),
	};

	@Override
	public void onInitializeReinforcedCore() {
		// init Reinforced Core
		initializeReinforcedCore();

		// Recipe Serializer
		ModRecipeSerializer.init();

		// Block Entity Behaviors
		ModCauldronBehavior.init();
		ModDispenserBehavior.init();

		// entrypoint: "reinfshulker"
		FabricLoader.getInstance()
				.getEntrypoints(MOD_ID, ReinforcedShulkerBoxesModInitializer.class)
				.forEach(ReinforcedShulkerBoxesModInitializer::onInitializeReinforcedShulkerBoxes);
	}

	private static void initializeReinforcedCore() {
		for (ReinforcingMaterial material : MATERIALS) {
			// Reinforcing Material
			ReinforcedCoreRegistry.registerReinforcingMaterial(material);

			// Reinforced Storage Screen Model
			ReinforcedCoreRegistry.registerSingleBlockScreenModel(material);

			// Reinforced Storage Screen Handler
			ReinforcedCoreRegistry.registerShulkerBoxScreenHandler(material);
		}
	}
}
