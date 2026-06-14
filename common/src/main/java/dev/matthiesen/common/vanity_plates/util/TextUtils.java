package dev.matthiesen.common.vanity_plates.util;

import dev.matthiesen.common.vanity_plates.Constants;
import net.minecraft.network.chat.Component;

public final class TextUtils {
    public static Component parse(String text) {
        text = convertColorCodes(text);
        return parseMinecraftComponent(text);
    }

    public static String convertColorCodes(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.replace('&', '§');
    }

    public static Component parseMinecraftComponent(String text) {
        if (text == null || text.isEmpty()) return Component.empty();

        try {
            return Component.literal(text);
        } catch (Exception e) {
            Constants.LOGGER.warn("Failed to parse component: {}", text, e);
            return Component.literal(text);
        }
    }
}
