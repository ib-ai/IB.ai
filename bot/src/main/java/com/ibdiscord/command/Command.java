package com.ibdiscord.command;

import com.ibdiscord.IBai;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.utils.UDatabase;
import com.ibdiscord.utils.objects.Comparator;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.core.entities.Channel;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Set;
import java.util.TreeSet;

/**
 * Copyright 2019 Arraying
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public abstract class Command {

    /**
     * All commands.
     */
    private static final Set<Command> COMMANDS = new TreeSet<>(new Comparator());

    @Getter private final String name;
    @Getter private final Set<String> aliases;
    private final CommandPermission permission;
    private final Set<Command> subCommands;
    @Getter @Setter private boolean enabled = true;
    protected String correctUsage;

    /**
     * Creates a new command.
     * @param name The name of the command, all lowercase.
     * @param aliases Any aliases the command has, also all lowercase.
     * @param permission The permission required to execute the command.
     * @param subCommands Any sub commands the command has.
     */
    protected Command(String name, Set<String> aliases, CommandPermission permission, Set<Command> subCommands) {
        this.name = name;
        this.aliases = aliases;
        this.permission = permission;
        this.subCommands = subCommands;
        this.correctUsage = "unknown";
    }

    /**
     * Initializes all commands.
     */
    public static void init() {
        for(CommandCollection commandCollection : CommandCollection.values()) {
            COMMANDS.add(commandCollection.getCommand());
        }
    }

    /**
     * Finds a command based on the criteria.
     * @param origin The place where to look for the command.
     * @param queryRaw The criteria.
     * @return The command, or null if it was not found.
     */
    public static Command find(Set<Command> origin, String queryRaw) {
        final String query = queryRaw.toLowerCase();
        origin = origin == null ? COMMANDS : origin;
        return origin.stream().filter(
                it -> it.getName().equals(query) || it.getAliases().contains(query))
                .findFirst()
                .orElse(null);
    }

    /**
     * Abstracted method that executes the command, this is executed when all preflight checks have been executed.
     * @param context The command context.
     */
    protected abstract void execute(CommandContext context);

    /**
     * Pre-processes the command. This takes care of any common checks such as permission.
     * @param context The command context.
     */
    public void preprocess(CommandContext context) {
        if(!enabled) {
            context.reply("Unfortunately this command is momentarily disabled.");
            return;
        }
        if(!permission.hasPermission(context.getMember(), (Channel) context.getChannel())) {
            context.reply("You do not have permission to execute the command.");
            return;
        }
        IBai.INSTANCE.getLogger().info("{} executed the command {} in {}", context.getMember().getUser().getId(), name, context.getGuild().getId());
        String[] args = context.getArguments();
        if(args.length == 0 || subCommands.size() == 0) {
            execute(context);
            return;
        }
        Command subCommand = find(subCommands, args[0]);
        if(subCommand == null) {
            context.reply("Unknown sub command %s.", args[0]);
        } else {
            subCommand.preprocess(context.clone(ArrayUtils.remove(context.getArguments(), 0)));
        }
    }

    /**
     * Sends the correct command usage to the context.
     * @param commandContext The command usage.
     */
    protected void sendUsage(CommandContext commandContext) {
        commandContext.reply(String.format("Correct usage: `%s%s`.", UDatabase.getPrefix(commandContext.getGuild()), correctUsage));
    }

}
