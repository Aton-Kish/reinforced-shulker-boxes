package atonkish.reinfshulker.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.RawShapedRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;

public class ReinforcedShulkerBoxCraftingRecipe extends ShapedRecipe {
    final RawShapedRecipe raw;
    final ItemStack result;

    public ReinforcedShulkerBoxCraftingRecipe(String group, CraftingRecipeCategory category, RawShapedRecipe raw,
            ItemStack result, boolean showNotification) {
        super(group, category, raw, result, showNotification);
        this.raw = raw;
        this.result = result;
    }

    public ReinforcedShulkerBoxCraftingRecipe(String group, CraftingRecipeCategory category, RawShapedRecipe raw,
            ItemStack result) {
        this(group, category, raw, result, true);
    }

    @Override
    public RecipeSerializer<? extends ShapedRecipe> getSerializer() {
        return ModRecipeSerializer.REINFORCED_SHULKER_BOX;
    }

    private RawShapedRecipe getRaw() {
        return this.raw;
    }

    private ItemStack getResult() {
        return this.result;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput craftingRecipeInput, RegistryWrapper.WrapperLookup wrapperLookup) {
        Item item = this.getResult().copy().getItem();
        ItemStack itemStack = craftingRecipeInput.getStackInSlot(4);
        return itemStack.copyComponentsToNewStack(item, 1);
    }

    public static class Serializer implements RecipeSerializer<ReinforcedShulkerBoxCraftingRecipe> {
        public static final MapCodec<ReinforcedShulkerBoxCraftingRecipe> CODEC = RecordCodecBuilder
                .mapCodec((instance) -> {
                    return instance.group(Codec.STRING.optionalFieldOf("group", "").forGetter((recipe) -> {
                        return recipe.getGroup();
                    }), CraftingRecipeCategory.CODEC.fieldOf("category").orElse(CraftingRecipeCategory.MISC)
                            .forGetter((recipe) -> {
                                return recipe.getCategory();
                            }), RawShapedRecipe.CODEC.forGetter((recipe) -> {
                                return recipe.getRaw();
                            }), ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter((recipe) -> {
                                return recipe.getResult();
                            }), Codec.BOOL.optionalFieldOf("show_notification", true).forGetter((recipe) -> {
                                return recipe.showNotification();
                            })).apply(instance, ReinforcedShulkerBoxCraftingRecipe::new);
                });
        public static final PacketCodec<RegistryByteBuf, ReinforcedShulkerBoxCraftingRecipe> PACKET_CODEC = PacketCodec
                .ofStatic(Serializer::write, Serializer::read);

        public Serializer() {
        }

        public MapCodec<ReinforcedShulkerBoxCraftingRecipe> codec() {
            return CODEC;
        }

        public PacketCodec<RegistryByteBuf, ReinforcedShulkerBoxCraftingRecipe> packetCodec() {
            return PACKET_CODEC;
        }

        private static ReinforcedShulkerBoxCraftingRecipe read(RegistryByteBuf buf) {
            String string = buf.readString();
            CraftingRecipeCategory craftingRecipeCategory = (CraftingRecipeCategory) buf
                    .readEnumConstant(CraftingRecipeCategory.class);
            RawShapedRecipe rawShapedRecipe = (RawShapedRecipe) RawShapedRecipe.PACKET_CODEC.decode(buf);
            ItemStack itemStack = (ItemStack) ItemStack.PACKET_CODEC.decode(buf);
            boolean bl = buf.readBoolean();
            return new ReinforcedShulkerBoxCraftingRecipe(string, craftingRecipeCategory, rawShapedRecipe, itemStack,
                    bl);
        }

        private static void write(RegistryByteBuf buf, ReinforcedShulkerBoxCraftingRecipe recipe) {
            buf.writeString(recipe.getGroup());
            buf.writeEnumConstant(recipe.getCategory());
            RawShapedRecipe.PACKET_CODEC.encode(buf, recipe.getRaw());
            ItemStack.PACKET_CODEC.encode(buf, recipe.getResult());
            buf.writeBoolean(recipe.showNotification());
        }
    }
}
