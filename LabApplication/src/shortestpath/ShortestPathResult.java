package shortestpath;

import util.Tuple2;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by paloka on 08.06.16.
 */
public class ShortestPathResult {

    private int xStart,yStart,xGoal,yGoal;
    private Collection<Tuple2<Integer,Integer>> openList;
    private Map<Tuple2<Integer,Integer>,Tuple2<Integer,Integer>> pathPredecessors;

    public ShortestPathResult(int xStart, int yStart, int xGoal, int yGoal, Collection<Tuple2<Integer,Integer>> openList, Map<Tuple2<Integer,Integer>,Tuple2<Integer,Integer>> pathPredecessors) {
        this.xStart = xStart;
        this.yStart = yStart;
        this.xGoal = xGoal;
        this.yGoal = yGoal;
        this.openList = openList;
        this.pathPredecessors = pathPredecessors;
    }

    public int getxGoal() {
        return xGoal;
    }

    public int getxStart() {
        return xStart;
    }

    public int getyGoal() {
        return yGoal;
    }

    public int getyStart() {
        return yStart;
    }

    public List<Tuple2<Integer,Integer>> getShortestPath(){
        List<Tuple2<Integer,Integer>> path  = new LinkedList<>();
        Tuple2<Integer,Integer> current = new Tuple2<>(xGoal,yGoal);
        Tuple2<Integer,Integer> start   = new Tuple2<>(xStart,yStart);
        while(!current.equals(start)){
            path.add(current);
            current = pathPredecessors.get(current);
        }
        return path;
    }

    public Collection<Tuple2<Integer,Integer>> getVisited(){
        return pathPredecessors.keySet();
    }

    public Collection<Tuple2<Integer,Integer>> getOpenList(){
        return this.openList;
    }
}
