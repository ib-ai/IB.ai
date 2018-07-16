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

/**
 * @author pants
 * @since 2018.02.18
 */
 
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class UtilWhiteSpace{

    private int width;
    private String column1 = null;
    private String column2 = null;
    private String column3 = null;

    public UtilWhiteSpace(int width){
        this.width = width;
    }

    public UtilWhiteSpace(int width, String column1, String column2){
        this.width = width;
        this.column1 = column1;
        this.column2 = column2;
    }

    public UtilWhiteSpace(int width, String column1, String column2, String column3){
        this.width = width;
        this.column1 = column1;
        this.column2 = column2;
        this.column3 = column3;
    }

    public void setwidth(int newWidth){
        width = newWidth;
    }

    public String s(){
        StringBuilder finalSpacesAft1 = new StringBuilder();
        StringBuilder finalSpacesAft2 = new StringBuilder();
        StringBuilder finalSpaces = new StringBuilder();
        StringBuilder finalOutput = new StringBuilder();

        if(column3 == null){
            int quantOfSpaces = width - column1.length();
            for(int i=0; i < quantOfSpaces; i++){
                finalSpaces.append(" ");
            }
            finalOutput.append(column1)
                    .append(finalSpaces.toString())
                    .append(column2);

            return finalOutput.toString();
        }else{
            int quantOfSpacesAft1 = width - column1.length();
            int quantOfSpacesAft2 = width - column2.length();
            for(int i=0; i < quantOfSpacesAft1; i++){
                finalSpacesAft1.append(" ");
            }
            for(int i=0; i < quantOfSpacesAft2; i++){
                finalSpacesAft2.append(" ");
            }

            finalOutput.append(column1)
                    .append(finalSpacesAft1.toString())
                    .append(column2)
                    .append(finalSpacesAft2.toString())
                    .append(column3);

            return finalOutput.toString();
        }
    }

    public String s(String newColumn1, String newColumn2){
        StringBuilder finalSpaces = new StringBuilder();
        StringBuilder finalOutput = new StringBuilder();
        column1 = newColumn1;
        column2 = newColumn2;

        int quantOfSpaces = width - column1.length();
        for(int i=0; i < quantOfSpaces; i++){
            finalSpaces.append(" ");
        }

        finalOutput.append(column1)
                .append(finalSpaces.toString())
                .append(column2);

        return finalOutput.toString();
    }

    public String s(String newColumn1, String newColumn2, String newColumn3){
        StringBuilder finalSpacesAft1 = new StringBuilder();
        StringBuilder finalSpacesAft2 = new StringBuilder();
        StringBuilder finalOutput = new StringBuilder();

        column1 = newColumn1;
        column2 = newColumn2;
        column3 = newColumn3;

        int quantOfSpacesAft1 = width - column1.length();
        int quantOfSpacesAft2 = width - column2.length();
        for(int i=0; i < quantOfSpacesAft1; i++){
            finalSpacesAft1.append(" ");
        }
        for(int i=0; i < quantOfSpacesAft2; i++){
            finalSpacesAft2.append(" ");
        }

        finalOutput.append(column1)
                .append(finalSpacesAft1.toString())
                .append(column2)
                .append(finalSpacesAft2.toString())
                .append(column3);

        return finalOutput.toString();
    }
}