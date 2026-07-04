package atonkish.reinfshulker.util;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import atonkish.reinfcore.api.ReinforcedCoreRegistry;
import atonkish.reinfcore.util.ReinforcingMaterial;

public enum ReinforcingMaterialSettings {
  COPPER(
      ReinforcedCoreRegistry.registerReinforcingMaterial("copper", 45, Items.COPPER_INGOT),
      BlockBehaviour.Properties.of().strength(2.0F, 6.0F).sound(SoundType.COPPER),
      new Item.Properties()),
  IRON(
      ReinforcedCoreRegistry.registerReinforcingMaterial("iron", 54, Items.IRON_INGOT),
      BlockBehaviour.Properties.of()
          .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
          .strength(2.0F, 6.0F)
          .sound(SoundType.METAL),
      new Item.Properties()),
  GOLD(
      ReinforcedCoreRegistry.registerReinforcingMaterial("gold", 81, Items.GOLD_INGOT),
      BlockBehaviour.Properties.of()
          .instrument(NoteBlockInstrument.BELL)
          .strength(2.0F, 6.0F)
          .sound(SoundType.METAL),
      new Item.Properties()),
  DIAMOND(
      ReinforcedCoreRegistry.registerReinforcingMaterial("diamond", 108, Items.DIAMOND),
      BlockBehaviour.Properties.of().strength(2.0F, 6.0F).sound(SoundType.METAL),
      new Item.Properties()),
  NETHERITE(
      ReinforcedCoreRegistry.registerReinforcingMaterial("netherite", 108, Items.NETHERITE_INGOT),
      BlockBehaviour.Properties.of().strength(2.0F, 1200.0F).sound(SoundType.NETHERITE_BLOCK),
      new Item.Properties().fireResistant());

  private final ReinforcingMaterial material;
  private final BlockBehaviour.Properties blockSettings;
  private final Item.Properties itemSettings;

  private ReinforcingMaterialSettings(
      ReinforcingMaterial material,
      BlockBehaviour.Properties blockSettings,
      Item.Properties itemSettings) {
    BlockBehaviour.StatePredicate contextPredicate =
        (state, world, pos) -> {
          BlockEntity blockEntity = world.getBlockEntity(pos);
          if (!(blockEntity instanceof ShulkerBoxBlockEntity)) {
            return true;
          }
          ShulkerBoxBlockEntity shulkerBoxBlockEntity = (ShulkerBoxBlockEntity) blockEntity;
          return shulkerBoxBlockEntity.isClosed();
        };

    this.material = material;
    this.blockSettings =
        blockSettings
            .forceSolidOn()
            .dynamicShape()
            .noOcclusion()
            .isSuffocating(contextPredicate)
            .isViewBlocking(contextPredicate)
            .pushReaction(PushReaction.DESTROY)
            .isRedstoneConductor(Blocks::always);
    this.itemSettings = itemSettings.stacksTo(1);
  }

  public ReinforcingMaterial getMaterial() {
    return this.material;
  }

  public BlockBehaviour.Properties getBlockSettings() {
    return this.blockSettings.mapColor(MapColor.COLOR_PURPLE);
  }

  public BlockBehaviour.Properties getColorBlockSettings(DyeColor color) {
    MapColor mapColor =
        switch (color) {
          case PURPLE -> MapColor.TERRACOTTA_PURPLE;
          default -> color.getMapColor();
        };

    return this.blockSettings.mapColor(mapColor);
  }

  public Item.Properties getItemSettings() {
    return this.itemSettings;
  }
}
