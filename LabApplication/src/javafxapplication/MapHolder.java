package javafxapplication;

import core.exception.InvalidCoordinateException;
import core.map.MapFacade;
import core.map.shortestpath.ShortestPathResult;
import core.util.Tuple2;
import core.util.Vector;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;

import static javafxapplication.ApplicationConstants.*;
import static javafxapplication.FieldVisualisation.*;

/**
 *
 *
 * @author Danny Rademacher, Patrick Loka
 * @version 1.0
 * @since 1.0
 */
public class MapHolder {
    private Vector cameraDim;
    private Vector cameraPos = new Vector(0, 0);

    private MapFacade map;
    private Vector startPoint, goalPoint;
    BooleanProperty hasStartPoint, hasGoalPoint, hasShortestPathResult;
    private ShortestPathResult shortestPathResult;
    private long shortestPathTime;

    private Canvas gridCanvas, openListCanvas, closedListCanvas, pathCanvas, detailsCanvas;
    private OnMouseClickedCallback onMouseClickedCallback;
    private int fieldSize = 10;
    private int padding = (fieldSize > 5) ? 1 : 0;

    MapHolder(Canvas gridCanvas, Canvas closedListCanvas, Canvas openListCanvas, Canvas pathCanvas, Canvas detailsCanvas) {
        this.gridCanvas = gridCanvas;
        this.openListCanvas = openListCanvas;
        this.closedListCanvas = closedListCanvas;
        this.pathCanvas = pathCanvas;
        this.detailsCanvas = detailsCanvas;

        this.hasStartPoint = new SimpleBooleanProperty(false);
        this.hasGoalPoint = new SimpleBooleanProperty(false);
        this.hasShortestPathResult = new SimpleBooleanProperty(false);

        gridCanvas.setOnScroll(event -> {
            if (event.getDeltaY() == 0) return;
            if (event.getDeltaY() > 0 && fieldSize + ZOOM_FACTOR <= ZOOM_MAX) this.fieldSize += ZOOM_FACTOR;
            if (event.getDeltaY() < 0 && fieldSize - ZOOM_FACTOR >= ZOOM_MIN) this.fieldSize -= ZOOM_FACTOR;
            padding = (fieldSize > 5) ? 1 : 0;
            updateCamera();
            renderMap();
        });

        gridCanvas.setOnMouseClicked(event -> {
            if (onMouseClickedCallback == null) return;
            int x = new Double((event.getX() - 1) / this.fieldSize).intValue();
            int y = new Double((event.getY() - 2) / this.fieldSize).intValue();
            try {
                this.onMouseClickedCallback.call(new Vector(x, y).add(cameraPos));
            } catch (InvalidCoordinateException e) {
                e.printStackTrace();
            }
        });
    }



    /* ------- Getter & Setter ------- */

    public Vector getStartPoint() {
        return startPoint;
    }

    public Vector getGoalPoint() {
        return goalPoint;
    }

    public String getShortestPathResult() {
        return "The running time is " + shortestPathTime + " ms. \nThe cost of the shortest path are " + String.format("%1$,.2f ", shortestPathResult.getCost()) + ".";
    }
    void setMap(MapFacade map) {
        this.map = map;
        startPoint = null;
        hasStartPoint.set(false);
        goalPoint = null;
        hasGoalPoint.set(false);
        updateCamera();
        refreshMap();
    }

    void setStartPoint(Vector coordinate) {
        hasStartPoint.set(true);
        startPoint = coordinate;
        refreshMap();
    }

    void setGoalPoint(Vector coordinate) {
        hasGoalPoint.set(true);
        goalPoint = coordinate;
        refreshMap();
    }

    void setShortestPath(Tuple2<ShortestPathResult, Long> result) {
        this.shortestPathResult = result.getArg1();
        hasShortestPathResult.set(true);
        shortestPathTime = result.getArg2();
        renderMap();
    }

    void setOnMouseClickedCallback(OnMouseClickedCallback callback) {
        this.onMouseClickedCallback = callback;
    }

    boolean isPassable(Vector coordinate) {
        return this.map.isPassable(coordinate);
    }


    /* ------- Functions for rendering canvas ------- */

