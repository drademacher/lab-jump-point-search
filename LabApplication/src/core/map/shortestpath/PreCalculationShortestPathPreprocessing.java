package core.map.shortestpath;

import core.map.MapFacade;
import core.map.movingrule.MovingRule;
import core.util.Tuple3;
import core.util.Vector;

import java.util.HashMap;

/**
 * PreCalculationShortestPathPreprocessing precomuptes the follower for a specific ExploreStrategy and stores them in a look up table
 * to get them fast in runtime.<br>
 * <br> PreCalculationShortestPathPreprocessing implements doPreprocessing by storing to all points and directions a following point.
 *
 * @author Patrick Loka
 * @version 1.0
 * @since 1.0
 */
abstract class PreCalculationShortestPathPreprocessing implements ExploreStrategy, ShortestPathPreprocessing {

    private HashMap<Vector, HashMap<Vector, Tuple3<Vector, Double, Boolean>>> preprocessing = new HashMap<>();

    /**
     * {@inheritDoc}
     * <br>
     * Implementation: runs through all passable points and explores the next relevant point considering the implemented strategy on the map to store it.
     *
     * @param map Given grid map to preprocess.
     * @param movingRule Allowed movements on the given grid map.
     * @since 1.0
     */
    @Override
    public void doPreprocessing(MapFacade map, MovingRule movingRule) {
        this.preprocessing = new HashMap<>();
        for (int x = 0; x < map.getXDim(); x++) {
            for (int y = 0; y < map.getYDim(); y++) {
                Vector current = new Vector(x, y);
                if (map.isPassable(current)) {
                    for (Vector direction : movingRule.getAllDirections()) {
                        this.exploreStrategy(map, current, direction, 0.0, null, movingRule);
                    }
                }
            }
        }
    }

    /**
     * Returns the stored preprocessed Point to the given key direction and key point.
     *
     * @param currentPoint point on the grid map
     * @param direction the direction, the current point is reached.
     * @return stored preprocessed point considering currentPoint and direction
     * @since 1.0
     */
    protected Tuple3<Vector, Double, Boolean> getPreprocessing(Vector currentPoint, Vector direction) {
        if (this.preprocessing.get(currentPoint) == null) return null;
        return this.preprocessing.get(currentPoint).get(direction);
    }

    /**
     * Stores a Point as preprocessed considering a key point and and a key direction.
     *
     * @param currentPoint key point
     * @param direction key direction
     * @param nextPoint point to store
     * @since 1.0
     */
    protected void putPreprocessing(Vector currentPoint, Vector direction, Tuple3<Vector, Double, Boolean> nextPoint) {
        preprocessing.putIfAbsent(currentPoint, new HashMap<>());
        preprocessing.get(currentPoint).put(direction, nextPoint);
    }

    /**
     * {@inheritDoc}
     *
     * @param map grid map on which the shortest path search is running.
     * @param currentPoint currently processed point on the map.
     * @param direction direction in which the currentPoint is reached.
     * @param cost costs paid to reach the currentPoint from the startPoint of the shortest path search.
     * @param goal goal point of the shortest path search.
     * @param movingRule movement rules for the given grid map.
     * @return &lt; Next Point to process | cost to reach next Point | flag for unreal jump points &gt;
     * @since 1.0
     */
    @Override
    public abstract Tuple3<Vector, Double, Boolean> exploreStrategy(MapFacade map, Vector currentPoint, Vector direction, Double cost, Vector goal, MovingRule movingRule);
}