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
import dev.matthiesen.vanity_plates.common.VanityPlates;
import dev.matthiesen.vanity_plates.common.config.VanityPlatesConfig;
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

    public VanityPlatesConfig.UiConfig getUiConfig() {
        return VanityPlates.INSTANCE.getConfig().uiConfig;
    }

    public VanityPlatesConfig.Permissions getPermsConfig() {
        return VanityPlates.INSTANCE.getConfig().permissions;
    }

    public Component getDisplayTitle() {
        return Component.literal(getUiConfig().displayTitle)
                .withStyle(style ->
                        style.withColor(getUiConfig().titleColor.toMcFormatting())
                                .withBold(true)
                );
    }

    public ItemStack getFrameItem() {
        return new ItemBuilder(Decoder.decode(getUiConfig().frameItemId))
                .setCustomName(Component.literal(" "))
                .build();
    }

    public ItemStack getNavItem(String label) {
        return new ItemBuilder(Decoder.decode(getUiConfig().navigationItemId))
                .hideAdditional()
                .setCustomName(
                        Component.literal(label)
                                .withStyle(getUiConfig().navigationItemTextColor.toMcFormatting())
                )
                .build();
    }

    public ItemStack getClearItem() {
        return new ItemBuilder(Decoder.decode(getUiConfig().clearItemId))
                .hideAdditional()
                .setCustomName(
                        Component.literal("Clear Prefix")
                                .withStyle(getUiConfig().clearItemTextColor.toMcFormatting())
                )
                .build();
    }

    public ItemStack getExitItem() {
        return new ItemBuilder(Decoder.decode(getUiConfig().exitItemId))
                .hideAdditional()
                .setCustomName(
                        Component.literal("Exit")
                                .withStyle(getUiConfig().exitItemTextColor.toMcFormatting())
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
        return new ItemBuilder(Decoder.decode(getUiConfig().pageItemId))
                .setCustomName(
                        Component.literal("Page " + currentPage + "/" + pageLength).withStyle(getUiConfig().pageItemTextColor.toMcFormatting())
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

//        if (hasBackButton) {
//            template = template.set(44, getBackButton());
//        }

        if (hasPermission(getPermsConfig().clearPrefix)) {
            template = template.set(47, getClearButton());
        }

        ChestTemplate builtTemplate = template.build();

        LinkedPage page = PaginationHelper.createPagesFromPlaceholders(builtTemplate, buttons, null);
        setPageTitleRecursive(page);

        return page;
    }
}
