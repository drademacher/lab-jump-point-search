package application;

import exception.InvalidCoordinateException;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import map.MapFacade;
import shortestpath.ShortestPathFacade;
import util.Tuple2;

import static application.ApplicationConstants.*;
import static application.FieldVisualisation.*;

/**
 * Created by paloka on 06.06.16.
 */
public class MapHolder {

    private MapFacade map;

    private Canvas canvas;
    private OnMouseClickedCallback onMouseClickedCallback;
    private int fieldSize   = 20;

    MapHolder(Canvas canvas){
        this.canvas = canvas;

        canvas.setOnScroll(event -> {
            if(event.getDeltaY()==0)  return;
            if(event.getDeltaY()>0 && fieldSize+ZOOM_FACTOR<=ZOOM_MAX)  this.fieldSize  += ZOOM_FACTOR;
            if(event.getDeltaY()<0 && fieldSize-ZOOM_FACTOR>=ZOOM_MIN)  this.fieldSize  -= ZOOM_FACTOR;
            //Todo: große Maps können ab einer bestimmten FIELD_SIZE nicht mehr angezeigt werden, der Versuch führt zu einer RuntimeException
            renderMap();
        });

        canvas.setOnMouseClicked(event -> {
            if(onMouseClickedCallback==null)    return;
            int x   = new Double((event.getX() -1) / this.fieldSize).intValue();
            int y   = new Double((event.getY() -2) / this.fieldSize).intValue();
            this.onMouseClickedCallback.call(x,y);
        });
    }

    void refreshMap(){
        renderMap();
    }

    void switchPassable(int x, int y){
        try {
            renderField(x,y,map.isPassable(x,y)?GRID_POINT:OBSTACLE_POINT);
        } catch (InvalidCoordinateException e) {
            e.printStackTrace();
            //Todo: ApplicationController.renderField - InvalidCoordinateException
        }
    }

    void setStartPoint(int x, int y){
        renderField(x,y,START_POINT);
    };

    void setGoalPoint(int x, int y){
        renderField(x,y,GOAL_POINT);
    }

    void setShortestPath(ShortestPathFacade shortestPath){
        for(Tuple2<Integer,Integer> jumpPoint:shortestPath.getJumppoints()){
            renderField(jumpPoint.getArg1(),jumpPoint.getArg2(),JUMP_POINT);
        }
        for(Tuple2<Integer,Integer> visitedPoint:shortestPath.getVisitedPoints()){
            renderField(visitedPoint.getArg1(),visitedPoint.getArg2(),VISITED_POINT);
        }
    }

    /* ------- Getter & Setter ------- */


    void setMap(MapFacade map){
        this.map    = map;
        //Todo: große Maps können ab einer bestimmten FIELD_SIZE nicht mehr angezeigt werden, der Versuch führt zu einer RuntimeException
    }

    void setOnMouseClickedCallback(OnMouseClickedCallback callback){
        this.onMouseClickedCallback = callback;
    }

    boolean isPassable(int x, int y) throws InvalidCoordinateException {
        return this.map.isPassable(x,y);
    }


    /* ------- Helper ------- */

    private void renderMap() {
        if(map==null || canvas==null)   return;
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        this.canvas.setWidth(this.fieldSize*this.map.getXDim() +1);
        this.canvas.setHeight(this.fieldSize*this.map.getYDim() +1);

        gc.setFill(Paint.valueOf("#212121"));
        gc.fillRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());

        for(int x=0;x<this.map.getXDim();x++){
            for(int y=0;y<this.map.getYDim();y++){
                try {
                    gc.setFill(this.map.isPassable(x,y)?GRID_POINT.getColor():OBSTACLE_POINT.getColor());
                    gc.fillRect(x * this.fieldSize + 1, y * this.fieldSize + 1, this.fieldSize -1, this.fieldSize - 1);
                } catch (InvalidCoordinateException e) {
                    e.printStackTrace();
                    //Todo: ApplicationController.renderMap - InvalidCoordinateException
                }
            }
        }
    }

    private void renderField(int x, int y, FieldVisualisation field) {
        if(this.canvas==null
                || x<0 || y<0
                || (this.canvas.getWidth()-1)/this.fieldSize<x
                || (this.canvas.getHeight()-1)/this.fieldSize<y) return;
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        gc.setFill(field.getColor());
        gc.fillRect(x * this.fieldSize + 1, y * this.fieldSize + 1, this.fieldSize -1, this.fieldSize - 1);
    }


    /* ------- Callback Type ------- */

    interface OnMouseClickedCallback {
        void call(int x, int y);
    }
}