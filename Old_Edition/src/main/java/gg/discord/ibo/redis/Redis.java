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
package gg.discord.ibo.redis;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;

import gg.discord.ibo.configuration.Configuration;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.01.02
 */

/* Notes:
 *  Initial Redis setup.
 *  Singleton class.
 */

/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class Redis {
    private static Redis instance;
    private StatefulRedisConnection<String, String> connection;

    private Redis(){}

    public static Redis getInstance(){
        if(instance == null){
            instance = new Redis();
        }
        return instance;
    }

    public void setupRedis() throws IOException{
        if(connection != null){
            return;
        }

        //Generating a URI to be used by the Redis Connection as an address to connect to my server.
        RedisURI.Builder uri = RedisURI.Builder.redis(Configuration.getInstance().getRedisIP(),
                                                      Configuration.getInstance().getRedisPort())
                                               .withDatabase(1)
                                               .withPassword(Configuration.getInstance().getRedisPassword());

        RedisClient client = RedisClient.create(uri.build()); //Connecting with the URI object
        connection = client.connect(); //Establishing the connection
        //Connection object 'connection' to be used to make calls to the database(s).
    }

    public void initValues(JDA jda){
        connection.sync().set("totalGuilds", String.valueOf(jda.getGuilds().size()));
    }

    /**<p>
     * Updates the guild-specific hash.<br>
     * Within which is a boolean stating if the server is the developer's server,<br>
 *   * the guild's prefix, and the ID of the guild's modlog.</p>
     */
    public void manageGuildHash(Guild guild){
        String guildID = guild.getId();
        String isDevServer = "false";
        String prefix = null;

        try{
            if(guild.getId().equals(Configuration.getInstance().getDevServerID())){
                isDevServer = "true";
            }
            prefix = Configuration.getInstance().getDefaultBotPrefix();

        }catch(IOException ex){
            ex.printStackTrace();
        }


        connection.sync().hset("guild:" + guildID,"isDevServer",isDevServer);
        connection.sync().hset("guild:" + guildID,"botPrefix",prefix);
        connection.sync().hset("guild:" + guildID, "modLogID", "000");
    }

    /**<p>
     * For when the bot is no longer in a guild, purges the guild's hash from the system.</p>
     */
    public void deleteGuildHash(Guild guild){
        connection.sync().del("guild:" + guild.getId());
        connection.sync().del("guild:" + guild.getId() + ":bans");
    }

    public void updateGuild(Guild guild){
        if(!connection.sync().hexists("guild:" + guild.getId(), "botPrefix")){
            manageGuildHash(guild);
        }
    }

    public void guildTotalIncrement(){
        connection.sync().incr("totalGuilds");
    }

    public void guildTotalDecrement(){
        connection.sync().decr("totalGuilds");
    }

    public void addUserBan(Guild guild, String bannedUserID, String banCaseNumber, String banMessageID){
        String banSetKey = "guild:" + guild.getId() + ":bans";
        connection.sync().sadd(banSetKey, banCaseNumber + ";" + bannedUserID + "?" + banMessageID);
    }

    public void updateGuildModLogID(Guild guild, String newID){
        connection.sync().hset("guild:" + guild.getId(), "modLogID", newID);
    }

    public void updateGuildPrefix(Guild guild, String newPrefix){
        connection.sync().hset("guild:" + guild.getId(), "botPrefix", newPrefix);
    }

    public boolean createTag(Guild guild, String trigger, String output){
        String tagSetKey = "guild:" + guild.getId() + ":tags";
        if(connection.sync().hget(tagSetKey, trigger) != null){
            return false;

        }else{
            connection.sync().hset(tagSetKey, trigger, output);
            return true;
        }

    }

    public void deleteTag(Guild guild, String trigger){
        String tagSetKey = "guild:" + guild.getId() + ":tags";
        connection.sync().hdel(tagSetKey, trigger);
    }

    public Map<String, String> getTags(Guild guild){
        String tagSetKey = "guild:" + guild.getId() + ":tags";
        return connection.sync().hgetall(tagSetKey);
    }

    public Set<String> getAllBans(Guild guild){
        String banSetKey = "guild:" + guild.getId() + ":bans";
        return connection.sync().smembers(banSetKey);
    }

    public Long getCaseNumber(Guild guild){
        String banSetKey = "guild:" + guild.getId() + ":bans";
        return(connection.sync().scard(banSetKey) + 1);
    }

    public String getGuildPrefix(Guild guild){
        return connection.sync().hget("guild:" + guild.getId(), "botPrefix");
    }

    public boolean getGuildIsDevServer(Guild guild){
        return connection.sync().hget("guild:" + guild.getId(), "isDevServer").equals("true");
    }

    public String getGuildModLogID(Guild guild){
        return connection.sync().hget("guild:" + guild.getId(), "modLogID");
    }
}