package com.ibdiscord.input;

import com.ibdiscord.utils.objects.Tuple;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.util.Map;
import java.util.concurrent.*;

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
public enum InputHandler {

    /**
     * The singleton instance.
     */
    INSTANCE;

    private final Map<Long, Map<Long, Tuple<Input, ScheduledFuture>>> inputs = new ConcurrentHashMap<>();
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

    /**
     * Starts an input for a member.
     * @param member The member.
     * @param input The input.
     * @param initial The initial message.
     */
    public void start(Member member, Input input, Message initial) {
        Tuple<Input, ScheduledFuture> tuple = getFor(member);
        if(tuple != null) {
            throw new IllegalStateException("Already processing input, cannot start new input.");
        }
        ScheduledFuture future = executorService.schedule(() -> {
                    stop(member);
                    initial.getChannel().sendMessage("Input timed out.").queue();
                },
                input.getTimeout(),
                TimeUnit.MILLISECONDS
        );
        tuple = new Tuple<>(input, future);
        inputs.get(member.getGuild().getIdLong()).put(member.getUser().getIdLong(), tuple);
        input.initialize(initial);
    }

    /**
     * Offers input for a member.
     * @param member The member.
     * @param message The message.
     * @return If the member is currently inputting, this will return false.
     * When this is false, the input should NOT be used further in message handling,
     * such as command execution. If this can be used, true will be returned.
     */
    public synchronized boolean offer(Member member, Message message) {
        construct(member);
        Tuple<Input, ScheduledFuture> tuple = getFor(member);
        if(tuple == null) {
            return true;
        }
        if(message.getContentRaw().equalsIgnoreCase("cancel")) {
            stop(member);
            return false;
        }
        boolean pass = tuple.getA().offer(message);
        if(pass) {
            Input next = tuple.getA().getSuccessor();
            stop(member);
            if(next != null) {
                start(member, next, message);
            }
        }
        return false;
    }

    /**
     * Cancels an input for a member.
     * @param member The member.
     */
    private void stop(Member member) {
        Tuple<Input, ScheduledFuture> tuple = getFor(member);
        if(tuple == null) {
            return;
        }
        tuple.getB().cancel(true);
        inputs.get(member.getGuild().getIdLong()).remove(member.getUser().getIdLong());
    }

    /**
     * Gets the current input for the member.
     * @param member The member.
     * @return A tuple of input and executor task.
     */
    private Tuple<Input, ScheduledFuture> getFor(Member member) {
        construct(member);
        if(!inputs.containsKey(member.getGuild().getIdLong())) {
            return null;
        }
        return inputs.get(member.getGuild().getIdLong()).get(member.getUser().getIdLong());
    }

    /**
     * Constructs a new map if necessary.
     * @param member The member, used to access the guild.
     */
    private void construct(Member member) {
        inputs.putIfAbsent(member.getGuild().getIdLong(), new ConcurrentHashMap<>());
    }

}