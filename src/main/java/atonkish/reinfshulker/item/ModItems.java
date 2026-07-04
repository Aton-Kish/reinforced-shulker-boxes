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
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

import atonkish.reinfcore.item.ModItemGroup;
import atonkish.reinfcore.item.ModItemGroups;
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
      ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COLORED_BLOCKS)
          .register(content -> content.accept(item));
      ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS)
          .register(content -> content.accept(item));
      ItemGroupEvents.modifyEntriesEvent(ModItemGroups.REINFORCED_STORAGE)
          .register(content -> content.accept(item));
      REINFORCED_SHULKER_BOX_MAP.get(material).put(color, item);
    }

    return REINFORCED_SHULKER_BOX_MAP.get(material).get(color);
  }

  public static void registerMaterialDyeColorItemGroupIcon(
      ReinforcingMaterial material, DyeColor color) {
    Item item = REINFORCED_SHULKER_BOX_MAP.get(material).get(color);
    ModItemGroup.setIcon(
        BuiltInRegistries.CREATIVE_MODE_TAB.getValue(ModItemGroups.REINFORCED_STORAGE), item);
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
