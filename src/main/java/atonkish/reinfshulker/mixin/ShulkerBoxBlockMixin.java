package atonkish.reinfshulker.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import atonkish.reinfshulker.block.entity.ReinforcedShulkerBoxBlockEntity;

@Mixin(ShulkerBoxBlock.class)
public class ShulkerBoxBlockMixin extends Block {
    // Dummy
    private ShulkerBoxBlockMixin(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Inject(at = @At("HEAD"), method = "onBreak", cancellable = true)
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo info) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ReinforcedShulkerBoxBlockEntity) {
            super.onBreak(world, pos, state, player);
            info.cancel();
        }
    }
}