package dev.matthiesen.neoforge.vanity_plates;

import dev.matthiesen.common.vanity_plates.VanityPlates;
import dev.matthiesen.common.vanity_plates.Constants;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public final class VanityPlatesNeoForge {
    public VanityPlatesNeoForge() {
        Constants.createInfoLog("Loading for NeoForge Mod Loader");
        VanityPlates.initialize();
    }
}
