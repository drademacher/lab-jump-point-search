package map;

import exception.InvalidCoordinateException;
import exception.MapInitialisationException;
import exception.NoPathFoundExeception;
import map.heuristic.HeuristicStrategy;
import map.movingRule.MovingRuleStrategy;
import map.shortestpath.ShortestPathResult;
import map.shortestpath.ShortestPathStrategy;
import util.Vector;

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

    public void setShortestPathAStar(){
        this.shortestPathStrategy.setShortestPathAStar();
    }

    public void setShortestPathJPS(){
        this.shortestPathStrategy.setShortestPathJPS();
    }

    public void setShortestPathJPSPlus() { this.shortestPathStrategy.setShortestPathJPSPlus(); }

    public void setShortestPathJPSBB(){
        this.shortestPathStrategy.setShortestPathJPSBB();
    }

    public void setShortestpathAStarBB(){
        this.shortestPathStrategy.setShortestPathAStarBB();
    }

    public void preprocessShortestPath(){
        this.shortestPathStrategy.preprocess(this.map, this.movingRuleStrategy.getMovingRule());
    }

    public ShortestPathResult runShortstPath(Vector start, Vector goal) throws NoPathFoundExeception {
        return this.shortestPathStrategy.run(this.map, start, goal, this.heuristicStrategy.getHeuristic(), this.movingRuleStrategy.getMovingRule());
    }
}