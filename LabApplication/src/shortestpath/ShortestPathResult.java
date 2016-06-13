package shortestpath;

import util.Coordinate;
import util.Tuple2;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by paloka on 08.06.16.
 */
public class ShortestPathResult {

    private Coordinate start, goal;
    private Collection<Coordinate> openList;
    private Map<Coordinate, Coordinate> pathPredecessors;

    public ShortestPathResult(Coordinate start, Coordinate goal, Collection<Coordinate> openList, Map<Coordinate, Coordinate> pathPredecessors) {
        this.start  = start;
        this.goal   = goal;
        this.openList = openList;
        this.pathPredecessors = pathPredecessors;
    }

    public Coordinate getStart(){
        return this.start;
    }

    public Coordinate getGoal(){
        return this.goal;
    }

    public List<Coordinate> getShortestPath() {
        List<Coordinate> path = new LinkedList<>();
        Coordinate current  = goal;
        while (!current.equals(start)) {
            path.add(current);
            current = pathPredecessors.get(current);
        }
        return path;
    }

    public Collection<Coordinate> getVisited() {
        return pathPredecessors.keySet();
    }

    public Collection<Coordinate> getOpenList() {
        return this.openList;
    }
}
