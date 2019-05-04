/**
 * Copyright 2017-2019 Arraying
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

package dev.vardy.punish;

import lombok.Getter;

public enum PunishmentType {

    /**
     * A verban warling.
     */
    WARN("Warning :raised_hand:", "???"),

    /**
     * A kick.
     */
    KICK("Kick :boot:", "???"),

    /**
     * A mute.
     */
    MUTE("Mute :zipper_mouth:", "Unmute :speaking_head:"),

    /**
     * A ban.
     */
    BAN("Ban :hammer:", "Unban :angel:");

    @Getter private final String displayInitial;
    @Getter private final String displayRevocation;

    /**
     * Creates a new punishment type.
     * @param displayInitial The display name for the initial punishment.
     * @param displayRevocation The display name for the revocation action.
     */
    PunishmentType(String displayInitial, String displayRevocation) {
        this.displayInitial = displayInitial;
        this.displayRevocation = displayRevocation;
    }

}
