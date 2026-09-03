package dev.matthiesen.vanity_plates.common;

import dev.matthiesen.libs.faststats.Token;
import dev.matthiesen.matthiesen_core.common.AbstractCommonMod;
import dev.matthiesen.matthiesen_core.common.api.platform.loader.ModConfigType;
import dev.matthiesen.vanity_plates.common.commands.VanityCommand;
import dev.matthiesen.vanity_plates.common.config.VPConfig;
import org.jetbrains.annotations.NotNull;

public final class VanityPlates extends AbstractCommonMod {
    public static final String MOD_ID = "vanity_plates";
    private static final String MOD_NAME = "Vanity Plates";
    private static @Token final String METRICS_TOKEN = "15f018eba784241058551101acde151d";

    public static final VanityPlates INSTANCE = new VanityPlates();

    public static String modConfig(String name) {
        return "vanity_plates/" + name + ".toml";
    }

    public VanityPlates() {
        super(MOD_ID, MOD_NAME);
    }

    @Override
    public void initialize() {
        super.initialize();
        registerModConfig(MOD_ID, ModConfigType.SERVER, VPConfig.SERVER_SPEC, modConfig("server"));
        registerModConfig(MOD_ID, ModConfigType.SERVER, VPConfig.GUI_SPEC, modConfig("gui"));

        getCommandsRegistryManager().registerCommand(VanityCommand.CMD);
        createInfoLog("Initialized");
    }

    @Override
    public @Token @NotNull String getMetricsToken() {
        return METRICS_TOKEN;
    }
}
