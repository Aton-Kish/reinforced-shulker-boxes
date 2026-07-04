package atonkish.reinfshulker.block.entity;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.block.ModBlocks;
import atonkish.reinfshulker.mixin.BlockEntityTypeAccessor;

public class ModBlockEntityType {
  public static final Map<ReinforcingMaterial, BlockEntityType<ReinforcedShulkerBoxBlockEntity>>
      REINFORCED_SHULKER_BOX_MAP = new LinkedHashMap<>();

  public static BlockEntityType<ReinforcedShulkerBoxBlockEntity> registerMaterial(
      String namespace, ReinforcingMaterial material) {
    if (!REINFORCED_SHULKER_BOX_MAP.containsKey(material)) {
      String id = material.getName() + "_shulker_box";
      Collection<Block> blocks = ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(material).values();
      BlockEntityType<ReinforcedShulkerBoxBlockEntity> blockEntityType =
          Registry.register(
              BuiltInRegistries.BLOCK_ENTITY_TYPE,
              Identifier.fromNamespaceAndPath(namespace, id),
              new BlockEntityType<>(
                  (blockPos, blockState) ->
                      new ReinforcedShulkerBoxBlockEntity(material, blockPos, blockState),
                  Set.copyOf(blocks)));
      REINFORCED_SHULKER_BOX_MAP.put(material, blockEntityType);

      BlockEntityType<?> shulkerBoxType =
          BuiltInRegistries.BLOCK_ENTITY_TYPE.getValue(
              Identifier.withDefaultNamespace("shulker_box"));
      ((BlockEntityTypeAccessor) shulkerBoxType).getValidBlocks().addAll(blocks);
    }

    return REINFORCED_SHULKER_BOX_MAP.get(material);
  }
}
