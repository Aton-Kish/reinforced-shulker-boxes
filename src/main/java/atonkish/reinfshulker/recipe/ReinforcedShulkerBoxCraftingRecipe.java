package atonkish.reinfshulker.recipe;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

public class ReinforcedShulkerBoxCraftingRecipe extends ShapedRecipe {
    public ReinforcedShulkerBoxCraftingRecipe(Identifier id, String group, CraftingRecipeCategory category, int width,
            int height, DefaultedList<Ingredient> input, ItemStack output, boolean showNotification) {
        super(id, group, category, width, height, input, output, showNotification);
    }

    public ReinforcedShulkerBoxCraftingRecipe(Identifier id, String group, CraftingRecipeCategory category,
            int width, int height, DefaultedList<Ingredient> input, ItemStack output) {
        this(id, group, category, width, height, input, output, true);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializer.REINFORCED_SHULKER_BOX;
    }

    @Override
    public ItemStack craft(RecipeInputInventory recipeInputInventory, DynamicRegistryManager dynamicRegistryManager) {
        ItemStack itemStack = this.getOutput(dynamicRegistryManager).copy();
        NbtCompound nbtCompound = recipeInputInventory.getStack(4).getNbt();

        if (nbtCompound != null) {
            itemStack.setNbt(nbtCompound.copy());
        }

        return itemStack;
    }

    /**
     * Compiles a pattern and series of symbols into a list of ingredients (the
     * matrix) suitable for matching against a crafting grid.
     */
    static DefaultedList<Ingredient> createPatternMatrix(String[] pattern, Map<String, Ingredient> symbols, int width,
            int height) {
        DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(width * height, Ingredient.EMPTY);
        HashSet<String> set = Sets.newHashSet(symbols.keySet());
        set.remove(" ");

        for (int i = 0; i < pattern.length; ++i) {
            for (int j = 0; j < pattern[i].length(); ++j) {
                String string = pattern[i].substring(j, j + 1);
                Ingredient ingredient = symbols.get(string);
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
        }

        return defaultedList;
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

        if (pattern.length == l) {
            return new String[0];
        }

        String[] strings = new String[pattern.length - l - k];

        for (int o = 0; o < strings.length; ++o) {
            strings[o] = pattern[o + k].substring(i, j + 1);
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

    static String[] getPattern(JsonArray json) {
        String[] strings = new String[json.size()];
        if (strings.length > 3) {
            throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");
        }

        if (strings.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        }

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

    /**
     * Reads the pattern symbols.
     *
     * @return a mapping from a symbol to the ingredient it represents
     */
    static Map<String, Ingredient> readSymbols(JsonObject json) {
        HashMap<String, Ingredient> map = Maps.newHashMap();

        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            if (entry.getKey().length() != 1) {
                throw new JsonSyntaxException(
                        "Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }

            if (" ".equals(entry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }

            map.put(entry.getKey(), Ingredient.fromJson(entry.getValue()));
        }

        map.put(" ", Ingredient.EMPTY);

        return map;
    }

    public static class Serializer implements RecipeSerializer<ReinforcedShulkerBoxCraftingRecipe> {
        @Override
        public ReinforcedShulkerBoxCraftingRecipe read(Identifier identifier, JsonObject jsonObject) {
            String string = JsonHelper.getString(jsonObject, "group", "");
            CraftingRecipeCategory craftingRecipeCategory = CraftingRecipeCategory.CODEC
                    .byId(JsonHelper.getString(jsonObject, "category", null), CraftingRecipeCategory.MISC);
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
            boolean bl = JsonHelper.getBoolean(jsonObject, "show_notification", true);

            return new ReinforcedShulkerBoxCraftingRecipe(identifier, string, craftingRecipeCategory, i, j,
                    defaultedList, itemStack, bl);
        }

        @Override
        public ReinforcedShulkerBoxCraftingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            int i = packetByteBuf.readVarInt();
            int j = packetByteBuf.readVarInt();
            String string = packetByteBuf.readString();
            CraftingRecipeCategory craftingRecipeCategory = packetByteBuf
                    .readEnumConstant(CraftingRecipeCategory.class);
            DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i * j, Ingredient.EMPTY);

            for (int k = 0; k < defaultedList.size(); ++k) {
                defaultedList.set(k, Ingredient.fromPacket(packetByteBuf));
            }

            ItemStack itemStack = packetByteBuf.readItemStack();
            boolean bl = packetByteBuf.readBoolean();

            return new ReinforcedShulkerBoxCraftingRecipe(identifier, string, craftingRecipeCategory, i, j,
                    defaultedList, itemStack, bl);
        }

        @Override
        public void write(PacketByteBuf packetByteBuf, ReinforcedShulkerBoxCraftingRecipe recipe) {
            packetByteBuf.writeVarInt(recipe.getWidth());
            packetByteBuf.writeVarInt(recipe.getHeight());
            packetByteBuf.writeString(recipe.getGroup());
            packetByteBuf.writeEnumConstant(recipe.getCategory());
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.write(packetByteBuf);
            }
            packetByteBuf.writeItemStack(recipe.getOutput(null));
            packetByteBuf.writeBoolean(recipe.showNotification());
        }
    }
}
