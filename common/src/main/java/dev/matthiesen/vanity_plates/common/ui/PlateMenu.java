package dev.matthiesen.vanity_plates.common.ui;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import ca.landonjw.gooeylibs2.api.button.linked.LinkType;
import ca.landonjw.gooeylibs2.api.button.linked.LinkedPageButton;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.LinkedPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.slot.TemplateSlotDelegate;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import dev.matthiesen.common.matthiesen_lib_api.utility.ItemBuilder;
import dev.matthiesen.common.matthiesen_lib_api.utility.RunSlashCommand;
import dev.matthiesen.vanity_plates.common.VanityPlates;
import dev.matthiesen.vanity_plates.common.config.VanityPlatesConfig;
import dev.matthiesen.vanity_plates.common.config.VanityPlatesUITweaks;
import dev.matthiesen.vanity_plates.common.util.Decoder;
import dev.matthiesen.vanity_plates.common.util.LPHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class PlateMenu {
    public ServerPlayer player;

    public PlateMenu(ServerPlayer player) {
        this.player = player;
    }

    public VanityPlatesUITweaks getUiConfig() {
        return VanityPlates.INSTANCE.getUiConfig();
    }

    public VanityPlatesUITweaks.Permissions getPermsConfig() {
        return VanityPlates.INSTANCE.getUiConfig().permissions;
    }

    public Component getDisplayTitle() {
        return Component.literal(getUiConfig().displayTitle)
                .withStyle(style ->
                        style.withColor(getUiConfig().colors.title.toMcFormatting())
                                .withBold(true)
                );
    }

    public ItemStack getFrameItem() {
        return new ItemBuilder(Decoder.decode(getUiConfig().displayItems.frameItemId))
                .setCustomName(Component.literal(" "))
                .build();
    }

    public ItemStack getNavItem(String label) {
        return new ItemBuilder(Decoder.decode(getUiConfig().displayItems.navigationItemId))
                .hideAdditional()
                .setCustomName(
                        Component.literal(label)
                                .withStyle(getUiConfig().colors.navigationItem.toMcFormatting())
                )
                .build();
    }

    public ItemStack getClearItem() {
        return new ItemBuilder(Decoder.decode(getUiConfig().displayItems.clearItemId))
                .hideAdditional()
                .setCustomName(
                        Component.literal("Clear Prefix")
                                .withStyle(getUiConfig().colors.clearItem.toMcFormatting())
                )
                .build();
    }

    public ItemStack getExitItem() {
        return new ItemBuilder(Decoder.decode(getUiConfig().displayItems.exitItemId))
                .hideAdditional()
                .setCustomName(
                        Component.literal("Exit")
                                .withStyle(getUiConfig().colors.exitItem.toMcFormatting())
                )
                .build();
    }

    private Button getExitButton() {
        return GooeyButton.builder()
                .display(getExitItem())
                .onClick(action -> UIManager.closeUI(player))
                .build();
    }

    public ItemStack getPageItem(int currentPage, int pageLength) {
        return new ItemBuilder(Decoder.decode(getUiConfig().displayItems.pageItemId))
                .setCustomName(
                        Component.literal("Page " + currentPage + "/" + pageLength).withStyle(getUiConfig().colors.pageItem.toMcFormatting())
                )
                .build();
    }

    private Button getInfoButton(int currentPage, int pageLength) {
        return GooeyButton.builder()
                .display(getPageItem(currentPage, pageLength))
                .build();
    }

    private TemplateSlotDelegate getInfoButtonTemplate(int currentPage, int pageLength) {
        Button infoButton = getInfoButton(pageLength, currentPage);
        return new TemplateSlotDelegate(infoButton, 49);
    }

    private void setPageTitleInternal(LinkedPage page, int pageLength) {
        int currentPage = page.getCurrentPage();
        page.setTitle(getDisplayTitle());
        page.getTemplate().setSlot(49, getInfoButtonTemplate(pageLength, currentPage));
    }

    private void setPageTitleRecursive(LinkedPage page) {
        int pageLength = page.getTotalPages();
        setPageTitleInternal(page, pageLength);
        LinkedPage next = page.getNext();
        if (next != null) {
            setPageTitleInternal(next, pageLength);
            setPageTitleRecursive(next);
        }
    }

    public List<Button> getButtons() {
        List<Button> buttonList = new ArrayList<>();
        var rawPlates = VanityPlates.INSTANCE.getConfig().availablePlates;

        for (VanityPlatesConfig.PlateEntry plate : rawPlates) {
            UiItem entryData = new UiItem(plate);
            if (entryData.hasPermission(player)) {
                Button newButton = entryData.getButton(player);
                buttonList.add(newButton);
            }
        }

        return buttonList;
    }

    public Button getClearButton() {
        return GooeyButton.builder()
                .display(getClearItem())
                .onClick(action -> {
                    LPHelper.clearUserPrefix(player.getUUID());
                    UIManager.closeUI(player);
                    UIManager.openUIForcefully(player, getPage());
                })
                .build();
    }

    public Button getFrameButton() {
        return GooeyButton.builder()
                .display(getFrameItem())
                .build();
    }

    public boolean hasPermission(String node) {
        if (node.isEmpty()) return true;
        return LPHelper.hasPermissionNode(player.getUUID(), node);
    }

    public Button getBackButton() {
        ItemStack displayItem = new ItemBuilder(Decoder.decode(getUiConfig().backButton.itemId))
                .hideAdditional()
                .setCustomName(
                        Component.literal(getUiConfig().backButton.label)
                                .withStyle(getUiConfig().backButton.textColor.toMcFormatting())
                )
                .build();

        return GooeyButton.builder()
                .display(displayItem)
                .onClick(action -> {
                    UIManager.closeUI(player);
                    var server = VanityPlates.getMinecraftServer();
                    if (server == null) return;
                    RunSlashCommand.asServer(server, getUiConfig().backButton.command
                            .replace("%player%", player.getName().getString())
                            .replace("%uuid%", player.getUUID().toString())
                    );
                })
                .build();
    }

    public Page getPage() {
        PlaceholderButton placeholder = new PlaceholderButton();
        List<Button> buttons = getButtons();

        LinkedPageButton previous = LinkedPageButton.builder()
                .display(getNavItem("Previous"))
                .linkType(LinkType.Previous)
                .build();

        LinkedPageButton next = LinkedPageButton.builder()
                .display(getNavItem("Next"))
                .linkType(LinkType.Next)
                .build();

        ChestTemplate.Builder template = ChestTemplate.builder(6)
                .rectangle(0, 0, 5, 9, placeholder)
                .set(45, previous)
                .set(49, getInfoButton(1, 1))
                .set(53, next)
                .set(54, getExitButton())
                .fill(getFrameButton());

        if (getUiConfig().backButton.enabled) {
            template = template.set(44, getBackButton());
        }

        if (hasPermission(getPermsConfig().clearPrefixUiButton)) {
            template = template.set(47, getClearButton());
        }

        ChestTemplate builtTemplate = template.build();

        LinkedPage page = PaginationHelper.createPagesFromPlaceholders(builtTemplate, buttons, null);
        setPageTitleRecursive(page);

        return page;
    }
}
