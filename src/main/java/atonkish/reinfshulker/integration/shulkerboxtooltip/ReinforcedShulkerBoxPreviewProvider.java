package atonkish.reinfshulker.integration.shulkerboxtooltip;

import com.misterpemodder.shulkerboxtooltip.api.PreviewContext;
import com.misterpemodder.shulkerboxtooltip.api.provider.BlockEntityPreviewProvider;

import net.minecraft.block.Block;
import net.minecraft.util.DyeColor;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.block.ReinforcedShulkerBoxBlock;

public class ReinforcedShulkerBoxPreviewProvider extends BlockEntityPreviewProvider {
    private static float[] SHULKER_BOX_COLOR = new float[] { 0.592f, 0.403f, 0.592f };
    protected final int maxRowSize;
    private final ReinforcingMaterial material;

    public ReinforcedShulkerBoxPreviewProvider(ReinforcingMaterial material) {
        super(material.getSize(), true);

        int size = material.getSize();
        this.maxRowSize = size <= 81 ? 9 : size / 9;

        this.material = material;
    }

    @Override
    public boolean showTooltipHints(PreviewContext context) {
        return true;
    }

    @Override
    public float[] getWindowColor(PreviewContext context) {
        DyeColor dye = ((ReinforcedShulkerBoxBlock) Block.getBlockFromItem(context.getStack().getItem())).getColor();
        if (dye != null) {
            float[] components = dye.getColorComponents();
            return new float[] { Math.max(0.15f, components[0]), Math.max(0.15f, components[1]),
                    Math.max(0.15f, components[2]) };
        } else {
            return SHULKER_BOX_COLOR;
        }
    }

    @Override
    public int getMaxRowSize(PreviewContext context) {
        return this.maxRowSize;
    }

    public ReinforcingMaterial getMaterial() {
        return this.material;
    }
}
