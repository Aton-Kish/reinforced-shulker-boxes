package atonkish.reinfshulker.block.cauldron;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;

import atonkish.reinfcore.util.ReinforcingMaterial;

import atonkish.reinfshulker.block.ModBlocks;
import atonkish.reinfshulker.block.ReinforcedShulkerBoxBlock;
import atonkish.reinfshulker.item.ModItems;
import atonkish.reinfshulker.stat.ModStats;

public class ModCauldronBehavior {
    private static final CauldronBehavior CLEAN_REINFORCED_SHULKER_BOX;

    public static void init() {
        Map<Item, CauldronBehavior> map = CauldronBehavior.WATER_CAULDRON_BEHAVIOR.map();
        for (Map<DyeColor, Item> materialShulkerBoxMap : ModItems.REINFORCED_SHULKER_BOX_MAP.values()) {
            for (DyeColor color : DyeColor.values()) {
                map.put(materialShulkerBoxMap.get(color), CLEAN_REINFORCED_SHULKER_BOX);
            }
        }
    }

    static {
        CLEAN_REINFORCED_SHULKER_BOX = (state, world, pos, player, hand, stack) -> {
            Block block = Block.getBlockFromItem(stack.getItem());
            if (!(block instanceof ReinforcedShulkerBoxBlock)) {
                return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
            } else {
                if (!world.isClient()) {
                    ReinforcingMaterial material = ((ReinforcedShulkerBoxBlock) block).getMaterial();
                    player.setStackInHand(hand, stack.copyComponentsToNewStack(
                            ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(material).get((DyeColor) null), 1));
                    player.incrementStat(ModStats.CLEAN_REINFORCED_SHULKER_BOX_MAP.get(material));
                    LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
                }

                return ActionResult.SUCCESS;
            }
        };
    }
}
