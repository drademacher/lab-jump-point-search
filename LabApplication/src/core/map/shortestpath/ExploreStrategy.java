package core.map.shortestpath;

import core.map.MapFacade;
import core.map.movingrule.MovingRule;
import core.util.Tuple2;
import core.util.Vector;

import java.util.Collection;

/**
 * ExploreStrategy defines which strategy is used to explore a grid map.
 *
 * @author Patrick Loka
 * @version 1.0
 * @since 1.0
 */
interface ExploreStrategy {

    /**
     * Defines one step in a grid map exploring strategy returning the next relevant reached point by running in a given direction.<br>
     * <br>
     * A valid implementation returns the next point to process by running from a given point in a given direction, making sure,
     * that intermediate points are unimportant for reaching a given goal considering the implemented strategy.
     *
     * @param map grid map on which the shortest path search is running.
     * @param currentPoint currently processed point on the map.
     * @param direction direction in which the currentPoint is reached.
     * @param cost costs paid to reach the currentPoint from the startPoint of the shortest path search.
     * @param goal goal point of the shortest path search.
     * @param movingRule movement rules for the given grid map.
     * @return &lt; Next Point to process | cost to reach next Point &gt;
     * @since 1.0
     */
    Tuple2<Vector, Double> exploreStrategy(MapFacade map, Vector currentPoint, Vector direction, Double cost, Vector goal, MovingRule movingRule);

    /**
     * Returns the moving directions to process for the given currentPoint.<br>
     * <br>
     * A valid implementation returns all outgoing directions where the current point is possibly on the shortest path to the goal point.
     *
     * @param map grid map on which the shortest path search is running.
     * @param currentPoint currently processed point.
     * @param predecessor predecessor of the current point.
     * @param movingRule movement rules for the given grid map.
     * @return Set of directions to explore next starting at the currentPoint.
     * @since 1.0
     */
    Collection<Vector> getDirectionsStrategy(MapFacade map, Vector currentPoint, Vector predecessor, MovingRule movingRule);
}
