package map.shortestpath;

import util.Tuple2;
import util.Vector;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by paloka on 08.06.16.
 */
public class ShortestPathResult {

    private Vector start, goal;
    private Collection<Vector> openList;
    private Map<Vector, Vector> pathPredecessors;

    public ShortestPathResult(Vector start, Vector goal, Collection<Vector> openList, Map<Vector, Vector> pathPredecessors) {
        this.start  = start;
        this.goal   = goal;
        this.openList = openList;
        this.pathPredecessors = pathPredecessors;
    }

    public Vector getStart(){
        return this.start;
    }

    public Vector getGoal(){
        return this.goal;
    }

    public List<Tuple2<Vector, Boolean>> getShortestPath() {
        List<Tuple2<Vector, Boolean>> path = new LinkedList<>();
        Vector current  = goal;
        do {
            Vector next = pathPredecessors.get(current);
            Vector direction = current.getDirectionTo(next);
            Boolean flag = true;
            while(!current.equals(next)) {
                path.add(new Tuple2<>(current, flag));
                current = current.add(direction);
                flag = false;
            }
        } while (!current.equals(start));
        return path;
    }

    public Collection<Vector> getVisited() {
        return pathPredecessors.keySet().stream().filter(v -> !openList.contains(v)).collect(Collectors.toCollection(ArrayList::new));
    }

    public Collection<Vector> getOpenList() {
        return this.openList;
    }
}
