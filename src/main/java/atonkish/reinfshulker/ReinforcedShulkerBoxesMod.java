package atonkish.reinfshulker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfcore.ReinforcedCoreConfig;
import atonkish.reinfcore.ReinforcedCoreMod;
import atonkish.reinfcore.api.ReinforcedCoreModInitializer;
import atonkish.reinfcore.api.ReinforcedCoreRegistry;
import atonkish.reinfcore.item.ModItemGroups;
import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.api.ReinforcedShulkerBoxesModInitializer;
import atonkish.reinfshulker.api.ReinforcedShulkerBoxesRegistry;
import atonkish.reinfshulker.block.cauldron.ModCauldronBehavior;
import atonkish.reinfshulker.block.dispenser.ModDispenserBehavior;
import atonkish.reinfshulker.item.ModCreativeModeTabs;
import atonkish.reinfshulker.item.ModItems;
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

    LOGGER.info("[reinfshulker] Initializing Reinforced Core config defaults");
    initializeReinforcedCoreConfig();

    // init bundled Reinforced Core state
    LOGGER.info("[reinfshulker] Registering Reinforced Core item group");
    registerReinforcedStorageItemGroup();

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
    LOGGER.info("[reinfshulker] Registering creative mode tab entries");
    ModCreativeModeTabs.init();

    LOGGER.info("[reinfshulker] Common initialization complete");
  }

  private static void initializeReinforcedCoreConfig() {
    try {
      ReinforcedCoreMod.CONFIG = AutoConfig.getConfigHolder(ReinforcedCoreConfig.class).getConfig();
    } catch (RuntimeException exception) {
      AutoConfig.register(ReinforcedCoreConfig.class, GsonConfigSerializer::new);
      ReinforcedCoreMod.CONFIG = AutoConfig.getConfigHolder(ReinforcedCoreConfig.class).getConfig();
    }
  }

  private static void registerReinforcedStorageItemGroup() {
    Registry.register(
        BuiltInRegistries.CREATIVE_MODE_TAB,
        ModItemGroups.REINFORCED_STORAGE,
        CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .title(Component.translatable("itemGroup.reinfcore.reinforced_storage"))
            .icon(
                () -> {
                  if (ModItems.REINFORCED_SHULKER_BOX_MAP.containsKey(
                          ReinforcingMaterialSettings.NETHERITE.getMaterial())
                      && ModItems.REINFORCED_SHULKER_BOX_MAP
                              .get(ReinforcingMaterialSettings.NETHERITE.getMaterial())
                              .get(null)
                          != null) {
                    return new ItemStack(
                        ModItems.REINFORCED_SHULKER_BOX_MAP
                            .get(ReinforcingMaterialSettings.NETHERITE.getMaterial())
                            .get(null));
                  }
                  return new ItemStack(Items.SHULKER_BOX);
                })
            .build());
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
