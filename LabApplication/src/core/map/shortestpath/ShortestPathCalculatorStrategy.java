package core.map.shortestpath;

import core.exception.NoPathFoundException;
import core.map.MapFacade;
import core.map.heuristic.Heuristic;
import core.map.movingrule.MovingRule;
import core.util.Tuple2;
import core.util.Vector;

/**
 * ShortestPathCalculatorStrategy provides the opportunity to memorise a specific ShortestPathCalculator implementation to use it for finding a shortest path on a grid map whenever needed.
 *
 * @author Patrick Loka
 * @version 1.0
 * @since 1.0
 */
public class ShortestPathCalculatorStrategy {

    private ShortestPathCalculator shortestPathCalculator;

    /* ------- ShortestPathCalculator Operations ------- */

    /**
     * Runs the preprocessing on the memorised ShortestPathCalculator implementation.
     *
     * @param map grid map, which should be preprocessed.
     * @param movingRule allowed moving rules on the map.
     * @return time [ms] needed for preprocessing.
     * @since 1.0
     */
    public long preprocess(MapFacade map, MovingRule movingRule) {
        long starttime = -System.currentTimeMillis();
        this.shortestPathCalculator.doPreprocessing(map, movingRule);
        return starttime + System.currentTimeMillis();
    }

    /**
     * Searches the shortests path on the given map by the memorised ShortestPahCalculator implementation.
     *
     * @param map grid map, on whith the search is executed.
     * @param start start point on the map.
     * @param goal goal point on the map.
     * @param heuristic heuristic to estimate the distance between two points on the map. This has influence on the processing order of exploring the map.
     * @param movingRule allowed movements on the map.
     * @return &lt; Shortest Path between start and goal | time [ms] needed to find shortest path &gt;
     * @throws NoPathFoundException Thrown, if goal is not reachable from start point or if start or goal are unpassable.
     * @since 1.0
     */
    public Tuple2<ShortestPathResult, Long> run(MapFacade map, Vector start, Vector goal, Heuristic heuristic, MovingRule movingRule) throws NoPathFoundException {
        long starttime = -System.currentTimeMillis();
        ShortestPathResult result = this.shortestPathCalculator.findShortestPath(map, start, goal, heuristic, movingRule);
        return new Tuple2<>(result, starttime + System.currentTimeMillis());
    }


    /* ------- ShortestPathCalculator Setter ------- */

    /**
     * Memorises the AStar implementation.<br>
     * <br>
     * Uses AStar strategy only.
     *
     * @since 1.0
     */
    public void setAStarShortestPath() {
        this.shortestPathCalculator = new AStarShortestPathCalculator(new NoShortestPathPruning());
    }

    /**
     * Memorises the JPS implementation.<br>
     * <br>
     * Uses JPS strategy only.
     *
     * @since 1.0
     */
    public void setJPSShortestPath() {
        this.shortestPathCalculator = new JPSShortestPathCalculator(new NoShortestPathPruning());
    }

    /**
     * Memorises the JPS+ implementation.<br>
     * <br>
     * Uses JPS lookup strategy and JPS bounding boxes as pruning strategy.<br>
     * <br>
     * Preprocessing required: Calculates all jump points by JPS strategy and save them in a look up table to use it to find the shortest path without exploring the map in runtime.
     *
     * @since 1.0
     */
    public void setJPSPlusShortestPath() {
        this.shortestPathCalculator = new PreCalculatedShortestPathCalculator(new JPSPreCalculationShortestPathPreprocessing(), new NoShortestPathPruning());
    }

    /**
     * Memorises the JPS+ BoundingBoxes implementation.<br>
     * <br>
     * Uses JPS lookup table strategy and JPS bounding boxes as pruning strategy.<br>
     * <br>
     * Preprocessing required: Calculates all jump points by JPS strategy and save them in a look up table to use it to find the shortest path without exploring the map in runtime.
     * Calculates bounding boxes by JPS strategy to estimate whether a specific point is on the shortest path between start and goal for pruning if not.
     *
     * @since 1.0
     */
    public void setJPSPlusBBShortestPath() {
        this.shortestPathCalculator = new PreCalculatedShortestPathCalculator(new JPSPreCalculationShortestPathPreprocessing(), new JPSBoundingBoxesShortestPathPruning());
    }

    /**
     * Memorises the AStar BoundingBoxes implementation.<br>
     * <br>
     * Uses AStar strategy and AStar bounding boxes as pruning strategy.<br>
     * <br>
     * Preprocessing required: Calculates bounding boxes by AStar strategy to estimate whether a specific point is on the shortest path between start and goal for pruning if not.
     *
     * @since 1.0
     */
    public void setAStarBBShortestPath() {
        this.shortestPathCalculator = new AStarShortestPathCalculator(new AStarBoundingBoxesShortestPathPruning());
    }

    /**
     * Memorises the JPS BoundingBoxes implementation.<br>
     * <br>
     * Uses JPS strategy and JPS bounding boxes as pruning strategy.<br>
     * <br>
     * Preprocessing required: Calculates bounding boxes by JPS strategy to estimate whether a specific point is on the shortest path between start and goal for pruning if not.
     *
     * @since 1.0
     */
    public void setJPSBBShortestPath() {
        this.shortestPathCalculator = new JPSShortestPathCalculator(new JPSBoundingBoxesShortestPathPruning());
    }
}