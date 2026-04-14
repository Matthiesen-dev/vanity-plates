package dev.matthiesen.common.vanity_plates.ui;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import dev.matthiesen.common.vanity_plates.config.ModConfig;
import dev.matthiesen.common.vanity_plates.permissions.PermissionManager;
import dev.matthiesen.common.vanity_plates.util.ItemBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Optional;

public class UiItem {
    public String rawDisplayItem;
    public String requiredPermission;
    public String label;
    public String prefix;

    public UiItem(ModConfig.PlateEntry entry) {
        rawDisplayItem = entry.displayItem;
        requiredPermission = entry.requiredPermission;
        label = entry.label;
        prefix = entry.prefix;
    }

    public ItemStack getDisplayItem(boolean active) {
        Optional<Item> hopeful = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse(rawDisplayItem));
        Item itemToUse = hopeful.orElse(Items.PAPER);
        ItemBuilder builder = new ItemBuilder(itemToUse).hideAdditional().setCustomName(Component.literal(label));
        if (active) {
            builder = builder.setEnchanted(true)
                    .addLore(new MutableComponent[]{
                            Component.literal("ACTIVE")
                                    .withStyle(style ->
                                            style.withColor(ChatFormatting.GREEN)
                                    )
                    });
        }
        return builder.build();
    }

    public boolean isActive(ServerPlayer player) {
        return PermissionManager.comparePrefix(player, prefix);
    }

    public boolean hasPermission(ServerPlayer player) {
        return PermissionManager.hasPermissionNode(player, requiredPermission);
    }

    public void onClickAction(ServerPlayer player) {
        if (!isActive(player)) {
            PermissionManager.setUserPrefix(player, prefix);
        }
        UIManager.closeUI(player);
        UIManager.openUIForcefully(player, new PlateMenu(player).getPage());
    }

    public Button getButton(ServerPlayer player) {
        return GooeyButton.builder()
                .display(getDisplayItem(isActive(player)))
                .onClick(action -> onClickAction(player))
                .build();
    }
}
