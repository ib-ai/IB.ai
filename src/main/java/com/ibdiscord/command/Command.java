/* Copyright 2017-2020 Arraying
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
import com.ibdiscord.command.permission.CommandPermission;
import com.ibdiscord.utils.UCommand;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public final @RequiredArgsConstructor class Command {

    private final String name;
    private final Set<String> aliases;
    private CommandPermission permission = CommandPermission.discord(Permission.MESSAGE_WRITE);
    private Set<Command> subCommands = new HashSet<>();
    private Consumer<CommandContext> action;

    /**
     * Restricts a command by setting its permission.
     * @param permission The permission.
     * @return The current command.
     */
    public Command restrict(CommandPermission permission) {
        this.permission = permission;
        return this;
    }

    /**
     * Adds a sub-command to the current command.
     * @param subCommand The sub-command.
     * @return The current command.
     */
    public Command sub(Command subCommand) {
        this.subCommands.add(subCommand);
        return this;
    }

    /**
     * Sets the command action.
     * @param action The consumer/action to use.
     * @return The current command.
     */
    public Command on(Consumer<CommandContext> action) {
        this.action = action;
        return this;
    }

    /**
     * Process the command and finally execute it. This takes care of any common checks such as permission and handles
     * recursive sub-commands before finally executing the deepest subcommand.
     * @param context The command context.
     */
    public void processAndExecute(CommandContext context) {
        if(!permission.hasPermission(context.getMember(), (GuildChannel) context.getChannel())) {
            context.replyI18n("error.permission");
            return;
        }
        IBai.INSTANCE.getLogger().info("{} executed the command {} in {}",
                context.getMember().getUser().getId(),
                name,
                context.getGuild().getId()
        );
        String[] args = context.getArguments();
        if(args.length == 0 || subCommands.size() == 0) {
            if(action == null) {
                return;
            }
            try {
                action.accept(context);
            } catch(RuntimeException exception) {
                context.replyRaw(exception.getMessage());
            }
            return;
        }
        Command subCommand = UCommand.query(subCommands, args[0]);
        if(subCommand == null) {
            context.replyI18n("error.unknown_sub", args[0]);
        } else {
            subCommand.processAndExecute(context.clone(ArrayUtils.remove(context.getArguments(), 0)));
        }
    }

    public String getName() {
        return name;
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public Set<Command> getSubCommands() {
        return subCommands;
    }

}
