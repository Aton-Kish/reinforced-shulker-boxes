package atonkish.reinfshulker.block.cauldron;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Util;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.block.ModBlocks;
import atonkish.reinfshulker.block.ReinforcedShulkerBoxBlock;
import atonkish.reinfshulker.item.ModItems;
import atonkish.reinfshulker.stat.ModStats;

public class ModCauldronBehavior {
    public static final Map<Item, CauldronBehavior> CLEAN_REINFORCED_SHULKER_BOX_MAP;
    private static final CauldronBehavior CLEAN_REINFORCED_SHULKER_BOX;

    public static void init() {
    }

    static Object2ObjectOpenHashMap<Item, CauldronBehavior> createMap() {
        return Util.make(new Object2ObjectOpenHashMap<>(), (map) -> {
            map.defaultReturnValue((state, world, pos, player, hand, stack) -> {
                return ActionResult.PASS;
            });
        });
    }

    static {
        CLEAN_REINFORCED_SHULKER_BOX = (state, world, pos, player, hand, stack) -> {
            Block block = Block.getBlockFromItem(stack.getItem());
            if (!(block instanceof ReinforcedShulkerBoxBlock)) {
                return ActionResult.PASS;
            } else {
                if (!world.isClient) {
                    ReinforcingMaterial material = ((ReinforcedShulkerBoxBlock) block).getMaterial();
                    ItemStack itemStack = new ItemStack(
                            ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(material).get((DyeColor) null));
                    if (stack.hasNbt()) {
                        itemStack.setNbt(stack.getNbt().copy());
                    }

                    player.setStackInHand(hand, itemStack);
                    player.incrementStat(ModStats.CLEAN_REINFORCED_SHULKER_BOX_MAP.get(material));
                    LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
                }

                return ActionResult.success(world.isClient);
            }
        };

        CLEAN_REINFORCED_SHULKER_BOX_MAP = createMap();
        for (Map<DyeColor, Item> materialShulkerBoxMap : ModItems.REINFORCED_SHULKER_BOX_MAP.values()) {
            for (DyeColor color : DyeColor.values()) {
                CLEAN_REINFORCED_SHULKER_BOX_MAP.put(materialShulkerBoxMap.get(color), CLEAN_REINFORCED_SHULKER_BOX);
            }
        }
    }
}
