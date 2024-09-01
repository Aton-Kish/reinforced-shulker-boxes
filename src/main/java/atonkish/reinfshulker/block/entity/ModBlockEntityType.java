package atonkish.reinfshulker.block.entity;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.block.ModBlocks;
import atonkish.reinfshulker.mixin.BlockEntityTypeAccessor;

public class ModBlockEntityType {
    public static final Map<ReinforcingMaterial, BlockEntityType<ReinforcedShulkerBoxBlockEntity>> REINFORCED_SHULKER_BOX_MAP = new LinkedHashMap<>();

    public static BlockEntityType<ReinforcedShulkerBoxBlockEntity> registerMaterial(String namespace,
            ReinforcingMaterial material) {
        if (!REINFORCED_SHULKER_BOX_MAP.containsKey(material)) {
            String id = material.getName() + "_shulker_box";
            Collection<Block> blocks = ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(material).values();
            BlockEntityType.Builder<ReinforcedShulkerBoxBlockEntity> builder = BlockEntityType.Builder
                    .create(ModBlockEntityType.createBlockEntityTypeFactory(material), blocks.toArray(new Block[0]));
            BlockEntityType<ReinforcedShulkerBoxBlockEntity> blockEntityType = ModBlockEntityType
                    .create(namespace, id, builder);
            REINFORCED_SHULKER_BOX_MAP.put(material, blockEntityType);

            ((BlockEntityTypeAccessor) BlockEntityType.SHULKER_BOX).getBlocks().addAll(blocks);
        }

        return REINFORCED_SHULKER_BOX_MAP.get(material);
    }

    private static BlockEntityType<ReinforcedShulkerBoxBlockEntity> create(String namespace, String id,
            BlockEntityType.Builder<ReinforcedShulkerBoxBlockEntity> builder) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(namespace, id),
                builder.build(null));
    }

    private static BlockEntityType.BlockEntityFactory<ReinforcedShulkerBoxBlockEntity> createBlockEntityTypeFactory(
            ReinforcingMaterial material) {
        return (BlockPos blockPos, BlockState blockState) -> new ReinforcedShulkerBoxBlockEntity(material, blockPos,
                blockState);
    }
}
