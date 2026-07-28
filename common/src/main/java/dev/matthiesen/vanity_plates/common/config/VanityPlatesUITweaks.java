package dev.matthiesen.vanity_plates.common.config;

import com.google.gson.annotations.SerializedName;
import dev.matthiesen.vanity_plates.common.util.Color;

public final class VanityPlatesUITweaks {
    @SerializedName("text")
    public Text text = new Text();

    @SerializedName("displayItems")
    public DisplayItems displayItems = new DisplayItems();

    @SerializedName("colors")
    public Colors colors = new Colors();

    @SerializedName("backButton")
    public BackButton backButton = new BackButton();

    @SerializedName("permissions")
    public Permissions permissions = new Permissions();

    public static class Text {
        @SerializedName("displayTitle")
        public String displayTitle = "Vanity Plates";

        @SerializedName("clearPrefix")
        public String clearPrefix = "Clear Prefix";

        @SerializedName("exit")
        public String exit = "Exit";

        @SerializedName("previousPage")
        public String previousPage = "Previous Page";

        @SerializedName("nextPage")
        public String nextPage = "Next Page";

        @SerializedName("pageIndicator")
        public String pageIndicator = "Page %current%/%length%";
    }

    public static class DisplayItems {
        @SerializedName("frameItemId")
        public String frameItemId = "minecraft:gray_stained_glass_pane";

        @SerializedName("prevNavigationItemId")
        public String prevNavigationItemId = "minecraft:arrow";

        @SerializedName("nextNavigationItemId")
        public String nextNavigationItemId = "minecraft:arrow";

        @SerializedName("clearItemId")
        public String clearItemId = "minecraft:name_tag";

        @SerializedName("pageItemId")
        public String pageItemId = "minecraft:book";

        @SerializedName("exitItemId")
        public String exitItemId = "minecraft:barrier";
    }

    public static class Colors {
        @SerializedName("title")
        public Color title = Color.GOLD;

        @SerializedName("navigationItem")
        public Color navigationItem = Color.AQUA;

        @SerializedName("clearItem")
        public Color clearItem = Color.RED;

        @SerializedName("pageItem")
        public Color pageItem = Color.GOLD;

        @SerializedName("exitItem")
        public Color exitItem = Color.RED;
    }

    public static class Permissions {
        @SerializedName("clearPrefixUiButton")
        public String clearPrefixUiButton = "";
    }

    public static class BackButton {
        @SerializedName("enabled")
        public boolean enabled = false;

        @SerializedName("itemId")
        public String itemId = "minecraft:recovery_compass";

        @SerializedName("label")
        public String label = "Back to Previous Menu";

        @SerializedName("textColor")
        public Color textColor = Color.RED;

        @SerializedName("command")
        public String command = "gui open example-main %player%"; // Available placeholders: %player% - gets player's name, %uuid% get's player's UUID
    }
}
