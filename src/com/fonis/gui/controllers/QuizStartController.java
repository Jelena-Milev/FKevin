package com.fonis.gui.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class QuizStartController implements Initializable {

    @FXML private ImageView logo;
    @FXML
    JFXButton startButton;


    public void onStartButtonClicked(ActionEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("com/fonis/gui/fxmls/quizQuestions.fxml"));
        Stage currentStage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        currentStage.getScene().setRoot(parent);
    }


//    private void fadeOut(){
//        FadeTransition fadeTransition=new FadeTransition(Duration.millis(2000), logo);
//        fadeTransition.setFromValue(1.0);
//        fadeTransition.setToValue(0.0);
//        fadeTransition.setOnFinished(e->{
//            logo.setOpacity(0.0);
//            this.fadeIn();
//        });
//        fadeTransition.play();
//    }
//
//    private void fadeIn(){
//        FadeTransition fadeTransition=new FadeTransition(Duration.millis(2000), logo);
//        fadeTransition.setFromValue(0.0);
//        fadeTransition.setToValue(1.0);
//        fadeTransition.setOnFinished(e->{
//            logo.setOpacity(1.0);
//            this.fadeOut();
//        });
//        fadeTransition.play();
//    }


    private void animation(){
        Path path=new Path();
        path.getElements().add(new MoveTo(210,150));
        path.getElements().add(new LineTo(-300, +290));
        path.getElements().add(new LineTo(210, 460));
        path.getElements().add(new LineTo(700, 290));
        path.getElements().add(new LineTo(210, 150));

        PathTransition transition=new PathTransition(Duration.seconds(15.0), path, logo);
        transition.setCycleCount(TranslateTransition.INDEFINITE);
        transition.play();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        animation();
    }
}
