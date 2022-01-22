package atonkish.reinfshulker.integration.shulkerboxtooltip;

import java.util.HashMap;

import com.misterpemodder.shulkerboxtooltip.api.ShulkerBoxTooltipApi;
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProvider;
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProviderRegistry;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfcore.util.ReinforcingMaterials;
import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;
import atonkish.reinfshulker.item.ModItems;

public class ShulkerBoxTooltip implements ShulkerBoxTooltipApi {
    public static final HashMap<ReinforcingMaterial, Item[]> REINFORCED_SHULKER_BOX_ITEMS_MAP;

    private static void register(PreviewProviderRegistry registry, String id, PreviewProvider provider, Item... items) {
        registry.register(new Identifier(ReinforcedShulkerBoxesMod.MOD_ID, id), provider, items);
    }

    @Override
    public void registerProviders(PreviewProviderRegistry registry) {
        for (ReinforcingMaterial material : ReinforcingMaterials.MAP.values()) {
            register(registry, material.getName() + "_shulker_box", new ReinforcedShulkerBoxPreviewProvider(material),
                    REINFORCED_SHULKER_BOX_ITEMS_MAP.get(material));
        }
    }

    static {
        REINFORCED_SHULKER_BOX_ITEMS_MAP = new HashMap<>();
        for (ReinforcingMaterial material : ReinforcingMaterials.MAP.values()) {
            Item[] items = ModItems.REINFORCED_SHULKER_BOX_MAP.get(material).values().toArray(new Item[0]);
            REINFORCED_SHULKER_BOX_ITEMS_MAP.put(material, items);
        }
    }
}
