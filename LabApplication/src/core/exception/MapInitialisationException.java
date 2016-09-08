package core.exception;

import core.util.Vector;

import java.io.File;

/**
 * A MapInitialisationException is thrown whenever an initialization of a Map fails caused by the input parameters.
 *
 * @author Patrick Loka
 * @version 1.0
 * @see core.map.Map
 * @see core.map.MapFactory
 * @see core.map.MapController
 * @since 1.0
 */
public class MapInitialisationException extends Exception {

    /**
     * Init MapInitialisationException considering the given file causing the Exception.
     *
     * @param file invalid file causing the Exception
     * @see core.map.MapFactory
     * @see core.map.MapController
     * @since 1.0
     */
    public MapInitialisationException(File file) {
        super("Error while Map initialisation by loading file "+file);
    }

    /**
     * Init MapInitialisationException considering the given Vector causing the Exception.
     *
     * @param dimension invalid dimension causing the Exception
     * @see core.map.Map
     * @see core.map.MapController
     * @since 1.0
     */
    public MapInitialisationException(Vector dimension) {
        super("Error while Map initialisation with dimension (" + dimension.getX() + "," + dimension.getY() + ")");
    }
}
