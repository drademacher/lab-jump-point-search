package core.map.heuristic;

import core.util.Vector;

/**
 * The Heuristic interface provides a method, to estimate the distance between two points based on a heuristic function.
 *
 * @author Patrick Loka
 * @version 1.0
 * @see HeuristicStrategy
 * @see ZeroHeuristic
 * @see GridHeuristic
 * @see ManhattanHeuristic
 * @see EuclideanHeuristic
 * @since 1.0
 */
public interface Heuristic {

    /**
     * Estimates the distance between two points p, q without considering a specific map or obstacles between p and q by a heuristic.<br>
     * <br>
     * A valid implementation fulfills following properties:<br>
     * (p,q) &ge; 0 <br>
     * (p,p) = 0 <br>
     * (p,q) = (q,p) <br>
     * (p,z) + (z,q) &le; (p,q) <br>
     *
     * @param p Point in a 2-dimensional vector space
     * @param q Point in a 2-dimensional vector space
     * @return Estimated distance of p and q without considering a specific map or obstacles between p and q.
     * @see ZeroHeuristic
     * @see GridHeuristic
     * @see ManhattanHeuristic
     * @see EuclideanHeuristic
     * @since 1.0
     */
    double estimateDistance(Vector p, Vector q);
}
