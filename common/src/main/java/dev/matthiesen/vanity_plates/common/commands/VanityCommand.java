package dev.matthiesen.vanity_plates.common.commands;

import ca.landonjw.gooeylibs2.api.UIManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.common.matthiesen_lib_api.command.AbstractCommand;
import dev.matthiesen.vanity_plates.common.VanityPlates;
import dev.matthiesen.vanity_plates.common.ui.PlateMenu;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public final class VanityCommand extends AbstractCommand {
    public static final VanityCommand CMD = new VanityCommand();

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registry, Commands.CommandSelection context) {
        var aliases = List.of("vanity", "plates");
        for (String alias : aliases) {
            dispatcher.register(
                    Commands.literal(alias)
                            .executes(this::action)
                            .then(
                                    Commands.literal("reload")
                                            .requires(this::isOP)
                                            .executes(this::reload)
                            )
            );
        }
    }

    private boolean isOP(CommandSourceStack source) {
        return source.getPlayer() != null && source.getPlayer().hasPermissions(4);
    }

    @Override
    public int action(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        if (player != null) {
            UIManager.openUIForcefully(player, new PlateMenu(player).getPage());
        }
        return 1;
    }

    private int reload(CommandContext<CommandSourceStack> context) {
        VanityPlates.INSTANCE.reload();
        return 1;
    }
}
