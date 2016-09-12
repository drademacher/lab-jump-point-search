package core.map;

import core.exception.InvalidCoordinateException;
import core.exception.MapInitialisationException;
import core.exception.NoPathFoundException;
import core.map.heuristic.HeuristicStrategy;
import core.map.movingrule.MovingRuleStrategy;
import core.map.shortestpath.ShortestPathResult;
import core.map.shortestpath.ShortestPathCalculatorStrategy;
import core.util.Tuple2;
import core.util.Vector;

import java.io.File;

/**
 * MapController controls all operations on a single holden Map instance.<br>
 * <br>
 * MapController is able to generate new Map instances of certain types and is able to build a Map instance by a file using the MapFactory. The new Map instance becomes the controlled one.<br>
 * <br>
 * MapController is able to modify the holden Map instance.<br>
 * <br>
 * MapController is able to choose different map moving rules by defining allowed and forbidden movements on a grid by MovingRuleStrategy.<br>
 * <br>
 * MapController is able to choose different heuristic functions to estimate the distance of two points on a grid by HeuristicStrategy.<br>
 * <br>
 * MapController is able to find the shortest path on the holden Map instance considering a chosen moving rule and heuristic with a preselected algorithm. For some selected algorithms a preprocessing is required.
 *
 * @author Patrick Loka, Danny Rademacher
 * @version 1.0
 * @see Map
 * @see MapFactory
 * @see ShortestPathCalculatorStrategy
 * @see MovingRuleStrategy
 * @see HeuristicStrategy
 * @since 1.0
 */
public class MapController {

    private Map map;

    private MapFactory mapFactory = new MapFactory();
    private MovingRuleStrategy movingRuleStrategy = new MovingRuleStrategy();
    private HeuristicStrategy heuristicStrategy = new HeuristicStrategy();
    private ShortestPathCalculatorStrategy shortestPathCalculatorStrategy = new ShortestPathCalculatorStrategy();


    //Todo: Wenn Danny die MapFactory mit JavaDocs versehen hat, soll er hier auch noch mal drübergucken! Ich weiß den Unterschied zwischen den einzeldenen Map Typen nicht wirklich. Bitte Beschreibungen zu allen Generierungsmethoden anpassen.
    /* ------- MapFactory Operations ------- */

    /**
     * Generates an empty Map instance and takes control over it.
     *
     * @param dimension dimension of the new Map instance
     * @return generated Map instance as MapFacade
     * @throws MapInitialisationException Thrown, if dimension input contains a non positive value.
     * @see MapFactory
     * @see Map
     * @see MapFacade
     * @since 1.0
     */
    public MapFacade setEmptyMap(Vector dimension) throws MapInitialisationException {
        this.map = mapFactory.createEmptyMap(dimension);
        return this.map;
    }

    /**
     * Generates a Map instance with uniform distributed obstacles and takes control over it.
     *
     * @param dimension dimension of the new Map instance
     * @param pPassable amount of passable fields on the new Map instance. Valid values betrween 0.0 and 1.0
     * @return generated Map instance as MapFacade
     * @throws MapInitialisationException Thrown, if dimension input contains a non positive value.
     * @see MapFactory
     * @see Map
     * @see MapFacade
     * @since 1.0
     */
    public MapFacade setRandomMap(Vector dimension, double pPassable) throws MapInitialisationException {
        this.map = mapFactory.createRandomMap(dimension, pPassable);
        return this.map;
    }

    /**
     * Generates a maze Map instance and takes control over it.
     *
     * @param dimension dimension of the new Map instance
     * @return generated Map instance as MapFacade
     * @throws MapInitialisationException Thrown, if dimension input contains a non positive value.
     * @see MapFactory
     * @see Map
     * @see MapFacade
     * @since 1.0
     */
    public MapFacade setMazeMap(Vector dimension) throws MapInitialisationException {
        this.map = mapFactory.createMazeMap(dimension);
        return this.map;
    }

    /**
     * Generates a maze Map instance containing rooms and takes control over it.
     *
     * @param dimension dimension of the new Map instance
     * @return generated Map instance as MapFacade
     * @throws MapInitialisationException Thrown, if dimension input contains a non positive value.
     * @see MapFactory
     * @see Map
     * @see MapFacade
     * @since 1.0
     */
    public MapFacade setMazeRoomMap(Vector dimension, int roomNumber) throws MapInitialisationException {
        this.map = mapFactory.createMazeRoomMap(dimension, roomNumber);
        return this.map;
    }

    /**
     * Generates a tree Map instance based on rooms and takes control over it.
     *
     * @param dimension dimension of the new Map instance
     * @return generated Map instance as MapFacade
     * @throws MapInitialisationException Thrown, if dimension input contains a non positive value.
     * @see MapFactory
     * @see Map
     * @see MapFacade
     * @since 1.0
     */
    public MapFacade setSingleRoomMap(Vector dimension) throws MapInitialisationException {
        this.map = mapFactory.createSingleRoomMap(dimension);
        return this.map;
    }

