package atonkish.reinfshulker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.world.item.DyeColor;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfcore.api.ReinforcedCoreModInitializer;
import atonkish.reinfcore.api.ReinforcedCoreRegistry;
import atonkish.reinfcore.item.ModItemGroups;
import atonkish.reinfcore.util.ReinforcingMaterial;
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
  private static boolean initialized = false;

  @Override
  public void onInitialize() {
    IS_REINFCHEST_LOADED = FabricLoader.getInstance().isModLoaded(ReinforcedChestsMod.MOD_ID);
    initialize();
  }

  @Override
  public void onInitializeReinforcedCore() {
    initialize();
  }

  private static void initialize() {
    if (initialized) {
      return;
    }
    initialized = true;

    LOGGER.info("[reinfshulker] Starting common initialization");

    // init bundled Reinforced Core state
    LOGGER.info("[reinfshulker] Registering Reinforced Core item group");
    ModItemGroups.init();

    // init Reinforced Core
    LOGGER.info("[reinfshulker] Registering Reinforced Core shulker support");
    initializeReinforcedCore();

    // init Reinforced Shulker Boxes
    LOGGER.info("[reinfshulker] Registering blocks, items, stats, and block entity types");
    initializeReinforcedShulkerBoxes();

    // entrypoint: "reinfshulker"
    FabricLoader.getInstance()
        .getEntrypoints(MOD_ID, ReinforcedShulkerBoxesModInitializer.class)
        .forEach(ReinforcedShulkerBoxesModInitializer::onInitializeReinforcedShulkerBoxes);

    // Recipe Serializer
    LOGGER.info("[reinfshulker] Registering recipe serializers");
    ModRecipeSerializer.init();

    // Block Entity Behaviors
    LOGGER.info("[reinfshulker] Registering cauldron and dispenser behavior");
    ModCauldronBehavior.init();
    ModDispenserBehavior.init();

    LOGGER.info("[reinfshulker] Common initialization complete");
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
      ReinforcedShulkerBoxesRegistry.registerMaterialDyeColorBlock(
          MOD_ID, material, (DyeColor) null, materialSettings.getBlockSettings());
      for (DyeColor color : DyeColor.values()) {
        ReinforcedShulkerBoxesRegistry.registerMaterialDyeColorBlock(
            MOD_ID, material, color, materialSettings.getColorBlockSettings(color));
      }
      ReinforcedShulkerBoxesRegistry.registerMaterialBlockEntityType(MOD_ID, material);

      // Items
      ReinforcedShulkerBoxesRegistry.registerMaterialDyeColorItem(
          MOD_ID, material, (DyeColor) null, materialSettings.getItemSettings());
      for (DyeColor color : DyeColor.values()) {
        ReinforcedShulkerBoxesRegistry.registerMaterialDyeColorItem(
            MOD_ID, material, color, materialSettings.getItemSettings());
      }
    }

    // Item Group Icon
    ReinforcedShulkerBoxesRegistry.registerMaterialDyeColorItemGroupIcon(
        MOD_ID, ReinforcingMaterialSettings.NETHERITE.getMaterial(), (DyeColor) null);
  }
}
