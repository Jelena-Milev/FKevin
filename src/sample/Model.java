package sample;

import com.fonis.entities.AbstractQuestion;
import com.fonis.entities.OpenQuestion;
import com.fonis.resources.Resources;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Model {

    // Prevents init of class
    private Model(){}

    public static List<AbstractQuestion> questions = new ArrayList<>();

    public static LinkedList<AbstractQuestion> getQuestionsByType(Resources.Entities questionType){
        LinkedList<AbstractQuestion> questions=new LinkedList<>();
        for (AbstractQuestion question: Model.questions) {
            if(question.getClass().equals(questionType.getEntityClass())){
                questions.add(question);
            }
        }
        return questions;
    }

    public static List<AbstractQuestion> loadRoundQuestions(List<AbstractQuestion> questions){
        List<AbstractQuestion> finalQuestions = new ArrayList<>();

        int low = 0; // 5
        int medium = 0; // 4
        int high = 0; // 3

        int counter = 0;
        Random random = new Random();
        while(counter < Resources.TOTAL_NUMBER_OF_QUESTIONS){
            int randomIndex = random.nextInt(questions.size());
            AbstractQuestion question = questions.get(randomIndex);
            if(question.getDifficulty() == Resources.QuestionDifficulty.LOW && low < Resources.QuestionDifficulty.LOW.getNumberOfQuestions()){
                finalQuestions.add(question);
                ++low;
                ++counter;
            }else if(question.getDifficulty() == Resources.QuestionDifficulty.MEDIUM && medium < Resources.QuestionDifficulty.MEDIUM.getNumberOfQuestions()){
                finalQuestions.add(question);
                ++medium;
                ++counter;
            }else if(question.getDifficulty() == Resources.QuestionDifficulty.HIGH && high < Resources.QuestionDifficulty.HIGH.getNumberOfQuestions()){
                finalQuestions.add(question);
                ++high;
                ++counter;
            }
            questions.remove(randomIndex);
        }

        return finalQuestions;
    }
}
