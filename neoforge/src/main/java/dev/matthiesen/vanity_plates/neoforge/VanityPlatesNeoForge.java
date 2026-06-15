package dev.matthiesen.vanity_plates.neoforge;

import dev.matthiesen.vanity_plates.common.VanityPlates;
import net.neoforged.fml.common.Mod;

@Mod(VanityPlates.MOD_ID)
public final class VanityPlatesNeoForge {
    public VanityPlatesNeoForge() {
        VanityPlates.INSTANCE.createInfoLog("Loading for NeoForge Mod Loader");
        VanityPlates.INSTANCE.initialize();
    }
}
