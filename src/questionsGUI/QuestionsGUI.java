package questionsGUI;

import com.fonis.entities.ClosedQuestion;
import com.fonis.entities.OpenQuestion;
import com.fonis.resources.Resources;
import com.fonis.services.ParsingServiceNeca;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class QuestionsGUI extends Application {

    ParsingServiceNeca parsingService = new ParsingServiceNeca();
    Button btnAddSingleQuestion;
    Button btnAddMoreQuestions;
    Button btnEditQuestion;
    Button btnRemoveQuestion;

    Scene startScene;
    Scene sceneForOption;

    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setStartScene();
        primaryStage.setTitle("Questions dialog");
        primaryStage.setScene(startScene);
        primaryStage.show();

        btnAddSingleQuestion.setOnAction(e -> changeScene(primaryStage));

    }

    private void changeScene(Stage primaryStage) {
        setSceneForOption();
        primaryStage.setScene(sceneForOption);
    }

    private void setStartScene(){
        VBox vBox = new VBox();

        startScene = new Scene(vBox, 900, 600);

        btnAddMoreQuestions = new Button("Add more questions");
        btnAddSingleQuestion = new Button("Add single question");
        btnRemoveQuestion = new Button("Remove question");
        btnEditQuestion = new Button("Edit question");

        btnAddMoreQuestions.setMaxWidth(Double.MAX_VALUE);
        btnAddSingleQuestion.setMaxWidth(Double.MAX_VALUE);
        btnEditQuestion.setMaxWidth(Double.MAX_VALUE);
        btnRemoveQuestion.setMaxWidth(Double.MAX_VALUE);

        btnAddMoreQuestions.setMinHeight(40);
        btnAddSingleQuestion.setMinHeight(40);
        btnEditQuestion.setMinHeight(40);
        btnRemoveQuestion.setMinHeight(40);


        vBox.setSpacing(20);
        vBox.setPadding(new Insets(200, 300, 200, 300));
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(btnAddMoreQuestions, btnAddSingleQuestion, btnEditQuestion, btnRemoveQuestion);

    }

    private void setSceneForOption(){
        BorderPane borderPane = new BorderPane();
        sceneForOption = new Scene(borderPane);

        BorderPane borderPaneForCenter=new BorderPane();
        GridPane paneForFields=new GridPane();
        paneForFields.setGridLinesVisible(true);
        HBox hBoxForButtons=new HBox();

        TextField questionText = new TextField();
        Label questionTextLabel=new Label("Question text: ");
        questionText.setPromptText("Question text");
        TextField correctAnswer = new TextField();
        correctAnswer.setPromptText("Correct text");
        Label correctAnswerLabel=new Label("Correct answer: ");
        TextField possibleAnswer1 = new TextField();
        possibleAnswer1.setPromptText("First possible answer");
        TextField possibleAnswer2 = new TextField();
        possibleAnswer2.setPromptText("Second possible answer");
        TextField possibleAnswer3 = new TextField();
        possibleAnswer3.setPromptText("Third possible answer");

        ToggleGroup choosingDifficulty=new ToggleGroup();
        ToggleButton lowDifficulty = new ToggleButton("Low");
        lowDifficulty.setToggleGroup(choosingDifficulty);
        ToggleButton mediumDifficulty = new ToggleButton("Medium");
        mediumDifficulty.setToggleGroup(choosingDifficulty);
        ToggleButton highDifficulty = new ToggleButton("High");
        highDifficulty.setToggleGroup(choosingDifficulty);
        HBox difficultyButtons=new HBox();
        difficultyButtons.getChildren().addAll(lowDifficulty, mediumDifficulty, highDifficulty);

        ToggleGroup choosingType=new ToggleGroup();
        ToggleButton open = new ToggleButton("Open");
        lowDifficulty.setToggleGroup(choosingType);
        ToggleButton close = new ToggleButton("Close");
        mediumDifficulty.setToggleGroup(choosingType);
        HBox typeButtons=new HBox(close, open);
        close.setSelected(true);
        open.setSelected(false);
        close.setOnAction(e-> {
            possibleAnswer1.setVisible(true);
            possibleAnswer2.setVisible(true);
            possibleAnswer3.setVisible(true);
        });
        open.setOnAction(e-> {
            possibleAnswer1.setVisible(false);
            possibleAnswer2.setVisible(false);
            possibleAnswer3.setVisible(false);
        });

        Button btnSaveQuestion=new Button("Save question");
        hBoxForButtons.setAlignment(Pos.CENTER);
        hBoxForButtons.setPadding(new Insets(10, 50, 100, 50));
        hBoxForButtons.getChildren().add(btnSaveQuestion);

        paneForFields.setAlignment(Pos.CENTER);
        paneForFields.setPadding(new Insets(100, 300, 100, 300));
        paneForFields.setHgap(60);
        paneForFields.setVgap(10);
        GridPane.setConstraints(typeButtons, 0, 1);
        GridPane.setConstraints(questionTextLabel, 0, 3);
        GridPane.setConstraints(questionText, 0, 4);
        GridPane.setConstraints(correctAnswerLabel, 0, 6);
        GridPane.setConstraints(correctAnswer, 0, 7);
        GridPane.setConstraints(difficultyButtons, 0, 10);
        GridPane.setConstraints(possibleAnswer1, 1, 3);
        GridPane.setConstraints(possibleAnswer2, 1, 6);
        GridPane.setConstraints(possibleAnswer3, 1, 9);
        paneForFields.getChildren().addAll(questionText, correctAnswer, difficultyButtons, typeButtons,
                questionTextLabel, correctAnswerLabel, possibleAnswer1, possibleAnswer2, possibleAnswer3);

        borderPane.setCenter(borderPaneForCenter);
        borderPaneForCenter.setCenter(paneForFields);
        borderPaneForCenter.setBottom(hBoxForButtons);

        btnSaveQuestion.setOnAction(e->{
           try {
               if (open.isSelected()) {
                   OpenQuestion newQuestion = new OpenQuestion();
                   newQuestion.setQuestionText(questionText.getText());
                   newQuestion.setCorrectAnswer(correctAnswer.getText());
                   if (lowDifficulty.isSelected()) {
                       newQuestion.setDifficulty(Resources.QuestionDifficulty.LOW);
                   } else if (mediumDifficulty.isSelected()) {
                       newQuestion.setDifficulty(Resources.QuestionDifficulty.MEDIUM);
                   } else newQuestion.setDifficulty(Resources.QuestionDifficulty.HIGH);
                   parsingService.addEntityToJsonFile(newQuestion, Resources.Entities.OPEN_QUESTION, true);
               } else if (close.isSelected()) {
                   ClosedQuestion newQuestion = new ClosedQuestion();
                   newQuestion.setQuestionText(questionText.getText());
                   newQuestion.setCorrectAnswer(correctAnswer.getText());
                   if (lowDifficulty.isSelected()) {
                       newQuestion.setDifficulty(Resources.QuestionDifficulty.LOW);
                   } else if (mediumDifficulty.isSelected()) {
                       newQuestion.setDifficulty(Resources.QuestionDifficulty.MEDIUM);
                   } else newQuestion.setDifficulty(Resources.QuestionDifficulty.HIGH);
                   newQuestion.setPossibleAnswers(new String[]{possibleAnswer1.getText(), possibleAnswer2.getText(), possibleAnswer3.getText()});
                   parsingService.addEntityToJsonFile(newQuestion, Resources.Entities.CLOSED_QUESTION, true);
               }

           }catch(IllegalArgumentException e1){
               Alert alert=new Alert(Alert.AlertType.ERROR);
               alert.setHeaderText("Error: ");
               alert.setHeaderText(e1.getMessage());
               alert.showAndWait();
            }

        });
    }



}
