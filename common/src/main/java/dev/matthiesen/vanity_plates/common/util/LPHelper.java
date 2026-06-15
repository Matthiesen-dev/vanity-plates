package dev.matthiesen.vanity_plates.common.util;

import dev.matthiesen.vanity_plates.common.VanityPlates;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PrefixNode;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class LPHelper {
    private static LuckPerms luckPerms;

    private static void xClearPrefix(User user) {
        user.data().clear(NodeType.PREFIX.predicate(pre -> pre.getPriority() == VanityPlates.INSTANCE.getConfig().prefixPriority));
    }

    private static LuckPerms getLuckPerms() {
        if (luckPerms == null) {
            try {
                luckPerms = LuckPermsProvider.get();
                VanityPlates.INSTANCE.createInfoLog("LuckPerms API loaded successfully");
            } catch (IllegalStateException e) {
                VanityPlates.INSTANCE.createErrorLog("LuckPerms not available", e);
                return null;
            }
        }
        return luckPerms;
    }

    private static User getLPUser(UUID playerUUID) {
        try {
            LuckPerms lp = getLuckPerms();
            if (lp == null) return null;
            UserManager userManager = lp.getUserManager();
            CompletableFuture<User> asyncUser = userManager.loadUser(playerUUID);
            return asyncUser.join();
        } catch (Exception e) {
            VanityPlates.INSTANCE.createErrorLog("Failed to load LuckPerms user for UUID: " + playerUUID, e);
            return null;
        }
    }

    private static void saveUser(User user) {
        try {
            LuckPerms lp = getLuckPerms();
            if (lp == null) return;
            UserManager userManager = lp.getUserManager();
            userManager.saveUser(user);
        } catch (Exception e) {
            VanityPlates.INSTANCE.createErrorLog("Failed to save LuckPerms user: " + user.getUsername(), e);
        }
    }

    public static boolean hasPermissionNode(UUID playerUUID, String node) {
        User user = getLPUser(playerUUID);
        if (user == null) return false;
        return user.getCachedData().getPermissionData().checkPermission(node).asBoolean();
    }

    public static boolean comparePrefix(UUID playerUUID, String prefix) {
        User user = getLPUser(playerUUID);
        if (user == null) return false;
        PrefixNode node = PrefixNode.builder(prefix, VanityPlates.INSTANCE.getConfig().prefixPriority).build();
        Collection<PrefixNode> prefixes = user.getNodes(NodeType.PREFIX);
        return prefixes.contains(node);
    }

    public static void setUserPrefix(UUID playerUUID, String newPrefix) {
        User user = getLPUser(playerUUID);
        if (user == null) return;
        PrefixNode node = PrefixNode.builder(newPrefix, VanityPlates.INSTANCE.getConfig().prefixPriority).build();
        xClearPrefix(user);
        user.data().add(node);
        saveUser(user);
    }

    public static void clearUserPrefix(UUID playerUUID) {
        User user = getLPUser(playerUUID);
        if (user == null) return;
        xClearPrefix(user);
        saveUser(user);
    }
}
