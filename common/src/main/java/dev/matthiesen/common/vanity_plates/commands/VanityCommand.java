package dev.matthiesen.common.vanity_plates.commands;

import ca.landonjw.gooeylibs2.api.UIManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.common.vanity_plates.VanityPlates;
import dev.matthiesen.common.vanity_plates.interfaces.ICommand;
import dev.matthiesen.common.vanity_plates.ui.PlateMenu;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class VanityCommand implements ICommand {
    private final List<String> aliases = List.of("vanity", "plates");

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registry, Commands.CommandSelection context) {
        for (String alias : aliases) {
            dispatcher.register(
                    Commands.literal(alias)
                            .executes(this::execute)
                            .then(
                                    Commands.literal("reload")
                                            .requires(src -> src.hasPermission(4))
                                            .executes(this::reload)
                            ));
        }
    }

    private int execute(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        if (player != null) {
            UIManager.openUIForcefully(player, new PlateMenu(player).getPage());
        }
        return 1;
    }

    private int reload(CommandContext<CommandSourceStack> ctx) {
        VanityPlates.reload();
        return 1;
    }
}
