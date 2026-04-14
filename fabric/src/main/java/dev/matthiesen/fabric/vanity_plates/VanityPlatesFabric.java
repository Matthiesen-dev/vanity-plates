package dev.matthiesen.fabric.vanity_plates;

import dev.matthiesen.common.vanity_plates.VanityPlates;
import dev.matthiesen.common.vanity_plates.Constants;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class VanityPlatesFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Constants.createInfoLog("Loading for Fabric Mod Loader");
        VanityPlates.initialize();
        CommandRegistrationCallback.EVENT.register(VanityPlates::registerCommands);
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            MinecraftServer runningServer = server.createCommandSourceStack().getServer();
            VanityPlates.onStartup(runningServer);
        });
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> VanityPlates.onShutdown());
    }

}
