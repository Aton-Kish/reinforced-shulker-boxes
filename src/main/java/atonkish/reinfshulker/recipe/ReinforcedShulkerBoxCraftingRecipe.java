package atonkish.reinfshulker.recipe;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.NotImplementedException;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.collection.DefaultedList;

public class ReinforcedShulkerBoxCraftingRecipe extends ShapedRecipe {
    public ReinforcedShulkerBoxCraftingRecipe(String group, CraftingRecipeCategory category, int width, int height,
            DefaultedList<Ingredient> ingredients, ItemStack result, boolean showNotification) {
        super(group, category, width, height, ingredients, result, showNotification);
    }

    public ReinforcedShulkerBoxCraftingRecipe(String group, CraftingRecipeCategory category, int width, int height,
            DefaultedList<Ingredient> ingredients, ItemStack result) {
        this(group, category, width, height, ingredients, result, true);

    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializer.REINFORCED_SHULKER_BOX;
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

    @VisibleForTesting
    static String[] removePadding(List<String> pattern) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;

        for (int m = 0; m < pattern.size(); ++m) {
            String string = pattern.get(m);
            i = Math.min(i, ReinforcedShulkerBoxCraftingRecipe.findFirstSymbol(string));
            int n = ReinforcedShulkerBoxCraftingRecipe.findLastSymbol(string);
            j = Math.max(j, n);
            if (n < 0) {
                if (k == m) {
                    ++k;
                }

                ++l;
                continue;
            }

            l = 0;
        }

        if (pattern.size() == l) {
            return new String[0];
        }

        String[] strings = new String[pattern.size() - l - k];

        for (int o = 0; o < strings.length; ++o) {
            strings[o] = pattern.get(o + k).substring(i, j + 1);
        }

        return strings;
    }

    private static int findFirstSymbol(String line) {
        int i;
        for (i = 0; i < line.length() && line.charAt(i) == ' '; ++i) {
        }

        return i;
    }

    private static int findLastSymbol(String pattern) {
        int i;
        for (i = pattern.length() - 1; i >= 0 && pattern.charAt(i) == ' '; --i) {
        }

        return i;
    }

    public static class Serializer implements RecipeSerializer<ReinforcedShulkerBoxCraftingRecipe> {
        static final Codec<List<String>> PATTERN_CODEC;
        static final Codec<String> KEY_ENTRY_CODEC;
        private static final Codec<ReinforcedShulkerBoxCraftingRecipe> CODEC;

        public Codec<ReinforcedShulkerBoxCraftingRecipe> codec() {
            return CODEC;
        }

        public ReinforcedShulkerBoxCraftingRecipe read(PacketByteBuf packetByteBuf) {
            int i = packetByteBuf.readVarInt();
            int j = packetByteBuf.readVarInt();
            String string = packetByteBuf.readString();
            CraftingRecipeCategory craftingRecipeCategory = (CraftingRecipeCategory) packetByteBuf
                    .readEnumConstant(CraftingRecipeCategory.class);
            DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i * j, Ingredient.EMPTY);

            for (int k = 0; k < defaultedList.size(); ++k) {
                defaultedList.set(k, Ingredient.fromPacket(packetByteBuf));
            }

            ItemStack itemStack = packetByteBuf.readItemStack();
            boolean bl = packetByteBuf.readBoolean();
            return new ReinforcedShulkerBoxCraftingRecipe(string, craftingRecipeCategory, i, j, defaultedList,
                    itemStack, bl);
        }

        public void write(PacketByteBuf packetByteBuf, ReinforcedShulkerBoxCraftingRecipe recipe) {
            packetByteBuf.writeVarInt(recipe.getWidth());
            packetByteBuf.writeVarInt(recipe.getHeight());
            packetByteBuf.writeString(recipe.getGroup());
            packetByteBuf.writeEnumConstant(recipe.getCategory());
            Iterator<Ingredient> ingredientsIterator = recipe.getIngredients().iterator();

            while (ingredientsIterator.hasNext()) {
                Ingredient ingredient = ingredientsIterator.next();
                ingredient.write(packetByteBuf);
            }

            packetByteBuf.writeItemStack(recipe.getResult(null));
            packetByteBuf.writeBoolean(recipe.showNotification());
        }

        static {
            PATTERN_CODEC = Codec.STRING.listOf().flatXmap((rows) -> {
                if (rows.size() > 3) {
                    return DataResult.error(() -> {
                        return "Invalid pattern: too many rows, 3 is maximum";
                    });
                } else if (rows.isEmpty()) {
                    return DataResult.error(() -> {
                        return "Invalid pattern: empty pattern not allowed";
                    });
                } else {
                    int i = ((String) rows.get(0)).length();
                    Iterator<String> rowsIterator = rows.iterator();

                    String string;
                    do {
                        if (!rowsIterator.hasNext()) {
                            return DataResult.success(rows);
                        }

                        string = rowsIterator.next();
                        if (string.length() > 3) {
                            return DataResult.error(() -> {
                                return "Invalid pattern: too many columns, 3 is maximum";
                            });
                        }
                    } while (i == string.length());

                    return DataResult.error(() -> {
                        return "Invalid pattern: each row must be the same width";
                    });
                }
            }, DataResult::success);
            KEY_ENTRY_CODEC = Codec.STRING.flatXmap((keyEntry) -> {
                if (keyEntry.length() != 1) {
                    return DataResult.error(() -> {
                        return "Invalid key entry: '" + keyEntry + "' is an invalid symbol (must be 1 character only).";
                    });
                } else {
                    return " ".equals(keyEntry) ? DataResult.error(() -> {
                        return "Invalid key entry: ' ' is a reserved symbol.";
                    }) : DataResult.success(keyEntry);
                }
            }, DataResult::success);
            CODEC = ShapedRecipe.Serializer.RawShapedRecipe.CODEC.flatXmap((recipe) -> {
                String[] strings = ReinforcedShulkerBoxCraftingRecipe.removePadding(recipe.pattern());
                int i = strings[0].length();
                int j = strings.length;
                DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i * j, Ingredient.EMPTY);
                Set<String> set = Sets.newHashSet(recipe.key().keySet());

                for (int k = 0; k < strings.length; ++k) {
                    String string = strings[k];

                    for (int l = 0; l < string.length(); ++l) {
                        String string2 = string.substring(l, l + 1);
                        Ingredient ingredient = string2.equals(" ") ? Ingredient.EMPTY
                                : (Ingredient) recipe.key().get(string2);
                        if (ingredient == null) {
                            return DataResult.error(() -> {
                                return "Pattern references symbol '" + string2 + "' but it's not defined in the key";
                            });
                        }

                        set.remove(string2);
                        defaultedList.set(l + i * k, ingredient);
                    }
                }

                if (!set.isEmpty()) {
                    return DataResult.error(() -> {
                        return "Key defines symbols that aren't used in pattern: " + set;
                    });
                } else {
                    ReinforcedShulkerBoxCraftingRecipe craftingRecipe = new ReinforcedShulkerBoxCraftingRecipe(
                            recipe.group(), recipe.category(), i, j, defaultedList,
                            recipe.result(), recipe.showNotification());
                    return DataResult.success(craftingRecipe);
                }
            }, (recipe) -> {
                throw new NotImplementedException(
                        "Serializing ReinforcedShulkerBoxCraftingRecipe is not implemented yet.");
            });
        }
    }
}
