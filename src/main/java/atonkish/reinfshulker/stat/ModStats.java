package atonkish.reinfshulker.stat;

import java.util.HashMap;

import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;

public class ModStats {
    public static final HashMap<ReinforcingMaterial, Identifier> CLEAN_REINFORCED_SHULKER_BOX_MAP;
    public static final HashMap<ReinforcingMaterial, Identifier> OPEN_REINFORCED_SHULKER_BOX_MAP;

    public static void init() {
    }

    private static Identifier register(String id, StatFormatter formatter) {
        Identifier identifier = new Identifier(ReinforcedShulkerBoxesMod.MOD_ID, id);
        Registry.register(Registry.CUSTOM_STAT, id, identifier);
        Stats.CUSTOM.getOrCreateStat(identifier, formatter);
        return identifier;
    }

    static {
        CLEAN_REINFORCED_SHULKER_BOX_MAP = new HashMap<>();
        OPEN_REINFORCED_SHULKER_BOX_MAP = new HashMap<>();
        for (ReinforcingMaterial material : ReinforcingMaterial.values()) {
            Identifier cleanIdentifier = register("clean_" + material.getName() + "_shulker_box",
                    StatFormatter.DEFAULT);
            Identifier openIdentifier = register("open_" + material.getName() + "_shulker_box", StatFormatter.DEFAULT);
            CLEAN_REINFORCED_SHULKER_BOX_MAP.put(material, cleanIdentifier);
            OPEN_REINFORCED_SHULKER_BOX_MAP.put(material, openIdentifier);
        }
    }
}
