package atonkish.reinfshulker.block.cauldron;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.cauldron.CauldronInteractions;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LayeredCauldronBlock;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.block.ModBlocks;
import atonkish.reinfshulker.block.ReinforcedShulkerBoxBlock;
import atonkish.reinfshulker.item.ModItems;
import atonkish.reinfshulker.stat.ModStats;

public class ModCauldronBehavior {
  private static final CauldronInteraction CLEAN_REINFORCED_SHULKER_BOX;
  private static final Method CAULDRON_DISPATCHER_PUT;

  public static void init() {
    for (Map<DyeColor, Item> materialShulkerBoxMap : ModItems.REINFORCED_SHULKER_BOX_MAP.values()) {
      for (DyeColor color : DyeColor.values()) {
        putWaterInteraction(materialShulkerBoxMap.get(color), CLEAN_REINFORCED_SHULKER_BOX);
      }
    }
  }

  private static void putWaterInteraction(Item item, CauldronInteraction interaction) {
    try {
      CAULDRON_DISPATCHER_PUT.invoke(CauldronInteractions.WATER, item, interaction);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new IllegalStateException("Failed to register reinforced shulker cauldron behavior", e);
    }
  }

  static {
    try {
      CAULDRON_DISPATCHER_PUT =
          CauldronInteraction.Dispatcher.class.getDeclaredMethod(
              "put", Item.class, CauldronInteraction.class);
      CAULDRON_DISPATCHER_PUT.setAccessible(true);
    } catch (NoSuchMethodException e) {
      throw new IllegalStateException("Cannot find cauldron interaction dispatcher put method", e);
    }

    CLEAN_REINFORCED_SHULKER_BOX =
        (state, world, pos, player, hand, stack) -> {
          Block block = Block.byItem(stack.getItem());
          if (!(block instanceof ReinforcedShulkerBoxBlock)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
          } else {
            if (!world.isClientSide()) {
              ReinforcingMaterial material = ((ReinforcedShulkerBoxBlock) block).getMaterial();
              player.setItemInHand(
                  hand,
                  stack.transmuteCopy(
                      ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(material).get((DyeColor) null), 1));
              player.awardStat(ModStats.CLEAN_REINFORCED_SHULKER_BOX_MAP.get(material));
              LayeredCauldronBlock.lowerFillLevel(state, world, pos);
            }

            return InteractionResult.SUCCESS;
          }
        };
  }
}
