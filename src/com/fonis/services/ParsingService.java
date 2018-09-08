package com.fonis.services;

import com.fonis.entities.Question;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ParsingService {

    private Gson gson;

    public ParsingService() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public List<Question> loadQuestionsFromJsonFileToList() {
        Type collectionType = new TypeToken<ArrayList<Question>>() {
        }.getType();
        List<Question> listOfQuestions = null;
        try (FileReader reader = new FileReader("data/questions.json")) {
            JsonObject jsonFileContent = gson.fromJson(reader, JsonObject.class);
            if (jsonFileContent != null && jsonFileContent.has("Questions")){
                JsonArray jsonArrayOfQuestions = jsonFileContent.getAsJsonArray("Questions");
                String arrayOfQuestionsString = gson.toJson(jsonArrayOfQuestions);
                listOfQuestions = gson.fromJson(arrayOfQuestionsString, collectionType);
            }
//            Second way of parsing jsonArray into LinkedList
//            for (JsonElement jsonQuestion :
//                    jsonArrayOfQuestions) {
//                listOfQuestions.add(gson.fromJson(jsonQuestion, Question.class));
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listOfQuestions;
    }
//    Loads questions from array which is value of a property named "Questions" into JsonArray and returns it.
    private JsonArray loadQuestionsFromJsonFileToJsonArray() {
        JsonArray jsonArrayOfQuestions = null;
        try (FileReader reader = new FileReader("data/questions.json")) {
            JsonObject jsonFileContent = gson.fromJson(reader, JsonObject.class);
            if (jsonFileContent != null && jsonFileContent.has("Questions"))
                jsonArrayOfQuestions = jsonFileContent.get("Questions").getAsJsonArray();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArrayOfQuestions;
    }

    public void addQuestionToJsonFile(Question newQuestion) throws Exception {
        String newQuestionAsJsonString = gson.toJson(newQuestion);
        JsonObject newQuestionAsJsonObject = gson.fromJson(newQuestionAsJsonString, JsonObject.class);

        JsonArray questionsInJsonArray = loadQuestionsFromJsonFileToJsonArray();

        if (questionsInJsonArray == null)
            questionsInJsonArray = new JsonArray();

        if (questionsInJsonArray.contains(newQuestionAsJsonObject))
            throw new Exception("This question already exist in the list!");
        questionsInJsonArray.add(newQuestionAsJsonObject);

        changeTheValueOfPropertyInJsonFile("Questions", questionsInJsonArray);
    }

    public void removeQuestionFromJsonFile(Question questionForRemoving) throws Exception {
        String removingQuestionAsJsonString = gson.toJson(questionForRemoving);
        JsonObject removingQuestionAsJsonObject = gson.fromJson(removingQuestionAsJsonString, JsonObject.class);

        JsonArray jsonArrayOfQuestions = loadQuestionsFromJsonFileToJsonArray();

        if (jsonArrayOfQuestions == null || jsonArrayOfQuestions.size() == 0)
            throw new Exception("List of questions is empty! There is no questions to be removed!");

        if (!jsonArrayOfQuestions.contains(removingQuestionAsJsonObject))
            throw new Exception("The question does not exist in the list of questions!");

        jsonArrayOfQuestions.remove(removingQuestionAsJsonObject);

        changeTheValueOfPropertyInJsonFile("Questions", jsonArrayOfQuestions);
    }



    private void changeTheValueOfPropertyInJsonFile(String propertyName, JsonArray newPropertyValue) throws Exception {
        FileReader reader = new FileReader("data/questions.json");
        JsonObject jsonFileContent = gson.fromJson(reader, JsonObject.class);
        reader.close();

//      If the file is empty, we have to create an object which we are going to write into the file.
        if (jsonFileContent == null)
            jsonFileContent = new JsonObject();

//      If an object from the file has a property named "Questions", we have to remove it in order to add it again
//      with a changed value. If the object has not such a property, there is nothing to be removed, and we only have
//      to add such a property.
        if (jsonFileContent.has(propertyName))
            jsonFileContent.remove(propertyName);

        jsonFileContent.add(propertyName, newPropertyValue);

        FileWriter writer = new FileWriter("data/questions.json");
        writer.write(gson.toJson(jsonFileContent));
        writer.close();
    }
}
