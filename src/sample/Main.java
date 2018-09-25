package sample;

import com.fonis.resources.Resources;
import com.fonis.services.ParsingService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application implements Initializable {

    ParsingService parsingService = new ParsingService();


    @Override
    public void start(Stage primaryStage) throws Exception {
        Resources.APP_LOCATION=Resources.getAppLocation();
        Model.updateModelQuestion(parsingService);
        Model.updateObservableQuestions(parsingService);
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("com/fonis/gui/fxmls/menu.fxml"));
        primaryStage.setTitle("FKevin");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
    }


    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
