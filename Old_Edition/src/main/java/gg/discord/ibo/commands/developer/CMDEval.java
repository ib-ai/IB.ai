/*******************************************************************************
 * Copyright 2018 pants
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

/* * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gg.discord.ibo.commands.developer;

import gg.discord.ibo.commands.CommandAbstract;

import gg.discord.ibo.configuration.Configuration;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.05.01
 */
 
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class CMDEval extends CommandAbstract{
    @Override
    public boolean safe(GuildMessageReceivedEvent event, Guild guild, User user){
        try{
            if(Objects.equals(user.getId(), Configuration.getInstance().getDevID())){
                return true;
            }

        }catch(IOException e){
            e.printStackTrace();
            return false;
        }

        return false;
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, Guild guild, User user, Message message, String[] args, String raw){

        String code = event.getMessage().getRawContent();
        if(code.contains(" ")){
            code = code.substring(code.indexOf(" ")).trim();
        }

        try{
            System.out.println("Executing Script!" + code);
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
            engine.eval("var imports = new JavaImporter(java.io, java.lang, java.util, Packages.net.dv8tion.jda.core, "
                    + "Packages.net.dv8tion.jda.core.entities, Packages.net.dv8tion.jda.core.managers);");
            engine.put("event", event);
            Object result = null;
            result = engine.eval("(function() { with (imports) {\n" + code + "\n} })();");
            String response = result == null ? "Executed successfully" : result.toString();

            if(response.length() > 2000) {
                response = response.substring(0, 1999);
            }

            event.getChannel().sendMessage(response).queue();

        }catch(ScriptException exception){
            String exceptionString = "```" + Arrays.toString(exception.getStackTrace()) + "```";

            boolean lengthProblem = false;
            StringBuilder newBuilder = new StringBuilder();
            if(exceptionString.length() >= 2000) {
                exceptionString = exceptionString.substring(0, 1990);

                newBuilder.append(exceptionString);
                newBuilder.append("`");
                lengthProblem = true;
            }

            if(lengthProblem){
                exceptionString = newBuilder.toString();
            }

            event.getChannel().sendMessage(exceptionString).queue();
            exception.printStackTrace();
        }
    }

    @Override
    public void post(GuildMessageReceivedEvent event, Guild guild, User user, Message message, String[] args, String raw) {

    }

    @Override
    public void onPermissionFailure(GuildMessageReceivedEvent event, Guild guild, User user, Message message, String[] args, String raw) {
        event.getChannel().sendMessage("You are not a developer.").queue();
    }
}