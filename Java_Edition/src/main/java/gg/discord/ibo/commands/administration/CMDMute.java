package gg.discord.ibo.commands.administration;

import gg.discord.ibo.commands.CommandAbstract;
import gg.discord.ibo.configuration.Configuration;
import gg.discord.ibo.redis.Redis;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.io.IOException;
import java.util.Objects;

public class CMDMute extends CommandAbstract{

    @Override
    public boolean safe(GuildMessageReceivedEvent event,
                        Guild guild,
                        User user){

        try{
            return (guild.getMember(user).getPermissions().contains(Permission.KICK_MEMBERS))
                    || Objects.equals(user.getId(), Configuration.getInstance().getDevID());

        }catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void execute(GuildMessageReceivedEvent event,
                        Guild guild,
                        User user,
                        Message message,
                        String[] args,
                        String raw){

        try{
            guild.getMemberById(args[0]);
        }catch(Exception ex){

            if(message.getMentionedUsers().isEmpty()){
                event.getChannel().sendMessage("Correct usage: `" + Redis.getInstance().getGuildPrefix(guild) + "Mute @User` or [Length in Hours]`"
                        + Redis.getInstance().getGuildPrefix(guild) + "Mute [UserID] [Length in Hours]`").queue();
                return;
            }
        }

        int length;
        try{
            length = Integer.parseInt(args[1]);
        }catch(Exception ex){

            event.getChannel().sendMessage("Correct usage: `" + Redis.getInstance().getGuildPrefix(guild) + "Mute @User` or `"
                    + Redis.getInstance().getGuildPrefix(guild) + "Mute [UserID]`").queue();
            return;
        }

        User userToMute;
        if(!(message.getMentionedUsers().isEmpty())){
            userToMute = message.getMentionedUsers().get(0);

        }else{
            userToMute = guild.getMemberById(args[0]).getUser();
        }


        String mutedRoleID = null;
        try{
            mutedRoleID = Configuration.getInstance().getMutedRoleID();
        }catch(IOException ex){

            ex.printStackTrace();
            event.getChannel().sendMessage("There's a problem with the 'Muted' role's ID in the system.").queue();
            return;
        }

        Role mutedRole = null;
        for(Role role : guild.getRoles()){
            if(role.getId().equals(mutedRoleID)){
                mutedRole = role;
            }
        }

        // If user has muted role already, toggle it
        if(mutedRole != null){
            for (Role role : guild.getMember(userToMute).getRoles()){
                if (role == mutedRole) {
                    guild.getController().removeSingleRoleFromMember(guild.getMember(userToMute), role).complete();
                    event.getChannel().sendMessage("User " + userToMute.getName() + userToMute.getDiscriminator() + " has been unmuted.").queue();
                    return;
                }
            }
        }else{
            return;
        }

        guild.getController().addSingleRoleToMember(guild.getMember(userToMute), mutedRole).complete();
        event.getChannel().sendMessage("User " + userToMute.getName() + userToMute.getDiscriminator() + " has been muted.").queue();
    }

    @Override
    public void post(GuildMessageReceivedEvent event,
                     Guild guild,
                     User user,
                     Message message,
                     String[] args,
                     String raw){
        ///
    }

    @Override
    public void onPermissionFailure(GuildMessageReceivedEvent event,
                                    Guild guild,
                                    User user,
                                    Message message,
                                    String[] args,
                                    String raw){
        event.getChannel().sendMessage("This command requires the `Kick Members` permission. " + user.getAsMention()).queue();
    }
}
