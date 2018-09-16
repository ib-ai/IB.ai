/*******************************************************************************
 * Copyright 2018 pants
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.ibdiscord.data.db.coordinator;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.09.14
 */

public class Prefixion {

    public static String getPrefixedKey(DataType type, String key) {
        return getPrefixedKey(type, key, null, null);
    }

    public static String getPrefixedKey(DataType type, String key, String keyTwo) {
        return getPrefixedKey(type, key, keyTwo, null);
    }

    public static String getPrefixedKey(DataType type, String key, String keyTwo, String keyThree) {
        String returnVal = type.getTypePrefix() + "_" + key;

        if(keyTwo != null) {
            returnVal = returnVal + "_" + keyTwo;
        }

        if(keyThree != null) {
            returnVal = returnVal + "_" + keyThree;
        }

        return returnVal;
    }
}
