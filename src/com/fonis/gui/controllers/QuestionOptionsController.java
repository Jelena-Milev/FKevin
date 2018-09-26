package com.fonis.gui.controllers;

import com.fonis.entities.AbstractQuestion;
import com.fonis.entities.ClosedQuestion;
import com.fonis.entities.OpenQuestion;
import com.fonis.resources.Resources;
import com.fonis.services.ParsingService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.Model;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class QuestionOptionsController implements Initializable {


    private static AbstractQuestion selectedQuestion;

    @FXML
    Button back;
    @FXML
    ListView questionsList;
    @FXML
    ComboBox difficulty;
    @FXML
    ComboBox questionType;
    @FXML
    TextField possibleAnswer1;
    @FXML
    TextField possibleAnswer2;
    @FXML
    TextField possibleAnswer3;
    @FXML
    TextArea questionText;
    @FXML
    TextField correctAnswer;
    @FXML
    Button editBtn;
    @FXML
    Button removeBtn;
    @FXML ToggleButton backupBtn;

    private ObservableList<AbstractQuestion> questionObservableList=Model.questionObservableList;

    private ParsingService parsingService = new ParsingService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        questionsList.getItems().setAll(this.questionObservableList);
        removeBtn.setDisable(true);
        editBtn.setDisable(true);

        this.questionObservableList.addListener(new ListChangeListener<AbstractQuestion>() {
            @Override
            public void onChanged(Change<? extends AbstractQuestion> c) {
                questionsList.getItems().setAll(questionObservableList);
            }
        });

        questionsList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
          @Override
          public void changed(ObservableValue observable, Object oldValue, Object newValue) {

              if (newValue != null) {
              showQuestion((AbstractQuestion) newValue);
              removeBtn.setDisable(false);
              editBtn.setDisable(false);
              QuestionOptionsController.selectedQuestion=(AbstractQuestion) newValue;
              }
              if (newValue == null) {
               clearFields();
               removeBtn.setDisable(true);
               editBtn.setDisable(true);
          }
          }
        });
    }

    public void backButtonClicked(ActionEvent event) throws IOException {
        Parent menuParent = FXMLLoader.load(getClass().getClassLoader().getResource("com/fonis/gui/fxmls/startScreen.fxml"));
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.getScene().setRoot(menuParent);
    }

    public void addButtonClicked(ActionEvent event) throws IOException {

        Parent addQuestionParent=FXMLLoader.load(getClass().getClassLoader().getResource("com/fonis/gui/fxmls/addQuestion.fxml"));

        Stage primaryStage=(Stage) (((Node)event.getSource()).getScene().getWindow());
        primaryStage.getScene().setRoot(addQuestionParent);
    }
    
    public void editButtonClicked(ActionEvent event) throws IOException {
        if(questionsList.getSelectionModel().getSelectedItem()!=null) {
            Parent editQuestionParent = FXMLLoader.load(getClass().getClassLoader().getResource("com/fonis/gui/fxmls/editQuestion.fxml"));
            Stage primaryStage = (Stage) (((Node) event.getSource()).getScene().getWindow());
            primaryStage.getScene().setRoot(editQuestionParent);
        } else{
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("You have to select question first");
            alert.setContentText("");
            alert.showAndWait();
        }
    }

    public void removeButtonClicked(){
        AbstractQuestion oldQuestion=(AbstractQuestion) questionsList.getSelectionModel().getSelectedItem();
        if(oldQuestion!=null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Are you sure you want to remove " + oldQuestion.getQuestionText() + " question?");
            alert.setContentText("");
            alert.setTitle("Confirm removing");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                parsingService.removeEntityFromJsonFile(oldQuestion, getQuestionEntityType(oldQuestion), isBackupButtonSelected());
                Model.updateObservableQuestions(this.parsingService);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("You have to select question first!");
            alert.setContentText("");
            alert.setTitle("Warning");
            alert.showAndWait();
        }
    }

    private void showQuestion(AbstractQuestion question) {
        clearFields();
        questionType.setValue(question.getClass().getSimpleName());
        difficulty.getSelectionModel().select(question.getDifficulty());
        questionText.setText(question.getQuestionText());
        correctAnswer.setText(question.getCorrectAnswer());
        if (question instanceof ClosedQuestion) {
            possibleAnswer1.setText(((ClosedQuestion) question).getPossibleAnswers()[0]);
            possibleAnswer2.setText(((ClosedQuestion) question).getPossibleAnswers()[1]);
            possibleAnswer3.setText(((ClosedQuestion) question).getPossibleAnswers()[2]);
        }
    }

    private void clearFields(){
        questionType.setValue(null);
        difficulty.setValue(null);
        questionText.setText("");
        correctAnswer.setText("");
        possibleAnswer1.setText("");
        possibleAnswer2.setText("");
        possibleAnswer3.setText("");
    }

    public Resources.Entities getQuestionEntityType(AbstractQuestion question){
        if(question instanceof OpenQuestion) {
            return Resources.Entities.OPEN_QUESTION;
        }
        else{
            return Resources.Entities.CLOSED_QUESTION;        }
    }

    private boolean isBackupButtonSelected(){
        if(backupBtn.isSelected()){
            return true;
        }
        else{
            return false;
        }
    }
//#TODO when there is no selected question, this method  returns the last one selected insted of null
    public static AbstractQuestion getSelectedQuestion(){
        return QuestionOptionsController.selectedQuestion;
    }


}
