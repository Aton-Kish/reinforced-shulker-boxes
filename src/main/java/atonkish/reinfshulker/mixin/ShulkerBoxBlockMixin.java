package atonkish.reinfshulker.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import atonkish.reinfshulker.block.ReinforcedShulkerBoxBlock;
import atonkish.reinfshulker.block.entity.ReinforcedShulkerBoxBlockEntity;

@Mixin(ShulkerBoxBlock.class)
public class ShulkerBoxBlockMixin {
    @Inject(method = "onBreak", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BlockEntity;setStackNbt(Lnet/minecraft/item/ItemStack;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo info,
            BlockEntity blockEntity, ShulkerBoxBlockEntity shulkerBoxBlockEntity, ItemStack itemStack) {
        if (blockEntity instanceof ReinforcedShulkerBoxBlockEntity) {
            ReinforcedShulkerBoxBlockEntity entity = (ReinforcedShulkerBoxBlockEntity) blockEntity;
            ((ItemStackAccessor) (Object) itemStack)
                    .setItem(ReinforcedShulkerBoxBlock.get(entity.getMaterial(), entity.getColor()).asItem());
        }
    }
}
