package dev.matthiesen.vanity_plates.fabric;

import dev.matthiesen.vanity_plates.common.VanityPlates;
import net.fabricmc.api.ModInitializer;

public final class VanityPlatesFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        VanityPlates.INSTANCE.createInfoLog("Loading for Fabric Mod Loader");
        VanityPlates.INSTANCE.initialize();
    }
}
