package dev.matthiesen.common.vanity_plates.ui;

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
    public static String rawDisplayItem;
    public static String requiredPermission;

    public static String label;
    public static String prefix;

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
            builder.setEnchanted(true)
                    .addLore(new MutableComponent[]{
                            Component.literal("ACTIVE")
                                    .withStyle(style ->
                                            style.withColor(ChatFormatting.GREEN)
                                    )
                    });
        }
        return builder.build();
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean hasPermission(ServerPlayer player) {
        return PermissionManager.hasPermissionNode(player, requiredPermission);
    }
}
