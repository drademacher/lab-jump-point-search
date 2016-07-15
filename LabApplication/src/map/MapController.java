package map;

import exception.InvalidCoordinateException;
import exception.MapInitialisationException;
import map.heuristic.HeuristicStrategy;
import map.movingRule.MovingRuleStrategy;
import map.shortestpath.ShortestPathResult;
import map.shortestpath.ShortestPathStrategy;
import util.Coordinate;

import java.io.File;

public class MapController {

    private Map map;

    private MapFactory mapFactory                       = new MapFactory();
    private MovingRuleStrategy movingRuleStrategy       = new MovingRuleStrategy();
    private HeuristicStrategy heuristicStrategy         = new HeuristicStrategy();
    private ShortestPathStrategy shortestPathStrategy   = new ShortestPathStrategy();


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


    /* ------- MapMovingRule Operations ------- */

    public void setMovingRuleBasic(){
        this.movingRuleStrategy.setMovingRuleBasic();
    }

    public void setMovingRuleNoCornerCutting(){
        this.movingRuleStrategy.setMovingRuleNoCornerCutting();
    }

    public void setMovingRuleNoDiagonal(){
        this.movingRuleStrategy.setMovingRuleNoDiagonal();
    }


    /* ------- MapHeuristic Operations ------- */

    public void setHeuristicZero() {
    this.heuristicStrategy.setHeuristicZero();
}

    public void setHeuristicManhattan() {
    this.heuristicStrategy.setHeuristicManhattan();
}

    public void setHeuristicGrid() {
    this.heuristicStrategy.setHeuristicGrid();
}

    public void setHeuristicEuclidean() {
    this.heuristicStrategy.setHeuristicEuclidean();
}


    /* ------- ShortestPath Operations ------- */

    public void setShortestPathAStar(){
        this.shortestPathStrategy.setShortestPathAStar();
    }

    public void setShortestPathJPS(){
        this.shortestPathStrategy.setShortestPathJPS();
    }

    public void setShortestPathJPSPlus() { this.shortestPathStrategy.setShortestPathJPSPlus(); }

    public ShortestPathResult findShortestPath(Coordinate start, Coordinate goal) {
        return this.shortestPathStrategy.getShortestPath().findShortestPath(this.map, start, goal, this.heuristicStrategy.getHeuristic(), this.movingRuleStrategy.getMovingRule());
    }
}