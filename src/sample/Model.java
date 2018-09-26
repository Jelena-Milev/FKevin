package sample;

import com.fonis.entities.AbstractQuestion;
import com.fonis.resources.Resources;
import com.fonis.services.ParsingService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Model {

    // Prevents inti of class
    private Model(){}

    public static List<AbstractQuestion> questions= new LinkedList<>();
    public static ObservableList<AbstractQuestion> questionObservableList=FXCollections.observableList(Model.questions);

    public static void updateModelQuestion(ParsingService parsingService){
        Model.questions.clear();
        Model.questions.addAll(parsingService.getEntitiesJsonAsList(Resources.Entities.OPEN_QUESTION));
        Model.questions.addAll(parsingService.getEntitiesJsonAsList(Resources.Entities.CLOSED_QUESTION));
    }

    public static LinkedList<AbstractQuestion> getQuestionsByType(Resources.Entities questionType){
        LinkedList<AbstractQuestion> questions=new LinkedList<>();
        for (AbstractQuestion question: Model.questions) {
            if(question.getClass().equals(questionType.getEntityClass())){
                questions.add(question);
            }
        }
        return questions;
    }

    public static List<AbstractQuestion> loadRoundQuestions(){
        ParsingService parsingService = new ParsingService();
        List<AbstractQuestion> questions=parsingService.getEntitiesJsonAsList(Resources.Entities.OPEN_QUESTION);
        questions.addAll(parsingService.getEntitiesJsonAsList(Resources.Entities.CLOSED_QUESTION));
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

    public static int getNumberOfPoints(List<AbstractQuestion> questions){
        int totalPoints = 0;
        for(AbstractQuestion question : questions){
            totalPoints += question.getPointsAfterValidation();
        }
        return totalPoints;
    }

    public static void updateObservableQuestions(ParsingService parsingService){
        Model.questionObservableList.clear();
        Model.questionObservableList.addAll(parsingService.getEntitiesJsonAsList(Resources.Entities.OPEN_QUESTION));
        Model.questionObservableList.addAll(parsingService.getEntitiesJsonAsList(Resources.Entities.CLOSED_QUESTION));
    }

}
