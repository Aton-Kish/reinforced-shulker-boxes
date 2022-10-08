package atonkish.reinfshulker.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.block.entity.ModBlockEntityType;
import atonkish.reinfshulker.block.entity.ReinforcedShulkerBoxBlockEntity;
import atonkish.reinfshulker.stat.ModStats;

public class ReinforcedShulkerBoxBlock extends ShulkerBoxBlock {
    private final ReinforcingMaterial material;

    public ReinforcedShulkerBoxBlock(ReinforcingMaterial material, @Nullable DyeColor color,
            AbstractBlock.Settings settings) {
        super(color, settings);
        this.material = material;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ReinforcedShulkerBoxBlockEntity(this.material, this.getColor(), pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state,
            BlockEntityType<T> type) {
        return checkType(type, ModBlockEntityType.REINFORCED_SHULKER_BOX_MAP.get(this.material),
                ReinforcedShulkerBoxBlockEntity::tick);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else if (player.isSpectator()) {
            return ActionResult.CONSUME;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ShulkerBoxBlockEntity) {
                ShulkerBoxBlockEntity shulkerBoxBlockEntity = (ShulkerBoxBlockEntity) blockEntity;
                if (canOpen(state, world, pos, shulkerBoxBlockEntity)) {
                    player.openHandledScreen(shulkerBoxBlockEntity);
                    player.incrementStat(ModStats.OPEN_REINFORCED_SHULKER_BOX_MAP.get(this.material));
                    PiglinBrain.onGuardedBlockInteracted(player, true);
                }

                return ActionResult.CONSUME;
            } else {
                return ActionResult.PASS;
            }
        }
    }

    private static boolean canOpen(BlockState state, World world, BlockPos pos, ShulkerBoxBlockEntity entity) {
        if (entity.getAnimationStage() != ShulkerBoxBlockEntity.AnimationStage.CLOSED) {
            return true;
        } else {
            Box box = ShulkerEntity.method_33347((Direction) state.get(FACING), 0.0F, 0.5F).offset(pos)
                    .contract(1.0E-6D);
            return world.isSpaceEmpty(box);
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ReinforcedShulkerBoxBlockEntity) {
            ReinforcedShulkerBoxBlockEntity reinforcedShulkerBoxBlockEntity = (ReinforcedShulkerBoxBlockEntity) blockEntity;
            if (!world.isClient && player.isCreative() && !reinforcedShulkerBoxBlockEntity.isEmpty()) {
                ItemStack itemStack = getItemStack(this.material, this.getColor());
                NbtCompound nbtCompound = reinforcedShulkerBoxBlockEntity.writeInventoryNbt(new NbtCompound());
                if (!nbtCompound.isEmpty()) {
                    itemStack.setSubNbt("BlockEntityTag", nbtCompound);
                }

                if (reinforcedShulkerBoxBlockEntity.hasCustomName()) {
                    itemStack.setCustomName(reinforcedShulkerBoxBlockEntity.getCustomName());
                }

                ItemEntity itemEntity = new ItemEntity(world, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D,
                        (double) pos.getZ() + 0.5D, itemStack);
                itemEntity.setToDefaultPickupDelay();
                world.spawnEntity(itemEntity);
            } else {
                reinforcedShulkerBoxBlockEntity.checkLootInteraction(player);
            }
        }

        super.onBreak(world, pos, state, player);
    }

    public static Block get(ReinforcingMaterial material, @Nullable DyeColor color) {
        return ModBlocks.REINFORCED_SHULKER_BOX_MAP.get(material).get(color);
    }

    public ReinforcingMaterial getMaterial() {
        return this.material;
    }

    public static ItemStack getItemStack(ReinforcingMaterial material, @Nullable DyeColor color) {
        return new ItemStack(get(material, color));
    }
}
