package atonkish.reinfshulker.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RawShapedRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.dynamic.Codecs;

public class ReinforcedShulkerBoxCraftingRecipe extends ShapedRecipe {
    final RawShapedRecipe raw;

    public ReinforcedShulkerBoxCraftingRecipe(String group, CraftingRecipeCategory category, RawShapedRecipe raw,
            ItemStack result, boolean showNotification) {
        super(group, category, raw, result, showNotification);
        this.raw = raw;
    }

    public ReinforcedShulkerBoxCraftingRecipe(String group, CraftingRecipeCategory category, RawShapedRecipe raw,
            ItemStack result) {
        this(group, category, raw, result, true);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializer.REINFORCED_SHULKER_BOX;
    }

    private RawShapedRecipe getRaw() {
        return this.raw;
    }

    @Override
    public ItemStack craft(RecipeInputInventory recipeInputInventory, DynamicRegistryManager dynamicRegistryManager) {
        ItemStack itemStack = this.getResult(dynamicRegistryManager).copy();
        NbtCompound nbtCompound = recipeInputInventory.getStack(4).getNbt();

        if (nbtCompound != null) {
            itemStack.setNbt(nbtCompound.copy());
        }

        return itemStack;
    }

    public static class Serializer implements RecipeSerializer<ReinforcedShulkerBoxCraftingRecipe> {
        public static final Codec<ReinforcedShulkerBoxCraftingRecipe> CODEC = RecordCodecBuilder.create((instance) -> {
            return instance
                    .group(Codecs.createStrictOptionalFieldCodec(Codec.STRING, "group", "").forGetter((recipe) -> {
                        return recipe.getGroup();
                    }), CraftingRecipeCategory.CODEC.fieldOf("category").orElse(CraftingRecipeCategory.MISC)
                            .forGetter((recipe) -> {
                                return recipe.getCategory();
                            }), RawShapedRecipe.CODEC.forGetter((recipe) -> {
                                return recipe.getRaw();
                            }), ItemStack.RECIPE_RESULT_CODEC.fieldOf("result").forGetter((recipe) -> {
                                return recipe.getResult(null);
                            }), Codecs.createStrictOptionalFieldCodec(Codec.BOOL, "show_notification", true)
                                    .forGetter((recipe) -> {
                                        return recipe.showNotification();
                                    }))
                    .apply(instance, ReinforcedShulkerBoxCraftingRecipe::new);
        });

        public Codec<ReinforcedShulkerBoxCraftingRecipe> codec() {
            return CODEC;
        }

        public ReinforcedShulkerBoxCraftingRecipe read(PacketByteBuf packetByteBuf) {
            String string = packetByteBuf.readString();
            CraftingRecipeCategory craftingRecipeCategory = (CraftingRecipeCategory) packetByteBuf
                    .readEnumConstant(CraftingRecipeCategory.class);
            RawShapedRecipe rawShapedRecipe = RawShapedRecipe.readFromBuf(packetByteBuf);
            ItemStack itemStack = packetByteBuf.readItemStack();
            boolean bl = packetByteBuf.readBoolean();
            return new ReinforcedShulkerBoxCraftingRecipe(string, craftingRecipeCategory, rawShapedRecipe, itemStack,
                    bl);
        }

        public void write(PacketByteBuf packetByteBuf, ReinforcedShulkerBoxCraftingRecipe recipe) {
            packetByteBuf.writeString(recipe.getGroup());
            packetByteBuf.writeEnumConstant(recipe.getCategory());
            recipe.getRaw().writeToBuf(packetByteBuf);
            packetByteBuf.writeItemStack(recipe.getResult(null));
            packetByteBuf.writeBoolean(recipe.showNotification());
        }
    }
}
