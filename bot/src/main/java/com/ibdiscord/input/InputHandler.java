package com.ibdiscord.input;

import com.ibdiscord.utils.objects.Tuple;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

import java.util.Map;
import java.util.concurrent.*;

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
