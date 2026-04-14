package dev.matthiesen.common.vanity_plates.commands;

import com.mojang.brigadier.CommandDispatcher;
import dev.matthiesen.common.vanity_plates.interfaces.ICommand;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.List;

public class CommandRegistry {
    public static final List<ICommand> COMMANDS = List.of(
            new VanityCommand()
    );

    public static void init(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registry, Commands.CommandSelection context) {
        for (ICommand command : COMMANDS) {
            command.register(dispatcher, registry, context);
        }
    }
}
