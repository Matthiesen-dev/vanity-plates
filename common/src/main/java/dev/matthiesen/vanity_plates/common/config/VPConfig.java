package dev.matthiesen.vanity_plates.common.config;

import dev.matthiesen.vanity_plates.common.config.def.PlateEntry;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public final class VPConfig {
    public static final ServerConfig SERVER_CONFIG;
    public static final ModConfigSpec SERVER_SPEC;

    public static final GUIServerConfig GUI_CONFIG;
    public static final ModConfigSpec GUI_SPEC;

    static {
        Pair<ServerConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(ServerConfig::new);
        SERVER_CONFIG = specPair.getLeft();
        SERVER_SPEC = specPair.getRight();

        Pair<GUIServerConfig, ModConfigSpec> guiSpecPair = new ModConfigSpec.Builder().configure(GUIServerConfig::new);
        GUI_CONFIG = guiSpecPair.getLeft();
        GUI_SPEC = guiSpecPair.getRight();
    }

    private static List<PlateEntry> availablePlatesCache = new ArrayList<>();

    public static List<PlateEntry> getAvailablePlates() {
        if (!availablePlatesCache.isEmpty()) {
            return availablePlatesCache;
        }
        List<PlateEntry> entries = SERVER_CONFIG.availablePlates.get()
                .stream()
                .map(PlateEntry::deserialize)
                .toList();
        availablePlatesCache = entries;
        return entries;
    }
}
