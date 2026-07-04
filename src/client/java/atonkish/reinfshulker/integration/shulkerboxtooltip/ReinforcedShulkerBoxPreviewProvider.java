package atonkish.reinfshulker.integration.shulkerboxtooltip;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import com.misterpemodder.shulkerboxtooltip.api.PreviewContext;
import com.misterpemodder.shulkerboxtooltip.api.color.ColorKey;
import com.misterpemodder.shulkerboxtooltip.api.provider.BlockEntityPreviewProvider;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.block.ReinforcedShulkerBoxBlock;

public class ReinforcedShulkerBoxPreviewProvider extends BlockEntityPreviewProvider {
  private final ReinforcingMaterial material;
  private final int maxRowSize;

  public ReinforcedShulkerBoxPreviewProvider(ReinforcingMaterial material) {
    super(material.getSize(), true, getPreviewRowSize(material), getPreviewRowSize(material));
    this.material = material;
    this.maxRowSize = getPreviewRowSize(material);
  }

  private static int getPreviewRowSize(ReinforcingMaterial material) {
    int size = material.getSize();
    return size <= 81 ? 9 : Math.max(9, size / 9);
  }

  @Override
  public int getInventoryMaxSize(PreviewContext context) {
    return this.material.getSize();
  }

  @Override
  public int getActiveSlotCount(PreviewContext context) {
    return this.material.getSize();
  }

  @Override
  public int getMaxRowSize(PreviewContext context) {
    return this.maxRowSize;
  }

  @Override
  public int getCompactMaxRowSize(PreviewContext context) {
    return this.maxRowSize;
  }

  @Override
  public int getPriority() {
    return 100000;
  }

  @Override
  @Environment(EnvType.CLIENT)
  public ColorKey getWindowColorKey(PreviewContext context) {
    if (context.stack().getItem() instanceof BlockItem blockItem
        && blockItem.getBlock() instanceof ReinforcedShulkerBoxBlock block) {
      DyeColor dye = block.getColor();

      if (dye == null) {
        return ColorKey.SHULKER_BOX;
      }

      return switch (dye) {
        case ORANGE -> ColorKey.ORANGE_SHULKER_BOX;
        case MAGENTA -> ColorKey.MAGENTA_SHULKER_BOX;
        case LIGHT_BLUE -> ColorKey.LIGHT_BLUE_SHULKER_BOX;
        case YELLOW -> ColorKey.YELLOW_SHULKER_BOX;
        case LIME -> ColorKey.LIME_SHULKER_BOX;
        case PINK -> ColorKey.PINK_SHULKER_BOX;
        case GRAY -> ColorKey.GRAY_SHULKER_BOX;
        case LIGHT_GRAY -> ColorKey.LIGHT_GRAY_SHULKER_BOX;
        case CYAN -> ColorKey.CYAN_SHULKER_BOX;
        case PURPLE -> ColorKey.PURPLE_SHULKER_BOX;
        case BLUE -> ColorKey.BLUE_SHULKER_BOX;
        case BROWN -> ColorKey.BROWN_SHULKER_BOX;
        case GREEN -> ColorKey.GREEN_SHULKER_BOX;
        case RED -> ColorKey.RED_SHULKER_BOX;
        case BLACK -> ColorKey.BLACK_SHULKER_BOX;
        default -> ColorKey.WHITE_SHULKER_BOX;
      };
    }

    return ColorKey.SHULKER_BOX;
  }
}
