package atonkish.reinfshulker.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface ReinforcedShulkerBoxesClientModInitializer {
    void onInitializeReinforcedShulkerBoxesClient();
}
