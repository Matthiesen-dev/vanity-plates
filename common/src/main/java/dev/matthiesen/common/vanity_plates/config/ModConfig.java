package dev.matthiesen.common.vanity_plates.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import dev.matthiesen.common.vanity_plates.Constants;

import java.util.List;

public class ModConfig {
    @SerializedName("permissionlevels")
    public PermissionLevels permissionLevels = new PermissionLevels();

    public static class PermissionLevels {
        @SerializedName("command.example")
        public int COMMAND_EXAMPLE_PERMISSION_LEVEL = 4;
    }

    @SerializedName("prefixPriority")
    public int prefixPriority = 1000;

    @SerializedName("availablePlates")
    public List<PlateEntry> availablePlates = List.of(demoPlate);

    private static final PlateEntry demoPlate = new PlateEntry().create("minecraft:paper", "Demo", "demo.plate", "[Demo]");

    public static class PlateEntry {
        @SerializedName("displayItem")
        public String displayItem;

        @SerializedName("label")
        public String label;

        @SerializedName("requiredPermission")
        public String requiredPermission;

        @SerializedName("prefix")
        public String prefix;

        public PlateEntry create(String displayItem, String label, String requiredPermission, String prefix) {
            PlateEntry item = new PlateEntry();
            item.displayItem = displayItem;
            item.label = label;
            item.requiredPermission = requiredPermission;
            item.prefix = prefix;
            return item;
        }
    }

    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();
}
