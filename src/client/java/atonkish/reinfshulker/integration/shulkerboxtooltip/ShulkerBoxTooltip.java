package atonkish.reinfshulker.integration.shulkerboxtooltip;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import com.misterpemodder.shulkerboxtooltip.api.ShulkerBoxTooltipApi;
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProviderRegistry;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfcore.util.ReinforcingMaterials;
import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;
import atonkish.reinfshulker.item.ModItems;

public class ShulkerBoxTooltip implements ShulkerBoxTooltipApi {
  @Override
  public void registerProviders(PreviewProviderRegistry registry) {
    System.out.println("[reinfshulker] Registering Shulker Box Tooltip providers");

    for (ReinforcingMaterial material : ReinforcingMaterials.MAP.values()) {
      Item[] items =
          ModItems.REINFORCED_SHULKER_BOX_MAP.get(material).values().toArray(new Item[0]);

      System.out.println(
          "[reinfshulker] Registering SBT provider for "
              + material.getName()
              + " size="
              + material.getSize()
              + " items="
              + items.length);

      registry.register(
          Identifier.fromNamespaceAndPath(
              ReinforcedShulkerBoxesMod.MOD_ID, material.getName() + "_shulker_box"),
          new ReinforcedShulkerBoxPreviewProvider(material),
          items);
    }
  }
}
