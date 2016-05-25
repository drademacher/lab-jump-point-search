package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class Main extends Application {
    final private Color BRIGHT = (Color) Paint.valueOf("#C0C0C0");
    final private Color DARK = (Color) Paint.valueOf("#4D4D4D");
    final private Color LINES = (Color) Paint.valueOf("#212121");

    private Canvas canvas;
    private int size = 20;
    private int n;
    private int m;

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

        render();
    }

    private void settings(Stage primaryStage) {
        final double w = ((StackPane) canvas.getParent()).getWidth();
        final double h = ((StackPane) canvas.getParent()).getHeight();

        n = (int) ((w - 1) / size);
        m = (int) ((h -1) / size);

        canvas.setWidth(size * n + 1);
        canvas.setHeight(size * m + 1);

        // fit window
        primaryStage.setWidth(primaryStage.getWidth() - w + canvas.getWidth());
        primaryStage.setHeight(primaryStage.getHeight() - h + canvas.getHeight());
        primaryStage.setResizable(false);
    }

    private void render() {
        // full rendering of the map
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(LINES);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++) {
                // change color
                // gc.setFill(CELLS[grid.getCell(x, y)]);

                // draw rect
                gc.setFill(BRIGHT);
                if (x == 2 && y > 3) {
                    gc.setFill(DARK);
                }
                gc.fillRect(x * size + 1, y * size + 1, size - 1, size - 1);
            }
        }

        // gc.clearRect(0, canvas.getHeight() - 1, canvas.getWidth(), canvas.getHeight());
        // gc.clearRect(canvas.getWidth() - 1, 0, canvas.getWidth(), canvas.getHeight());
    }
}
