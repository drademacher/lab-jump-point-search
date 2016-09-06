package map.heuristic;

import util.Vector;

/**
 * ManhattanHeuristic implements all Heuristic operations.<br>
 * <br>
 * ManhattanHeuristic implements estimateDistance(p,q) of Heuristic by calculating the manhattan distance of p and q.
 *
 * @author Patrick Loka
 * @version 1.0
 * @see HeuristicStrategy
 * @since 1.0
 */
class ManhattanHeuristic implements Heuristic{

    /**
     * Implementation: Estimates the distance between p and q by calculating the manhattan distance.<br>
     * <br>
     * Specification: {@inheritDoc}
     *
     * @param p,q Points in a 2-dimensional vector space
     * @return Manhattan distance between p and q.
     * @since 1.0
     */
    @Override
    public double estimateDistance(Vector p, Vector q) {
        return Math.abs(p.getX() - q.getX()) + Math.abs(p.getY() + q.getY());
    }
}
