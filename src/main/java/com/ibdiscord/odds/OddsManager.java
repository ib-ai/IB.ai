/* Copyright 2018-2020 Jarred Vardy
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

package com.ibdiscord.odds;

import com.ibdiscord.IBai;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.utils.UDatabase;
import com.ibdiscord.utils.UFormatter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.PrivateChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class OddsManager {

    /**
     * List of bets between users that are yet to be resolved.
     */
    private static ArrayList<Bet> pendingBets = new ArrayList<>();

    /**
     * Private constructor to prevent instantiation of "static" class.
     */
    private OddsManager() { }

    /**
     * Used to create a new bet between two users.
     * The new bet is set with the odds and both users' IDs.
     * The users' guesses are set once they DM the bot with guesses.
     * @param context The command context of the bet creation command that was called.
     * @param userA The ID of the first user.
     * @param userB The ID of the second user.
     * @param denominator The 'odds' of the bet being fulfilled.
     */
    public static void newBet(CommandContext context, String userA, String userB, int denominator) {
        boolean userAlreadyEngaged = pendingBets.stream()
                .anyMatch(bet -> bet.getUserA().equals(userA)
                        || bet.getUserA().equals(userB)
                        || bet.getUserB().equals(userA)
                        || bet.getUserB().equals(userB));

        if(!userAlreadyEngaged) {
            pendingBets.add(
                    new Bet(userA,
                        userB,
                        denominator,
                        context.getGuild().getId(),
                        context.getChannel().getId()
                    )
            );
            context.reply("New odds have been set! %s and %s, please DM me your guesses between 1 and %d. "
                            + "If both of you send me the same number, the forfeit must be fulfilled. "
                            + "Use `%sodds cancel` to cancel.",
                    context.getMember().getAsMention(),
                    context.getMessage().getMentionedMembers().get(0).getAsMention(),
                    denominator,
                    UDatabase.getPrefix(context.getGuild())
            );
        } else {
            context.reply("One of you are already engaged in an odds. Use `%sodds cancel` to cancel "
                + "pending odds.", UDatabase.getPrefix(context.getGuild()));
        }
    }

    /**
     * Used to handle new guesses sent by users over the bot's DMs.
     * @param channel The DM channel the user guessed from.
     * @param userID The ID of the user sending the DM.
     * @param rawGuess The string sent by the user.
     */
    @SuppressWarnings("ConstantConditions")
    public static void newGuess(PrivateChannel channel, String userID, String rawGuess) {
        int guess = 0;
        boolean valid;
        try {
            guess = Integer.parseInt(rawGuess);
            valid = true;
        } catch(NumberFormatException ex) {
            valid = false;
        }

        boolean finalValid = valid;
        int finalGuess = guess;
        pendingBets.stream()
                .filter(bet -> bet.getUserA().equals(userID) || bet.getUserB().equals(userID))
                .forEach(bet -> {
                    if(!finalValid) {
                        channel.sendMessage(String.format("Your guess must be an integer between 1 and %d.",
                                bet.getProbabilityDenominator())
                        ).queue();
                        return;
                    }

                    if(0 >= finalGuess || finalGuess > bet.getProbabilityDenominator()) {
                        channel.sendMessage(String.format("Your guess must be between 1 and %d.",
                                bet.getProbabilityDenominator())
                        ).queue();
                        return;
                    }

                    if(bet.getUserA().equals(userID)) {
                        bet.setGuessUserA(finalGuess);
                    } else {
                        bet.setGuessUserB(finalGuess);
                    }

                    if(bet.getGuessUserA() != 0 && bet.getGuessUserB() != 0) {
                        Guild guild = IBai.getJda().getGuildById(bet.getInitialGuild());
                        String message;

                        if(bet.getGuessUserA() == bet.getGuessUserB()) {
                            message = String.format("ODDS MATCHED! You both guessed %d. Better get it done.)",
                                    finalGuess
                            );
                        } else {
                            message = String.format("ODDS FAILED. %s guessed `%d` and %s guessed `%d`. "
                                         + "Better luck next time.",
                                    UFormatter.formatMember(guild.getMemberById(bet.getUserA()).getUser()),
                                    bet.getGuessUserA(),
                                    UFormatter.formatMember(guild.getMemberById(bet.getUserB()).getUser()),
                                    bet.getGuessUserB()
                            );
                        }

                        assert guild != null;
                        guild.getTextChannelById(bet.getInitialChannel())
                                .sendMessage(message)
                                .queue();
                        bet.setFulfilled(true);
                    }
                });

        // Remove bet(s) that was fulfilled
        pendingBets.removeAll(pendingBets.stream()
                .filter(Bet::isFulfilled)
                .collect(Collectors.toList())
        );
    }

    /**
     * Used to cancel bets that are pending and unfulfilled.
     * @param context The command context of the bet cancellation command that was called.
     * @param userID The ID of the user cancelling the bet.
     */
    @SuppressWarnings("ConstantConditions")
    public static void cancelBet(CommandContext context, String userID) {
        // Due to users only being allowed one pending bet at a time,
        // 'priorBet' should always only contain one or zero element.
        List<Bet> priorBet = pendingBets.stream()
                .filter(bet -> bet.getUserA().equals(userID) || bet.getUserB().equals(userID))
                .collect(Collectors.toList());

        if(priorBet.isEmpty()) {
            context.reply("You have no pending odds.");
        } else {
            pendingBets.removeAll(priorBet);
            context.reply("Successfully canceled odds between %s and %s.",
                 UFormatter.formatMember(context.getGuild().getMemberById(priorBet.get(0).getUserA()).getUser()),
                 UFormatter.formatMember(context.getGuild().getMemberById(priorBet.get(0).getUserB()).getUser())
            );
        }
    }
}
