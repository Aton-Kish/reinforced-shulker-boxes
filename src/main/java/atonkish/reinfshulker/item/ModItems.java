package atonkish.reinfshulker.item;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.block.ModBlocks;

public class ModItems {
  public static final Map<ReinforcingMaterial, Map<DyeColor, Item>> REINFORCED_SHULKER_BOX_MAP =
      new LinkedHashMap<>();
  public static final Map<ReinforcingMaterial, Map<DyeColor, Item.Properties>>
      REINFORCED_SHULKER_BOX_SETTINGS_MAP = new LinkedHashMap<>();

  public static Item registerMaterialDyeColor(
      ReinforcingMaterial material, DyeColor color, Item.Properties settings) {
    if (!REINFORCED_SHULKER_BOX_SETTINGS_MAP.containsKey(material)) {
      REINFORCED_SHULKER_BOX_SETTINGS_MAP.put(material, new LinkedHashMap<>());
    }

    if (!REINFORCED_SHULKER_BOX_MAP.containsKey(material)) {
      REINFORCED_SHULKER_BOX_MAP.put(material, new LinkedHashMap<>());
    }

    if (!REINFORCED_SHULKER_BOX_SETTINGS_MAP.get(material).containsKey(color)) {
      REINFORCED_SHULKER_BOX_SETTINGS_MAP.get(material).put(color, settings);
    }

    if (!REINFORCED_SHULKER_BOX_MAP.get(material).containsKey(color)) {
      Item item =
          ModItems.register(
              ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(material).get(color),
              REINFORCED_SHULKER_BOX_SETTINGS_MAP.get(material).get(color));
      REINFORCED_SHULKER_BOX_MAP.get(material).put(color, item);
    }

    return REINFORCED_SHULKER_BOX_MAP.get(material).get(color);
  }

  public static void registerMaterialDyeColorItemGroupIcon(
      ReinforcingMaterial material, DyeColor color) {
    // The 26.2 creative tab icon is supplied when the tab is registered.
  }

  private static ResourceKey<Item> keyOf(ResourceKey<Block> blockKey) {
    return ResourceKey.create(Registries.ITEM, blockKey.identifier());
  }

  public static Item register(Block block, Item.Properties settings) {
    return register(block, BlockItem::new, settings);
  }

  private static Item register(
      Block block, BiFunction<Block, Item.Properties, Item> factory, Item.Properties settings) {
    return register(
        keyOf(block.builtInRegistryHolder().key()),
        itemSettings -> (Item) factory.apply(block, itemSettings),
        settings.useBlockDescriptionPrefix());
  }

  private static Item register(
      ResourceKey<Item> key, Function<Item.Properties, Item> factory, Item.Properties settings) {
    Item item = factory.apply(settings.setId(key));
    if (item instanceof BlockItem blockItem) {
      blockItem.registerBlocks(Item.BY_BLOCK, item);
    }

    return Registry.register(BuiltInRegistries.ITEM, key, item);
  }
}
