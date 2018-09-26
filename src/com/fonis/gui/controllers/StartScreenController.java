package com.fonis.gui.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class StartScreenController{

    @FXML
    private JFXButton exitButton;

    @FXML
    private JFXButton editorButton;

    public void onExitButtonClicked(){
        Stage stage = (Stage) this.exitButton.getScene().getWindow();
        stage.close();
    }

    public void onEditorButtonClicked(ActionEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("com/fonis/gui/fxmls/questionOptions.fxml"));
        Stage currentStage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        currentStage.getScene().setRoot(parent);
        currentStage.show();
    }

    public void onQuizButtonClicked(ActionEvent event) throws IOException {
        Parent parent=FXMLLoader.load(getClass().getClassLoader().getResource("com/fonis/gui/fxmls/QuizStart.fxml"));
        Stage currentStage=(Stage) ((Node)event.getSource()).getScene().getWindow();

        currentStage.getScene().setRoot(parent);
        currentStage.show();
    }
}
