package sample;

import com.fonis.entities.AbstractQuestion;
import com.fonis.entities.OpenQuestion;
import com.fonis.resources.Resources;
import com.fonis.services.ParsingService;
import com.fonis.services.ParsingServiceNeca;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Model {


    public static List<AbstractQuestion> questions= new LinkedList<>();
    public static ObservableList<AbstractQuestion> questionObservableList=FXCollections.observableList(Model.questions);

    public static void updateModelQuestion(ParsingServiceNeca parsingService){
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

    public static void updateObservableQuestions(ParsingServiceNeca parsingService){
        Model.questionObservableList.clear();
        Model.questionObservableList.addAll(parsingService.getEntitiesJsonAsList(Resources.Entities.OPEN_QUESTION));
        Model.questionObservableList.addAll(parsingService.getEntitiesJsonAsList(Resources.Entities.CLOSED_QUESTION));
    }

}
