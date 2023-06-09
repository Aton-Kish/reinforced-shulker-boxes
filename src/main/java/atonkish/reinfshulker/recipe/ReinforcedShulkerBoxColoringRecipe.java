package atonkish.reinfshulker.recipe;

import net.minecraft.block.Block;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import org.jetbrains.annotations.Nullable;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.block.ReinforcedShulkerBoxBlock;

public class ReinforcedShulkerBoxColoringRecipe extends SpecialCraftingRecipe {
    public ReinforcedShulkerBoxColoringRecipe(Identifier identifier, CraftingRecipeCategory category) {
        super(identifier, category);
    }

    @Override
    public boolean matches(RecipeInputInventory recipeInputInventory, World world) {
        int i = 0;
        int j = 0;

        for (int k = 0; k < recipeInputInventory.size(); ++k) {
            ItemStack itemStack = recipeInputInventory.getStack(k);
            if (itemStack.isEmpty()) {
                continue;
            }

            if (Block.getBlockFromItem(itemStack.getItem()) instanceof ReinforcedShulkerBoxBlock) {
                ++i;
            } else if (itemStack.getItem() instanceof DyeItem) {
                ++j;
            } else {
                return false;
            }

            if (j <= 1 && i <= 1) {
                continue;
            }

            return false;
        }

        return i == 1 && j == 1;
    }

    @Override
    public ItemStack craft(RecipeInputInventory recipeInputInventory, DynamicRegistryManager dynamicRegistryManager) {
        ItemStack itemStack = ItemStack.EMPTY;
        DyeItem dyeItem = (DyeItem) Items.WHITE_DYE;

        @Nullable
        ReinforcingMaterial material = null;

        for (int i = 0; i < recipeInputInventory.size(); ++i) {
            ItemStack itemStack2 = recipeInputInventory.getStack(i);
            if (itemStack2.isEmpty()) {
                continue;
            }

            Item item = itemStack2.getItem();
            Block block = Block.getBlockFromItem(item);
            if (block instanceof ReinforcedShulkerBoxBlock) {
                itemStack = itemStack2;
                material = ((ReinforcedShulkerBoxBlock) block).getMaterial();
                continue;
            }

            if (!(item instanceof DyeItem)) {
                continue;
            }

            dyeItem = (DyeItem) item;
        }

        ItemStack itemStack3 = ReinforcedShulkerBoxBlock.getItemStack(material, dyeItem.getColor());
        if (itemStack.hasNbt()) {
            itemStack3.setNbt(itemStack.getNbt().copy());
        }

        return itemStack3;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializer.REINFORCED_SHULKER_BOX_COLORING;
    }
}
