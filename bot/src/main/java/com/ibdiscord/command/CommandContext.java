package com.ibdiscord.command;

import lombok.Getter;
import net.dv8tion.jda.api.entities.*;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Copyright 2017-2019 Arraying
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
public final class CommandContext {

    @Getter final Message message;
    @Getter private final Guild guild;
    @Getter private final MessageChannel channel;
    @Getter private final Member member;
    @Getter private final String[] arguments;
    @Getter private final Set<Option> options;

    /**
     * Creates a new command context. Which is essentially metadata regarding command execution.
     * @param message The message.
     * @param arguments The arguments.
     * @param options Any options of the command.
     */
    private CommandContext(Message message, String[] arguments, Set<Option> options) {
        this.message = message;
        this.guild = message.getGuild();
        this.channel = message.getChannel();
        this.member = message.getMember();
        this.arguments = arguments;
        this.options = options;
    }

    /**
     * Constructs a command context by wrapping the constructor.
     * @param message The message.
     * @param args The arguments.
     * @return A command context.
     */
    public static CommandContext construct(Message message, String[] args) {
        Set<Option> options = new LinkedHashSet<>();
        Set<Integer> toRemove = new HashSet<>();
        for(int i = 0; i < args.length; i++) {
            String arg = args[i];
            if(!isParameter(arg)) {
                continue; // not a parameter
            }
            String name = arg.substring(1).toLowerCase();
            String value = null;
            boolean declareAsValue = false;
            toRemove.add(i);
            if(isParameter(name)) { // parameter with value
                name = name.substring(1);
                declareAsValue = true;
                if(i != args.length - 1) {
                    int next = i + 1;
                    if(!isParameter(args[next])) { // there's a next value and the next item won't be a parameter
                        value = args[next];
                        toRemove.add(next); // can continue iterating, because next value won't trigger parameter.
                    }
                    toRemove.add(next);
                }
            }
            if(declareAsValue && value == null) {
                continue;
            }
            options.add(new Option(name, value, declareAsValue));
        }
        int decrementer = 0;
        for(int i : toRemove) {
            args = ArrayUtils.remove(args, i - decrementer); // the array size will shrink for every time something is removed
            decrementer++; // make sure the correct indexes are removed
        }
        return new CommandContext(message, args, options);
    }

    /**
     * Whether or not the value is a parameter.
     * @param value The value.
     * @return True if it is, false otherwise.
     */
    private static boolean isParameter(String value) {
        return value.startsWith("-") && value.length() > 1;
    }

    /**
     * Replies to the context.
     * @param message The message.
     * @param format Any message formats.
     */
    public void reply(String message, Object... format) {
        channel.sendMessageFormat(message, (Object[]) format).queue(null, Throwable::printStackTrace);
    }

    /**
     * Replies to the context.
     * @param message A message embed.
     */
    public void reply(MessageEmbed message) {
        channel.sendMessage(message).queue(null, Throwable::printStackTrace);
    }

    /**
     * Clones the command context with new arguments, for subcommands.
     * @param arguments The new arguments.
     * @return A new command context.
     */
    CommandContext clone(String[] arguments) {
        return new CommandContext(message, arguments, options);
    }

}
