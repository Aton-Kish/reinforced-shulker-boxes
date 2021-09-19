package atonkish.reinfshulker.mixin;

import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import atonkish.reinfshulker.block.cauldron.ModCauldronBehavior;

@Mixin(AbstractCauldronBlock.class)
public class AbstractCauldronBlockMixin {
    @Inject(at = @At("HEAD"), method = "onUse", cancellable = true)
    public void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit,
            CallbackInfoReturnable<ActionResult> infoReturnable) {
        Class<?> cauldronClass = this.getClass();
        if (cauldronClass == LeveledCauldronBlock.class) {
            ItemStack itemStack = player.getStackInHand(hand);
            Item item = itemStack.getItem();
            if (ModCauldronBehavior.CLEAN_REINFORCED_SHULKER_BOX_MAP.containsKey(item)) {
                CauldronBehavior cauldronBehavior = ModCauldronBehavior.CLEAN_REINFORCED_SHULKER_BOX_MAP.get(item);
                infoReturnable.setReturnValue(cauldronBehavior.interact(state, world, pos, player, hand, itemStack));
            }
        }
    }
}
