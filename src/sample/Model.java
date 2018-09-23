package sample;

import com.fonis.entities.AbstractQuestion;
import com.fonis.entities.OpenQuestion;
import com.fonis.resources.Resources;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Model {

    public static List<AbstractQuestion> questions= new ArrayList<>();

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
