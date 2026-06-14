package dev.matthiesen.common.vanity_plates;

import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.config.ConfigManager;
import dev.matthiesen.common.vanity_plates.config.VanityPlatesConfig;
import dev.matthiesen.common.vanity_plates.registry.CommandRegistry;
import dev.matthiesen.common.vanity_plates.util.MetricManager;

public final class VanityPlates {
    private static final ConfigManager<VanityPlatesConfig> CONFIG_MANAGER =
            new ConfigManager<>(VanityPlatesConfig.class, "config", Constants.MOD_ID);

    public static void initialize() {
        MetricManager.init();
        reload();
        CommandRegistry.init();

        MatthiesenLibApi.registerReloadRunnable(Constants.MOD_ID, VanityPlates::reload);
        Constants.createInfoLog("Initialized");
    }

    public static void reload() {
        CONFIG_MANAGER.loadConfig();
        Constants.createInfoLog("Reloaded Config");
    }

    public static VanityPlatesConfig getConfig() {
        return CONFIG_MANAGER.getConfig();
    }
}
