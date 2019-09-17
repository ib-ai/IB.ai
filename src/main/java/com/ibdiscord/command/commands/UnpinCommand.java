/*
 LICENSE HEADER
*/

package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;

import java.util.Set;

public class UnpinCommand extends Command {

    /**
     * Creates a new Pin command.
     */
    public UnpinCommand() {
        super("unpin",
                Set.of(),
                CommandPermission.discord(),
                Set.of()
        );

        this.correctUsage = "&unpin <messageID>";
    }

    @Override
    protected void execute(CommandContext context) {
        Role helperRole = context.getGuild().getRolesByName("Helper", true).get(0);
        if(!context.getMember().getRoles().contains(helperRole) || helperRole == null) {
            context.reply("You must be a helper to use this command.");
            return;
        }

        if(context.getArguments().length == 0) {
            sendUsage(context);
            return;
        }

        Message message = context.getChannel().getHistory().getMessageById(context.getArguments()[0]);
        if(message == null) {
            context.reply("Invalid message ID provided. You must be in the same channel as the message.");
        } else {
            message.unpin().queue();
        }
    }
}
