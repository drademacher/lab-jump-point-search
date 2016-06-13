package exception;

import util.Coordinate;

/**
 * Created by paloka on 01.06.16.
 */
public class MapInitialisationException extends Exception {
    public MapInitialisationException() {
        super("Error while Map initialisation");
    }

    public MapInitialisationException(Coordinate dimension) {
        super("Error while Map initialisation with dimension (" + dimension.getX() + "," + dimension.getY() + ")");
    }
}
