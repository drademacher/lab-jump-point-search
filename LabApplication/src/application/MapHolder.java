package application;

import exception.InvalidCoordinateException;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import map.MapFacade;
import shortestpath.ShortestPathResult;
import util.Coordinate;

import static application.ApplicationConstants.*;
import static application.FieldVisualisation.*;

/**
 * Created by paloka on 06.06.16.
 */
public class MapHolder {

    private MapFacade map;
    private ShortestPathResult shortestPathResult;

    private Canvas canvas;
    private OnMouseClickedCallback onMouseClickedCallback;
    private int fieldSize = 10;

    MapHolder(Canvas canvas) {
        this.canvas = canvas;

        canvas.setOnScroll(event -> {
            if (event.getDeltaY() == 0) return;
            if (event.getDeltaY() > 0 && fieldSize + ZOOM_FACTOR <= ZOOM_MAX) this.fieldSize += ZOOM_FACTOR;
            if (event.getDeltaY() < 0 && fieldSize - ZOOM_FACTOR >= ZOOM_MIN) this.fieldSize -= ZOOM_FACTOR;
            //Todo: große Maps können ab einer bestimmten FIELD_SIZE nicht mehr angezeigt werden, der Versuch führt zu einer RuntimeException
            renderMap();
        });

        canvas.setOnMouseClicked(event -> {
            if (onMouseClickedCallback == null) return;
            int x = new Double((event.getX() - 1) / this.fieldSize).intValue();
            int y = new Double((event.getY() - 2) / this.fieldSize).intValue();
            this.onMouseClickedCallback.call(new Coordinate(x,y));
        });
    }

    void refreshMap() {
        this.shortestPathResult = null;
        renderMap();
    }

    void switchPassable(Coordinate coordinate) {
        try {
            renderField(coordinate, map.isPassable(coordinate) ? GRID_POINT : OBSTACLE_POINT);
        } catch (InvalidCoordinateException e) {
            e.printStackTrace();
            //Todo: ApplicationController.renderField - InvalidCoordinateException
        }
    }

    void setStartPoint(Coordinate coordinate) {
        renderField(coordinate, START_POINT);
    }

    void setGoalPoint(Coordinate coordinate) {
        renderField(coordinate, GOAL_POINT);
    }

    void setShortestPath(ShortestPathResult shortestPath) {
        this.shortestPathResult = shortestPath;
        renderShortestPathResult();
    }

    /* ------- Getter & Setter ------- */


    void setMap(MapFacade map) {
        this.map = map;
        //Todo: große Maps können ab einer bestimmten FIELD_SIZE nicht mehr angezeigt werden, der Versuch führt zu einer RuntimeException
    }

    MapFacade getMap() {
        // TODO: getter nur fürs speichern benutzt
        return map;
    }

    void setOnMouseClickedCallback(OnMouseClickedCallback callback) {
        this.onMouseClickedCallback = callback;
    }

    boolean isPassable(Coordinate coordinate) throws InvalidCoordinateException {
        return this.map.isPassable(coordinate);
    }


    /* ------- Helper ------- */

    private void renderMap() {
        if (map == null || canvas == null) return;
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        this.canvas.setWidth(this.fieldSize * this.map.getXDim() + 1);
        this.canvas.setHeight(this.fieldSize * this.map.getYDim() + 1);

        gc.setFill(Paint.valueOf("#212121"));
        gc.fillRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());

        for (int x = 0; x < this.map.getXDim(); x++) {
            for (int y = 0; y < this.map.getYDim(); y++) {
                try {
                    gc.setFill(this.map.isPassable(new Coordinate(x,y)) ? GRID_POINT.getColor() : OBSTACLE_POINT.getColor());
                    gc.fillRect(x * this.fieldSize + 1, y * this.fieldSize + 1, this.fieldSize - 1, this.fieldSize - 1);
                } catch (InvalidCoordinateException e) {
                    e.printStackTrace();
                    //Todo: ApplicationController.renderMap - InvalidCoordinateException
                }
            }
        }
        renderShortestPathResult();
    }

    private void renderShortestPathResult() {
        if (shortestPathResult == null) return;
        for (Coordinate jumpPoint : this.shortestPathResult.getOpenList()) {
            renderField(jumpPoint, JUMP_POINT);
        }
        for (Coordinate visitedPoint : this.shortestPathResult.getVisited()) {
            renderField(visitedPoint, VISITED_POINT);
        }
        for (Coordinate pathPoint : this.shortestPathResult.getShortestPath()) {
            renderField(pathPoint, PATH_POINT);
        }
        setStartPoint(this.shortestPathResult.getStart());
        setGoalPoint(this.shortestPathResult.getGoal());
    }

    private void renderField(Coordinate coordinate, FieldVisualisation field) {
        if (this.canvas == null
                || coordinate.getX() < 0 || coordinate.getY() < 0
                || (this.canvas.getWidth() - 1) / this.fieldSize < coordinate.getX()
                || (this.canvas.getHeight() - 1) / this.fieldSize < coordinate.getY()) return;
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        gc.setFill(field.getColor());
        gc.fillRect(coordinate.getX() * this.fieldSize + 1, coordinate.getY() * this.fieldSize + 1, this.fieldSize - 1, this.fieldSize - 1);
    }


    /* ------- Callback Type ------- */

    interface OnMouseClickedCallback {
        void call(Coordinate coordinate);
    }
}