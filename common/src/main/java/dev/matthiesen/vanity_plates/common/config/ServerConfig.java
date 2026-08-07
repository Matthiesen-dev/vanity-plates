package dev.matthiesen.vanity_plates.common.config;

import com.electronwill.nightconfig.core.Config;
import dev.matthiesen.vanity_plates.common.config.def.PlateEntry;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;

public final class ServerConfig {

    public ModConfigSpec.IntValue prefixPriority;
    public ModConfigSpec.ConfigValue<List<? extends Config>> availablePlates;

    public ServerConfig(ModConfigSpec.Builder builder) {
        builder.comment("Vanity Plates Server Config")
                .push("vanity_plates");

        prefixPriority = builder.comment("Priority of the prefix when displayed in chat.")
                .defineInRange("prefixPriority", 1000, Integer.MIN_VALUE, Integer.MAX_VALUE);
        availablePlates = builder.comment(
                        "List of available vanity plates",
                        "Each entry should be a config object with the following fields:",
                        "- 'displayItem': The item to display in the GUI (string, e.g., 'minecraft:paper')",
                        "- 'label': The label to display in the GUI (string)",
                        "- 'requiredPermission': The permission required to use this plate (string)",
                        "- 'prefix': The prefix to display in chat (string)",
                        "- 'customModelData': Optional custom model data for the display item (integer, optional)"
                )
                .defineListAllowEmpty(
                        List.of("availablePlates"),
                        ServerConfig::getDefaultPlateEntries,
                        null,
                        o -> o instanceof Config
                );

        builder.pop();
    }

    public static List<? extends Config> getDefaultPlateEntries() {
        List<Config> defaultPlates = new ArrayList<>();
        PlateEntry defaultPlate = PlateEntry.DEMO_PLATE_ENTRY;
        Config demoPlate = defaultPlate.serialize();
        defaultPlates.add(demoPlate);
        return defaultPlates;
    }
}
