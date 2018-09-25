package com.fonis.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    public void questionOptionButtonClicked(ActionEvent event) throws IOException {

        Parent questionOptionsParent = FXMLLoader.load(getClass().getClassLoader().getResource("com/fonis/gui/fxmls/questionOptions.fxml"));
        Stage primaryStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();

        Scene scene = new Scene(questionOptionsParent);
        primaryStage.setScene(scene);
        primaryStage.show();

        Rectangle2D windowDimension = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((windowDimension.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((windowDimension.getHeight() - primaryStage.getHeight()) / 2);
    }

}
