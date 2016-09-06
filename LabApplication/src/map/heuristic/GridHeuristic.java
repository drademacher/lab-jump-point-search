package map.heuristic;

import util.MathUtil;
import util.Vector;

/**
 * GridHeuristic implements all Heuristic operations.<br>
 * <br>
 * GridHeuristic implements estimateDistance(p,q) of Heuristic by calculating the shortest path on a grid without obstacles.
 *
 * @author Patrick Loka
 * @version 1.0
 * @see HeuristicStrategy
 * @since 1.0
 */
class GridHeuristic implements Heuristic{

    /**
     * Implementation: Estimates the distance between p and q by calculating the shortest path on a grid without obstacles.<br>
     * <br>
     * Specification: {@inheritDoc}
     *
     * @param p,q Points in a 2-dimensional vector space
     * @return Distance of p and q on a grid without obstacles.
     * @since 1.0
     */
    @Override
    public double estimateDistance(Vector p, Vector q) {
        int deltaX = Math.abs(p.getX() - q.getX());
        int deltaY = Math.abs(p.getY() - q.getY());
        int min = Math.min(deltaX, deltaY);
        int max = Math.max(deltaX, deltaY);
        return max - min + MathUtil.SQRT2 * min;
    }
}
