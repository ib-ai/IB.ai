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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gg.discord.ibo;

import gg.discord.ibo.utils.UtilMassKicker;
import gg.discord.ibo.utils.UtilSplasher;

import java.io.IOException;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.01.02
 */

/* Notes:
 *  Main class in ibbot - Starts init process.
 *  Sets up database and config files.
 *  Throws up a splash screen.
 */

/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class Main {

    /**
     * The app's main class, beginning startup chain and showing a splashscreen in the console.
     * @param args Arguments passed on app startup. Here, none are used.
     * @see IBBot#startup()
     */
    public static void main(String[] args) throws IOException{
        IBBot.startup();
        Thread.currentThread().setName("Main");
        UtilSplasher.makeItRain();

        if(!(args.length == 0)) {
            if(args[0] == "purge") {

                UtilMassKicker kicker = new UtilMassKicker();
                kicker.create();
            }
        }
    }
}