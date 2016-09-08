package core.map;

import core.util.Vector;

/**
 * The MapFacade interface allows to share information about the state of an specific implementation without giving the opportunity to manipulate data. (read only)
 *
 * @author Patrick Loka
 * @version 1.0
 * @see Map
 * @see MapController
 * @since 1.0
 */
public interface MapFacade {

    /**
     * Returns x dimension of the MapFacade instance<br>
     * <br>
     * A valid implementation returns the maximum x-value of all known vectors
     *
     * @return x dimension of the MapFacade instance
     * @see Map
     * @since 1.0
     */
    int getXDim();

    /**
     * Returns y dimension of the MapFacade instance<br>
     * <br>
     * A valid implementation returns the maximum y-value of all known vectors
     *
     * @return y dimension of the MapFacade instance
     * @see Map
     * @since 1.0
     */
    int getYDim();

    /**
     * Checks whether a vector is passable or not<br>
     * <br>
     * A valid implementation returns only true, iff the vector is explicit marked as passable
     *
     * @param vector point to check whether it is allowed to pass or not
     * @return true, if input vector is passable, else false
     * @see Map
     * @since 1.0
     */
    boolean isPassable(Vector vector);
}
