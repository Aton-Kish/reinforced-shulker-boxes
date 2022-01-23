package atonkish.reinfshulker.stat;

import java.util.LinkedHashMap;

import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import atonkish.reinfcore.util.ReinforcingMaterial;

public class ModStats {
    public static final LinkedHashMap<ReinforcingMaterial, Identifier> CLEAN_REINFORCED_SHULKER_BOX_MAP = new LinkedHashMap<>();
    public static final LinkedHashMap<ReinforcingMaterial, Identifier> OPEN_REINFORCED_SHULKER_BOX_MAP = new LinkedHashMap<>();

    public static Identifier registerMaterialOpen(String namespace, ReinforcingMaterial material) {
        String id = "clean_" + material.getName() + "_shulker_box";
        Identifier identifier = register(namespace, id, StatFormatter.DEFAULT);

        CLEAN_REINFORCED_SHULKER_BOX_MAP.put(material, identifier);

        return identifier;
    }

    public static Identifier registerMaterialClean(String namespace, ReinforcingMaterial material) {
        String id = "open_" + material.getName() + "_shulker_box";
        Identifier identifier = register(namespace, id, StatFormatter.DEFAULT);

        OPEN_REINFORCED_SHULKER_BOX_MAP.put(material, identifier);

        return identifier;
    }

    private static Identifier register(String namespace, String id, StatFormatter formatter) {
        Identifier identifier = new Identifier(namespace, id);
        Registry.register(Registry.CUSTOM_STAT, id, identifier);
        Stats.CUSTOM.getOrCreateStat(identifier, formatter);
        return identifier;
    }
}
