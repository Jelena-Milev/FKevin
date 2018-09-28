package com.fonis.gui.controllers;

import com.fonis.entities.Participant;
import com.fonis.resources.Resources;
import com.fonis.services.ParsingService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
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
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserInfoScreenController implements Initializable {

    @FXML
    private JFXTextField name;
    @FXML
    private JFXTextField surname;
    @FXML
    private JFXTextField email;
    @FXML
    private JFXTextField phoneNumber;
    @FXML
    private JFXButton endBtn;
    @FXML
    private Label message;
    @FXML
    private Label thanksMessage;
    @FXML
    ImageView fKevin;

    private Participant participant;
    private ParsingService parsingService=new ParsingService();

    private int totalPoints;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.thanksMessage.setText("");
        this.bindEndButton();
    }

    private void saveParticipantInfo() {
        this.participant = new Participant();
        this.participant.setName(this.name.getText());
        this.participant.setSurname(this.surname.getText());
        this.participant.setEmail(this.email.getText());
        this.participant.setPhoneNumber(this.phoneNumber.getText());
        this.participant.setTotalPoints(this.totalPoints);
    }

    private void bindEndButton() {
        this.endBtn.disableProperty().bind(Bindings.isEmpty(name.textProperty())
                .or(Bindings.isEmpty(surname.textProperty()))
                .or(Bindings.isEmpty(email.textProperty()))
        );
    }

    public void onEndButtonClicked(ActionEvent event) throws IOException {
        this.saveParticipantInfo();
        this.parsingService.addEntityToJsonFile(this.participant, Resources.Entities.PARTICIPANT, true);
        this.showThanksScene(event);

    }

    public void setTotalPoints(int totalPoints){
        this.totalPoints = totalPoints;
    }

    private void showThanksScene(ActionEvent event) throws IOException {
        this.setEndState();
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(13));
        pauseTransition.setOnFinished(pauseEvent -> {
            try{
                this.changeToStartScene(event);
            }catch(IOException e){
                e.printStackTrace();
            }
        });
        pauseTransition.play();
    }

    private void setEndState(){
        this.name.setVisible(false);
        this.surname.setVisible(false);
        this.email.setVisible(false);
        this.phoneNumber.setVisible(false);
        this.endBtn.setVisible(false);
        this.message.setText("");
        this.showEndMessage();
    }

    private void changeToStartScene(ActionEvent event) throws IOException{
        Parent parent= FXMLLoader.load(getClass().getClassLoader().getResource("com/fonis/gui/fxmls/quizStart.fxml"));
        Stage currentStage=(Stage) ((Node)event.getSource()).getScene().getWindow();

        currentStage.getScene().setRoot(parent);
    }

    private void showMessage(Label label, String text) {
        final IntegerProperty i = new SimpleIntegerProperty(0);
        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(70),
                event -> {
                    if (i.get() > text.length()) {
                        timeline.stop();
                    } else {
                        label.setText(text.substring(0, i.get()));
                        i.set(i.get() + 1);
                    }
                });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void showEndMessage(){
        this.fKevin.setVisible(false);
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(1700));
        pauseTransition.setOnFinished(pauseEvent -> {
            this.fKevin.setVisible(true);
            showMessage(message, "Congratulation! You won "+ this.totalPoints +" / "+ Resources.maxPoints + " points!");
        });
        pauseTransition.play();
        showMessage(this.thanksMessage, "Thanks for playing ");
    }

}
