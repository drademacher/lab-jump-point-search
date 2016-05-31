package view;

import controller.map.Map;
import controller.map.NotAFieldException;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

/**
 * Created by danny on 31-May-16.
 */
public class CanvasController {
    private int xSizeVis, ySizeVis;

    private Map map;
    private Canvas canvas;

    private void settings(Stage primaryStage) {
        map = new Map(Constant.defaultXDim, Constant.defaultYDim);

        // fit window

        // System.out.print(primaryStage.getMaxHeight());

        primaryStage.setTitle("Visualization of Various Shortest Path Algorithms on Grid Graphs");
        // primaryStage.setWidth(primaryStage.getWidth() - w + canvas.getWidth());
        // primaryStage.setHeight(primaryStage.getHeight() - h + canvas.getHeight());

        // TODO: make this depedent on xSizeVis not on actual map size
        primaryStage.widthProperty().addListener((obs, prev, next) -> {
            final double w = ((StackPane) canvas.getParent()).getWidth();
            final double new_w = w + next.doubleValue() - prev.doubleValue() - 1;

            xSizeVis = (int) (new_w / Constant.size);
            if (xSizeVis > map.getxDim()) {
                xSizeVis = map.getxDim();
            }
            canvas.setWidth(Constant.size * xSizeVis + 1);

            renderCanvas();
        });

        primaryStage.heightProperty().addListener((obs, prev, next) -> {
            final double h = ((StackPane) canvas.getParent()).getHeight();
            final double new_h = h + next.doubleValue() - prev.doubleValue() - 1;

            ySizeVis = (int) (new_h / Constant.size);
            if (ySizeVis > map.getyDim()) {
                ySizeVis = map.getyDim();
            }
            canvas.setHeight(Constant.size * ySizeVis + 1);

            renderCanvas();
        });

        primaryStage.setWidth(1200);
        primaryStage.setHeight(800);

    }

    public void renderCanvas() {
        // full rendering of the map
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Paint.valueOf("#212121"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (int x = 0; x < map.getxDim(); x++) {
            for (int y = 0; y < map.getyDim(); y++) {
                // change color
                // gc.setFill(CELLS[map.getCell(x, y)]);

                // draw rect
                try {
                    gc.setFill(map.getField(x, y).getColor());
                } catch (NotAFieldException e) {
                    // should not happen
                }
                gc.fillRect(x * Constant.size + 1, y * Constant.size + 1, Constant.size - 1, Constant.size - 1);
            }
        }
    }
}
