/* Copyright 2017-2021 Arraying
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

package com.ibdiscord.button;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.requests.Requester;
import net.dv8tion.jda.internal.requests.RestActionImpl;
import net.dv8tion.jda.internal.requests.Route;
import okhttp3.RequestBody;

/**
 * Wrapper for an interaction callback.
 */
public final class ButtonCallbackAction extends RestActionImpl<Void> {

    private final int added;
    private final int removed;

    /**
     * Creates the callback.
     * @param api The API.
     * @param id The ID.
     * @param token The token.
     * @param added The number of roles added.
     * @param removed The number of roles removed.
     */
    public ButtonCallbackAction(JDA api, String id, String token, int added, int removed) {
        super(api, Route.post(String.format("/interactions/%s/%s/callback", id, token)).compile());
        this.added = added;
        this.removed = removed;
    }

    /**
     * Sends the correct payload.
     * @return The payload.
     */
    @Override
    protected RequestBody finalizeData() {
        DataObject dataObject = DataObject.empty();
        dataObject.put("type", 4);
        DataObject message = DataObject.empty();
        String content;
        if (added > 0 && removed == 0) {
            content = "Added " + added + " role(s).";
        } else if (removed > 0 && added == 0) {
            content = "Removed " + removed + " role(s).";
        } else if(added > 0 && removed > 0) {
            content = "Added " + added + " role(s) and removed " + removed + " role(s).";
        } else {
            content = "Did not update roles";
        }
        message.put("content", content);
        message.put("flags", 64); // Only send to the user.
        dataObject.put("data", message);
        return RequestBody.create(Requester.MEDIA_TYPE_JSON, dataObject.toJson());
    }
}
