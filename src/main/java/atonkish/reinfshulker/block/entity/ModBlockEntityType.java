package atonkish.reinfshulker.block.entity;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.block.ModBlocks;
import atonkish.reinfshulker.mixin.BlockEntityTypeAccessor;
import atonkish.reinfshulker.mixin.BlockEntityTypeInvoker;

public class ModBlockEntityType {
    public static final Map<ReinforcingMaterial, BlockEntityType<ReinforcedShulkerBoxBlockEntity>> REINFORCED_SHULKER_BOX_MAP = new LinkedHashMap<>();

    public static BlockEntityType<ReinforcedShulkerBoxBlockEntity> registerMaterial(String namespace,
            ReinforcingMaterial material) {
        if (!REINFORCED_SHULKER_BOX_MAP.containsKey(material)) {
            String id = material.getName() + "_shulker_box";
            Collection<Block> blocks = ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(material).values();
            BlockEntityType<ReinforcedShulkerBoxBlockEntity> blockEntityType = BlockEntityTypeInvoker.create(
                    Identifier.of(namespace, id).toString(),
                    (blockPos, blockState) -> new ReinforcedShulkerBoxBlockEntity(material, blockPos, blockState),
                    blocks.toArray(new Block[0]));
            REINFORCED_SHULKER_BOX_MAP.put(material, blockEntityType);

            ((BlockEntityTypeAccessor) BlockEntityType.SHULKER_BOX).getBlocks().addAll(blocks);
        }

        return REINFORCED_SHULKER_BOX_MAP.get(material);
    }
}
