package com.fonis.gui.controllers;

import com.fonis.entities.AbstractQuestion;
import com.fonis.entities.OpenQuestion;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ThanksSceneController implements Initializable{

//    #TODO add delay

    public void showStartScreen(ActionEvent event) throws IOException {
        Parent parent= FXMLLoader.load(getClass().getClassLoader().getResource("com/fonis/gui/fxmls/startScreen.fxml"));
        Stage currentStage= (Stage) ((Node)event.getSource()).getScene().getWindow();
        currentStage.getScene().setRoot(parent);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){

    }
}
