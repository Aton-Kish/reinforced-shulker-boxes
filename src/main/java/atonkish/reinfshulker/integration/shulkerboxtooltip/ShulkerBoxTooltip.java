package atonkish.reinfshulker.integration.shulkerboxtooltip;

import com.misterpemodder.shulkerboxtooltip.api.ShulkerBoxTooltipApi;
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProvider;
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProviderRegistry;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfcore.util.ReinforcingMaterials;
import atonkish.reinfshulker.block.entity.ModBlockEntityType;
import atonkish.reinfshulker.item.ModItems;

public class ShulkerBoxTooltip implements ShulkerBoxTooltipApi {
    private static void register(PreviewProviderRegistry registry, String namespace, String id,
            PreviewProvider provider, Item... items) {
        registry.register(new Identifier(namespace, id), provider, items);
    }

    @Override
    public void registerProviders(PreviewProviderRegistry registry) {
        for (ReinforcingMaterial material : ReinforcingMaterials.MAP.values()) {
            String namespace = BlockEntityType.getId(ModBlockEntityType.REINFORCED_SHULKER_BOX_MAP.get(material))
                    .getNamespace();
            String id = material.getName() + "_shulker_box";
            Item[] items = ModItems.REINFORCED_SHULKER_BOX_MAP.get(material).values().toArray(new Item[0]);
            register(registry, namespace, id, new ReinforcedShulkerBoxPreviewProvider(material), items);
        }
    }
}
