package com.fonis.gui.controllers;

import com.fonis.entities.AbstractQuestion;
import com.fonis.entities.ClosedQuestion;
import com.fonis.entities.OpenQuestion;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.Model;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class QuizQuestionsController implements Initializable {

    @FXML
    private JFXToggleButton answerOne;

    @FXML
    private JFXToggleButton answerTwo;

    @FXML
    private JFXToggleButton answerThree;

    @FXML
    private JFXToggleButton answerFour;

    @FXML
    private TextArea questionText;

    @FXML
    private JFXTextField guessedAnswer;

    @FXML
    private JFXProgressBar progressBar;

    @FXML
    private JFXButton nextButton;

    private List<AbstractQuestion> questions;
    private int currentIndex = 0;

    public void buttonToggled(ActionEvent event) {
        JFXToggleButton selectedButton = (JFXToggleButton) event.getTarget();
        this.questions.get(currentIndex).setGuessedAnswer(selectedButton.getText());

        // Hack Fix for next button binding
        this.guessedAnswer.setText(selectedButton.getText());

        this.deselectButtons();
        selectedButton.setSelected(true);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resetComponents();
        this.bindNextButton();
        this.questionText.setFocusTraversable(false);
        this.questionText.setMouseTransparent(true);
        this.questions = Model.loadRoundQuestions();
        this.displayQuestion();
    }

    private void displayQuestion() {
        AbstractQuestion question = this.questions.get(this.currentIndex);
        this.resetComponents();

        this.loadQuestionText(question);
//        this.questionText.setText(question.getQuestionText());
        if (question instanceof OpenQuestion) {
            this.loadOpenQuestion();
        } else {
            this.loadClosedQuestion((ClosedQuestion) question);
        }

        this.answerOne.setDisableVisualFocus(true);
    }

    private void loadQuestionText(AbstractQuestion question) {
        String questionText = question.getQuestionText();

        final IntegerProperty i = new SimpleIntegerProperty(0);
        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(20),
                event -> {
                    if (i.get() > questionText.length()) {
                        timeline.stop();
                    } else {
                        this.questionText.setText(questionText.substring(0, i.get()));
                        i.set(i.get() + 1);
                    }
                });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void loadOpenQuestion() {
        this.guessedAnswer.setVisible(true);
    }

    private void loadClosedQuestion(ClosedQuestion question) {
        List<String> answers = new ArrayList<>();
        String[] possibleAnswers = question.getPossibleAnswers();
        answers.addAll(Arrays.asList(possibleAnswers));
        answers.add(question.getCorrectAnswer());

        Collections.shuffle(answers);
        this.answerOne.setText(answers.get(0));
        this.answerTwo.setText(answers.get(1));
        this.answerThree.setText(answers.get(2));
        this.answerFour.setText(answers.get(3));

        this.showButtons();
    }

    private void resetComponents() {
        this.answerOne.setText("");
        this.answerTwo.setText("");
        this.answerThree.setText("");
        this.answerFour.setText("");

        this.deselectButtons();

        this.answerOne.setVisible(false);
        this.answerTwo.setVisible(false);
        this.answerThree.setVisible(false);
        this.answerFour.setVisible(false);

        this.guessedAnswer.clear();
        this.guessedAnswer.setVisible(false);
    }

    private void deselectButtons() {
        this.answerOne.setSelected(false);
        this.answerTwo.setSelected(false);
        this.answerThree.setSelected(false);
        this.answerFour.setSelected(false);
    }

    private void showButtons() {
        this.answerOne.setVisible(true);
        this.answerTwo.setVisible(true);
        this.answerThree.setVisible(true);
        this.answerFour.setVisible(true);
    }

    private void bindNextButton() {
        this.nextButton.disableProperty().bind(Bindings.isEmpty(this.guessedAnswer.textProperty()));
    }

    private void updateProgressBar() {
        double progressPercent = ((double) (this.currentIndex + 1) / this.questions.size());
        this.progressBar.setProgress(progressPercent);
    }

    public void onNextButtonClicked(ActionEvent event) throws IOException {
        if (this.questions.get(currentIndex) instanceof OpenQuestion) {
            this.questions.get(currentIndex).setGuessedAnswer(this.guessedAnswer.getText());
        }

        this.updateProgressBar();

        if (this.currentIndex == this.questions.size() - 2) {
            this.nextButton.setText("FINISH");
        }
        if (this.currentIndex == this.questions.size() - 1) {
            System.out.println("MADE IT TO THE END");
            // Calculate points
            int totalPoints = Model.getNumberOfPoints(this.questions);

            // Save points somewhere, possibly Static int in Model?

            // Switch to user info screen
            this.switchToUserInfoScreen(event);
        } else {
            ++this.currentIndex;
            this.displayQuestion();
        }
    }

    public boolean canAdvanceToNextQuestion() {
        if (!this.questions.get(this.currentIndex).getGuessedAnswer().isEmpty()) {
            return true;
        }
        if (!this.guessedAnswer.getText().isEmpty()) {
            return true;
        }

        return false;
    }

    private void switchToUserInfoScreen(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("com/fonis/gui/fxmls/userInfoScreen.fxml"));
        Stage currentStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();

        currentStage.getScene().setRoot(parent);
        currentStage.show();
    }
}
