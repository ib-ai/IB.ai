/* Copyright 2022 Arraying
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

package com.ibdiscord.listeners;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class QualityListener extends ListenerAdapter {

    // Can be obtained with https://emn178.github.io/online-tools/sha256.html.
    private static final String[] DENY_LIST_HASHES = new String[] {
        "6e41afc31f5929349ce19d25075301b6fdb83ed65796964530c58eb7cc367c8a",
        "a5aa0500698da39f398d20a41d8c85ddf1e6f7eef07a9211e1e73b952e525ea2",
        "c48a1fe24f84cdab28676c1f1113a2fbbe84c704434293829c02e86c08b7193a",
        "6c2cdeb47253b1969b117c901b6b0c0745d6da82c43ec80bd9d2b6d2e8466393",
    };

    /**
     * In order for communities to thrive, they must adhere to certain qualities.
     * StackOverflow strives to provide a high quality environment to avoid what is happening to Quora happening there.
     * In order to facilitate the environment that inquiring IB learners require, it is of utmost importance to do the same.
     * The purpose of this is to remove sub-sh!tpost tier posts that do not break the rules but significantly negatively
     * impact the flourishing community discourse taking part in many parts of the server.
     * @param event The message event.
     */
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException exception) {
            exception.printStackTrace(); // We do not need to handle this error, console log is fine.
            return;
        }
        // Hash with hex string.
        byte[] candidateDigest = digest.digest(event.getMessage().getContentRaw().getBytes());
        String candidateHash = byteArrayToHex(candidateDigest);
        // Check if there is a match.
        for (String deniedHash : DENY_LIST_HASHES) {
            if (candidateHash.equals(deniedHash)) {
                event.getMessage().delete().queue(null, Throwable::printStackTrace);
                break;
            }
        }
    }

    /**
     * Converts a byte array to lower hex string.
     * Compatible with https://emn178.github.io/online-tools/sha256.html .
     * @param input The bytes.
     * @return The hex string.
     */
    private String byteArrayToHex(byte[] input) {
        StringBuilder builder = new StringBuilder();
        for (byte b : input) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

}
