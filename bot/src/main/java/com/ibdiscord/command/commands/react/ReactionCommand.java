package com.ibdiscord.command.commands.react;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.entries.ReactionData;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;

import java.util.Set;

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
public final class ReactionCommand extends Command {

    /**
     * Creates the command.
     */
    public ReactionCommand() {
        super("reaction",
                Set.of("react", "reactionroles", "rr"),
                CommandPermission.discord(Permission.MANAGE_ROLES),
                Set.of(new Add(), new Delete())
        );
        this.correctUsage = "reaction <add/remove> <channel ID> <message ID> <emoji ID>";
    }

    /**
     * Shows the usage.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        sendUsage(context);
    }

    private static final class Add extends ReactionManageCommand {

        /**
         * Creates the command.
         */
        private Add() {
            super("add", Set.of("a", "create"));
        }

        /**
         * Adds to the data.
         * @param data The data.
         * @param emote The emote.
         * @param role The role.
         */
        @Override
        protected void modifyData(ReactionData data, Emote emote, Role role) {
            data.set(emote.getId(), role.getId());
        }

        /**
         * Adds a reaction.
         * @param message The message.
         * @param emote The emote.
         */
        @Override
        protected void modifyMessage(Message message, Emote emote) {
            message.addReaction(emote).queue();
        }

    }

    private static final class Delete extends ReactionManageCommand {

        /**
         * Creates the command.
         */
        private Delete() {
            super("delete", Set.of("d", "remove"));
        }

        /**
         * Removes from the data.
         * @param data The data.
         * @param emote The emote.
         * @param role The role.
         */
        @Override
        protected void modifyData(ReactionData data, Emote emote, Role role) {
            data.unset(emote.getId());
        }

        /**
         * Removes a reaction.
         * @param message The message.
         * @param emote The emote.
         */
        @Override
        protected void modifyMessage(Message message, Emote emote) {
            message.clearReactions().queue();
        }

    }

}
