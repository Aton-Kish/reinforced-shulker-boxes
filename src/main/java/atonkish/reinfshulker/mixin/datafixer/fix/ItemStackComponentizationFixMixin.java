package atonkish.reinfshulker.mixin.datafixer.fix;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mojang.serialization.Dynamic;

import net.minecraft.util.datafix.fixes.ItemStackComponentizationFix;
import net.minecraft.world.item.DyeColor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfcore.util.ReinforcingMaterials;
import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;

@Mixin(ItemStackComponentizationFix.class)
public class ItemStackComponentizationFixMixin {
  @Inject(at = @At("RETURN"), method = "fixBlockEntityTag", cancellable = true)
  private static <T> void fixBlockEntityData(
      @Coerce Object data,
      Dynamic<T> dynamic,
      String blockEntityId,
      CallbackInfoReturnable<Dynamic<T>> cir) {
    Set<String> itemIds = new HashSet<>();
    for (ReinforcingMaterial material : ReinforcingMaterials.MAP.values()) {
      itemIds.add(
          String.format("%s:%s_shulker_box", ReinforcedShulkerBoxesMod.MOD_ID, material.getName()));
      for (DyeColor color : DyeColor.values()) {
        itemIds.add(
            String.format(
                "%s:%s_%s_shulker_box",
                ReinforcedShulkerBoxesMod.MOD_ID, color.getId(), material.getName()));
      }
    }

    if (itemMatches(data, itemIds)) {
      List<Dynamic<T>> list =
          dynamic
              .get("Items")
              .asList(
                  itemsDynamic ->
                      itemsDynamic
                          .emptyMap()
                          .set(
                              "slot",
                              itemsDynamic.createInt(
                                  itemsDynamic.get("Slot").asByte((byte) 0) & 255))
                          .set("item", itemsDynamic.remove("Slot")));
      if (!list.isEmpty()) {
        setComponent(data, "minecraft:container", dynamic.createList(list.stream()));
      }
      cir.setReturnValue(dynamic.remove("Items"));
    }
  }

  private static boolean itemMatches(Object data, Set<String> itemIds) {
    try {
      Method method = data.getClass().getDeclaredMethod("is", Set.class);
      method.setAccessible(true);
      return (boolean) method.invoke(data, itemIds);
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }

  private static <T> void setComponent(Object data, String id, Dynamic<T> value) {
    try {
      Method method =
          data.getClass().getDeclaredMethod("setComponent", String.class, Dynamic.class);
      method.setAccessible(true);
      method.invoke(data, id, value);
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }
}
