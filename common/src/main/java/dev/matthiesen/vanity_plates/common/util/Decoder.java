package dev.matthiesen.vanity_plates.common.util;

import dev.matthiesen.common.matthiesen_lib_api.utility.ItemDecoder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public final class Decoder {
    public static Item decode(String string) {
        return ItemDecoder.stringToItem(string, Items.BARRIER);
    }
}
