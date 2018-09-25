package sample;

import com.fonis.entities.AbstractQuestion;
import com.fonis.entities.OpenQuestion;
import com.fonis.resources.Resources;
import com.fonis.services.ParsingService;
import com.fonis.services.ParsingServiceNeca;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Model {


    public static List<AbstractQuestion> questions= new LinkedList<>();

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

}
