package map.shortestpath;

import util.Vector;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    public List<Vector> getShortestPath() {
        List<Vector> path = new LinkedList<>();
        Vector current  = goal;
        do {
            Vector next = pathPredecessors.get(current);
            Vector direction = current.getDirectionTo(next);
            while(!current.equals(next)) {
                path.add(current);
                current = current.add(direction);
            }
        } while (!current.equals(start));
        return path;
    }

    public Collection<Vector> getVisited() {
        return pathPredecessors.keySet();
    }

    public Collection<Vector> getOpenList() {
        return this.openList;
    }
}
