package com.fonis.gui.controllers;

import com.fonis.entities.AbstractQuestion;
import com.fonis.entities.ClosedQuestion;
import com.fonis.entities.OpenQuestion;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.xml.soap.Text;
import java.net.URL;
import java.util.*;

public class QuizStartController implements Initializable {
    @FXML
    HBox openQuesionAnswer;
    @FXML
    VBox closedQuestionAnswer;
    @FXML
    Text questionText;
    @FXML
    RadioButton possibleAns1;
    @FXML
    RadioButton possibleAns2;
    @FXML
    RadioButton possibleAns3;
    @FXML
    RadioButton possibleAns4;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        openQuesionAnswer.setVisible(false);
        closedQuestionAnswer.setVisible(false);
    }

    public void showQuestion(AbstractQuestion question){
        questionText.setTextContent(question.getQuestionText());
        if(question instanceof OpenQuestion){
            closedQuestionAnswer.setVisible(false);
            openQuesionAnswer.setVisible(true);
        } else if(question instanceof ClosedQuestion){
            openQuesionAnswer.setVisible(false);
            closedQuestionAnswer.setVisible(true);
            List<String> givenAnswers= new ArrayList<String>(4);
            givenAnswers.addAll(Arrays.asList(((ClosedQuestion) question).getPossibleAnswers()));
            givenAnswers.add(question.getCorrectAnswer());
            Collections.shuffle(givenAnswers);
            possibleAns1.setText(givenAnswers.get(0));
            possibleAns1.setText(givenAnswers.get(1));
            possibleAns1.setText(givenAnswers.get(2));
            possibleAns1.setText(givenAnswers.get(3));
        }

    }
}
