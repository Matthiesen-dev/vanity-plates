package dev.matthiesen.vanity_plates.common.config.def;

import com.electronwill.nightconfig.core.Config;
import org.jetbrains.annotations.Nullable;

public final class PlateEntry {
    public String displayItem;
    public String label;
    public String requiredPermission;
    public String prefix;
    public @Nullable Integer customModelData;

    public PlateEntry(String displayItem, String label, String requiredPermission, String prefix, @Nullable Integer customModelData) {
        this.displayItem = displayItem;
        this.customModelData = customModelData;
        this.label = label;
        this.requiredPermission = requiredPermission;
        this.prefix = prefix;
    }

    public PlateEntry(String displayItem, String label, String requiredPermission, String prefix) {
        this(displayItem, label, requiredPermission, prefix, null);
    }

    public static PlateEntry DEMO_PLATE_ENTRY =
            new PlateEntry("minecraft:paper", "Demo", "demo.plate", "[Demo]");

    public static PlateEntry deserialize(Config config) {
        String displayItem = config.get("displayItem");
        String label = config.get("label");
        String requiredPermission = config.get("requiredPermission");
        String prefix = config.get("prefix");
        @Nullable Integer customModelData = config.getIntOrElse("customModelData", null);
        return new PlateEntry(displayItem, label, requiredPermission, prefix, customModelData);
    }

    public Config serialize() {
        Config config = Config.inMemory();
        config.set("displayItem", this.displayItem);
        config.set("label", this.label);
        config.set("requiredPermission", this.requiredPermission);
        config.set("prefix", this.prefix);
        if (this.customModelData != null) {
            config.set("customModelData", this.customModelData);
        }
        return config;
    }
}