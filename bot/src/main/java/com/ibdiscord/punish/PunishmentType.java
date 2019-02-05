package com.ibdiscord.punish;

import lombok.Getter;

/**
 * Copyright 2019 Arraying
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
