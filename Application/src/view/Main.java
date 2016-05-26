package view;

import grid.Map;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    private Global global = Global.getInstance();

    private Canvas canvas;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // load stuff
        FXMLLoader fxmlLoader = new FXMLLoader();
        BorderPane p = fxmlLoader.load(getClass().getResource("main.fxml").openStream());
        Controller ctrl = fxmlLoader.getController();
        this.canvas = ctrl.canvas;

        // initialize stage
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(p, 1200, 800));
        primaryStage.show();

        settings(primaryStage);

        ctrl.showNiceMap();
        ctrl.renderCanvas();
    }

    private void settings(Stage primaryStage) {
        final double w = ((StackPane) canvas.getParent()).getWidth();
        final double h = ((StackPane) canvas.getParent()).getHeight();

        global.n = (int) ((w - 1) / global.size);
        global.m = (int) ((h -1) / global.size);
        global.map = new Map(global.n, global.m);

        canvas.setWidth(global.size * global.n + 1);
        canvas.setHeight(global.size * global.m + 1);

        // fit window
        primaryStage.setWidth(primaryStage.getWidth() - w + canvas.getWidth());
        primaryStage.setHeight(primaryStage.getHeight() - h + canvas.getHeight());
        primaryStage.setResizable(false);
    }


}
