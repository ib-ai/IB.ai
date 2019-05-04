/**
 * Copyright 2017-2019 Jarred Vardy
 *
 * This file is part of LoyalBot.
 *
 * LoyalBot is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LoyalBot is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LoyalBot. If not, see http://www.gnu.org/licenses/.
 */

package dev.vardy.startup.tasks;

import dev.vardy.LoyalBot;
import dev.vardy.data.LocalConfig;
import dev.vardy.listeners.*;
import dev.vardy.startup.AbstractStartupTask;
import lombok.Getter;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

public final class StartBot extends AbstractStartupTask {

    @Getter private static JDA jda;

    /**
     * Creates the task.
     */
    public StartBot() {
        super("Start-Bot");
    }

    /**
     * Attempts to start the bot.
     * @throws Exception Any exception.
     */
    @Override
    public void doTask() throws Exception {
        LocalConfig localConfig = LoyalBot.INSTANCE.getConfig();

        // TODO: Move to proper bot instantiater
        jda = new JDABuilder(AccountType.BOT)
                .setToken(localConfig.getBotToken())
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setGame(Game.playing(String.format("v%s | %shelp", localConfig.getBotVersion(), localConfig.getStaticPrefix())))
                .addEventListener(new GuildListener(), new MessageListener(), new MonitorListener(), new ReactionListener(), new ReadyListener())
                .build();
        jda.setAutoReconnect(true);
        jda.awaitReady();
    }

}
