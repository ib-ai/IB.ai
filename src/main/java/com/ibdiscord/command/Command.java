/* Copyright 2017-2019 Arraying
 *
 * This file is part of IB.ai.
 *
 * IB.ai is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * IB.ai is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with IB.ai. If not, see http://www.gnu.org/licenses/.
 */

package com.ibdiscord.command;

import com.ibdiscord.IBai;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.localisation.ILocalised;
import com.ibdiscord.localisation.Localiser;
import com.ibdiscord.utils.UDatabase;
import com.ibdiscord.utils.objects.Comparator;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.GuildChannel;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Set;
import java.util.TreeSet;

public abstract class Command implements ILocalised {

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
     * @param permission The permission required to execute the command.
     * @param subCommands Any sub commands the command has.
     */
    protected Command(String name, CommandPermission permission, Set<Command> subCommands) {
        this.name = name;
        this.aliases = Localiser.getAllCommandAliases("command_aliases." + name);
        this.permission = permission;
        this.subCommands = subCommands;
        this.correctUsage = "Unknown";
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
        if(!permission.hasPermission(context.getMember(), (GuildChannel) context.getChannel())) {
            context.reply("You do not have permission to execute the command.");
            return;
        }
        IBai.INSTANCE.getLogger().info("{} executed the command {} in {}",
                context.getMember().getUser().getId(),
                name,
                context.getGuild().getId()
        );
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
        commandContext.reply(String.format("Correct usage: `%s%s`.",
                UDatabase.getPrefix(commandContext.getGuild()), correctUsage)
        );
    }
}