    private void renderMap() {
        if (map == null) return;

        openListCanvas.getGraphicsContext2D().clearRect(0, 0, this.openListCanvas.getWidth(), this.openListCanvas.getHeight());
        closedListCanvas.getGraphicsContext2D().clearRect(0, 0, this.closedListCanvas.getWidth(), this.closedListCanvas.getHeight());
        pathCanvas.getGraphicsContext2D().clearRect(0, 0, this.pathCanvas.getWidth(), this.pathCanvas.getHeight());
        detailsCanvas.getGraphicsContext2D().clearRect(0, 0, this.detailsCanvas.getWidth(), this.detailsCanvas.getHeight());

        GraphicsContext gc = this.gridCanvas.getGraphicsContext2D();
        gc.setFill(OBSTACLE_POINT.getColor());
        gc.fillRect(0, 0, this.gridCanvas.getWidth(), this.gridCanvas.getHeight());

        for (int x = 0; x < cameraDim.getX(); x++) {
            for (int y = 0; y < cameraDim.getY(); y++) {
                renderField(gridCanvas, new Vector(x, y).add(cameraPos), isPassable(new Vector(x, y).add(cameraPos)) ? GRID_POINT : OBSTACLE_POINT);
            }
        }

        if (shortestPathResult != null) {
            for (Vector point : this.shortestPathResult.getClosedList())
                renderField(closedListCanvas, point, JUMP_POINT);
            for (Vector point : this.shortestPathResult.getOpenList())
                renderField(openListCanvas, point, VISITED_POINT);
            for (Tuple2<Vector, Boolean> pathPoint : this.shortestPathResult.getShortestPath())
                renderField(pathPoint.getArg2() ? pathCanvas : detailsCanvas, pathPoint.getArg1(), PATH_POINT);
        } else {
            if (startPoint != null) renderField(pathCanvas, startPoint, START_POINT);
            if (goalPoint != null) renderField(pathCanvas, goalPoint, GOAL_POINT);
        }
    }


    void refreshMap() {
        this.shortestPathResult = null;
        hasShortestPathResult.set(false);
        renderMap();
    }

    void updateField(Vector coordinate) {
        renderField(gridCanvas, coordinate, isPassable(coordinate) ? GRID_POINT : OBSTACLE_POINT);
    }

    private void renderField(Canvas canvas, Vector coordinate, FieldVisualisation field) {
        coordinate = coordinate.sub(cameraPos);
        if (coordinate.getX() < 0 || coordinate.getY() < 0 || cameraDim.getX() < coordinate.getX() || cameraDim.getY() < coordinate.getY()) return;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(field.getColor());
        gc.fillRect(coordinate.getX() * this.fieldSize + 1, coordinate.getY() * this.fieldSize + 1, this.fieldSize - padding, this.fieldSize - padding);
    }


    /* ------- Camera logic ------- */

    public void moveCamera(Vector diff) {
        int factor = 1; // TODO: implement logic for a nice multiplier
        cameraPos = cameraPos.add(diff.mult(factor));
        adjustCamera();
        renderMap();
    }

    private void updateCamera() {
        StackPane pane = (StackPane) gridCanvas.getParent();

        cameraDim = new Vector((int) Math.min(((pane.getWidth() - 1) / fieldSize), map.getXDim()), (int) Math.min(((pane.getHeight() - 1) / fieldSize), map.getYDim()));

        this.gridCanvas.setWidth(this.fieldSize * cameraDim.getX() + 1);
        this.gridCanvas.setHeight(this.fieldSize * cameraDim.getY() + 1);
        this.closedListCanvas.setWidth(this.fieldSize * cameraDim.getX() + 1);
        this.closedListCanvas.setHeight(this.fieldSize * cameraDim.getY() + 1);
        this.openListCanvas.setWidth(this.fieldSize * cameraDim.getX() + 1);
        this.openListCanvas.setHeight(this.fieldSize * cameraDim.getY() + 1);
        this.pathCanvas.setWidth(this.fieldSize * cameraDim.getX() + 1);
        this.pathCanvas.setHeight(this.fieldSize * cameraDim.getY() + 1);
        this.detailsCanvas.setWidth(this.fieldSize * cameraDim.getX() + 1);
        this.detailsCanvas.setHeight(this.fieldSize * cameraDim.getY() + 1);

        adjustCamera();
    }

    /**
     * Adjusts the camera in case the camera position does not match the windows or map boundaries.
     */
    private void adjustCamera() {
        if (map == null) return;
        cameraPos = new Vector(
                Math.min(Math.max(0, cameraPos.getX()), map.getXDim() - cameraDim.getX()),
                Math.min(Math.max(0, cameraPos.getY()), map.getYDim() - cameraDim.getY()));
    }




    /* ------- Callback Type ------- */

    interface OnMouseClickedCallback {
        void call(Vector coordinate) throws InvalidCoordinateException;
    }
}