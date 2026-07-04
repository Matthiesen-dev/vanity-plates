package dev.matthiesen.vanity_plates.common.util;

import net.minecraft.ChatFormatting;

public enum Color {
    BLACK("BLACK"),
    DARK_BLUE("DARK_BLUE"),
    DARK_GREEN("DARK_GREEN"),
    DARK_AQUA("DARK_AQUA"),
    DARK_RED("DARK_RED"),
    DARK_PURPLE("DARK_PURPLE"),
    GOLD("GOLD"),
    GRAY("GRAY"),
    DARK_GRAY("DARK_GRAY"),
    BLUE("BLUE"),
    GREEN("GREEN"),
    AQUA("AQUA"),
    RED("RED"),
    LIGHT_PURPLE("LIGHT_PURPLE"),
    YELLOW("YELLOW"),
    WHITE("WHITE");

    private final String name;

    Color(String name) {
        this.name = name;
    }

    public ChatFormatting toMcFormatting() {
        return ChatFormatting.getByName(name);
    }
}
