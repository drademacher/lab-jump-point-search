package view;

import controller.map.Map;
import controller.map.NotAFieldException;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

/**
 * Created by danny on 31-May-16.
 */
public class GridCanvas {
    private static GridCanvas ourInstance = new GridCanvas();

    private Global global = Global.getInstance();
    private Map map;
    private Canvas canvas;

    public static GridCanvas getInstance() {
        return ourInstance;
    }

    private GridCanvas() {
    }


    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void renderCanvas() {

    }
}
