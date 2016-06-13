package map;

import exception.InvalidCoordinateException;
import exception.MapInitialisationException;
import util.Coordinate;

import java.util.Arrays;

/**
 * Created by paloka on 01.06.16.
 */
class Map implements MapFacade {

    private final boolean[][] map;
    private int xDim;
    private int yDim;

    Map(Coordinate dimension, boolean areFieldsPassable) throws MapInitialisationException {
        this.xDim = dimension.getX();
        this.yDim = dimension.getY();
        if (this.xDim < 1 || this.yDim < 1) throw new MapInitialisationException(dimension);
        this.map = new boolean[this.xDim][this.yDim];
        if (areFieldsPassable) {
            for (boolean[] col : this.map) Arrays.fill(col, true);
        }
    }


    /* ------- Getter & Setter ------- */

    @Override
    public int getXDim() {
        return xDim;
    }

    @Override
    public int getYDim() {
        return yDim;
    }

    @Override
    public boolean isPassable(Coordinate coordinate) throws InvalidCoordinateException {
        isValideCoordinate(coordinate);
        return map[coordinate.getX()][coordinate.getY()];
    }

    void switchPassable(Coordinate coordinate) throws InvalidCoordinateException {
        isValideCoordinate(coordinate);
        map[coordinate.getX()][coordinate.getY()] = !map[coordinate.getX()][coordinate.getY()];
    }


    /* ------- Helper ------- */

    private void isValideCoordinate(Coordinate coordinate) throws InvalidCoordinateException {
        if (coordinate.getX() < 0 || coordinate.getY() < 0 || coordinate.getX() >= this.xDim || coordinate.getY() >= this.yDim) throw new InvalidCoordinateException(coordinate);
    }
}
