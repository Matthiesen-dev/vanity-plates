package dev.matthiesen.vanity_plates.common;

import dev.matthiesen.libs.faststats.Token;
import dev.matthiesen.matthiesen_core.common.AbstractCommonMod;
import dev.matthiesen.matthiesen_core.common.api.events.PlatformEvents;
import dev.matthiesen.matthiesen_core.common.utility.config.ConfigManager;
import dev.matthiesen.vanity_plates.common.commands.VanityCommand;
import dev.matthiesen.vanity_plates.common.config.VanityPlatesConfig;
import dev.matthiesen.vanity_plates.common.config.VanityPlatesUITweaks;
import org.jetbrains.annotations.NotNull;

public final class VanityPlates extends AbstractCommonMod {
    public static final String MOD_ID = "vanity_plates";
    private static final String MOD_NAME = "Vanity Plates";
    private static @Token final String METRICS_TOKEN = "15f018eba784241058551101acde151d";

    private final ConfigManager<VanityPlatesConfig> CONFIG_MANAGER =
            createConfigManager(VanityPlatesConfig.class, "config");
    private final ConfigManager<VanityPlatesUITweaks> UI_CONFIG_MANAGER =
            createConfigManager(VanityPlatesUITweaks.class, "ui");

    public static final VanityPlates INSTANCE = new VanityPlates();

    public VanityPlates() {
        super(MOD_ID, MOD_NAME);
    }

    @Override
    public void initialize() {
        super.initialize();
        reload().run();

        getCommandsRegistryManager().registerCommand(VanityCommand.CMD);

        PlatformEvents.SERVER_RELOAD.subscribe(event -> reload().run());

        createInfoLog("Initialized");
    }

    @Override
    public @Token @NotNull String getMetricsToken() {
        return METRICS_TOKEN;
    }

    public Runnable reload() {
        return () -> {
            CONFIG_MANAGER.loadConfig();
            UI_CONFIG_MANAGER.loadConfig();
            createInfoLog("Reloaded Config");
        };
    }

    public VanityPlatesConfig getConfig() {
        return CONFIG_MANAGER.getConfig();
    }

    public VanityPlatesUITweaks getUiConfig() {
        return UI_CONFIG_MANAGER.getConfig();
    }
}
