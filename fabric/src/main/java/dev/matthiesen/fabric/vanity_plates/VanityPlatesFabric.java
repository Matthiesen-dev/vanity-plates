package dev.matthiesen.fabric.vanity_plates;

import dev.matthiesen.common.vanity_plates.VanityPlates;
import dev.matthiesen.common.vanity_plates.Constants;
import net.fabricmc.api.ModInitializer;

public final class VanityPlatesFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Constants.createInfoLog("Loading for Fabric Mod Loader");
        VanityPlates.initialize();
    }
}
