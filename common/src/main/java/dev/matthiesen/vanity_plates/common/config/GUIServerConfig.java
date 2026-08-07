package dev.matthiesen.vanity_plates.common.config;

import dev.matthiesen.vanity_plates.common.util.Color;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class GUIServerConfig {

    // UI Text
    public ModConfigSpec.ConfigValue<String> displayTitle;
    public ModConfigSpec.ConfigValue<String> clearPrefix;
    public ModConfigSpec.ConfigValue<String> exit;
    public ModConfigSpec.ConfigValue<String> previousPage;
    public ModConfigSpec.ConfigValue<String> nextPage;
    public ModConfigSpec.ConfigValue<String> pageIndicator;

    // UI Display Items
    public ModConfigSpec.ConfigValue<String> frameItemId;
    public ModConfigSpec.ConfigValue<String> prevNavigationItemId;
    public ModConfigSpec.ConfigValue<String> nextNavigationItemId;
    public ModConfigSpec.ConfigValue<String> clearItemId;
    public ModConfigSpec.ConfigValue<String> pageItemId;
    public ModConfigSpec.ConfigValue<String> exitItemId;

    // UI Colors
    public ModConfigSpec.EnumValue<Color> titleColor;
    public ModConfigSpec.EnumValue<Color> navigationItemColor;
    public ModConfigSpec.EnumValue<Color> clearItemColor;
    public ModConfigSpec.EnumValue<Color> pageItemColor;
    public ModConfigSpec.EnumValue<Color> exitItemColor;

    // UI Optional Back button
    public ModConfigSpec.BooleanValue backButton_enabled;
    public ModConfigSpec.ConfigValue<String> backButton_itemId;
    public ModConfigSpec.ConfigValue<String> backButton_label;
    public ModConfigSpec.EnumValue<Color> backButton_color;
    public ModConfigSpec.ConfigValue<String> backButton_command;

    // UI Permissions
    public ModConfigSpec.ConfigValue<String> clearPrefixUiButtonPermission;

    public GUIServerConfig(ModConfigSpec.Builder builder) {
        builder.comment("GUI Configuration").push("gui");

        builder.comment("GUI Text").push("text");
        displayTitle = builder.comment("Title of the GUI")
                .define("displayTitle", "Vanity Plates");
        clearPrefix = builder.comment("Text for the 'Clear Prefix' button")
                .define("clearPrefix", "Clear Prefix");
        exit = builder.comment("Text for the 'Exit' button")
                .define("exit", "Exit");
        previousPage = builder.comment("Text for the 'Previous Page' button")
                .define("previousPage", "Previous Page");
        nextPage = builder.comment("Text for the 'Next Page' button")
                .define("nextPage", "Next Page");
        pageIndicator = builder.comment("Text for the page indicator, use %current% and %length% as placeholders for the current page and total pages")
                .define("pageIndicator", "Page %current%/%length%");
        builder.pop(); // Closes "gui.text"

        builder.comment("GUI Display Items").push("displayItems");
        frameItemId = builder.comment("Item ID for the frame item")
                .define("frameItemId", "minecraft:gray_stained_glass_pane");
        prevNavigationItemId = builder.comment("Item ID for the 'Previous Page' button")
                .define("prevNavigationItemId", "minecraft:arrow");
        nextNavigationItemId = builder.comment("Item ID for the 'Next Page' button")
                .define("nextNavigationItemId", "minecraft:arrow");
        clearItemId = builder.comment("Item ID for the 'Clear Prefix' button")
                .define("clearItemId", "minecraft:name_tag");
        pageItemId = builder.comment("Item ID for the page indicator")
                .define("pageItemId", "minecraft:book");
        exitItemId = builder.comment("Item ID for the 'Exit' button")
                .define("exitItemId", "minecraft:barrier");
        builder.pop(); // Closes "gui.displayItems"

        builder.comment("GUI Colors").push("colors");
        titleColor = builder.comment("Color for the GUI title")
                .defineEnum("titleColor", Color.GOLD);
        navigationItemColor = builder.comment("Color for the navigation items")
                .defineEnum("navigationItemColor", Color.AQUA);
        clearItemColor = builder.comment("Color for the 'Clear Prefix' button")
                .defineEnum("clearItemColor", Color.RED);
        pageItemColor = builder.comment("Color for the page indicator")
                .defineEnum("pageItemColor", Color.GOLD);
        exitItemColor = builder.comment("Color for the 'Exit' button")
                .defineEnum("exitItemColor", Color.RED);
        builder.pop(); // Closes "gui.colors"

        builder.comment("GUI Back button Configuration").push("backButton");
        backButton_enabled = builder.comment("Enable or disable the back button in the GUI")
                .define("enabled", false);
        backButton_itemId = builder.comment("Item ID for the back button")
                .define("itemId", "minecraft:recovery_compass");
        backButton_label = builder.comment("Label for the back button")
                .define("label", "Back to Previous Menu");
        backButton_color = builder.comment("Color for the back button")
                .defineEnum("color", Color.RED);
        backButton_command = builder.comment("Command to execute when the back button is pressed")
                .define("command", "gui open example-main %player%");
        builder.pop(); // Closes "gui.backButton"

        builder.comment("GUI Permissions").push("permissions");
        clearPrefixUiButtonPermission = builder.comment("Permission required to see the 'Clear Prefix' button in the GUI")
                .define("clearPrefixUiButtonPermission", "");
        builder.pop(); // Closes "gui.permissions"

        builder.pop(); // Closes "gui"
    }
}
