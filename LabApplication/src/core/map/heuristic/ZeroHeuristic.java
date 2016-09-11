package core.map.heuristic;

import core.util.Vector;

/**
 * ZeroHeuristic implements all Heuristic operations. <br>
 * <br>
 * ZeroHeuristic implements estimateDistance(p,q) of Heuristic by returning 0 independent on the input.
 *
 * @author Patrick Loka
 * @version 1.0
 * @see HeuristicStrategy
 * @since 1.0
 */
class ZeroHeuristic implements Heuristic {

    /**
     * {@inheritDoc}
     * <br>
     * Implementation: Estimates the distance between p and q with 0 independent on the input.
     *
     * @param p,q Points in a 2-dimensional vector space
     * @return 0
     * @since 1.0
     */
    @Override
    public double estimateDistance(Vector p, Vector q) {
        return 0;
    }
}
