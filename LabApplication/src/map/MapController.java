package map;

import exception.InvalidCoordinateException;
import exception.MapInitialisationException;
import shortestpath.ShortestPathAlgorithm;
import shortestpath.ShortestPathAlgorithmFactory;
import shortestpath.ShortestPathResult;
import shortestpath.heuristic.HeuristicStrategy;
import shortestpath.movingRule.MovingRuleStrategy;
import util.Coordinate;

import java.io.File;

public class MapController {

    private Map map;

    private MapFactory mapFactory                   = new MapFactory();
    private HeuristicStrategy heuristicStrategy     = new HeuristicStrategy();
    private MovingRuleStrategy movingRuleStrategy   = new MovingRuleStrategy();
    private ShortestPathAlgorithmFactory shortestPathAlgorithmFactory = new ShortestPathAlgorithmFactory();


    /* ------- MapFactory Operations ------- */

    public MapFacade setEmptyMap(Coordinate dimension) throws MapInitialisationException {
        this.map = mapFactory.createEmptyMap(dimension);
        return this.map;
    }

    public MapFacade setRandomMap(Coordinate dimension, double pPassable) throws MapInitialisationException {
        this.map = mapFactory.createRandomMap(dimension, pPassable);
        return this.map;
    }

    public MapFacade setMazeMap(Coordinate dimension) throws MapInitialisationException {
        this.map = mapFactory.createMazeMap(dimension);
        return this.map;
    }

    public MapFacade setMazeRoomMap(Coordinate dimension) throws MapInitialisationException {
        this.map = mapFactory.createMazeRoomMap(dimension);
        return this.map;
    }

    public MapFacade setSingleRoomMap(Coordinate dimension) throws MapInitialisationException {
        this.map = mapFactory.createSingleRoomMap(dimension);
        return this.map;
    }

    public MapFacade setDoubleRoomMap(Coordinate dimension) throws MapInitialisationException {
        this.map = mapFactory.createDoubleRoomMap(dimension);
        return this.map;
    }

    public MapFacade setLoopRoomMap(Coordinate dimension) throws MapInitialisationException {
        this.map = mapFactory.createLoopRoomMap(dimension);
        return this.map;
    }

    public MapFacade loadMap(File file) throws MapInitialisationException {
        this.map = mapFactory.loadMap(file);
        return this.map;
    }


    /* ------- Map Operations ------- */

    public void switchPassable(Coordinate coordinate) throws InvalidCoordinateException {
        this.map.switchPassable(coordinate);
    }

    public void saveMap(File file) {
        this.map.save(file);
    }


    /* ------- MapHeuristic Operations ------- */

    public void setHeuristicZero() {
        this.heuristicStrategy.setHeuristicZero();
    }

    public void setHeuristicGrid() {
        this.heuristicStrategy.setHeuristicGrid();
    }

    public void setHeuristicEuclidean() {
        this.heuristicStrategy.setHeuristicEucidean();
    }


    /* ------- MapMovingRule Operations ------- */

    public void setMovingRuleOrthogonalOnly(){
        this.movingRuleStrategy.setMovingRuleOrthogonalOnly();
    }

    public void setMovingRuleCornerCutting(){
        this.movingRuleStrategy.setMovingRuleCornerCutting();
    }

    public void setMovingRuleNoCornerCutting(){
        this.movingRuleStrategy.setMovingRuleNoCornerCutting();
    }


    /* ------- ShortestPath Operations ------- */

    public ShortestPathResult findShortestPathWithAStar(Coordinate start, Coordinate goal) {
        ShortestPathAlgorithm aStar = shortestPathAlgorithmFactory.createAStar();
        return aStar.findShortestPath(this.map, start, goal, this.heuristicStrategy.getHeuristic(), this.movingRuleStrategy.getMovingRule());
    }

    public ShortestPathResult findShortestPathWithJPS(Coordinate start, Coordinate goal) {
        ShortestPathAlgorithm jps = shortestPathAlgorithmFactory.createJPS();
        return jps.findShortestPath(this.map, start, goal, this.heuristicStrategy.getHeuristic(), this.movingRuleStrategy.getMovingRule());
    }
}