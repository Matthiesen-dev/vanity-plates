package dev.matthiesen.common.vanity_plates.permissions;

import dev.matthiesen.common.vanity_plates.Constants;
import dev.matthiesen.common.vanity_plates.VanityPlates;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PrefixNode;
import net.minecraft.server.level.ServerPlayer;

import java.util.concurrent.CompletableFuture;

public class PermissionManager {
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
        user.data().clear(NodeType.PREFIX.predicate(pre -> pre.getPriority() == VanityPlates.config.prefixPriority));
    }

    public static boolean hasPermissionNode(ServerPlayer player, String node) {
        User user = getLPUser(player);
        if (user == null) return false;
        return user.getCachedData().getPermissionData().checkPermission(node).asBoolean();
    }

    public static CachedMetaData getUserMetadata(ServerPlayer player) {
        User user = getLPUser(player);
        if (user == null) return null;
        return user.getCachedData().getMetaData();
    }

    public static void setUserPrefix(ServerPlayer player, String newPrefix) {
        User user = getLPUser(player);
        if (user == null) return;
        PrefixNode node = PrefixNode.builder(newPrefix, VanityPlates.config.prefixPriority).build();
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
