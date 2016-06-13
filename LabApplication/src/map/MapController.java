package map;

import exception.InvalidCoordinateException;
import exception.MapInitialisationException;
import shortestpath.Heuristic;
import shortestpath.ShortestPathAlgorithm;
import shortestpath.ShortestPathAlgorithmFactory;
import shortestpath.ShortestPathResult;

import java.io.File;

public class MapController {

    private Map map;
    private MapFactory mapFactory = new MapFactory();
    private ShortestPathAlgorithmFactory shortestPathAlgorithmFactory = new ShortestPathAlgorithmFactory();
    private Heuristic heuristic;


    /* ------- Factory ------- */

    public MapFacade setEmptyMap(int xDim, int yDim) throws MapInitialisationException {
        this.map = mapFactory.createEmptyMap(xDim, yDim);
        return this.map;
    }

    public MapFacade setRandomMap(int xDim, int yDim, double pPassable) throws MapInitialisationException {
        this.map = mapFactory.createRandomMap(xDim, yDim, pPassable);
        return this.map;
    }

    public MapFacade setMazeMap(int xDim, int yDim) throws MapInitialisationException {
        this.map = mapFactory.createMazeMap(xDim, yDim);
        return this.map;
    }

    public MapFacade setMazeRoomMap(int xDim, int yDim) throws MapInitialisationException {
        this.map = mapFactory.createMazeRoomMap(xDim, yDim);
        return this.map;
    }

    public MapFacade setSingleRoomMap(int xDim, int yDim) throws MapInitialisationException {
        this.map = mapFactory.createSingleRoomMap(xDim, yDim);
        return this.map;
    }

    public MapFacade setDoubleRoomMap(int xDim, int yDim) throws MapInitialisationException {
        this.map = mapFactory.createDoubleRoomMap(xDim, yDim);
        return this.map;
    }

    public MapFacade setLoopRoomMap(int xDim, int yDim) throws MapInitialisationException {
        this.map = mapFactory.createLoopRoomMap(xDim, yDim);
        return this.map;
    }

    public MapFacade loadMap(File file) throws MapInitialisationException {
        this.map = mapFactory.loadMap(file);
        return this.map;
    }


    /* ------- Map Operations ------- */

    public void switchPassable(int x, int y) throws InvalidCoordinateException {
        this.map.switchPassable(x, y);
    }

    public ShortestPathResult findShortestPathWithAStar(int xStart, int yStart, int xGoal, int yGoal) {
        ShortestPathAlgorithm aStar = shortestPathAlgorithmFactory.createAStar();
        return aStar.findShortestPath(this.map, xStart, yStart, xGoal, yGoal, this.heuristic);
    }


    /* ------- MapHeuristic Setter ------- */

    public void setZeroHeuristic() {
        this.heuristic = (x1, y1, x2, y2) -> 0;
    }

    public void setEuclideanHeuristic() {
        this.heuristic = (x1, y1, x2, y2) -> Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public void setGridHeuristic() {
        this.heuristic = (x1, y1, x2, y2) -> {
            int deltaX = Math.abs(x1 - x2);
            int deltaY = Math.abs(y1 - y2);
            int min = Math.min(deltaX, deltaY);
            int max = Math.max(deltaX, deltaY);
            return max - min + Math.sqrt(2) * min;
        };
    }
}