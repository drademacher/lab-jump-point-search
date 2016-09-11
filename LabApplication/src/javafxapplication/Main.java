package javafxapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * Main class to start up the application.
 *
 * @author Danny Rademacher
 * @version 1.0
 * @since 1.0
 */
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
    }
}
