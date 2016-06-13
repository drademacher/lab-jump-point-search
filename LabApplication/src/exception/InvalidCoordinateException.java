package exception;

import util.Coordinate;

/**
 * Created by paloka on 01.06.16.
 */
public class InvalidCoordinateException extends Exception {
    public InvalidCoordinateException(Coordinate coordinate) {
        super("Field is not part of the map: (" + coordinate.getX() + "," + coordinate.getY() + ")");
    }
}
