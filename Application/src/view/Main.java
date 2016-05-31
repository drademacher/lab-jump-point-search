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



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // load stuff
        FXMLLoader fxmlLoader = new FXMLLoader();
        BorderPane p = fxmlLoader.load(getClass().getResource("main.fxml").openStream());
        ctrl = fxmlLoader.getController();

        GridCanvas.getInstance().setCanvas(ctrl.canvas);

        // initialize stage

        primaryStage.setScene(new Scene(p));
        // primaryStage.setMaximized();
        primaryStage.show();

        settings(primaryStage);

        // ctrl.showNiceMap();

        GridCanvas.getInstance().renderCanvas();
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

        // TODO: make this dependend on xSizeVis not on actuall map size
        primaryStage.widthProperty().addListener((obs, prev, next) -> {
            final double w = ((StackPane) ctrl.canvas.getParent()).getWidth();
            final double new_w = w + next.doubleValue() - prev.doubleValue() - 1;

            global.xSizeVis = (int) (new_w / global.size);
            ctrl.canvas.setWidth(global.size * global.xSizeVis + 1);

            GridCanvas.getInstance().renderCanvas();
        });

        primaryStage.heightProperty().addListener((obs, prev, next) -> {
            final double h = ((StackPane) ctrl.canvas.getParent()).getHeight();
            final double new_h = h + next.doubleValue() - prev.doubleValue() - 1;

            global.ySizeVis = (int) (new_h / global.size);
            ctrl.canvas.setHeight(global.size * global.ySizeVis + 1);

            GridCanvas.getInstance().renderCanvas();
        });

        primaryStage.setWidth(1200);
        primaryStage.setHeight(800);

        global.n = global.xSizeVis;
        global.m = global.ySizeVis;

        global.map = new Map(global.n, global.m);
    }


}
