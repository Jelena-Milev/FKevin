package com.fonis.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    public void questionOptionButtonClicked(ActionEvent event) throws IOException {

        Parent questionOptionsParent = FXMLLoader.load(getClass().getClassLoader().getResource("com/fonis/gui/fxmls/questionOptions.fxml"));
        Stage primaryStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();

        primaryStage.getScene().setRoot(questionOptionsParent);
    }

}
