package dev.matthiesen.vanity_plates.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.matthiesen_core.common.api.command.CoreCommand;
import dev.matthiesen.matthiesen_core.common.api.server_menu.ServerUIManager;
import dev.matthiesen.vanity_plates.common.ui.PlateMenu;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public final class VanityCommand implements CoreCommand {
    public static final VanityCommand CMD = new VanityCommand();

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registry, Commands.CommandSelection context) {
        var aliases = List.of("vanity", "plates");
        for (String alias : aliases) {
            dispatcher.register(
                    Commands.literal(alias)
                            .executes(this::action)
            );
        }
    }

    public int action(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        if (player != null) {
            ServerUIManager.openUIForcefully(player, new PlateMenu(player).getPage());
        }
        return 1;
    }
}