    /**
     * Generates Map instance with rooms containing circles and takes control over it.
     *
     * @param dimension dimension of the new Map instance
     * @return generated Map instance as MapFacade
     * @throws MapInitialisationException Thrown, if dimension input contains a non positive value.
     * @see MapFactory
     * @see Map
     * @see MapFacade
     * @since 1.0
     */
    public MapFacade setDoubleRoomMap(Vector dimension) throws MapInitialisationException {
        this.map = mapFactory.createDoubleRoomMap(dimension);
        return this.map;
    }

    /**
     * Generates Map instance with rooms containing circles and takes control over it.
     *
     * @param dimension dimension of the new Map instance
     * @return generated Map instance as MapFacade
     * @throws MapInitialisationException Thrown, if dimension input contains a non positive value.
     * @see MapFactory
     * @see Map
     * @see MapFacade
     * @since 1.0
     */
    public MapFacade setLoopRoomMap(Vector dimension) throws MapInitialisationException {
        this.map = mapFactory.createLoopRoomMap(dimension);
        return this.map;
    }

    /**
     * Loads a Map instance from a file and takes control over it.
     *
     * @param file A valid file is of the type .map
     * @return loaded Map instance as MapFacade
     * @throws MapInitialisationException Thrown, if file has not the defined .map format.
     * @see MapFactory
     * @see Map
     * @see MapFacade
     * @since 1.0
     */
    public MapFacade loadMap(File file) throws MapInitialisationException {
        this.map = mapFactory.loadMap(file);
        return this.map;
    }


    /* ------- Map Operations ------- */

    /**
     * Switches the passable state on the controlled Map instance of a given point.
     * After running this method, a given passable field on the controlled Map instance becomes unpassable and an given unpassable field becomes passable.
     *
     * @param coordinate Point where passable state should be switched.
     * @throws InvalidCoordinateException Thrown if coordinate is no Point on the controlled Map instance.
     * @see Map
     * @since 1.0
     */
    public void switchPassable(Vector coordinate) throws InvalidCoordinateException {
        this.map.switchPassable(coordinate);
    }

    /**
     * Saves the current state of the controlled Map instance as .map file.
     *
     * @param file filename of the file the map should be saved. To generate a valid file it should be type .map.
     * @see Map
     * @since 1.0
     */
    public void saveMap(File file) {
        this.map.save(file);
    }


    /* ------- MapMovingRule Operations ------- */

    /**
     * Sets new used movements rules: AllNeighbor.<br>
     * <br>
     * All orthogonal and diagonal movements are allowed as long as the reached point is passable.
     *
     * @see MovingRuleStrategy
     * @since 1.0
     */
    public void setAllNeighborMovingRule() {
        this.movingRuleStrategy.setAllNeighborMovingRule();
    }

    /**
     * Sets new used movements rules: UncutNeighbor.<br>
     * <br>
     * All orthogonal movements are allowed as long as the reached point is passable.
     * All diagonal movements are allowed as long as the reached point is passable and no obstacle is cut.
     *
     * @see MovingRuleStrategy
     * @since 1.0
     */
    public void setUncutNeighborMovingRule() {
        this.movingRuleStrategy.setUncutNeighborMovingRule();
    }

    /**
     * Sets new used movements rules: OrthogonalNeighbor.<br>
     * <br>
     * All orthogonal movements are allowed as long as the reached point is passable.
     * All diagonal movements are forbidden.
     *
     * @see MovingRuleStrategy
     * @since 1.0
     */
    public void setOrthogonalNeighborMovingRule() {
        this.movingRuleStrategy.setOrthogonalNeighborMovingRule();
    }


    /* ------- MapHeuristic Operations ------- */

    /**
     * Sets new used heuristic function to estimate the distance of two points on a grid: ZeroHeuristic.<br>
     * <br>
     * Estimated the distance between two points on a grid is constant 0.
     *
     * @see HeuristicStrategy
     * @since 1.0
     */
    public void setZeroHeuristic() {
        this.heuristicStrategy.setZeroHeuristic();
    }

    /**
     * Sets new used heuristic function to estimate the distance of two points on a grid: ManhattanHeuristic.<br>
     * <br>
     * Estimates the distance between two points on a grid by calculating the manhattan distance.
     *
     * @see HeuristicStrategy
     * @since 1.0
     */
    public void setManhattanHeuristic() {
        this.heuristicStrategy.setManhattanHeuristic();
    }

    /**
     * Sets new used heuristic function to estimate the distance of two points on a grid: GridHeuristic.<br>
     * <br>
     * Estimates the distance between two points on a grid by calculating the distance on a grid without obstacles (diagonal movements allowed)
     *
     * @see HeuristicStrategy
     * @since 1.0
     */
    public void setGridHeuristic() {
        this.heuristicStrategy.setGridHeuristic();
    }

