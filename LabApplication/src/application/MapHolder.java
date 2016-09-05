package application;

import exception.InvalidCoordinateException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import map.MapFacade;
import map.shortestpath.ShortestPathResult;
import util.Tuple2;
import util.Vector;

import static application.ApplicationConstants.*;
import static application.FieldVisualisation.*;

/**
 * Created by paloka on 06.06.16.
 */
public class MapHolder {
    private int xDimVis;
    private int xOffsetVis = 0;
    private int yDimVis;
    private int yOffsetVis = 0;

    private MapFacade map;
    private Vector startPoint, goalPoint;
    private BooleanProperty hasStartPoint, hasGoalPoint;
    private ShortestPathResult shortestPathResult;

    private Canvas gridCanvas, openlistCanvas, closedListCanvas, pathCanvas, detailsCanvas;
    private OnMouseClickedCallback onMouseClickedCallback;
    private int fieldSize = 10;

    MapHolder(Canvas gridCanvas, Canvas closedListCanvas, Canvas openListCanvas, Canvas pathCanvas, Canvas detailsCanvas) {
        this.gridCanvas = gridCanvas;
        this.openlistCanvas = openListCanvas;
        this.closedListCanvas = closedListCanvas;
        this.pathCanvas = pathCanvas;
        this.detailsCanvas = detailsCanvas;

        this.hasStartPoint = new SimpleBooleanProperty(false);
        this.hasGoalPoint = new SimpleBooleanProperty(false);

        gridCanvas.setOnScroll(event -> {
            if (event.getDeltaY() == 0) return;
            if (event.getDeltaY() > 0 && fieldSize + ZOOM_FACTOR <= ZOOM_MAX) this.fieldSize += ZOOM_FACTOR;
            if (event.getDeltaY() < 0 && fieldSize - ZOOM_FACTOR >= ZOOM_MIN) this.fieldSize -= ZOOM_FACTOR;
            updateDimVis();
            renderMap();
        });

        gridCanvas.setOnMouseClicked(event -> {
            if (onMouseClickedCallback == null) return;
            int x = new Double((event.getX() - 1) / this.fieldSize).intValue();
            int y = new Double((event.getY() - 2) / this.fieldSize).intValue();
            this.onMouseClickedCallback.call(new Vector(xOffsetVis + x, yOffsetVis + y));
        });
    }

    void refreshMap() {
        this.shortestPathResult = null;
        renderMap();
    }

    void switchPassable(Vector coordinate) {
        renderField(gridCanvas, coordinate, map.isPassable(coordinate) ? GRID_POINT : OBSTACLE_POINT);
    }

    void setStartPoint(Vector coordinate) {
        hasStartPoint.set(true);
        if (startPoint != null) clearField(pathCanvas, startPoint);
        startPoint = coordinate;
        if (shortestPathResult != null) {
            refreshMap();
        } else {
            renderField(pathCanvas, startPoint, START_POINT);
        }
    }

    void setGoalPoint(Vector coordinate) {
        hasGoalPoint.set(true);
        if (goalPoint != null) clearField(pathCanvas, goalPoint);
        goalPoint = coordinate;
        if (shortestPathResult != null) {
            refreshMap();
        } else {
            renderField(pathCanvas, goalPoint, GOAL_POINT);
        }
    }

    void setShortestPath(ShortestPathResult shortestPath) {
        this.shortestPathResult = shortestPath;
        renderMap();
    }


    /* ------- Getter & Setter ------- */

    public BooleanProperty isSetStartPoint() {
        return hasStartPoint;
    }

    public BooleanProperty isSetGoalPoint() {
        return hasGoalPoint;
    }

    public Vector getStartPoint() {
        return startPoint;
    }

    public Vector getGoalPoint() {
        return goalPoint;
    }

    void setMap(MapFacade map) {
        this.map = map;
        startPoint = null;
        hasStartPoint.set(false);
        goalPoint = null;
        hasGoalPoint.set(false);
        updateDimVis();
        refreshMap();
    }

    void setOnMouseClickedCallback(OnMouseClickedCallback callback) {
        this.onMouseClickedCallback = callback;
    }

    boolean isPassable(Vector coordinate) throws InvalidCoordinateException {
        return this.map.isPassable(coordinate);
    }


    /* ------- Helper ------- */

    private void renderMap() {
        if (map == null || gridCanvas == null) return;
        GraphicsContext gc = this.gridCanvas.getGraphicsContext2D();

        int padding = (fieldSize > 5) ? 1 : 0;

        gc.setFill(OBSTACLE_POINT.getColor());
        gc.fillRect(0, 0, this.gridCanvas.getWidth(), this.gridCanvas.getHeight());

        for (int x = 0; x < xDimVis; x++) {
            for (int y = 0; y < yDimVis; y++) {
                gc.setFill(this.map.isPassable(new Vector(xOffsetVis + x,yOffsetVis + y)) ? GRID_POINT.getColor() : OBSTACLE_POINT.getColor());
                gc.fillRect(x * this.fieldSize + 1, y * this.fieldSize + 1, this.fieldSize - padding, this.fieldSize - padding);
            }
        }
        renderShortestPathResult();
        if (startPoint != null) renderField(pathCanvas, startPoint, START_POINT);
        if (goalPoint != null) renderField(pathCanvas, goalPoint, GOAL_POINT);
    }

