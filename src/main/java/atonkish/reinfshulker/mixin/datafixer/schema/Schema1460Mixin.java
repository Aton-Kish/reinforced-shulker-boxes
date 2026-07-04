package atonkish.reinfshulker.mixin.datafixer.schema;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;

import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.V1460;
import net.minecraft.world.item.DyeColor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;

@Mixin(V1460.class)
public class Schema1460Mixin {
  @Inject(at = @At("RETURN"), method = "registerBlockEntities", cancellable = true)
  private void registerBlockEntities(
      Schema schema, CallbackInfoReturnable<Map<String, Supplier<TypeTemplate>>> cir) {
    Map<String, Supplier<TypeTemplate>> map = cir.getReturnValue();

    // TODO: materials should be able to be resolved dynamically.
    for (String material : List.of("copper", "iron", "gold", "diamond", "netherite")) {
      schema.register(
          map,
          String.format("%s:%s_shulker_box", ReinforcedShulkerBoxesMod.MOD_ID, material),
          () -> {
            return DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)));
          });
      for (DyeColor color : DyeColor.values()) {
        schema.register(
            map,
            String.format(
                "%s:%s_%s_shulker_box",
                ReinforcedShulkerBoxesMod.MOD_ID, color.getName(), material),
            () -> {
              return DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)));
            });
      }
    }
  }
}
