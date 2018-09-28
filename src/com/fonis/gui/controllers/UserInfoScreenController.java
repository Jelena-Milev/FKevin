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
import javafx.scene.layout.VBox;
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
    private Label congratsMessage;
    @FXML
    private Label score;
    @FXML
    private Label pointsMessage;
    @FXML
    ImageView fKevin;
    @FXML
    ImageView thanksLogo;

    @FXML private VBox messagesBox;
    @FXML private VBox infoBox;

    private Participant participant;
    private ParsingService parsingService=new ParsingService();

    private int totalPoints;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.clearMessagesComponents();
        this.messagesBox.setVisible(false);
        this.thanksLogo.setVisible(false);
        this.infoBox.setVisible(true);
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
        this.infoBox.setVisible(false);
        this.messagesBox.setVisible(true);
        this.showEndMessage();
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(12));
        pauseTransition.setOnFinished(pauseEvent -> {
            try{
                this.changeToStartScene(event);
            }catch(IOException e){
                e.printStackTrace();
            }
        });
        pauseTransition.play();
    }


    private void showEndMessage(){
        this.thanksLogo.setVisible(false);
        PauseTransition pauseTransition1 = new PauseTransition(Duration.millis(1700));
        pauseTransition1.setOnFinished(pauseEvent -> {
            this.thanksLogo.setVisible(true);
        });

        PauseTransition pauseTransition2 = new PauseTransition(Duration.millis(1800));
        pauseTransition2.setOnFinished(pauseEvent -> {
            showMessage(this.congratsMessage, "Congratulation! You won ");
//                    + this.totalPoints +" / "+ Resources.maxPoints + " points!");
        });

        PauseTransition pauseTransition3 = new PauseTransition(Duration.millis(3600));
        pauseTransition3.setOnFinished(pauseEvent -> {
            showMessage(this.score, this.totalPoints +" / "+ Resources.maxPoints);
        });

        PauseTransition pauseTransition4 = new PauseTransition(Duration.millis(4200));
        pauseTransition4.setOnFinished(pauseEvent -> {
            showMessage(this.pointsMessage, "points!");
        });

        pauseTransition1.play();
        pauseTransition2.play();
        pauseTransition3.play();
        pauseTransition4.play();
        showMessage(this.thanksMessage, "Thanks for playing ");
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

    private void clearMessagesComponents(){
        this.thanksMessage.setText("");
        this.congratsMessage.setText("");
        this.score.setText("");
        this.pointsMessage.setText("");
    }

}
