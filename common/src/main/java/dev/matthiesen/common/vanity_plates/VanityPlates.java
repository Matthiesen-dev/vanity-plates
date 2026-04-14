package dev.matthiesen.common.vanity_plates;

import com.mojang.brigadier.CommandDispatcher;
import dev.matthiesen.common.vanity_plates.commands.CommandRegistry;
import dev.matthiesen.common.vanity_plates.config.ConfigManager;
import dev.matthiesen.common.vanity_plates.config.ModConfig;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;

public class VanityPlates {
    public static ModConfig config;

    public static void initialize() {
        Constants.createInfoLog("Initialized");
        config = new ConfigManager().loadConfig();
    }

    public static void reload() {
        config = new ConfigManager().loadConfig();
        Constants.createInfoLog("Reloaded Config");
    }

    public static void onStartup(MinecraftServer server) {
        Constants.createInfoLog("Server starting, Setting up");
    }

    public static void onShutdown() {
        Constants.createInfoLog("Server stopping, shutting down");
        new ConfigManager().saveConfig();
    }

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registry, Commands.CommandSelection context) {
        Constants.createInfoLog("Registering Commands");
        CommandRegistry.init(dispatcher, registry, context);
    }
}
