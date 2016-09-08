package core.map;

import core.exception.InvalidCoordinateException;
import core.exception.MapInitialisationException;
import core.exception.NoPathFoundException;
import core.map.heuristic.HeuristicStrategy;
import core.map.movingrule.MovingRuleStrategy;
import core.map.shortestpath.ShortestPathResult;
import core.map.shortestpath.ShortestPathStrategy;
import core.util.Tuple2;
import core.util.Vector;

import java.io.File;

public class MapController {

    private Map map;

    private MapFactory mapFactory                       = new MapFactory();
    private MovingRuleStrategy movingRuleStrategy       = new MovingRuleStrategy();
    private HeuristicStrategy heuristicStrategy         = new HeuristicStrategy();
    private ShortestPathStrategy shortestPathStrategy   = new ShortestPathStrategy();


    /* ------- MapFactory Operations ------- */

    public MapFacade setEmptyMap(Vector dimension) throws MapInitialisationException {
        this.map = mapFactory.createEmptyMap(dimension);
        return this.map;
    }

    public MapFacade setRandomMap(Vector dimension, double pPassable) throws MapInitialisationException {
        this.map = mapFactory.createRandomMap(dimension, pPassable);
        return this.map;
    }

    public MapFacade setMazeMap(Vector dimension) throws MapInitialisationException {
        this.map = mapFactory.createMazeMap(dimension);
        return this.map;
    }

    public MapFacade setMazeRoomMap(Vector dimension) throws MapInitialisationException {
        this.map = mapFactory.createMazeRoomMap(dimension);
        return this.map;
    }

    public MapFacade setSingleRoomMap(Vector dimension) throws MapInitialisationException {
        this.map = mapFactory.createSingleRoomMap(dimension);
        return this.map;
    }

    public MapFacade setDoubleRoomMap(Vector dimension) throws MapInitialisationException {
        this.map = mapFactory.createDoubleRoomMap(dimension);
        return this.map;
    }

    public MapFacade setLoopRoomMap(Vector dimension) throws MapInitialisationException {
        this.map = mapFactory.createLoopRoomMap(dimension);
        return this.map;
    }

    public MapFacade loadMap(File file) throws MapInitialisationException {
        this.map = mapFactory.loadMap(file);
        return this.map;
    }


    /* ------- Map Operations ------- */

    public void switchPassable(Vector coordinate) throws InvalidCoordinateException {
        this.map.switchPassable(coordinate);
    }

    public void saveMap(File file) {
        this.map.save(file);
    }


    /* ------- MapMovingRule Operations ------- */

    public void setAllNeighborMovingRule(){
        this.movingRuleStrategy.setAllNeighborMovingRule();
    }

    public void setUncutNeighborMovingRule(){
        this.movingRuleStrategy.setUncutNeighborMovingRule();
    }

    public void setOrthogonalNeighborMovingRule(){
        this.movingRuleStrategy.setOrthogonalNeighborMovingRule();
    }


    /* ------- MapHeuristic Operations ------- */

    public void setZeroHeuristic() {
    this.heuristicStrategy.setZeroHeuristic();
}

    public void setManhattanHeuristic() {
    this.heuristicStrategy.setManhattanHeuristic();
}

    public void setGridHeuristic() {
    this.heuristicStrategy.setGridHeuristic();
}

    public void setEuclideanHeuristic() {
    this.heuristicStrategy.setEuclideanHeuristic();
}


    /* ------- ShortestPath Operations ------- */

    public void setAStarShortestPath(){
        this.shortestPathStrategy.setAStarShortestPath();
    }

    public void setJPSShortestPath(){
        this.shortestPathStrategy.setJPSShortestPath();
    }

    public void setJPSPlusShortestPath() { this.shortestPathStrategy.setJPSPlusShortestPath(); }

    public void setJPSPlusBBShortestPath(){
        this.shortestPathStrategy.setJPSPlusBBShortestPath();
    }

    public void setAStarBBShortestPath(){
        this.shortestPathStrategy.setAStarBBShortestPath();
    }

    public void setJPSBBShortestPath(){
        this.shortestPathStrategy.setJPSBBShortestPath();
    }

    public long preprocessShortestPath(){
        return this.shortestPathStrategy.preprocess(this.map, this.movingRuleStrategy.getMovingRule());
    }

    public Tuple2<ShortestPathResult,Long> runShortstPath(Vector start, Vector goal) throws NoPathFoundException {
        return this.shortestPathStrategy.run(this.map, start, goal, this.heuristicStrategy.getHeuristic(), this.movingRuleStrategy.getMovingRule());
    }
}