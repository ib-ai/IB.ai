/* Copyright 2017-2019 Arraying, Jarred Vardy <jarred.vardy@gmail.com>
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

package com.ibdiscord.command.commands.react;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.react.EmoteData;
import com.ibdiscord.data.db.entries.react.ReactionData;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class ReactionCommand extends Command {

    /**
     * Creates the command.
     */
    public ReactionCommand() {
        super("reaction",
                Set.of("react", "reactionroles", "rr"),
                CommandPermission.discord(Permission.MANAGE_ROLES),
                Set.of(new Add(),
                        new Delete()
                )
        );
        this.correctUsage = "reaction <create/delete> <channel ID> <message ID> <emoji> <role IDs... (1+)>";
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
            super("add", Set.of("a", "create", "c"));
        }

        /**
         * Adds to the data.
         * @param data The data.
         * @param emote The emote.
         * @param roleIDs List of roleIDs.
         */
        @Override
        protected void modifyData(ReactionData data, String emote, List<String> roleIDs) {
            String uniqueID = UUID.randomUUID().toString();
            data.set(emote, uniqueID);

            EmoteData emoteData = DataContainer.INSTANCE.getGravity().load(new EmoteData(uniqueID));
            roleIDs.forEach(emoteData::add);
            DataContainer.INSTANCE.getGravity().save(emoteData);
        }

        /**
         * Adds a reaction.
         * @param message The message.
         * @param emote The emote.
         */
        @Override
        protected void modifyMessage(Message message, Object emote) {
            if(emote instanceof Emote) {
                message.addReaction((Emote) emote).queue();
            } else {
                message.addReaction(emote.toString()).queue();
            }
        }

    }

    private static final class Delete extends ReactionManageCommand {

        /**
         * Creates the command.
         */
        private Delete() {
            super("delete", Set.of("d", "remove", "r"));
        }

        /**
         * Removes from the data.
         * @param data The data.
         * @param emote The emote.
         * @param roleIDs List of roleIDs.
         */
        @Override
        protected void modifyData(ReactionData data, String emote, List<String> roleIDs) {
            DataContainer.INSTANCE.getGravity().load(new EmoteData(data.get(emote).asString())).delete();
            data.unset(emote);
        }

        /**
         * Does absolutely nothing for now.
         * @param message The message.
         * @param emote The emote.
         */
        @Override
        protected void modifyMessage(Message message, Object emote) { }

    }

}
