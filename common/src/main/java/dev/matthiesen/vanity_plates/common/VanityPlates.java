package dev.matthiesen.vanity_plates.common;

import dev.matthiesen.common.matthiesen_lib_api.abstracts.AbstractCommonMod;
import dev.matthiesen.common.matthiesen_lib_api.config.ConfigManager;
import dev.matthiesen.libs.faststats.Token;
import dev.matthiesen.vanity_plates.common.commands.VanityCommand;
import dev.matthiesen.vanity_plates.common.config.VanityPlatesConfig;
import org.jetbrains.annotations.Nullable;

public class VanityPlates extends AbstractCommonMod {
    public static final String MOD_ID = "vanity_plates";
    private static final String MOD_NAME = "Vanity Plates";
    private static @Token final String METRICS_TOKEN = "15f018eba784241058551101acde151d";

    private final ConfigManager<VanityPlatesConfig> CONFIG_MANAGER =
            createConfigManager(VanityPlatesConfig.class, "config");

    public static final VanityPlates INSTANCE = new VanityPlates();

    public VanityPlates() {
        super(MOD_ID, MOD_NAME);
    }

    @Override
    public void initialize() {
        super.initialize();
        reload().run();

        registerCommand(VanityCommand.CMD);
        createInfoLog("Initialized");
    }

    @Override
    public @Nullable @Token String getMetricsToken() {
        return METRICS_TOKEN;
    }

    @Override
    public Runnable reload() {
        return () -> {
            CONFIG_MANAGER.loadConfig();
            createInfoLog("Reloaded Config");
        };
    }

    public VanityPlatesConfig getConfig() {
        return CONFIG_MANAGER.getConfig();
    }
}
