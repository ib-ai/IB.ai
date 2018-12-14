package com.ibdiscord.data.db;

import de.arraying.gravity.data.types.TypeMap;

public class BannedUserData extends TypeMap {

    private String userID;

    public BannedUserData(String userID) {
        this.userID = userID;
    }

    @Override
    protected String getUniqueIdentifier() {
        return "ban_" + userID;
    }
}
