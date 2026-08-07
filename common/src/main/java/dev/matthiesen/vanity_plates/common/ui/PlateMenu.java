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
import dev.matthiesen.matthiesen_core.common.utility.commands.RunSlashCommand;
import dev.matthiesen.matthiesen_core.common.utility.item.ItemBuilder;
import dev.matthiesen.vanity_plates.common.config.def.PlateEntry;
import dev.matthiesen.vanity_plates.common.config.VPConfig;
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

    public Component getDisplayTitle() {
        return Component.literal(VPConfig.GUI_CONFIG.displayTitle.get())
                .withStyle(style ->
                        style.withColor(VPConfig.GUI_CONFIG.titleColor.get().toMcFormatting())
                                .withBold(true)
                );
    }

    public ItemStack getFrameItem() {
        return new ItemBuilder(Decoder.decode(VPConfig.GUI_CONFIG.frameItemId.get()))
                .setCustomName(Component.literal(" "))
                .build();
    }

    public ItemStack getNavItem(String label, boolean prev) {
        var item = prev ? VPConfig.GUI_CONFIG.prevNavigationItemId.get() : VPConfig.GUI_CONFIG.nextNavigationItemId.get();
        return new ItemBuilder(Decoder.decode(item))
                .hideAdditional()
                .setCustomName(
                        Component.literal(label)
                                .withStyle(VPConfig.GUI_CONFIG.navigationItemColor.get().toMcFormatting())
                )
                .build();
    }

    public ItemStack getClearItem() {
        return new ItemBuilder(Decoder.decode(VPConfig.GUI_CONFIG.clearItemId.get()))
                .hideAdditional()
                .setCustomName(
                        Component.literal(VPConfig.GUI_CONFIG.clearPrefix.get())
                                .withStyle(VPConfig.GUI_CONFIG.clearItemColor.get().toMcFormatting())
                )
                .build();
    }

    public ItemStack getExitItem() {
        return new ItemBuilder(Decoder.decode(VPConfig.GUI_CONFIG.exitItemId.get()))
                .hideAdditional()
                .setCustomName(
                        Component.literal(VPConfig.GUI_CONFIG.exit.get())
                                .withStyle(VPConfig.GUI_CONFIG.exitItemColor.get().toMcFormatting())
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
        return new ItemBuilder(Decoder.decode(VPConfig.GUI_CONFIG.pageItemId.get()))
                .setCustomName(Component.literal(
                        VPConfig.GUI_CONFIG.pageIndicator.get()
                                .replace("%current%", Integer.toString(currentPage))
                                .replace("%length%", Integer.toString(pageLength))
                        ).withStyle(VPConfig.GUI_CONFIG.pageItemColor.get().toMcFormatting())
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
        var rawPlates = VPConfig.getAvailablePlates();

        for (PlateEntry plate : rawPlates) {
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
        ItemStack displayItem = new ItemBuilder(Decoder.decode(VPConfig.GUI_CONFIG.backButton_itemId.get()))
                .hideAdditional()
                .setCustomName(
                        Component.literal(VPConfig.GUI_CONFIG.backButton_label.get())
                                .withStyle(VPConfig.GUI_CONFIG.backButton_color.get().toMcFormatting())
                )
                .build();

        return GooeyButton.builder()
                .display(displayItem)
                .onClick(action -> {
                    UIManager.closeUI(player);
                    RunSlashCommand.asServer(VPConfig.GUI_CONFIG.backButton_command.get()
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
                .display(getNavItem(VPConfig.GUI_CONFIG.previousPage.get(), true))
                .linkType(LinkType.Previous)
                .build();

        LinkedPageButton next = LinkedPageButton.builder()
                .display(getNavItem(VPConfig.GUI_CONFIG.nextPage.get(), false))
                .linkType(LinkType.Next)
                .build();

        ChestTemplate.Builder template = ChestTemplate.builder(6)
                .rectangle(0, 0, 5, 9, placeholder)
                .set(46, previous)
                .set(49, getInfoButton(1, 1))
                .set(52, next)
                .set(53, getExitButton())
                .fill(getFrameButton());

        if (VPConfig.GUI_CONFIG.backButton_enabled.get()) {
            template = template.set(45, getBackButton());
        }

        if (hasPermission(VPConfig.GUI_CONFIG.clearPrefixUiButtonPermission.get())) {
            template = template.set(48, getClearButton());
        }

        ChestTemplate builtTemplate = template.build();

        LinkedPage page = PaginationHelper.createPagesFromPlaceholders(builtTemplate, buttons, null);
        setPageTitleRecursive(page);

        return page;
    }
}
