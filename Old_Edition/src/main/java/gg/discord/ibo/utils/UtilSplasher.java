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
package gg.discord.ibo.utils;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

import gg.discord.ibo.configuration.Configuration;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author pants
 * @since 2018.01.02
 */

/* Notes:
 *  Contains splashscreen method to throw on startup.
 */
 
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class UtilSplasher {
    public static void makeItRain() throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("SplashScreen.txt"));

        try {
            String line = br.readLine();

            while(line != null){
                if(line.contains("AUTHOR")){
                    line = line.replaceAll("AUTHOR", Configuration.getInstance().getBotAuthor());
                }
                if(line.contains("VERSION")){
                    line = line.replaceAll("VERSION", Configuration.getInstance().getBotVersion());
                }

                System.out.println(line);
                line = br.readLine();
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}