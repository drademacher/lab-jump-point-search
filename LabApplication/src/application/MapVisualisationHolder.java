package application;

import exception.InvalidCoordinateException;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import map.MapFacade;

import static application.ApplicationConstants.*;
import static application.FieldVisualisation.GRID_POINT;
import static application.FieldVisualisation.OBSTACLE_POINT;

/**
 * Created by paloka on 06.06.16.
 */
public class MapVisualisationHolder {

    private MapFacade map;

    private Canvas canvas;
    private OnMouseClickedCallback onMouseClickedCallback;
    private int fieldSize   = 20;

    MapVisualisationHolder(Canvas canvas){
        this.canvas = canvas;

        canvas.setOnScroll(event -> {
            if(event.getDeltaY()==0)  return;
            if(event.getDeltaY()>0 && fieldSize+ZOOM_FACTOR<=ZOOM_MAX)  this.fieldSize  += ZOOM_FACTOR;
            if(event.getDeltaY()<0 && fieldSize-ZOOM_FACTOR>=ZOOM_MIN)  this.fieldSize  -= ZOOM_FACTOR;
            renderMap();
        });

        canvas.setOnMouseClicked(event -> {
            if(onMouseClickedCallback==null)    return;
            int x   = new Double((event.getX() -1) / this.fieldSize).intValue();
            int y   = new Double((event.getY() -2) / this.fieldSize).intValue();
            this.onMouseClickedCallback.call(x,y);
        });
    }


    /* ------- Setter ------- */

    void setMap(MapFacade map){
        this.map    = map;
        renderMap();
    }

    void setOnMouseClickedCallback(OnMouseClickedCallback callback){
        this.onMouseClickedCallback = callback;
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
                } catch (InvalidCoordinateException e) {
                    e.printStackTrace();
                    //Todo: ApplicationController.renderMap - InvalidCoordinateException
                }
                gc.fillRect(x * this.fieldSize + 1, y * this.fieldSize + 1, this.fieldSize -1, this.fieldSize - 1);
            }
        }
    }


    /* ------- Callback Type ------- */

    interface OnMouseClickedCallback {
        void call(int x, int y);
    }
}