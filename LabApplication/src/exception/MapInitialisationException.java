package exception;

import util.Vector;

/**
 * Created by paloka on 01.06.16.
 */
public class MapInitialisationException extends Exception {
    public MapInitialisationException() {
        super("Error while Map initialisation");
    }

    public MapInitialisationException(Vector dimension) {
        super("Error while Map initialisation with dimension (" + dimension.getX() + "," + dimension.getY() + ")");
    }
}
