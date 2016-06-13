package map;

import exception.InvalidCoordinateException;
import exception.MapInitialisationException;
import shortestpath.Heuristic;
import shortestpath.ShortestPathAlgorithm;
import shortestpath.ShortestPathAlgorithmFactory;
import shortestpath.ShortestPathResult;
import util.Coordinate;

import java.io.File;

public class MapController {

    private Map map;
    private MapFactory mapFactory = new MapFactory();
    private ShortestPathAlgorithmFactory shortestPathAlgorithmFactory = new ShortestPathAlgorithmFactory();
    private Heuristic heuristic;


    /* ------- Factory ------- */

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

    public ShortestPathResult findShortestPathWithAStar(Coordinate start, Coordinate goal) {
        ShortestPathAlgorithm aStar = shortestPathAlgorithmFactory.createAStar();
        return aStar.findShortestPath(this.map, start, goal, this.heuristic);
    }


    /* ------- MapHeuristic Setter ------- */

    public void setZeroHeuristic() {
        this.heuristic = (p,q) -> 0;
    }

    public void setEuclideanHeuristic() {
        this.heuristic = (p,q) -> Math.sqrt((p.getX() - q.getX()) * (p.getX() - q.getX()) + (p.getY() - q.getY()) * (p.getY() - q.getY()));
    }

    public void setGridHeuristic() {
        this.heuristic = (p,q) -> {
            int deltaX = Math.abs(p.getX() - q.getX());
            int deltaY = Math.abs(p.getY() - q.getY());
            int min = Math.min(deltaX, deltaY);
            int max = Math.max(deltaX, deltaY);
            return max - min + Math.sqrt(2) * min;
        };
    }
}