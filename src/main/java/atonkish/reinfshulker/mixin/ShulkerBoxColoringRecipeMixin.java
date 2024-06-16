package atonkish.reinfshulker.mixin;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ShulkerBoxColoringRecipe;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import atonkish.reinfshulker.block.ReinforcedShulkerBoxBlock;

@Mixin(ShulkerBoxColoringRecipe.class)
public class ShulkerBoxColoringRecipeMixin {
    @Inject(at = @At("HEAD"), method = "matches", cancellable = true)
    public void matches(CraftingRecipeInput craftingRecipeInput, World world,
            CallbackInfoReturnable<Boolean> infoReturnable) {
        for (int k = 0; k < craftingRecipeInput.getSize(); ++k) {
            ItemStack itemStack = craftingRecipeInput.getStackInSlot(k);
            if (Block.getBlockFromItem(itemStack.getItem()) instanceof ReinforcedShulkerBoxBlock) {
                infoReturnable.setReturnValue(false);
            }
        }
    }
}
