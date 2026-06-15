package dev.matthiesen.vanity_plates.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class VanityPlatesConfig {
    @SerializedName("prefixPriority")
    public int prefixPriority = 1000;

    @SerializedName("availablePlates")
    public List<PlateEntry> availablePlates = List.of(demoPlate);

    private static final PlateEntry demoPlate = new PlateEntry().create("minecraft:paper", "Demo", "demo.plate", "[Demo]");

    public static class PlateEntry {
        @SerializedName("displayItem")
        public String displayItem;

        @SerializedName("customModelData")
        public @Nullable Integer customModelData;

        @SerializedName("label")
        public String label;

        @SerializedName("requiredPermission")
        public String requiredPermission;

        @SerializedName("prefix")
        public String prefix;

        public PlateEntry create(String displayItem, String label, String requiredPermission, String prefix, @Nullable Integer customModelData) {
            PlateEntry item = new PlateEntry();
            item.displayItem = displayItem;
            item.customModelData = customModelData;
            item.label = label;
            item.requiredPermission = requiredPermission;
            item.prefix = prefix;
            return item;
        }

        public PlateEntry create(String displayItem, String label, String requiredPermission, String prefix) {
            return create(displayItem, label, requiredPermission, prefix, null);
        }
    }

    @SuppressWarnings("unused")
    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();
}
