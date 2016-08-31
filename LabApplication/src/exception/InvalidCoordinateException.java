package exception;

import util.Vector;

/**
 * Created by paloka on 01.06.16.
 */
public class InvalidCoordinateException extends Exception {
    public InvalidCoordinateException(Vector coordinate) {
        super("Field is not part of the map: (" + coordinate.getX() + "," + coordinate.getY() + ")");
    }
}
