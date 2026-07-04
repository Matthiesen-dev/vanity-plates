package dev.matthiesen.vanity_plates.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import dev.matthiesen.vanity_plates.common.util.Color;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class VanityPlatesConfig {
    @SerializedName("prefixPriority")
    public int prefixPriority = 1000;

    @SerializedName("availablePlates")
    public List<PlateEntry> availablePlates = List.of(demoPlate);

    @SerializedName("uiConfig")
    public UiConfig uiConfig = new UiConfig();

    @SerializedName("permissions")
    public Permissions permissions = new Permissions();

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

    public static class UiConfig {
        @SerializedName("displayTitle")
        public String displayTitle = "Vanity Plates";

        @SerializedName("titleColor")
        public Color titleColor = Color.GOLD;

        @SerializedName("frameItemId")
        public String frameItemId = "minecraft:gray_stained_glass_pane";

        @SerializedName("navigationItemId")
        public String navigationItemId = "minecraft:arrow";

        @SerializedName("navigationItemTextColor")
        public Color navigationItemTextColor = Color.AQUA;

        @SerializedName("clearItemId")
        public String clearItemId = "minecraft:name_tag";

        @SerializedName("clearItemTextColor")
        public Color clearItemTextColor = Color.RED;

        @SerializedName("pageItemId")
        public String pageItemId = "minecraft:book";

        @SerializedName("pageItemTextColor")
        public Color pageItemTextColor = Color.GOLD;

        @SerializedName("exitItemId")
        public String exitItemId = "minecraft:barrier";

        @SerializedName("exitItemTextColor")
        public Color exitItemTextColor = Color.RED;
    }

    public static class Permissions {
        @SerializedName("clearPrefix")
        public String clearPrefix = "";
    }

    @SuppressWarnings("unused")
    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();
}
