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

/**
 * @author Arraying
 * @since 2018.09.17
 */

package com.ibdiscord.command;

import lombok.Getter;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import org.apache.commons.lang3.ArrayUtils;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public final class CommandContext {

    @Getter final Message message;
    @Getter private final Guild guild;
    @Getter private final MessageChannel channel;
    @Getter private final Member member;
    @Getter private final String[] arguments;
    @Getter private final Set<Option> options;

    private CommandContext(Message message, String[] arguments, Set<Option> options) {
        this.message = message;
        this.guild = message.getGuild();
        this.channel = message.getChannel();
        this.member = message.getMember();
        this.arguments = arguments;
        this.options = options;
    }

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
        System.out.println(toRemove);
        int decrementer = 0;
        for(int i : toRemove) {
            args = ArrayUtils.remove(args, i - decrementer); // the array size will shrink for every time something is removed
            decrementer++; // make sure the correct indexes are removed
        }
        return new CommandContext(message, args, options);
    }

    private static boolean isParameter(String value) {
        return value.startsWith("-") && value.length() > 1;
    }

    public void reply(String message, Object... format) {
        channel.sendMessageFormat(message, (Object[]) format).queue(null, Throwable::printStackTrace);
    }

    CommandContext clone(String[] arguments) {
        return new CommandContext(message, arguments, options);
    }
}
