package core.map.shortestpath;

import core.util.Tuple2;
import core.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by paloka on 08.06.16.
 */
public class ShortestPathResult {

    private Vector start, goal;
    // TODO: die openlist scheint eigentlich die Liste der processed points zu speichern - WTF? (union von closedList und openList)
    private Collection<Vector> openList;
    private Map<Vector, Vector> pathPredecessors;
    private double cost;

    public ShortestPathResult(Vector start, Vector goal, Collection<Vector> openList, Map<Vector, Vector> pathPredecessors, double cost) {
        this.start  = start;
        this.goal   = goal;
        this.openList = openList;
        this.pathPredecessors = pathPredecessors;
        this.cost   = cost;
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
        path.add(new Tuple2<>(current, true));
        return path;
    }

    public Collection<Vector> getClosedList() {
        return this.openList.stream().filter(p -> !pathPredecessors.keySet().contains(p)).collect(Collectors.toCollection(ArrayList::new));
    }

    public Collection<Vector> getOpenList() {
        return pathPredecessors.keySet();
    }

    public double getCost(){
        return this.cost;
    }
}
