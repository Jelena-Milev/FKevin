package sample;

import com.fonis.resources.Resources;
import com.fonis.services.ParsingService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application implements Initializable {

    ParsingService parsingService = new ParsingService();


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("com/fonis/gui/fxmls/startScreen.fxml"));
        this.loadInitialData();

        primaryStage.setTitle("FKevin");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        root.requestFocus();
    }


    private void loadInitialData(){
        Resources.APP_LOCATION = Resources.getAppLocation();
        Model.updateModelQuestion(this.parsingService);
        Model.updateObservableQuestions(this.parsingService);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
