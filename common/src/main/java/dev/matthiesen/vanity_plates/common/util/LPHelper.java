package dev.matthiesen.vanity_plates.common.util;

import dev.matthiesen.matthiesen_core.common.core.permissions.LuckPermsHelper;
import dev.matthiesen.vanity_plates.common.config.VPConfig;
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
        PrefixNode node = PrefixNode.builder(prefix, VPConfig.SERVER_CONFIG.prefixPriority.getAsInt()).build();
        Collection<PrefixNode> prefixes = user.getNodes(NodeType.PREFIX);
        return prefixes.contains(node);
    }

    public static void setUserPrefix(UUID playerUUID, String newPrefix) {
        LuckPermsHelper.INSTANCE.setUserPrefix(playerUUID, newPrefix, VPConfig.SERVER_CONFIG.prefixPriority.getAsInt(), pre -> pre.getPriority() == VPConfig.SERVER_CONFIG.prefixPriority.getAsInt());
    }

    public static void clearUserPrefix(UUID playerUUID) {
        LuckPermsHelper.INSTANCE.clearUserPrefix(playerUUID, pre -> pre.getPriority() == VPConfig.SERVER_CONFIG.prefixPriority.getAsInt());
    }
}
