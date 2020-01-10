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

import com.ibdiscord.localisation.ILocalised;
import com.ibdiscord.utils.UInput;
import com.ibdiscord.utils.UString;
import com.ibdiscord.utils.UTime;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class CommandContext implements ILocalised {

    @Getter private final JDA jda;
    @Getter private final Message message;
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
        this.jda = message.getJDA();
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
        for(int i : toRemove) { // The array size will shrink for every time something is removed
            args = ArrayUtils.remove(args, i - decrementer);
            decrementer++;
        }
        return new CommandContext(message, args, options);
    }

    /**
     * Clones the command context with new arguments, for subcommands.
     * @param arguments The new arguments.
     * @return A new command context.
     */
    CommandContext clone(String[] arguments) {
        return new CommandContext(message, arguments, options);
    }

    /**
     * Asserts that the command arguments are at least the given length.
     * This will throw a {@link java.lang.RuntimeException} if the assertion fails.
     * @param length The length.
     * @param error The i18n key.
     */
    public void assertArguments(int length, String error) {
        if(arguments.length < length) {
            throw new RuntimeException(__(this, error));
        }
    }

    /**
     * Asserts that an escaped version of the string will not exceed the length limit.
     * @param value The string.
     * @param length The limit (1 based indexing).
     * @param error The i18n key.
     */
    public void assertLength(String value, int length, String error) {
        if(UString.escapeFormatting(value).length() > length) {
            throw new RuntimeException(__(this, error));
        }
    }

    /**
     * Asserts that a string is a valid regular expression.
     * @param regex The string value.
     * @param error The i18n key.
     * @return The original string.
     */
    public String assertRegex(String regex, String error) {
        if(!UInput.isValidRegex(regex)) {
            throw new RuntimeException(__(this, error));
        }
        return regex;
    }

    /**
     * Asserts that the string is an integer.
     * This will throw a {@link java.lang.RuntimeException} if the assertion fails.
     * @param value The string value.
     * @param lower The lower bound (inclusive). Null for no limit.
     * @param upper The upper bound (inclusive). Null for no limit.
     * @param error The i18n key.
     * @return The integer, as an integer.
     */
    public int assertInt(String value, Integer lower, Integer upper, String error) {
        try {
            int integer = Integer.valueOf(value);
            if(lower != null
                    && integer < lower) {
                throw new RuntimeException(__(this, error));
            }
            if(upper != null
                    && integer > upper) {
                throw new RuntimeException(__(this, error));
            }
            return integer;
        } catch(NumberFormatException exception) {
            throw new RuntimeException(__(this, error));
        }
    }

    /**
     * Asserts that the string is a long.
     * This will throw a {@link java.lang.RuntimeException} if the assertion fails.
     * @param value The string value.
     * @param lower The lower bound (inclusive). Null for no limit.
     * @param upper The upper bound (inclusive). Null for no limit.
     * @param error The i18n key.
     * @return The long, as a long.
     */
    public long assertLong(String value, Long lower, Long upper, String error) {
        try {
            long penis = Long.valueOf(value);
            if(lower != null
                    && penis < lower) {
                throw new RuntimeException(__(this, error));
            }
            if(upper != null
                    && penis > upper) {
                throw new RuntimeException(__(this, error));
            }
            return penis;
        } catch(NumberFormatException exception) {
            throw new RuntimeException(__(this, error));
        }
    }

    /**
     * Asserts that a valid duration can be parsed into milliseconds.
     * @param duration The duration.
     * @param error The i18n key.
     * @return The duration, in milliseconds.
     */
    public long assertDuration(String duration, String error) {
        long result = UTime.parseDuration(duration);
        if(result <= 0) {
            throw new RuntimeException(__(this, error));
        }
        return result;
    }

    /**
     * Asserts that the arguments provided contain at least one quoted string.
     * @param minimum The minimum occurrences. Null for 1.
     * @param error The i18n key.
     * @return The quotes.
     */
    public List<String> assertQuotes(Integer minimum, String error) {
        List<String> quotes = UInput.extractQuotedStrings(arguments);
        if(quotes.isEmpty() || (minimum != null
                && quotes.size() < minimum)) {

            throw new RuntimeException(__(this, error));
        }
        return quotes;
    }

    /**
     * Asserts that a member can be extracted from the identifier.
     * @param identifier The identifier, e.g. ID, mention, Name#Discrim.
     * @param error The i18n key.
     * @return The Member.
     */
    public Member assertMember(String identifier, String error) {
        Member member = UInput.getMember(guild, identifier);
        if(member == null) {
            throw new RuntimeException(__(this, error));
        }
        return member;
    }

    /**
     * Replies, and translates the reply.
     * @param message The i18n key.
     * @param format The formatters.
     */
    public void replyI18n(String message, Object... format) {
        replyRaw(__(this, message, format), (Object[]) format);
    }

    /**
     * Replies raw text without translating.
     * @param message The message.
     */
    public void replyRaw(String message, Object... format) {
        channel.sendMessageFormat(message, (Object[]) format).queue(null, Throwable::printStackTrace);
    }

    /**
     * Replies with an embed.
     * @param embed The embed.
     */
    public void replyEmbed(MessageEmbed embed) {
        channel.sendMessage(embed).queue(null, Throwable::printStackTrace);
    }

    /**
     * Replies with the syntax message.
     * @param command The command.
     */
    public void replySyntax(Command command) {
        replyI18n("error.generic_syntax_sub",
                command.getSubCommands().stream()
                        .map(Command::getName)
                        .collect(Collectors.joining(", ")));
    }

    private static boolean isParameter(String value) {
        return value.startsWith("-") && value.length() > 1;
    }

}
