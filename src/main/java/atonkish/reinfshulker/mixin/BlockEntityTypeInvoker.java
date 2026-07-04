package atonkish.reinfshulker.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockEntityType.class)
public interface BlockEntityTypeInvoker {
  @Invoker("register")
  public static <T extends BlockEntity> BlockEntityType<T> create(
      String id,
      BlockEntityType.BlockEntitySupplier<? extends T> blockEntityFactory,
      Block... blocks) {
    throw new AssertionError();
  }
}
