package dev.matthiesen.common.vanity_plates.ui;

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
import dev.matthiesen.common.vanity_plates.VanityPlates;
import dev.matthiesen.common.vanity_plates.config.ModConfig;
import dev.matthiesen.common.vanity_plates.permissions.PermissionManager;
import dev.matthiesen.common.vanity_plates.util.ItemBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public record PlateMenu(ServerPlayer player) {
    public Component getDisplayTitle() {
        return Component.literal("Vanity Plates")
                .withStyle(style ->
                        style.withColor(ChatFormatting.GOLD)
                                .withBold(true)
                );
    }

    public static ItemStack getFrameItem() {
        return new ItemBuilder(Items.GRAY_STAINED_GLASS_PANE)
                .setCustomName(Component.literal(" "))
                .build();
    }

    public static ItemStack getNavItem(String label) {
        return new ItemBuilder(Items.ARROW)
                .hideAdditional()
                .setCustomName(
                        Component.literal(label)
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.AQUA)
                                )
                )
                .build();
    }

    public static ItemStack getClearItem() {
        return new ItemBuilder(Items.ARROW)
                .hideAdditional()
                .setCustomName(
                        Component.literal("Clear Prefix")
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.RED)
                                )
                )
                .build();
    }

    public static ItemStack getPageItem(int currentPage, int pageLength) {
        return new ItemBuilder(Items.BOOK)
                .setCustomName(
                        Component.literal("Page " + currentPage + "/" + pageLength).withStyle(style -> style.withColor(ChatFormatting.GOLD))
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
        var rawPlates = VanityPlates.config.availablePlates;

        for (ModConfig.PlateEntry plate : rawPlates) {
            UiItem entryData = new UiItem(plate);
            if (entryData.hasPermission(player)) {
                boolean isActive = PermissionManager.comparePrefix(player, entryData.getPrefix());

                Button newButton = GooeyButton.builder()
                        .display(entryData.getDisplayItem(isActive))
                        .onClick(action -> {
                            PermissionManager.setUserPrefix(player, entryData.getPrefix());
                            UIManager.closeUI(player);
                        })
                        .build();

                buttonList.add(newButton);
            }
        }

        return buttonList;
    }

    public Button getClearButton() {
        return GooeyButton.builder()
                .display(getClearItem())
                .onClick(action -> {
                    PermissionManager.clearUserPrefix(player);
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

        ChestTemplate template = ChestTemplate.builder(6)
                .rectangle(0, 0, 5, 9, placeholder)
                .set(53, next)
                .set(47, getClearButton())
                .set(49, getInfoButton(1, 1))
                .set(45, previous)
                .fill(getFrameButton())
                .build();

        LinkedPage page = PaginationHelper.createPagesFromPlaceholders(template, buttons, null);
        setPageTitleRecursive(page);

        return page;
    }
}
