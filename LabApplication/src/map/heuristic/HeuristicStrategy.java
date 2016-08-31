package map.heuristic;

import util.Vector;
import util.MathUtil;

/**
 * Created by paloka on 08.07.16.
 */
public class HeuristicStrategy {

    private Heuristic heuristic;


    /* ------- Heuristic Getter & Setter ------- */

    public Heuristic getHeuristic(){
        return this.heuristic;
    }

    public void setHeuristicZero(){
        this.heuristic = new HeuristicZero();
    }

    public void setHeuristicManhattan() {
        this.heuristic = new HeuristicManhatten();
    }

    public void setHeuristicGrid(){
        this.heuristic = new HeuristicGrid();
    }

    public void setHeuristicEuclidean(){
        this.heuristic = new HeuristicEuclidean();
    }


    /* ------- Heuristic Implementations ------- */

    private class HeuristicZero implements Heuristic{
        @Override
        public double estimateDistance(Vector p, Vector q) {
            return 0;
        }
    }

    private class HeuristicManhatten implements Heuristic{
        @Override
        public double estimateDistance(Vector p, Vector q) {
            return Math.abs(p.getX() - q.getX()) + Math.abs(p.getY() + q.getY());
        }
    }

    private class HeuristicGrid implements Heuristic{
        @Override
        public double estimateDistance(Vector p, Vector q) {
            int deltaX = Math.abs(p.getX() - q.getX());
            int deltaY = Math.abs(p.getY() - q.getY());
            int min = Math.min(deltaX, deltaY);
            int max = Math.max(deltaX, deltaY);
            return max - min + MathUtil.SQRT2 * min;
        }
    }

    private class HeuristicEuclidean implements Heuristic{
        @Override
        public double estimateDistance(Vector p, Vector q) {
            return Math.sqrt((p.getX() - q.getX()) * (p.getX() - q.getX()) + (p.getY() - q.getY()) * (p.getY() - q.getY()));
        }

    }
}
