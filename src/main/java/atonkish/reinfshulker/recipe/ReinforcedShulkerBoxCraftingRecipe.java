package atonkish.reinfshulker.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

public class ReinforcedShulkerBoxCraftingRecipe extends ShapedRecipe {
  final ShapedRecipePattern raw;
  final ItemStackTemplate result;

  public ReinforcedShulkerBoxCraftingRecipe(
      String group,
      CraftingBookCategory category,
      ShapedRecipePattern raw,
      ItemStack result,
      boolean showNotification) {
    this(
        new Recipe.CommonInfo(showNotification),
        new CraftingRecipe.CraftingBookInfo(category, group),
        raw,
        ItemStackTemplate.fromNonEmptyStack(result));
  }

  public ReinforcedShulkerBoxCraftingRecipe(
      Recipe.CommonInfo commonInfo,
      CraftingRecipe.CraftingBookInfo bookInfo,
      ShapedRecipePattern raw,
      ItemStackTemplate result) {
    super(commonInfo, bookInfo, raw, result);
    this.raw = raw;
    this.result = result;
  }

  public ReinforcedShulkerBoxCraftingRecipe(
      String group, CraftingBookCategory category, ShapedRecipePattern raw, ItemStack result) {
    this(group, category, raw, result, true);
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  public RecipeSerializer getSerializer() {
    return ModRecipeSerializer.REINFORCED_SHULKER_BOX;
  }

  private ShapedRecipePattern getRaw() {
    return this.raw;
  }

  private ItemStackTemplate getResult() {
    return this.result;
  }

  private Recipe.CommonInfo getCommonInfo() {
    return this.commonInfo;
  }

  private CraftingRecipe.CraftingBookInfo getBookInfo() {
    return this.bookInfo;
  }

  @Override
  public ItemStack assemble(CraftingInput craftingRecipeInput) {
    Item item = this.getResult().create().getItem();
    ItemStack itemStack = craftingRecipeInput.getItem(4);
    return itemStack.transmuteCopy(item, 1);
  }

  public static class Serializer {
    public static final MapCodec<ReinforcedShulkerBoxCraftingRecipe> CODEC =
        RecordCodecBuilder.mapCodec(
            (instance) -> {
              return instance
                  .group(
                      Recipe.CommonInfo.MAP_CODEC.forGetter(
                          (recipe) -> {
                            return recipe.getCommonInfo();
                          }),
                      CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(
                          (recipe) -> {
                            return recipe.getBookInfo();
                          }),
                      ShapedRecipePattern.MAP_CODEC.forGetter(
                          (recipe) -> {
                            return recipe.getRaw();
                          }),
                      ItemStackTemplate.CODEC
                          .fieldOf("result")
                          .forGetter(
                              (recipe) -> {
                                return recipe.result;
                              }))
                  .apply(instance, ReinforcedShulkerBoxCraftingRecipe::new);
            });
    public static final StreamCodec<RegistryFriendlyByteBuf, ReinforcedShulkerBoxCraftingRecipe>
        PACKET_CODEC =
            StreamCodec.of(
                atonkish.reinfshulker.recipe.ReinforcedShulkerBoxCraftingRecipe.Serializer::write,
                atonkish.reinfshulker.recipe.ReinforcedShulkerBoxCraftingRecipe.Serializer::read);

    public Serializer() {}

    public MapCodec<ReinforcedShulkerBoxCraftingRecipe> codec() {
      return CODEC;
    }

    public StreamCodec<RegistryFriendlyByteBuf, ReinforcedShulkerBoxCraftingRecipe> streamCodec() {
      return PACKET_CODEC;
    }

    private static ReinforcedShulkerBoxCraftingRecipe read(RegistryFriendlyByteBuf buf) {
      Recipe.CommonInfo commonInfo = Recipe.CommonInfo.STREAM_CODEC.decode(buf);
      CraftingRecipe.CraftingBookInfo bookInfo =
          CraftingRecipe.CraftingBookInfo.STREAM_CODEC.decode(buf);
      ShapedRecipePattern rawShapedRecipe =
          (ShapedRecipePattern) ShapedRecipePattern.STREAM_CODEC.decode(buf);
      ItemStackTemplate itemStack = (ItemStackTemplate) ItemStackTemplate.STREAM_CODEC.decode(buf);
      return new ReinforcedShulkerBoxCraftingRecipe(
          commonInfo, bookInfo, rawShapedRecipe, itemStack);
    }

    private static void write(
        RegistryFriendlyByteBuf buf, ReinforcedShulkerBoxCraftingRecipe recipe) {
      Recipe.CommonInfo.STREAM_CODEC.encode(buf, recipe.getCommonInfo());
      CraftingRecipe.CraftingBookInfo.STREAM_CODEC.encode(buf, recipe.getBookInfo());
      ShapedRecipePattern.STREAM_CODEC.encode(buf, recipe.getRaw());
      ItemStackTemplate.STREAM_CODEC.encode(buf, recipe.getResult());
    }
  }
}
