package view;

import controller.map.Map;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    private Global global = Global.getInstance();
    private Controller ctrl;
    private Canvas canvas;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // load stuff
        FXMLLoader fxmlLoader = new FXMLLoader();
        BorderPane p = fxmlLoader.load(getClass().getResource("main.fxml").openStream());
        ctrl = fxmlLoader.getController();
        this.canvas = ctrl.canvas;

        // initialize stage

        primaryStage.setScene(new Scene(p));
        // primaryStage.setMaximized();
        primaryStage.show();

        settings(primaryStage);

        // ctrl.showNiceMap();
        ctrl.renderCanvas();
    }

    private void settings(Stage primaryStage) {
        // fit window
        primaryStage.setMinWidth(800);
        // TODO: setting min height to 600 causing the buttons to disappear
        primaryStage.setMinHeight(700);
        // System.out.print(primaryStage.getMaxHeight());

        primaryStage.setTitle("Visualization of Various Shortest Path Algorithms on Grid Graphs");
        // primaryStage.setWidth(primaryStage.getWidth() - w + canvas.getWidth());
        // primaryStage.setHeight(primaryStage.getHeight() - h + canvas.getHeight());

        primaryStage.widthProperty().addListener((obs, prev, next) -> {
            final double w = ((StackPane) canvas.getParent()).getWidth();
            final double new_w = w + next.doubleValue() - prev.doubleValue() - 1;

            global.n = (int) (new_w / global.size);
            canvas.setWidth(global.size * global.n + 1);
            global.map = new Map(global.n, global.m);
            ctrl.renderCanvas();
        });

        primaryStage.heightProperty().addListener((obs, prev, next) -> {
            final double h = ((StackPane) canvas.getParent()).getHeight();
            final double new_h = h + next.doubleValue() - prev.doubleValue() - 1;

            global.m = (int) (new_h / global.size);
            canvas.setHeight(global.size * global.m + 1);
            global.map = new Map(global.n, global.m);
            ctrl.renderCanvas();
        });

        primaryStage.setWidth(1200);
        primaryStage.setHeight(800);
    }


}
