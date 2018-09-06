package com.fonis.services;

import com.fonis.entities.Question;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ParsingService {

    private Gson gson;

    public ParsingService() {
        this.gson=new GsonBuilder().setPrettyPrinting().create();
    }

    public List<Question> loadQuestionsFromJsonFileToList(){
        Type collectionType=new TypeToken<ArrayList<Question>>(){}.getType();
        List<Question> listOfQuestions=null;
        try (FileReader reader=new FileReader("data/questions.json")){
            listOfQuestions=gson.fromJson(reader, collectionType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listOfQuestions;
    }

    public void addQuestionToJsonFile(Question newQuestion) throws Exception {
        List<Question> listOfQuestions = loadQuestionsFromJsonFileToList();

        if (listOfQuestions == null)
            listOfQuestions = new ArrayList<>();

        if (listOfQuestions.contains(newQuestion))
            throw new Exception("This question already exist in the list!");

        listOfQuestions.add(newQuestion);

        FileWriter writer = new FileWriter("data/questions.json");
        writer.write(gson.toJson(listOfQuestions));
        writer.close();
    }

    public void removeQuestionFromJsonFile(Question questionForRemoving) throws Exception {
        List<Question> listOfQuestions= loadQuestionsFromJsonFileToList();

        if(listOfQuestions==null || listOfQuestions.size()==0)
            throw new Exception("List of questions is empty! There is no questions to be removed!");

        if(!listOfQuestions.contains(questionForRemoving))
            throw new Exception("Question given does not exist in the list of questions!");

        listOfQuestions.remove(questionForRemoving);

        FileWriter writer=new FileWriter("data/questions.json");
        writer.write(gson.toJson(listOfQuestions));
        writer.close();
    }


}
