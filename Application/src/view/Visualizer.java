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
public class Visualizer {
    private Visualizer ourInstance = new Visualizer();

    private int xSizeVis, ySizeVis;

    private Map map;
    private Canvas canvas;


    private Visualizer() {

    }

    public Visualizer getOurInVisualizer() {
        return ourInstance;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }


}