    /**
     * Sets new used heuristic function to estimate the distance of two points on a grid: .<br>
     * <br>
     * Estimates the distance between two points on a grid by calculating the euclidean distance.
     *
     * @see HeuristicStrategy
     * @since 1.0
     */
    public void setEuclideanHeuristic() {
        this.heuristicStrategy.setEuclideanHeuristic();
    }


    /* ------- ShortestPathCalculator Operations ------- */

    /**
     * Sets new used function to find the shortest path between two points on a grid with passable and non passable points: AStar.<br>
     * <br>
     * Uses AStar strategy only.
     *
     * @see ShortestPathCalculatorStrategy
     * @since 1.0
     */
    public void setAStarShortestPath() {
        this.shortestPathCalculatorStrategy.setAStarShortestPath();
    }

    /**
     * Sets new used function to find the shortest path between two points on a grid with passable and non passable points: Jump Point Search.<br>
     * <br>
     * Uses JPS strategy only.
     *
     * @see ShortestPathCalculatorStrategy
     * @since 1.0
     */
    public void setJPSShortestPath() {
        this.shortestPathCalculatorStrategy.setJPSShortestPath();
    }

    /**
     * Sets new used function to find the shortest path between two points on a grid with passable and non passable points: JPS+.<br>
     * <br>
     * Uses JPS lookup strategy and JPS bounding boxes as pruning strategy.<br>
     * <br>
     * Preprocessing required: Calculates all jump points by JPS strategy and save them in a look up table to use it to find the shortest path without exploring the map in runtime.
     *
     * @see ShortestPathCalculatorStrategy
     * @since 1.0
     */
    public void setJPSPlusShortestPath() {
        this.shortestPathCalculatorStrategy.setJPSPlusShortestPath();
    }

    /**
     * Sets new used function to find the shortest path between two points on a grid with passable and non passable points: JPS+BB.<br>
     * <br>
     * Uses JPS lookup table strategy and JPS bounding boxes as pruning strategy.<br>
     * <br>
     * Preprocessing required: Calculates all jump points by JPS strategy and save them in a look up table to use it to find the shortest path without exploring the map in runtime.
     * Calculates bounding boxes by JPS strategy to estimate whether a specific point is on the shortest path between start and goal for pruning if not.
     *
     * @see ShortestPathCalculatorStrategy
     * @since 1.0
     */
    public void setJPSPlusBBShortestPath() {
        this.shortestPathCalculatorStrategy.setJPSPlusBBShortestPath();
    }

    /**
     * Sets new used function to find the shortest path between two points on a grid with passable and non passable points: AStarBB.<br>
     * <br>
     * Uses AStar strategy and AStar bounding boxes as pruning strategy.<br>
     * <br>
     * Preprocessing required: Calculates bounding boxes by AStar strategy to estimate whether a specific point is on the shortest path between start and goal for pruning if not.
     *
     * @see ShortestPathCalculatorStrategy
     * @since 1.0
     */
    public void setAStarBBShortestPath() {
        this.shortestPathCalculatorStrategy.setAStarBBShortestPath();
    }

    /**
     * Sets new used function to find the shortest path between two points on a grid with passable and non passable points: JPSBB.<br>
     * <br>
     * Uses JPS strategy and JPS bounding boxes as pruning strategy.<br>
     * <br>
     * Preprocessing required: Calculates bounding boxes by JPS strategy to estimate whether a specific point is on the shortest path between start and goal for pruning if not.
     *
     * @see ShortestPathCalculatorStrategy
     * @since 1.0
     */
    public void setJPSBBShortestPath() {
        this.shortestPathCalculatorStrategy.setJPSBBShortestPath();
    }

    /**
     * Does needed preprocessing considering the selected shortest path strategy and the selected moving rule.
     *
     * @return time [ms] needed for preprocessing.
     * @see ShortestPathCalculatorStrategy
     * @since 1.0
     */
    public long preprocessShortestPath() {
        return this.shortestPathCalculatorStrategy.preprocess(this.map, this.movingRuleStrategy.getMovingRule());
    }

    /**
     * Searches the shortest path from start to goal considering the selected shortest path strategy, the proprocessing data, the selected moving rule and the selected heuristic.
     *
     * @param start point on map to start
     * @param goal  point on map to reach
     * @return &lt; Shortest Path between start and goal | time [ms] needed to find shortest path &gt;
     * @throws NoPathFoundException Thrown, if goal is not reachable from start point or if start or goal are unpassable.
     * @see ShortestPathCalculatorStrategy
     * @see ShortestPathResult
     * @since 1.0
     */
    public Tuple2<ShortestPathResult, Long> runShortstPath(Vector start, Vector goal) throws NoPathFoundException {
        return this.shortestPathCalculatorStrategy.run(this.map, start, goal, this.heuristicStrategy.getHeuristic(), this.movingRuleStrategy.getMovingRule());
    }
}