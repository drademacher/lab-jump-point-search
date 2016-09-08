package core.exception;

import core.util.Vector;

/**
 * An InvalidCoordinateException in thrown whenever a a operation on a Map instance is called with an input coordinate which is outside the Map dimensions.
 *
 * @author Patrick Loka
 * @version 1.0
 * @see core.map.Map
 * @see core.map.MapController
 * @since 1.0
 */
public class InvalidCoordinateException extends Exception {

    /**
     * Init InvalidCoordinateException considering the given Vector causing the Exception.
     *
     * @param coordinate invalid coordinate causing the Exception
     * @see core.map.Map
     * @see core.map.MapController
     * @since 1.0
     */
    public InvalidCoordinateException(Vector coordinate) {
        super("Field is not part of the map: (" + coordinate.getX() + "," + coordinate.getY() + ")");
    }
}
