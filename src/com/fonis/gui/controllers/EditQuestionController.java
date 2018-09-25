package com.fonis.gui.controllers;

import com.fonis.entities.AbstractQuestion;
import com.fonis.entities.ClosedQuestion;
import com.fonis.entities.OpenQuestion;
import com.fonis.resources.Resources;
import com.fonis.services.ParsingService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.Model;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditQuestionController implements Initializable {

   private ParsingService parsingService = new ParsingService();
   private AbstractQuestion questionForEditing;
    @FXML
    ComboBox questionType;
    @FXML
    ComboBox difficulty;
    @FXML
    TextArea questionText;
    @FXML
    TextField correctAnswer;
    @FXML
    TextField possibleAnswer1;
    @FXML
    TextField possibleAnswer2;
    @FXML
    TextField possibleAnswer3;
    @FXML
    VBox vBoxPossible1;
    @FXML
    VBox vBoxPossible2;
    @FXML
    VBox vBoxPossible3;
    @FXML
    ToggleButton backupBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        difficulty.getItems().addAll(Resources.QuestionDifficulty.values());
        this.questionForEditing=QuestionOptionsController.getSelectedQuestion();
        this.showQuestion(questionForEditing);
    }

    public void questionTypeChosen() {
        if (questionType.getSelectionModel().getSelectedItem().toString().equals("OpenQuestion")) {
            vBoxPossible1.setVisible(false);
            vBoxPossible2.setVisible(false);
            vBoxPossible3.setVisible(false);
        } else {
            vBoxPossible1.setVisible(true);
            vBoxPossible2.setVisible(true);
            vBoxPossible3.setVisible(true);
        }
    }

    public void saveButtonClicked(){
        AbstractQuestion newQuestion=this.getQuestionFromFields();
        if(newQuestion!=null){
            Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("");
            alert.setTitle("Confirm editing");
            alert.setHeaderText("Are you sure you want to edit "+this.questionForEditing.getQuestionText()+" question?");
            Optional<ButtonType> answer=alert.showAndWait();
            if(answer.get()==ButtonType.OK){
                try {
                    questionForEditing.editExistingQuestion(newQuestion, this.getQuestionEntityType(newQuestion),
                            parsingService, this.isBackupButtonSelected());
                    Model.updateObservableQuestions(parsingService);
                } catch (IllegalStateException e) {
                    Alert error=new Alert(Alert.AlertType.ERROR);
                    error.setContentText("");
                    error.setTitle("Error");
                    error.setHeaderText(e.getMessage());
                    error.showAndWait();
                }
            }
        }
    }


    public void cancelButtonClicked(ActionEvent event) throws IOException {
        Parent questionOptionsParent = FXMLLoader.load(getClass().getClassLoader().getResource("com/fonis/gui/fxmls/questionOptions.fxml"));
        Stage primaryStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();

        Scene scene = new Scene(questionOptionsParent);
        primaryStage.setScene(scene);
        primaryStage.show();

        Rectangle2D windowDimension = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((windowDimension.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((windowDimension.getHeight() - primaryStage.getHeight()) / 2);
    }

    private void showQuestion(AbstractQuestion question) {
        questionType.setValue(question.getClass().getSimpleName());
        questionTypeChosen();
//        difficulty.setValue(question.getDifficulty().toString());
        difficulty.getSelectionModel().select(question.getDifficulty().toString());
        questionText.setText(question.getQuestionText());
        correctAnswer.setText(question.getCorrectAnswer());
        if (question instanceof ClosedQuestion) {
            possibleAnswer1.setText(((ClosedQuestion) question).getPossibleAnswers()[0]);
            possibleAnswer2.setText(((ClosedQuestion) question).getPossibleAnswers()[1]);
            possibleAnswer3.setText(((ClosedQuestion) question).getPossibleAnswers()[2]);
        }
    }

    private AbstractQuestion getQuestionFromFields() {
        if (questionType.getSelectionModel().getSelectedItem().toString().equals("OpenQuestion")) {
            return generateOpenQuestion();
        } else {
            return generateClosedQuestion();
        }
    }

    private OpenQuestion generateOpenQuestion() {
        try {
            OpenQuestion question = new OpenQuestion();
            question.setQuestionText(questionText.getText());
            question.setCorrectAnswer(correctAnswer.getText());
            question.setDifficulty(this.getSelectedDifficulty());
            return question;
        }catch (IllegalArgumentException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        return null;
    }

    private ClosedQuestion generateClosedQuestion() {
        try {
            ClosedQuestion question = new ClosedQuestion();
            question.setQuestionText(questionText.getText());
            question.setCorrectAnswer(correctAnswer.getText());
            question.setDifficulty(this.getSelectedDifficulty());
            question.setPossibleAnswers(new String[]{
                    possibleAnswer1.getText(),
                    possibleAnswer2.getText(),
                    possibleAnswer3.getText()
            });
            return question;
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        return null;
    }

    public Resources.Entities getQuestionEntityType(AbstractQuestion question){
        if(question instanceof OpenQuestion) {
            return Resources.Entities.OPEN_QUESTION;
        }
        else{
            return Resources.Entities.CLOSED_QUESTION;        }
    }

    public Resources.QuestionDifficulty getSelectedDifficulty(){
        String selectedDifficulty=difficulty.getSelectionModel().getSelectedItem().toString();
        if(selectedDifficulty.equals("LOW")){
            return Resources.QuestionDifficulty.LOW;
        } else if(selectedDifficulty.equals("MEDIUM")){
            return Resources.QuestionDifficulty.MEDIUM;
        } else return Resources.QuestionDifficulty.HIGH;
    }

    private boolean isBackupButtonSelected(){
        if(backupBtn.isSelected()){
            return true;
        }
        else{
            return false;
        }
    }
}
