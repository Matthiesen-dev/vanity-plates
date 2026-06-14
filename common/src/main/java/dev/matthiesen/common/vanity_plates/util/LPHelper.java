package dev.matthiesen.common.vanity_plates.util;

import dev.matthiesen.common.vanity_plates.Constants;
import dev.matthiesen.common.vanity_plates.VanityPlates;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PrefixNode;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public final class LPHelper {
    private static LuckPerms luckPerms;

    public static LuckPerms getLuckPerms() {
        if (luckPerms == null) {
            try {
                luckPerms = LuckPermsProvider.get();
                Constants.createInfoLog("LuckPerms API loaded successfully");
            } catch (IllegalStateException e) {
                Constants.createErrorLog("LuckPerms not available");
                return null;
            }
        }
        return luckPerms;
    }

    public static User getLPUser(ServerPlayer player) {
        LuckPerms lp = getLuckPerms();
        if (lp == null) return null;
        UserManager userManager = lp.getUserManager();
        CompletableFuture<User> asyncUser = userManager.loadUser(player.getUUID());
        return asyncUser.join();
    }

    public static void saveUser(User user) {
        LuckPerms lp = getLuckPerms();
        if (lp == null) return;
        UserManager userManager = lp.getUserManager();
        userManager.saveUser(user);
    }

    public static void xClearPrefix(User user) {
        user.data().clear(NodeType.PREFIX.predicate(pre -> pre.getPriority() == VanityPlates.getConfig().prefixPriority));
    }

    public static boolean hasPermissionNode(ServerPlayer player, String node) {
        User user = getLPUser(player);
        if (user == null) return false;
        return user.getCachedData().getPermissionData().checkPermission(node).asBoolean();
    }

    public static boolean comparePrefix(ServerPlayer player, String prefix) {
        User user = getLPUser(player);
        if (user == null) return false;
        PrefixNode node = PrefixNode.builder(prefix, VanityPlates.getConfig().prefixPriority).build();
        Collection<PrefixNode> prefixes = user.getNodes(NodeType.PREFIX);
        return prefixes.contains(node);
    }

    public static void setUserPrefix(ServerPlayer player, String newPrefix) {
        User user = getLPUser(player);
        if (user == null) return;
        PrefixNode node = PrefixNode.builder(newPrefix, VanityPlates.getConfig().prefixPriority).build();
        xClearPrefix(user);
        user.data().add(node);
        saveUser(user);
    }

    public static void clearUserPrefix(ServerPlayer player) {
        User user = getLPUser(player);
        if (user == null) return;
        xClearPrefix(user);
        saveUser(user);
    }
}
