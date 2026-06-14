package dev.matthiesen.common.vanity_plates.util;

import dev.matthiesen.common.vanity_plates.Constants;
import net.minecraft.network.chat.Component;

public final class TextUtils {
    public static Component parse(String text) {
        if (text == null || text.isEmpty()) return Component.empty();
        try {
            text = text.replace('&', '§');
            return Component.literal(text);
        } catch (Exception e) {
            Constants.createErrorLog("Failed to parse component: " + text, e);
            return Component.literal(text);
        }
    }
}
