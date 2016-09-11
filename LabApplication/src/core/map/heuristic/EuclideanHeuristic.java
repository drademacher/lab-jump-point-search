package core.map.heuristic;

import core.util.Vector;

/**
 * EuclideanHeuristic implements all Heuristic operations.<br>
 * <br>
 * EuclideanHeuristic implements estimateDistance(p,q) of Heuristic by calculating the euclidean distance of p and q.
 *
 * @author Patrick Loka
 * @version 1.0
 * @see HeuristicStrategy
 * @since 1.0
 */
class EuclideanHeuristic implements Heuristic {

    /**
     * {@inheritDoc}
     * <br>
     * Implementation: Estimates the distance between p and q by calculating the euclidean distance.
     *
     * @param p,q Points in a 2-dimensional vector space
     * @return Euclidean distance between p and q.
     * @since 1.0
     */
    @Override
    public double estimateDistance(Vector p, Vector q) {
        return Math.sqrt((p.getX() - q.getX()) * (p.getX() - q.getX()) + (p.getY() - q.getY()) * (p.getY() - q.getY()));
    }
}
