package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
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

        // settings
        n = (int) (((StackPane) canvas.getParent()).getWidth() / size);
        m = (int) (((StackPane) canvas.getParent()).getHeight() / size);


        canvas.setWidth(size * n);
        canvas.setHeight(size * m);

        render();
    }

    private void render() {
        // full rendering of the map
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++) {
                // change color
                // gc.setFill(CELLS[grid.getCell(x, y)]);

                // draw rect
                gc.setFill(Color.BLACK);
                gc.fillRect(x * size, y * size, size, size);

                gc.setFill(Color.GRAY);
                gc.fillRect(x * size, y * size, size - 1, size - 1);
            }
        }

        gc.clearRect(0, canvas.getHeight() - 2, canvas.getWidth(), canvas.getHeight());
        gc.clearRect(canvas.getWidth() - 2, 0, canvas.getWidth(), canvas.getHeight());
    }
}
