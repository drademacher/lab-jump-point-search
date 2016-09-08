package core.exception;

import core.util.Vector;

/**
 * A NoPathFoundException is thrown whenever there is no path between start and goal considering the current map and moving rule.
 *
 * @author Patrick Loka
 * @version 1.0
 * @see core.map.shortestpath.ShortestPath
 * @see core.map.MapController
 * @since 1.0
 */
public class NoPathFoundException extends Exception {

    /**
     * Init MapInitialisationException considering the given start and gold point of the required path causing the Exception.
     *
     * @param start
     * @param goal
     * @see core.map.shortestpath.ShortestPath
     * @see core.map.MapController
     */
    public NoPathFoundException(Vector start, Vector goal) {
        super("No path found between "+start+" and "+goal);
    }
}
