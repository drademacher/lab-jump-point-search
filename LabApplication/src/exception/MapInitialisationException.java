package exception;

/**
 * Created by paloka on 01.06.16.
 */
public class MapInitialisationException extends Exception {
    public MapInitialisationException() {
        super("Error while Map initialisation");
    }

    public MapInitialisationException(int xDim, int yDim) {
        super("Error while Map initialisation with dimension (" + xDim + "," + yDim + ")");
    }
}
