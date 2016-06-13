package map;

import exception.InvalidCoordinateException;
import util.Coordinate;

/**
 * Created by paloka on 02.06.16.
 */
public interface MapFacade {

    int getXDim();

    int getYDim();

    boolean isPassable(Coordinate coordinate) throws InvalidCoordinateException;
}
