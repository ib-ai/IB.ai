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

import de.arraying.kotys.JSON;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.02.18
 */
 
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class UtilSubjects{
    private static ArrayList<String> subjectList = new ArrayList<String>();
    private static JSON subjectsJSON;

    public static ArrayList<String> getSubjects(){
        return subjectList;
    }

    public static JSON getSubjectsJSON(){
        return subjectsJSON;
    }

    //TODO: THIS MIGHT NOT WORK?!
    public static void initSubjectList(){
        subjectsJSON = new JSON(read("SubjectList.JSON"));

        List<Object> subs = subjectsJSON.array("listOfSubjects").raw();
        ArrayList<String> outOfOrder = new ArrayList<>();

        for(Object o : subs){
            outOfOrder.add((String) o);
        }

        String[] listSize = new String[outOfOrder.size()];
        ArrayList<String> arrayListOfSortedSubjects = new ArrayList<>();
        arrayListOfSortedSubjects.addAll(bubbleAlphabet(outOfOrder.toArray(listSize)));
        subjectList = arrayListOfSortedSubjects;
    }

    public static List<String> bubbleAlphabet(String[] input){

        boolean condition = true;
        String temp;

        while(condition){
            condition = false;
            for(int i=0; i < input.length-1; i++){

                int compare = input[i].compareTo(input[i+1]);
                if(compare > 0){
                    temp = input[i];
                    input[i] = input[i+1];
                    input[i+1] = temp;
                    condition = true;
                }
            }
        }

        return Arrays.asList(input);
    }

    private static String read(String filePath){
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();

            while(line != null){
                stringBuilder.append(line);
                stringBuilder.append(System.lineSeparator());
                line = bufferedReader.readLine();
            }

            bufferedReader.close();
            return stringBuilder.toString();

        }catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public static void updateSubjectList(){
        subjectList = new ArrayList<String>();

        List<Object> subs = subjectsJSON.array("listOfSubjects").raw();
        for(Object o : subs){
            subjectList.add((String) o);
        }

        String jsonString = subjectsJSON.marshal();
        try{
            File newFile = new File("SubjectList.JSON");
            FileWriter fileWriter = new FileWriter(newFile, false);
            fileWriter.write(jsonString);
            fileWriter.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}