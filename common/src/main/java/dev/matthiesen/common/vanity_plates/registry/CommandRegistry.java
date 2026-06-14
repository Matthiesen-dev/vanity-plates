package dev.matthiesen.common.vanity_plates.registry;

import dev.matthiesen.common.matthiesen_lib_api.registry.AbstractCommandRegistry;
import dev.matthiesen.common.vanity_plates.commands.VanityCommand;

public final class CommandRegistry extends AbstractCommandRegistry {
    private static final CommandRegistry INSTANCE = new CommandRegistry();

    public CommandRegistry() {
        super();
    }

    static {
        INSTANCE.register(VanityCommand.CMD);
    }

    public static void init() {}
}
