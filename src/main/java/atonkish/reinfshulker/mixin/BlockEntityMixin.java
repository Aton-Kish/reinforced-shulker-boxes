package atonkish.reinfshulker.mixin;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import atonkish.reinfshulker.block.entity.BlockEntityInterface;

@Mixin(BlockEntity.class)
public class BlockEntityMixin implements BlockEntityInterface {
    @Shadow
    @Mutable
    private BlockEntityType<?> type;

    public void setType(BlockEntityType<?> type) {
        this.type = type;
    }
}
