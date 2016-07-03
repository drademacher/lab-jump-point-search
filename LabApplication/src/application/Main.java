package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.Enumeration;
import java.util.ResourceBundle;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        ResourceBundle bundle = new ResourceBundle() {
            @Override
            protected Object handleGetObject(String key) {
                return primaryStage;
            }

            @Override
            public Enumeration<String> getKeys() {
                return null;
            }
        };
        fxmlLoader.setResources(bundle);
        Scene scene = new Scene(fxmlLoader.load(getClass().getResource("main.fxml").openStream()));
        primaryStage.setTitle("Visualization of Various Shortest Path Algorithms on Grid Graphs");
        primaryStage.setScene(scene);
        primaryStage.setWidth(ApplicationConstants.WINDOW_WIDTH);
        primaryStage.setHeight(ApplicationConstants.WINDOW_HEIGHT);
        primaryStage.show();

        // TODO: konnte ich leider nur in main aufrufen - scene ist erst nach primaryStage.show() verfügbar :-S
        scene.setOnKeyPressed((KeyEvent e) -> {
            if (e.getCode() == KeyCode.RIGHT)    MapHolder.getOurInstance().setCamera(1, 0);
            if (e.getCode() == KeyCode.LEFT)    MapHolder.getOurInstance().setCamera(-1, 0);
            if (e.getCode() == KeyCode.UP)    MapHolder.getOurInstance().setCamera(0, -1);
            if (e.getCode() == KeyCode.DOWN)    MapHolder.getOurInstance().setCamera(0, 1);
            e.consume();
        });
    }
}
