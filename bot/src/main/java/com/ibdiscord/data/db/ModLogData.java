package com.ibdiscord.data.db;

import de.arraying.gravity.data.types.TypeValue;

public class ModLogData extends TypeValue {

    private String guildID;

    public ModLogData(String guildID) {
        this.guildID = guildID;
    }

    @Override
    protected String getUniqueIdentifier() {
        return "guild_" + this.guildID + "_modLogID";
    }
}
