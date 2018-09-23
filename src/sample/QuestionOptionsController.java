package sample;

import com.fonis.entities.AbstractQuestion;
import com.fonis.entities.ClosedQuestion;
import com.fonis.entities.OpenQuestion;
import com.fonis.resources.Resources;
import com.fonis.services.ParsingServiceNeca;
import com.sun.xml.internal.bind.v2.TODO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ResourceBundle;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Node;

public class QuestionOptionsController implements Initializable {

    @FXML
    Button back;
    @FXML
    ListView questionsList;
    @FXML
    ComboBox difficulty;
    @FXML
    ChoiceBox questionType;
    @FXML
    TextField possibleAnswer1;
    @FXML
    TextField possibleAnswer2;
    @FXML
    TextField possibleAnswer3;
    @FXML
    TextField questionText;
    @FXML
    TextField correctAnswer;
    @FXML Button editBtn;
    @FXML Button removeBtn;

    ParsingServiceNeca service = new ParsingServiceNeca();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Model.questions.addAll(service.getEntitiesJsonAsList(Resources.Entities.OPEN_QUESTION));
        Model.questions.addAll(service.getEntitiesJsonAsList(Resources.Entities.CLOSED_QUESTION));
        questionsList.getItems().addAll(Model.questions);
        questionType.getItems().setAll(Resources.Entities.CLOSED_QUESTION.toString(), Resources.Entities.OPEN_QUESTION.toString());
        difficulty.getItems().setAll(Arrays.asList(Resources.QuestionDifficulty.values()));
        questionsList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                    @Override
                    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                        if(newValue!=null){
                            removeBtn.setDisable(false);
                            editBtn.setDisable(false);
                            showQuestion((AbstractQuestion)newValue);
                        }
                        if(questionsList.getSelectionModel().getSelectedItem()==null){
                            removeBtn.setDisable(true);
                            editBtn.setDisable(true);
                        }
                    }
                }
        );
    }

    public void backButtonClicked(ActionEvent event) throws IOException {

        Parent menuParent = FXMLLoader.load(getClass().getResource("menu.fxml"));
        Scene menuScene = new Scene(menuParent);

        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.setScene(menuScene);
        primaryStage.show();

        Rectangle2D windowDimension = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((windowDimension.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((windowDimension.getHeight() - primaryStage.getHeight()) / 2);
    }

    public void questionTypeChosen() {
        if (questionType.getSelectionModel().getSelectedItem().equals("OpenQuestion")) {
            possibleAnswer1.setEditable(false);
            possibleAnswer2.setEditable(false);
            possibleAnswer3.setEditable(false);
        } else {
            possibleAnswer1.setEditable(true);
            possibleAnswer2.setEditable(true);
            possibleAnswer3.setEditable(true);
        }
    }

    public void addButtonPressed() {
        AbstractQuestion question;
        if (questionType.getSelectionModel().getSelectedItem().equals("OpenQuestion")) {
            question = new OpenQuestion();
        } else {
            question = new ClosedQuestion();
        }
        try {
            question.setQuestionText(questionText.getText());
            question.setCorrectAnswer(correctAnswer.getText());

            question.setDifficulty((Resources.QuestionDifficulty)difficulty.getSelectionModel().getSelectedItem());

            if (question instanceof ClosedQuestion) {
                ((ClosedQuestion) question).setPossibleAnswers(new String[]{possibleAnswer1.getText(), possibleAnswer2.getText(), possibleAnswer3.getText()});
                service.addEntityToJsonFile(question, Resources.Entities.CLOSED_QUESTION, true);
            } else {
                service.addEntityToJsonFile(question, Resources.Entities.OPEN_QUESTION, true);
            }
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void editButtonClicked() {
        AbstractQuestion oldQuestion = (AbstractQuestion) questionsList.getSelectionModel().getSelectedItem();
        AbstractQuestion newQuestion=getQuestionFromFields();
        try {
        if (newQuestion instanceof ClosedQuestion) {
            oldQuestion.editExistingQuestion(newQuestion, Model.questions, Resources.Entities.CLOSED_QUESTION);
        } else {
            oldQuestion.editExistingQuestion(newQuestion, Model.questions, Resources.Entities.OPEN_QUESTION);

        }
//        Resources.Entities entityType=(Resources.Entities)questionType.getSelectionModel().getSelectedItem();

//            oldQuestion.editExistingQuestion(newQuestion, Model.questions, entityType);
        } catch (IllegalStateException e) {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }
        updateQuestionList();
    }

    private void showQuestion(AbstractQuestion question) {
        questionType.setValue(question.getClass().getSimpleName());
        difficulty.setValue(question.getDifficulty());
        questionText.setText(question.getQuestionText());
        correctAnswer.setText(question.getCorrectAnswer());
        if (question instanceof ClosedQuestion) {
            possibleAnswer1.setText(((ClosedQuestion) question).getPossibleAnswers()[0]);
            possibleAnswer2.setText(((ClosedQuestion) question).getPossibleAnswers()[1]);
            possibleAnswer3.setText(((ClosedQuestion) question).getPossibleAnswers()[2]);
        }
    }

    private AbstractQuestion getQuestionFromFields() {
        AbstractQuestion question=null;
        try {
            if (questionType.getSelectionModel().getSelectedItem().equals("OpenQuestion")) {
                question = new OpenQuestion();
            } else {
                question = new ClosedQuestion();
                ((ClosedQuestion) question).setPossibleAnswers(new String[]{possibleAnswer1.getText(), possibleAnswer2.getText(), possibleAnswer3.getText()});
            }
            question.setQuestionText(questionText.getText());
            question.setCorrectAnswer(correctAnswer.getText());
            question.setDifficulty((Resources.QuestionDifficulty)difficulty.getSelectionModel().getSelectedItem());
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }
        return question;
    }

    private void updateQuestionList(){
        Model.questions=new ArrayList<>();
        Model.questions.addAll(service.getEntitiesJsonAsList(Resources.Entities.OPEN_QUESTION));
        Model.questions.addAll(service.getEntitiesJsonAsList(Resources.Entities.CLOSED_QUESTION));
        questionsList.getItems().setAll(Model.questions);

    }
}
