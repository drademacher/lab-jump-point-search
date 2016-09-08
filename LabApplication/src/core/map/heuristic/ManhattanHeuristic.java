package core.map.heuristic;

import core.util.Vector;

/**
 * ManhattanHeuristic implements all Heuristic operations.<br>
 * <br>
 * ManhattanHeuristic implements estimateDistance(p,q) of Heuristic by calculating the manhattan distance of p and q (Calculating the shortest path on a grid without obstacles without diagonal movements).
 *
 * @author Patrick Loka
 * @version 1.0
 * @see HeuristicStrategy
 * @since 1.0
 */
class ManhattanHeuristic implements Heuristic{

    /**
     * {@inheritDoc}
     * <br>
     * Implementation: Estimates the distance between p and q by calculating the manhattan distance (Calculating the shortest path on a grid without obstacles without diagonal movements).
     *
     * @param p,q Points in a 2-dimensional vector space
     * @return Manhattan distance between p and q (Calculating the shortest path on a grid without obstacles without diagonal movements).
     * @since 1.0
     */
    @Override
    public double estimateDistance(Vector p, Vector q) {
        return Math.abs(p.getX() - q.getX()) + Math.abs(p.getY() + q.getY());
    }
}
