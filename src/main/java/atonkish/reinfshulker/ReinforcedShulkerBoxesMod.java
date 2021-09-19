package atonkish.reinfshulker;

import net.fabricmc.api.ModInitializer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import atonkish.reinfshulker.block.ModBlocks;
import atonkish.reinfshulker.block.cauldron.ModCauldronBehavior;
import atonkish.reinfshulker.block.dispenser.ModDispenserBehavior;
import atonkish.reinfshulker.block.entity.ModBlockEntityType;
import atonkish.reinfshulker.item.ModItems;
import atonkish.reinfshulker.recipe.ModRecipeSerializer;
import atonkish.reinfshulker.stat.ModStats;

public class ReinforcedShulkerBoxesMod implements ModInitializer {
	public static final String MOD_ID = "reinfshulker";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// Stats
		ModStats.init();

		// Blocks
		ModBlocks.init();
		ModBlockEntityType.init();

		// Items
		ModItems.init();

		// Recipe Serializer
		ModRecipeSerializer.init();

		// Block Entity Behaviors
		ModCauldronBehavior.init();
		ModDispenserBehavior.init();
	}
}
