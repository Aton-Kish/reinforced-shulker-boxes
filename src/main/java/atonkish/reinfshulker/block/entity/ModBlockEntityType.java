package atonkish.reinfshulker.block.entity;

import java.util.HashMap;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;
import atonkish.reinfshulker.block.ModBlocks;

public class ModBlockEntityType {
    public static final HashMap<ReinforcingMaterial, BlockEntityType<ReinforcedShulkerBoxBlockEntity>> REINFORCED_SHULKER_BOX_MAP;

    public static void init() {
    }

    private static BlockEntityType<ReinforcedShulkerBoxBlockEntity> create(String id,
            FabricBlockEntityTypeBuilder<ReinforcedShulkerBoxBlockEntity> builder) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(ReinforcedShulkerBoxesMod.MOD_ID, id),
                builder.build(null));
    }

    private static FabricBlockEntityTypeBuilder.Factory<ReinforcedShulkerBoxBlockEntity> createBlockEntityTypeFactory(
            ReinforcingMaterial material) {
        return (BlockPos blockPos, BlockState blockState) -> new ReinforcedShulkerBoxBlockEntity(material, blockPos,
                blockState);
    }

    static {
        REINFORCED_SHULKER_BOX_MAP = new HashMap<>();
        for (ReinforcingMaterial material : ReinforcingMaterial.values()) {
            Block[] blocks = ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(material).values().toArray(new Block[0]);
            BlockEntityType<ReinforcedShulkerBoxBlockEntity> blockEntityType = create(
                    material.getName() + "_shulker_box",
                    FabricBlockEntityTypeBuilder.create(createBlockEntityTypeFactory(material), blocks));
            REINFORCED_SHULKER_BOX_MAP.put(material, blockEntityType);
        }
    }
}
