package com.fonis.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class QuizStartController{


    public void onBackButtonClicked(ActionEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("com/fonis/gui/fxmls/startScreen.fxml"));
        Stage currentStage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        currentStage.getScene().setRoot(parent);
    }

    public void onStartButtonClicked(ActionEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("com/fonis/gui/fxmls/quizQuestions.fxml"));
        Stage currentStage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        currentStage.getScene().setRoot(parent);
    }
}
