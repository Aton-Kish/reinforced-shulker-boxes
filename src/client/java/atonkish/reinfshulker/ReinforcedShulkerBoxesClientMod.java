package atonkish.reinfshulker;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.loader.api.FabricLoader;

import atonkish.reinfcore.api.ReinforcedCoreClientModInitializer;
import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.api.ReinforcedShulkerBoxesClientModInitializer;
import atonkish.reinfshulker.api.ReinforcedShulkerBoxesClientRegistry;
import atonkish.reinfshulker.block.entity.ModBlockEntityType;
import atonkish.reinfshulker.client.render.block.entity.ReinforcedShulkerBoxBlockEntityRenderer;
import atonkish.reinfshulker.util.ReinforcingMaterialSettings;

@Environment(EnvType.CLIENT)
public class ReinforcedShulkerBoxesClientMod
    implements ClientModInitializer, ReinforcedCoreClientModInitializer {
  private static boolean initialized = false;

  @Override
  public void onInitializeClient() {
    initialize();
  }

  @Override
  public void onInitializeReinforcedCoreClient() {
    initialize();
  }

  private static void initialize() {
    if (initialized) {
      return;
    }
    initialized = true;

    initializeReinforcedCoreClient();
    initializeReinforcedShulkerBoxesClient();

    FabricLoader.getInstance()
        .getEntrypoints(
            String.format("%s-client", ReinforcedShulkerBoxesMod.MOD_ID),
            ReinforcedShulkerBoxesClientModInitializer.class)
        .forEach(
            ReinforcedShulkerBoxesClientModInitializer::onInitializeReinforcedShulkerBoxesClient);
  }

  private static void initializeReinforcedCoreClient() {
    for (ReinforcingMaterialSettings materialSettings : ReinforcingMaterialSettings.values()) {
      ReinforcingMaterial material = materialSettings.getMaterial();

      net.minecraft.client.gui.screens.MenuScreens.register(
          atonkish.reinfcore.screen.ModScreenHandlerType.REINFORCED_SHULKER_BOX_MAP.get(material),
          atonkish.reinfshulker.client.gui.screen.ReinforcedShulkerBoxScreen::new);
    }
  }

  private static void initializeReinforcedShulkerBoxesClient() {
    for (ReinforcingMaterialSettings materialSettings : ReinforcingMaterialSettings.values()) {
      ReinforcingMaterial material = materialSettings.getMaterial();

      ReinforcedShulkerBoxesClientRegistry.registerMaterialDefaultSprite(
          ReinforcedShulkerBoxesMod.MOD_ID, material);
      ReinforcedShulkerBoxesClientRegistry.registerMaterialColoringSprites(
          ReinforcedShulkerBoxesMod.MOD_ID, material);

      BlockEntityRendererRegistry.register(
          ModBlockEntityType.REINFORCED_SHULKER_BOX_MAP.get(material),
          ReinforcedShulkerBoxBlockEntityRenderer::new);
    }
  }
}
