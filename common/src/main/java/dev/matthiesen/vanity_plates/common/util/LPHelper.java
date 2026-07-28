package dev.matthiesen.vanity_plates.common.util;

import dev.matthiesen.matthiesen_core.common.core.permissions.LuckPermsHelper;
import dev.matthiesen.vanity_plates.common.VanityPlates;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PrefixNode;

import java.util.Collection;
import java.util.UUID;

public final class LPHelper {

    public static boolean hasPermissionNode(UUID playerUUID, String node) {
        return LuckPermsHelper.INSTANCE.hasPermissionNode(playerUUID, node);
    }

    public static boolean comparePrefix(UUID playerUUID, String prefix) {
        User user = LuckPermsHelper.INSTANCE.getUser(playerUUID);
        if (user == null) return false;
        PrefixNode node = PrefixNode.builder(prefix, VanityPlates.INSTANCE.getConfig().prefixPriority).build();
        Collection<PrefixNode> prefixes = user.getNodes(NodeType.PREFIX);
        return prefixes.contains(node);
    }

    public static void setUserPrefix(UUID playerUUID, String newPrefix) {
        LuckPermsHelper.INSTANCE.setUserPrefix(playerUUID, newPrefix, VanityPlates.INSTANCE.getConfig().prefixPriority, pre -> pre.getPriority() == VanityPlates.INSTANCE.getConfig().prefixPriority);
    }

    public static void clearUserPrefix(UUID playerUUID) {
        LuckPermsHelper.INSTANCE.clearUserPrefix(playerUUID, pre -> pre.getPriority() == VanityPlates.INSTANCE.getConfig().prefixPriority);
    }
}
