package atonkish.reinfshulker.item;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;

public class ModCreativeModeTabs {
  private static boolean initialized = false;

  public static void init() {
    if (initialized) {
      return;
    }
    initialized = true;

    Registry.register(
        BuiltInRegistries.CREATIVE_MODE_TAB,
        ResourceKey.create(
            Registries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(
                ReinforcedShulkerBoxesMod.MOD_ID, "reinforced_shulker_boxes")),
        CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .title(Component.translatable("itemGroup.reinfshulker.reinforced_shulker_boxes"))
            .icon(ModCreativeModeTabs::icon)
            .displayItems(
                (parameters, output) ->
                    ModItems.REINFORCED_SHULKER_BOX_MAP.values().stream()
                        .flatMap(map -> map.values().stream())
                        .forEach(output::accept))
            .build());
  }

  private static ItemStack icon() {
    return ModItems.REINFORCED_SHULKER_BOX_MAP.values().stream()
        .flatMap(map -> map.values().stream())
        .findFirst()
        .map(ItemStack::new)
        .orElse(ItemStack.EMPTY);
  }
}
