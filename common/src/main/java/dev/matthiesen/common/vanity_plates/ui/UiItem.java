package dev.matthiesen.common.vanity_plates.ui;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import dev.matthiesen.common.matthiesen_lib_api.utility.ItemBuilder;
import dev.matthiesen.common.vanity_plates.config.VanityPlatesConfig;
import dev.matthiesen.common.vanity_plates.util.LPHelper;
import dev.matthiesen.common.vanity_plates.util.TextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class UiItem {
    public String rawDisplayItem;
    public String requiredPermission;
    public String label;
    public String prefix;
    public @Nullable Integer customModelData;

    public UiItem(VanityPlatesConfig.PlateEntry entry) {
        rawDisplayItem = entry.displayItem;
        requiredPermission = entry.requiredPermission;
        label = entry.label;
        prefix = entry.prefix;
        if (entry.customModelData != null) {
            customModelData = entry.customModelData;
        } else {
            customModelData = null;
        }
    }

    public ItemStack getDisplayItem(boolean active) {
        Optional<Item> hopeful = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse(rawDisplayItem));
        Item itemToUse = hopeful.orElse(Items.PAPER);
        ItemBuilder builder = new ItemBuilder(itemToUse).hideAdditional().setCustomName(TextUtils.parse(label));
        if (customModelData != null) {
            builder = builder.setModelData(customModelData);
        }
        Component[] lore = new Component[] {
                active
                    ? Component.literal("ACTIVE").withStyle(style -> style.withColor(ChatFormatting.GREEN))
                        : Component.literal("Click to set as your prefix")
                          .withStyle(style -> style.withColor(ChatFormatting.GRAY))
        };
        builder = builder.addLore(lore);
        if (active) {
            builder = builder.modifyStack(stack -> {
                stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
                return stack;
            });
        }
        return builder.build();
    }

    public boolean isActive(ServerPlayer player) {
        return LPHelper.comparePrefix(player, prefix);
    }

    public boolean hasPermission(ServerPlayer player) {
        return LPHelper.hasPermissionNode(player, requiredPermission);
    }

    public void onClickAction(ServerPlayer player) {
        if (!isActive(player)) {
            LPHelper.setUserPrefix(player, prefix);
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
