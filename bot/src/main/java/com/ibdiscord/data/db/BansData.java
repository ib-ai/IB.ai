package com.ibdiscord.data.db;

import de.arraying.gravity.data.types.TypeSet;

public class BansData extends TypeSet {

    private String guildID;

    public BansData(String guildID) {
        this.guildID = guildID;
    }

    @Override
    protected String getUniqueIdentifier() {
        return "guild_" + this.guildID + "_bans";
    }
}
