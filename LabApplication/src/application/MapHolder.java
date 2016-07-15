package application;

import exception.InvalidCoordinateException;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import map.MapFacade;
import map.shortestpath.ShortestPathResult;
import util.Coordinate;

import static application.ApplicationConstants.*;
import static application.FieldVisualisation.*;

/**
 * Created by paloka on 06.06.16.
 */
public class MapHolder {
    private static MapHolder ourInstance;

    private int xDimVis;
    private int xOffsetVis = 0;
    private int yDimVis;
    private int yOffsetVis = 0;

    private MapFacade map;
    private ShortestPathResult shortestPathResult;

    private Canvas gridCanvas, obstacleCanvas, openlistCanvas, pathCanvas, detailsCanvas;
    private OnMouseClickedCallback onMouseClickedCallback;
    private int fieldSize = 10;

    MapHolder(Canvas gridCanvas, Canvas obstacleCanvas, Canvas openlistCanvas, Canvas pathCanvas, Canvas detailsCanvas) {
        ourInstance = this;

        this.gridCanvas = gridCanvas;
        this.obstacleCanvas = obstacleCanvas;
        this.openlistCanvas = openlistCanvas;
        this.pathCanvas = pathCanvas;
        this.detailsCanvas = detailsCanvas;

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
            this.onMouseClickedCallback.call(new Coordinate(xOffsetVis + x, yOffsetVis + y));
        });
    }

    public static MapHolder getOurInstance() {
        return ourInstance;
    }

    void refreshMap() {
        this.shortestPathResult = null;
        renderMap();
    }

    void switchPassable(Coordinate coordinate) {
        renderField(gridCanvas, coordinate, map.isPassable(coordinate) ? GRID_POINT : OBSTACLE_POINT);
    }

    void setStartPoint(Coordinate coordinate) {
        renderField(pathCanvas, coordinate, START_POINT);
    }

    void setGoalPoint(Coordinate coordinate) {
        renderField(pathCanvas, coordinate, GOAL_POINT);
    }

    void setShortestPath(ShortestPathResult shortestPath) {
        this.shortestPathResult = shortestPath;
        renderShortestPathResult();
    }


    /* ------- Getter & Setter ------- */

    void setMap(MapFacade map) {
        this.map = map;
        updateDimVis();
    }

    void setOnMouseClickedCallback(OnMouseClickedCallback callback) {
        this.onMouseClickedCallback = callback;
    }

    boolean isPassable(Coordinate coordinate) throws InvalidCoordinateException {
        return this.map.isPassable(coordinate);
    }


    /* ------- Helper ------- */

    private void renderMap() {
        if (map == null || gridCanvas == null) return;
        GraphicsContext gc = this.gridCanvas.getGraphicsContext2D();

        int padding = (fieldSize > 5) ? 1 : 0;

        // TODO: this removes the grid structure are large obstacle groups
        gc.setFill(OBSTACLE_POINT.getColor());
        gc.fillRect(0, 0, this.gridCanvas.getWidth(), this.gridCanvas.getHeight());

        for (int x = 0; x < xDimVis; x++) {
            for (int y = 0; y < yDimVis; y++) {
                // TODO: just print the grid point and discard the drawing of obstacles!
                gc.setFill(this.map.isPassable(new Coordinate(xOffsetVis + x,yOffsetVis + y)) ? GRID_POINT.getColor() : OBSTACLE_POINT.getColor());
                gc.fillRect(x * this.fieldSize + 1, y * this.fieldSize + 1, this.fieldSize - padding, this.fieldSize - padding);
            }
        }
        renderShortestPathResult();
    }

    private void updateDimVis() {
        StackPane pane = (StackPane) gridCanvas.getParent();
        xDimVis = (int) ((pane.getWidth() - 1) / fieldSize);
        xDimVis = Math.min(xDimVis, map.getXDim());
        yDimVis = (int) ((pane.getHeight() - 1) / fieldSize);
        yDimVis = Math.min(yDimVis, map.getYDim());

        this.gridCanvas.setWidth(this.fieldSize * xDimVis + 1);
        this.gridCanvas.setHeight(this.fieldSize * yDimVis + 1);
        this.obstacleCanvas.setWidth(this.fieldSize * xDimVis + 1);
        this.obstacleCanvas.setHeight(this.fieldSize * yDimVis + 1);
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
        pathCanvas.getGraphicsContext2D().clearRect(0, 0, this.pathCanvas.getWidth(), this.pathCanvas.getHeight());

        if (shortestPathResult == null) return;
        for (Coordinate jumpPoint : this.shortestPathResult.getOpenList()) {
            renderField(openlistCanvas, jumpPoint, JUMP_POINT);
        }
        for (Coordinate visitedPoint : this.shortestPathResult.getVisited()) {
            renderField(openlistCanvas, visitedPoint, VISITED_POINT);
        }
        for (Coordinate pathPoint : this.shortestPathResult.getShortestPath()) {
            renderField(pathCanvas, pathPoint, PATH_POINT);
        }
        setStartPoint(this.shortestPathResult.getStart());
        setGoalPoint(this.shortestPathResult.getGoal());
    }

    private void renderField(Canvas canvas, Coordinate coordinate, FieldVisualisation field) {
        if (coordinate.getX() < xOffsetVis || coordinate.getY() < yOffsetVis
                || xDimVis + xOffsetVis < coordinate.getX()
                || yDimVis + yOffsetVis < coordinate.getY()) return;

        coordinate = new Coordinate(coordinate.getX() - xOffsetVis, coordinate.getY() - yOffsetVis);

        int padding = (fieldSize > 5) ? 1 : 0;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(field.getColor());
        gc.fillRect(coordinate.getX() * this.fieldSize + 1, coordinate.getY() * this.fieldSize + 1, this.fieldSize - padding, this.fieldSize - padding);
    }


    /* ------- Callback Type ------- */

    interface OnMouseClickedCallback {
        void call(Coordinate coordinate);
    }
}