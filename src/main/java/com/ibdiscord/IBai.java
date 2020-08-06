/* Copyright 2018-2020 Jarred Vardy <vardy@riseup.net>, Ray Clark, Arraying
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

package com.ibdiscord;

import com.ibdiscord.command.registry.CommandRegistrar;
import com.ibdiscord.command.registry.CommandRegistry;
import com.ibdiscord.data.LocalConfig;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.DataContainerPostgres;
import com.ibdiscord.exceptions.JavaVersionException;
import com.ibdiscord.i18n.LocaleException;
import com.ibdiscord.i18n.LocaliserHandler;
import com.ibdiscord.listeners.FilterListener;
import com.ibdiscord.listeners.GuildListener;
import com.ibdiscord.listeners.MessageListener;
import com.ibdiscord.listeners.MonitorListener;
import com.ibdiscord.listeners.ReactionListener;
import com.ibdiscord.listeners.ReadyListener;
import com.ibdiscord.utils.UFormatter;
import com.ibdiscord.utils.UJavaVersion;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import javax.security.auth.login.LoginException;

public enum IBai {

    /**
     *  Singleton instance of Bot.
     */
    INSTANCE;

    @Getter private LocalConfig config;
    @Getter private CommandRegistry commandRegistry;
    @Getter private Logger logger = LoggerFactory.getLogger(getClass());
    @Getter private JDA jda;

    /**
     * Entry point of the program.
     * @param args The arguments.
     * @throws JavaVersionException An exception in the Java version.
     */
    public static void main(String[] args) throws JavaVersionException {
        /* Checks Java version
         * Error thrown on version != 10 and terminates
         * Docker will handle the JRE10 dependency if executed properly
         */
        UJavaVersion.checkVersion();

        Thread.currentThread().setName("Main");
        IBai.INSTANCE.init();
    }

    /**
     * Initializes the bot.
     */
    private void init() {
        config = new LocalConfig();
        DataContainer.INSTANCE.connect();
        DataContainerPostgres.INSTANCE.connect();
        try {
            LocaliserHandler.INSTANCE.initialize(new File(config.getLangBase()));
        } catch(IOException | LocaleException exception) {
            exception.printStackTrace();
            return;
        }
        commandRegistry = new CommandRegistry();
        for(CommandRegistrar registrar : CommandRegistrar.KNOWN) {
            registrar.register(commandRegistry);
        }
        try {
            jda = new JDABuilder()
                    .setToken(config.getBotToken())
                    .setStatus(OnlineStatus.DO_NOT_DISTURB)
                    .setActivity(Activity.playing(String.format("v%s | %shelp",
                            config.getBotVersion(),
                            config.getStaticPrefix()))
                    )
                    .addEventListeners(new FilterListener(),
                            new GuildListener(),
                            new MessageListener(),
                            new MonitorListener(),
                            new ReactionListener(),
                            new ReadyListener()
                    )
                    .build();
            jda.setAutoReconnect(true);
            jda.awaitReady();
        } catch (LoginException | InterruptedException ex) {
            ex.printStackTrace();
        }
        UFormatter.makeASplash();
    }

}
