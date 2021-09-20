package atonkish.reinfshulker.recipe;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

public class ReinforcedShulkerBoxCraftingRecipe extends ShapedRecipe {
    final int width;
    final int height;
    final DefaultedList<Ingredient> input;
    final ItemStack output;
    final String group;

    public ReinforcedShulkerBoxCraftingRecipe(Identifier id, String group, int width, int height,
            DefaultedList<Ingredient> input, ItemStack output) {
        super(id, group, width, height, input, output);
        this.group = group;
        this.width = width;
        this.height = height;
        this.input = input;
        this.output = output;
    }

    public ItemStack craft(CraftingInventory craftingInventory) {
        ItemStack inputItemStack = craftingInventory.getStack(4);
        ItemStack outputItemStack = this.getOutput();

        if (inputItemStack.hasNbt()) {
            outputItemStack.setNbt(inputItemStack.getNbt().copy());
        }

        return outputItemStack;
    }

    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializer.REINFORCED_SHULKER_BOX;
    }

    /**
     * Compiles a pattern and series of symbols into a list of ingredients (the
     * matrix) suitable for matching against a crafting grid.
     */
    static DefaultedList<Ingredient> createPatternMatrix(String[] pattern, Map<String, Ingredient> symbols, int width,
            int height) {
        DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(width * height, Ingredient.EMPTY);
        Set<String> set = Sets.newHashSet((Iterable<String>) symbols.keySet());
        set.remove(" ");

        for (int i = 0; i < pattern.length; ++i) {
            for (int j = 0; j < pattern[i].length(); ++j) {
                String string = pattern[i].substring(j, j + 1);
                Ingredient ingredient = (Ingredient) symbols.get(string);
                if (ingredient == null) {
                    throw new JsonSyntaxException(
                            "Pattern references symbol '" + string + "' but it's not defined in the key");
                }

                set.remove(string);
                defaultedList.set(j + width * i, ingredient);
            }
        }

        if (!set.isEmpty()) {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
        } else {
            return defaultedList;
        }
    }

    /**
     * Removes empty space from around the recipe pattern.
     *
     * <p>
     * Turns patterns such as:
     * </p>
     *
     * <pre>
     * {@code
     * "   o"
     * "   a"
     * "    "
     * }
     * </pre>
     *
     * Into:
     *
     * <pre>
     * {@code
     * "o"
     * "a"
     * }
     * </pre>
     *
     * @return a new recipe pattern with all leading and trailing empty rows/columns
     *         removed
     */
    @VisibleForTesting
    static String[] removePadding(String... pattern) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;

        for (int m = 0; m < pattern.length; ++m) {
            String string = pattern[m];
            i = Math.min(i, findFirstSymbol(string));
            int n = findLastSymbol(string);
            j = Math.max(j, n);
            if (n < 0) {
                if (k == m) {
                    ++k;
                }

                ++l;
            } else {
                l = 0;
            }
        }

        if (pattern.length == l) {
            return new String[0];
        } else {
            String[] strings = new String[pattern.length - l - k];

            for (int o = 0; o < strings.length; ++o) {
                strings[o] = pattern[o + k].substring(i, j + 1);
            }

            return strings;
        }
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

    static String[] getPattern(JsonArray json) {
        String[] strings = new String[json.size()];
        if (strings.length > 3) {
            throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");
        } else if (strings.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        } else {
            for (int i = 0; i < strings.length; ++i) {
                String string = JsonHelper.asString(json.get(i), "pattern[" + i + "]");
                if (string.length() > 3) {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");
                }

                if (i > 0 && strings[0].length() != string.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }

                strings[i] = string;
            }

            return strings;
        }
    }

    /**
     * Reads the pattern symbols.
     *
     * @return a mapping from a symbol to the ingredient it represents
     */
    static Map<String, Ingredient> readSymbols(JsonObject json) {
        Map<String, Ingredient> map = Maps.newHashMap();
        Iterator<Entry<String, JsonElement>> var2 = json.entrySet().iterator();

        while (var2.hasNext()) {
            Entry<String, JsonElement> entry = var2.next();
            if (((String) entry.getKey()).length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + (String) entry.getKey()
                        + "' is an invalid symbol (must be 1 character only).");
            }

            if (" ".equals(entry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }

            map.put((String) entry.getKey(), Ingredient.fromJson((JsonElement) entry.getValue()));
        }

        map.put(" ", Ingredient.EMPTY);
        return map;
    }

    public static class Serializer implements RecipeSerializer<ReinforcedShulkerBoxCraftingRecipe> {
        public ReinforcedShulkerBoxCraftingRecipe read(Identifier identifier, JsonObject jsonObject) {
            String string = JsonHelper.getString(jsonObject, "group", "");
            Map<String, Ingredient> map = ReinforcedShulkerBoxCraftingRecipe
                    .readSymbols(JsonHelper.getObject(jsonObject, "key"));
            String[] strings = ReinforcedShulkerBoxCraftingRecipe.removePadding(
                    ReinforcedShulkerBoxCraftingRecipe.getPattern(JsonHelper.getArray(jsonObject, "pattern")));
            int i = strings[0].length();
            int j = strings.length;
            DefaultedList<Ingredient> defaultedList = ReinforcedShulkerBoxCraftingRecipe.createPatternMatrix(strings,
                    map, i, j);
            ItemStack itemStack = ReinforcedShulkerBoxCraftingRecipe
                    .outputFromJson(JsonHelper.getObject(jsonObject, "result"));
            return new ReinforcedShulkerBoxCraftingRecipe(identifier, string, i, j, defaultedList, itemStack);
        }

        public ReinforcedShulkerBoxCraftingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            int i = packetByteBuf.readVarInt();
            int j = packetByteBuf.readVarInt();
            String string = packetByteBuf.readString();
            DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i * j, Ingredient.EMPTY);

            for (int k = 0; k < defaultedList.size(); ++k) {
                defaultedList.set(k, Ingredient.fromPacket(packetByteBuf));
            }

            ItemStack itemStack = packetByteBuf.readItemStack();
            return new ReinforcedShulkerBoxCraftingRecipe(identifier, string, i, j, defaultedList, itemStack);
        }

        public void write(PacketByteBuf packetByteBuf, ReinforcedShulkerBoxCraftingRecipe recipe) {
            packetByteBuf.writeVarInt(recipe.width);
            packetByteBuf.writeVarInt(recipe.height);
            packetByteBuf.writeString(recipe.group);
            Iterator<Ingredient> var3 = recipe.input.iterator();

            while (var3.hasNext()) {
                Ingredient ingredient = (Ingredient) var3.next();
                ingredient.write(packetByteBuf);
            }

            packetByteBuf.writeItemStack(recipe.output);
        }
    }

}
