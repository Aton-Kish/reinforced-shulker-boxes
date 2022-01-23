package atonkish.reinfshulker.block.entity;

import java.util.LinkedHashMap;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.block.ModBlocks;

public class ModBlockEntityType {
    public static final LinkedHashMap<ReinforcingMaterial, BlockEntityType<ReinforcedShulkerBoxBlockEntity>> REINFORCED_SHULKER_BOX_MAP = new LinkedHashMap<>();

    public static BlockEntityType<ReinforcedShulkerBoxBlockEntity> registerMaterial(String namespace,
            ReinforcingMaterial material) {
        String id = material.getName() + "_shulker_box";
        Block[] blocks = ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(material).values().toArray(new Block[0]);
        FabricBlockEntityTypeBuilder<ReinforcedShulkerBoxBlockEntity> builder = FabricBlockEntityTypeBuilder
                .create(createBlockEntityTypeFactory(material), blocks);
        BlockEntityType<ReinforcedShulkerBoxBlockEntity> blockEntityType = create(namespace, id, builder);

        REINFORCED_SHULKER_BOX_MAP.put(material, blockEntityType);

        return blockEntityType;
    }

    private static BlockEntityType<ReinforcedShulkerBoxBlockEntity> create(String namespace, String id,
            FabricBlockEntityTypeBuilder<ReinforcedShulkerBoxBlockEntity> builder) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(namespace, id),
                builder.build(null));
    }

    private static FabricBlockEntityTypeBuilder.Factory<ReinforcedShulkerBoxBlockEntity> createBlockEntityTypeFactory(
            ReinforcingMaterial material) {
        return (BlockPos blockPos, BlockState blockState) -> new ReinforcedShulkerBoxBlockEntity(material, blockPos,
                blockState);
    }
}
