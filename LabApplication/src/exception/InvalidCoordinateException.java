package exception;

/**
 * Created by paloka on 01.06.16.
 */
public class InvalidCoordinateException extends Exception {
    public InvalidCoordinateException(int x, int y) {
        super("Field is not part of the map: (" + x + "," + y + ")");
    }
}