    private void updateDimVis() {
        StackPane pane = (StackPane) gridCanvas.getParent();
        xDimVis = (int) ((pane.getWidth() - 1) / fieldSize);
        xDimVis = Math.min(xDimVis, map.getXDim());
        yDimVis = (int) ((pane.getHeight() - 1) / fieldSize);
        yDimVis = Math.min(yDimVis, map.getYDim());

        this.gridCanvas.setWidth(this.fieldSize * xDimVis + 1);
        this.gridCanvas.setHeight(this.fieldSize * yDimVis + 1);
        this.closedListCanvas.setWidth(this.fieldSize * xDimVis + 1);
        this.closedListCanvas.setHeight(this.fieldSize * yDimVis + 1);
        this.openlistCanvas.setWidth(this.fieldSize * xDimVis + 1);
        this.openlistCanvas.setHeight(this.fieldSize * yDimVis + 1);
        this.pathCanvas.setWidth(this.fieldSize * xDimVis + 1);
        this.pathCanvas.setHeight(this.fieldSize * yDimVis + 1);
        this.detailsCanvas.setWidth(this.fieldSize * xDimVis + 1);
        this.detailsCanvas.setHeight(this.fieldSize * yDimVis + 1);

        // adjust offsets if they are broken!
        setCamera(0, 0);
    }

    public void setCamera(int diffX, int diffY) {
        if (map == null) return;
        xOffsetVis += diffX;
        xOffsetVis = Math.min(Math.max(0, xOffsetVis), map.getXDim() - xDimVis);
        yOffsetVis += diffY;
        yOffsetVis = Math.min(Math.max(0, yOffsetVis), map.getYDim() - yDimVis);
        renderMap();
    }

    private void renderShortestPathResult() {
        openlistCanvas.getGraphicsContext2D().clearRect(0, 0, this.openlistCanvas.getWidth(), this.openlistCanvas.getHeight());
        closedListCanvas.getGraphicsContext2D().clearRect(0, 0, this.closedListCanvas.getWidth(), this.closedListCanvas.getHeight());
        pathCanvas.getGraphicsContext2D().clearRect(0, 0, this.pathCanvas.getWidth(), this.pathCanvas.getHeight());

        if (shortestPathResult == null) return;
        for (Vector jumpPoint : this.shortestPathResult.getOpenList()) {
            renderField(closedListCanvas, jumpPoint, JUMP_POINT);
        }
        for (Vector visitedPoint : this.shortestPathResult.getVisited()) {
            renderField(openlistCanvas, visitedPoint, VISITED_POINT);
            clearField(closedListCanvas, visitedPoint);
        }
        for (Tuple2<Vector, Boolean> pathPoint : this.shortestPathResult.getShortestPath()) {
            renderField(pathPoint.getArg2() ? pathCanvas : detailsCanvas, pathPoint.getArg1(), PATH_POINT);
        }
    }

    private void renderField(Canvas canvas, Vector coordinate, FieldVisualisation field) {
        if (coordinate.getX() < xOffsetVis || coordinate.getY() < yOffsetVis
                || xDimVis + xOffsetVis < coordinate.getX()
                || yDimVis + yOffsetVis < coordinate.getY()) return;

        coordinate = new Vector(coordinate.getX() - xOffsetVis, coordinate.getY() - yOffsetVis);

        int padding = (fieldSize > 5) ? 1 : 0;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(field.getColor());
        gc.fillRect(coordinate.getX() * this.fieldSize + 1, coordinate.getY() * this.fieldSize + 1, this.fieldSize - padding, this.fieldSize - padding);
    }

    private void clearField(Canvas canvas, Vector coordinate) {
        if (coordinate.getX() < xOffsetVis || coordinate.getY() < yOffsetVis
                || xDimVis + xOffsetVis < coordinate.getX()
                || yDimVis + yOffsetVis < coordinate.getY()) return;

        coordinate = new Vector(coordinate.getX() - xOffsetVis, coordinate.getY() - yOffsetVis);

        int padding = (fieldSize > 5) ? 1 : 0;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(coordinate.getX() * this.fieldSize + 1, coordinate.getY() * this.fieldSize + 1, this.fieldSize - padding, this.fieldSize - padding);
    }


    /* ------- Callback Type ------- */

    interface OnMouseClickedCallback {
        void call(Vector coordinate);
    }
}