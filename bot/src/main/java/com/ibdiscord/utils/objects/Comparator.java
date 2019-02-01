package com.ibdiscord.utils.objects;

import com.ibdiscord.command.Command;

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
public final class Comparator implements java.util.Comparator<Command> {

    /**
     * Compares two commands by name in order to present commands alphabetically.
     * @param o1 The first command.
     * @param o2 The second command.
     * @return A comparison integer.
     */
    @Override
    public int compare(Command o1, Command o2) {
        return o1.getName().compareTo(o2.getName());
    }

}
