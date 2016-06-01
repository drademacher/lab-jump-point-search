package view;

import controller.map.Map;
import controller.map.NotAFieldException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
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
        // load FXML and put it on the scene
        FXMLLoader fxmlLoader = new FXMLLoader();
        Scene scene = new Scene(fxmlLoader.load(getClass().getResource("main.fxml").openStream()));
        Controller ctrl = fxmlLoader.getController();

        // stage settings
        primaryStage.setTitle("Visualization of Various Shortest Path Algorithms on Grid Graphs");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        // primaryStage.setWidth(Consts.windowWidth);
        primaryStage.setMinHeight(700);
        // primaryStage.setHeight(Consts.windowHeight);

        // show stage
        primaryStage.show();


        ctrl.postLoad();
        // ctrl.renderCanvas();


    }



}
