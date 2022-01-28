package atonkish.reinfshulker.integration.quickshulker;

import java.util.function.BiConsumer;

import net.kyrptonaught.quickshulker.api.QuickOpenableRegistry;
import net.kyrptonaught.quickshulker.api.RegisterQuickShulker;
import net.kyrptonaught.shulkerutils.ItemStackInventory;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import atonkish.reinfcore.screen.ReinforcedStorageScreenHandler;
import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.block.ReinforcedShulkerBoxBlock;
import atonkish.reinfshulker.block.entity.ModBlockEntityType;

public class QuickShulker implements RegisterQuickShulker {
    private static BiConsumer<PlayerEntity, ItemStack> REINFORCED_SHULKER_BOX_CONSUMER;

    @Override
    public void registerProviders() {
        QuickOpenableRegistry.register(ReinforcedShulkerBoxBlock.class, REINFORCED_SHULKER_BOX_CONSUMER);
    }

    static {
        REINFORCED_SHULKER_BOX_CONSUMER = (PlayerEntity player, ItemStack stack) -> {
            ReinforcedShulkerBoxBlock block = (ReinforcedShulkerBoxBlock) ((BlockItem) stack.getItem()).getBlock();
            ReinforcingMaterial material = block.getMaterial();
            ItemStackInventory inventory = new ItemStackInventory(stack, material.getSize());
            String namespace = BlockEntityType.getId(ModBlockEntityType.REINFORCED_SHULKER_BOX_MAP.get(material))
                    .getNamespace();

            ScreenHandlerFactory screenHandlerFactory = (int syncId, PlayerInventory playerInventory,
                    PlayerEntity playerEntity) -> ReinforcedStorageScreenHandler.createShulkerBoxScreen(material,
                            syncId, playerInventory, inventory);
            Text text = stack.hasCustomName() ? stack.getName()
                    : new TranslatableText(
                            "container." + namespace + "." + material.getName() + "ShulkerBox");

            player.openHandledScreen(new SimpleNamedScreenHandlerFactory(screenHandlerFactory, text));
        };
    }
}
