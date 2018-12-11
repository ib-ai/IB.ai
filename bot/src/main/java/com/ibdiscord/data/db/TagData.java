package com.ibdiscord.data.db;

import de.arraying.gravity.data.types.TypeMap;

import java.util.Set;

public class TagData extends TypeMap {

    private String guildID;

    public TagData(String guildID) {
        this.guildID = guildID;
    }

    @Override
    protected String getUniqueIdentifier() {
        return "guild_" + this.guildID + "_tags ";
    }
}
