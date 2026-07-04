package atonkish.reinfshulker.integration.modmenu;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;

import atonkish.reinfcore.ReinforcedCoreConfig;
import atonkish.reinfcore.ReinforcedCoreMod;
import atonkish.reinfcore.util.ReinforcedStorageScreenType;

@Environment(EnvType.CLIENT)
public class ModMenu implements ModMenuApi {
  @Override
  public ConfigScreenFactory<?> getModConfigScreenFactory() {
    return parent -> {
      ensureConfigRegistered();
      return createConfigScreen(parent);
    };
  }

  private static Screen createConfigScreen(Screen parent) {
    ConfigBuilder builder =
        ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(Component.translatable("text.autoconfig.reinfcore.title"));

    ConfigCategory category =
        builder.getOrCreateCategory(Component.translatable("text.autoconfig.reinfcore.title"));

    ConfigEntryBuilder entryBuilder = builder.entryBuilder();

    category.addEntry(
        entryBuilder
            .startEnumSelector(
                Component.translatable("text.autoconfig.reinfcore.option.screenType"),
                ReinforcedStorageScreenType.class,
                ReinforcedCoreMod.CONFIG.screenType)
            .setDefaultValue(ReinforcedStorageScreenType.SCROLL)
            .setSaveConsumer(value -> ReinforcedCoreMod.CONFIG.screenType = value)
            .build());

    category.addEntry(
        entryBuilder
            .startIntSlider(
                Component.translatable("text.autoconfig.reinfcore.option.scrollScreen.rows"),
                ReinforcedCoreMod.CONFIG.scrollScreen.rows,
                6,
                9)
            .setDefaultValue(6)
            .setSaveConsumer(value -> ReinforcedCoreMod.CONFIG.scrollScreen.rows = value)
            .build());

    builder.setSavingRunnable(
        () -> {
          ensureConfigRegistered();
          AutoConfig.getConfigHolder(ReinforcedCoreConfig.class).save();
        });

    return builder.build();
  }

  private static void ensureConfigRegistered() {
    try {
      ReinforcedCoreMod.CONFIG = AutoConfig.getConfigHolder(ReinforcedCoreConfig.class).getConfig();
    } catch (RuntimeException exception) {
      AutoConfig.register(ReinforcedCoreConfig.class, GsonConfigSerializer::new);
      ReinforcedCoreMod.CONFIG = AutoConfig.getConfigHolder(ReinforcedCoreConfig.class).getConfig();
    }
  }
}
