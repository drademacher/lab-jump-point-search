package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Scene scene = new Scene(fxmlLoader.load(getClass().getResource("main.fxml").openStream()));
        primaryStage.setTitle("Visualization of Various Shortest Path Algorithms on Grid Graphs");
        primaryStage.setScene(scene);
        primaryStage.setWidth(ViewConstants.WINDOW_WIDTH);
        primaryStage.setHeight(ViewConstants.WINDOW_HEIGHT);
        primaryStage.show();
    }
}
