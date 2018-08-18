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
package gg.discord.ibo.configuration;

import de.arraying.kotys.JSON;
import de.arraying.kotys.JSONField;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.01.02
 */

/* Notes:
 *  Class responsible for setting up config to be used by bot initialisation.
 *  Contains static instances of certain variables used by bot.
 *  Singleton class.
 */

/* * * * * * * * * * * * * * * * * * * * * * * * * * * */


public class Configuration {
    private static Configuration instance;
    private static JSON configJSON;

    @JSONField(key = "botAuthor")          private String botAuthor;
    @JSONField(key = "botVersion")         private String botVersion;
    @JSONField(key = "defaultBotPrefix")   private String defaultBotPrefix;
    @JSONField(key = "botGithubLink")      private String botGithubLink;
    @JSONField(key = "botGame")            private String botGame;

    @JSONField(key = "redisIP")            private String redisIP;
    @JSONField(key = "redisPort")          private int    redisPort;
    @JSONField(key = "redisPassword")      private String redisPassword;

    @JSONField(key = "devID")              private String devID;
    @JSONField(key = "devServerID")        private String devServerID;

    @JSONField(key = "botToken")           private String botToken;
    @JSONField(key = "botBetaToken")       private String botBetaToken;
    @JSONField(key = "welcomeChannelID")   private String welcomeChannelID;
    @JSONField(key = "mainServerID")       private String mainServerID;
    @JSONField(key = "mutedRoleID")        private String mutedRoleID;


    private Configuration(){}

    public static Configuration getInstance() throws IOException{
        if(instance == null){
            configJSON = new JSON(read("Configuration.JSON"));
            instance = configJSON.marshal(Configuration.class);
        }
        return instance;
    }

    private static String read(String path) throws IOException{
        String finalJSON = null;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

        try{
            StringBuilder builder1 = new StringBuilder();
            String line = bufferedReader.readLine();

            while(line != null){
                builder1.append(line);
                builder1.append(System.lineSeparator());
                line = bufferedReader.readLine();
            }
            finalJSON = builder1.toString();

        }catch (IOException ex){
            ex.printStackTrace();
        }finally{
            bufferedReader.close();
        }

        return finalJSON;
    }

    public String getBotAuthor(){
        return botAuthor;
    }

    public String getBotVersion(){
        return botVersion;
    }

    public String getDefaultBotPrefix(){
        return defaultBotPrefix;
    }

    public String getBotGithubLink(){
        return botGithubLink;
    }

    public String getBotGame(){
        return botGame;
    }

    public String getRedisIP(){
        return redisIP;
    }

    public int getRedisPort(){
        return redisPort;

    }

    public String getRedisPassword(){
        return redisPassword;
    }

    public String getDevID(){
        return devID;
    }

    public String getDevServerID(){
        return devServerID;
    }

    public String getBotToken(){
        return botToken;
    }

    public String getBotBetaToken(){
        return botBetaToken;
    }

    public String getWelcomeChannelID(){
        return welcomeChannelID;
    }

    public String getMainServerID(){
        return mainServerID;
    }

    public String getMutedRoleID(){
        return mutedRoleID;
    }

    //Example of how this setup is mutable
    //Keeping this here incase I need it later tm

    /*

    private static void write(String jsonString){
        try{
            File configFile = new File("Configuration.JSON");
            FileWriter fw = new FileWriter(configFile, false);
            fw.write(jsonString);
            fw.close();

        }catch (IOException ex){
            ex.printStackTrace();
        }

    }

    public void setBotAuthor(String botAuthor){
        this.botAuthor = botAuthor;
        configJSON.put("botAuthor", botAuthor);
        write(configJSON.marshal());
    }

    */
}