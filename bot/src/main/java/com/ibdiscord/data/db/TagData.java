package com.ibdiscord.data.db;

import de.arraying.gravity.data.types.TypeMap;

import java.util.Set;

public class TagData extends TypeMap {
    @Override
    protected String getUniqueIdentifier() {
        //TODO: Add guild ID
        return "guild_auto-responds";
    }
}
