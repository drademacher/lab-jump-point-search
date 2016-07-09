package shortestpath.heuristic;

import util.MathUtil;

/**
 * Created by paloka on 08.07.16.
 */
public class HeuristicStrategy {

    private Heuristic heuristic;

    public Heuristic getHeuristic(){
        return this.heuristic;
    }

    public void setHeuristicZero(){
        this.heuristic = (p,q) -> 0;
    }

    public void setHeuristicGrid(){
        this.heuristic = (p,q) -> {
            int deltaX = Math.abs(p.getX() - q.getX());
            int deltaY = Math.abs(p.getY() - q.getY());
            int min = Math.min(deltaX, deltaY);
            int max = Math.max(deltaX, deltaY);
            return max - min + MathUtil.SQRT2 * min;
        };
    }

    public void setHeuristicEucidean(){
        this.heuristic = (p,q) -> Math.sqrt((p.getX() - q.getX()) * (p.getX() - q.getX()) + (p.getY() - q.getY()) * (p.getY() - q.getY()));
    }
}
