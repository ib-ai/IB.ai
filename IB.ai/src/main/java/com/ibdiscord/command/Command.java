package com.ibdiscord.command;

import com.ibdiscord.command.commands.PingCommand;
import com.ibdiscord.command.commands.TestOptionsCommand;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.core.entities.Channel;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Set;
import java.util.TreeSet;

/**
 * Copyright 2018 Arraying
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

    protected Command(String name, Set<String> aliases, CommandPermission permission, Set<Command> subCommands) {
        this.name = name;
        this.aliases = aliases;
        this.permission = permission;
        this.subCommands = subCommands;
    }

    public static void init() {
        for(Collection collection : Collection.values()) {
            COMMANDS.add(collection.command);
        }
    }

    public static Command find(Set<Command> origin, String queryRaw) {
        final String query = queryRaw.toLowerCase();
        origin = origin == null ? COMMANDS : origin;
        return origin.stream().filter(
                it -> it.getName().equals(query) || it.getAliases().contains(query))
                .findFirst()
                .orElse(null);
    }

    protected abstract void execute(CommandContext context);

    public void preprocess(CommandContext context) {
        if(!enabled) {
            context.reply("Unfortunately this command is momentarily disabled.");
            return;
        }
        if(!permission.hasPermission(context.getMember(), (Channel) context.getChannel())) {
            context.reply("You do not have permission to execute the command.");
            return;
        }
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

    public static final class Comparator implements java.util.Comparator<Command> {

        @Override
        public int compare(Command o1, Command o2) {
            return o1.name.compareTo(o2.name);
        }

    }

    /**
     * A collection of commands so that they can be registered easily.
     */
    private enum Collection {

        PING(new PingCommand()),
        TEST_OPTIONS(new TestOptionsCommand());

        private final Command command;

        Collection(Command command) {
            this.command = command;
        }
    }

}
