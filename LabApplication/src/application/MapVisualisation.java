package application;

import exception.InvalidCoordinateException;
import exception.MapInitialisationException;
import javafx.scene.paint.Color;

import java.util.Arrays;

/**
 * Created by paloka on 02.06.16.
 */
class MapVisualisation {

    private FieldVisualisation[][] mapVisualisation;
    private int xDim;
    private int yDim;

    MapVisualisation(int xDim, int yDim, FieldVisualisation fieldVisualisation) throws MapInitialisationException {
        if(xDim<1||yDim<1) throw new MapInitialisationException(xDim,yDim);
        this.xDim   = xDim;
        this.yDim   = yDim;
        this.mapVisualisation    = new FieldVisualisation[xDim][yDim];
        for(FieldVisualisation[] col:mapVisualisation)  Arrays.fill(col, fieldVisualisation);
    }


    /* ------- Getter & Setter ------- */

    int getXDim() {
        return xDim;
    }

    int getYDim() {
        return yDim;
    }

    Color getColor(int x, int y) throws InvalidCoordinateException {
        isValideCoordinate(x,y);
        return mapVisualisation[x][y].getColor();
    }

    void setFieldVisualisation(int x, int y, FieldVisualisation fieldVisualisation) throws InvalidCoordinateException {
        isValideCoordinate(x,y);
        mapVisualisation[x][y]   = fieldVisualisation;
    }


    /* ------- Helper ------- */

    private void isValideCoordinate(int x, int y) throws InvalidCoordinateException {
        if(x<0||y<0||x>=xDim||y>=yDim) throw new InvalidCoordinateException(x,y);
    }
}
