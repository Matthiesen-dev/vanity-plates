package dev.matthiesen.common.vanity_plates.util;
import dev.matthiesen.common.vanity_plates.Constants;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItemBuilder {
    private final ItemStack stack;

    public ItemBuilder(Item item) {
        this.stack = new ItemStack(item);
    }

    public ItemBuilder addLore(Component[] newLore) {
        ItemLore itemLore = stack.get(DataComponents.LORE);
        if (itemLore == null) {
            itemLore = new ItemLore(List.of());
        }

        List<Component> list = Stream.concat(itemLore.lines().stream(), Arrays.stream(newLore))
                .collect(Collectors.toList());

        // Go through every Component, and get the current style and set Italic to false
        list = list.stream()
                .map(component ->
                        component.copy().withStyle(component.getStyle().withItalic(false))
                )
                .collect(Collectors.toList());

        itemLore = new ItemLore(list);
        stack.set(DataComponents.LORE, itemLore);
        return this;
    }

    public ItemBuilder hideAdditional() {
        stack.set(DataComponents.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
        return this;
    }

    public ItemBuilder setCustomName(Component customName) {
        stack.set(DataComponents.CUSTOM_NAME, customName);
        return this;
    }

    public ItemBuilder setCustomName(String plainText) {
        plainText = convertColorCodes(plainText);
        Component textComp = parseMinecraftComponent(plainText);
        return setCustomName(textComp);
    }

    public ItemBuilder setEnchanted(boolean value) {
        stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, value);
        return this;
    }

    private String convertColorCodes(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.replace('&', '§');
    }

    private Component parseMinecraftComponent(String text) {
        if (text == null || text.isEmpty()) return Component.empty();

        try {
            return Component.literal(text);
        } catch (Exception e) {
            Constants.LOGGER.warn("Failed to parse component: {}", text, e);
            return Component.literal(text);
        }
    }

    public ItemStack build() {
        return this.stack;
    }
}