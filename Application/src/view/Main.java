package view;

import controller.map.Map;
import controller.map.NotAFieldException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class Main extends Application {
    // private Controller ctrl;
    private Canvas canvas;
    private Map map;



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // load stuff
        FXMLLoader fxmlLoader = new FXMLLoader();
        BorderPane p = fxmlLoader.load(getClass().getResource("main.fxml").openStream());
        Controller ctrl = fxmlLoader.getController();

        // initialize stage
        primaryStage.setScene(new Scene(p));
        primaryStage.show();

        // TODO: setting min height to 600 causing the buttons to disappear
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(700);

    }



}
