package com.fonis.gui.controllers;

import com.fonis.entities.Participant;
import com.fonis.resources.Resources;
import com.fonis.services.ParsingService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
    private
    JFXButton endBtn;
    private Participant participant;
    private ParsingService parsingService=new ParsingService();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.bindEndButton();
    }
    private void saveParticipantInfo() {
        this.participant = new Participant();
        participant.setName(this.name.getText());
        participant.setSurname(this.surname.getText());
        participant.setEmail(this.email.getText());
        participant.setPhoneNumber(this.phoneNumber.getText());
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

    private void showThanksScene(ActionEvent event) throws IOException {
        Parent parent= FXMLLoader.load(getClass().getClassLoader().getResource("com/fonis/gui/fxmls/thanksScene.fxml"));
        Stage currentStage=(Stage) ((Node)event.getSource()).getScene().getWindow();

        currentStage.getScene().setRoot(parent);
        currentStage.show();
    }

}
