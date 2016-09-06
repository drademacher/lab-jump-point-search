package map.heuristic;

import util.Vector;

/**
 * Implements:
 * estimateDistance(Vector p,Vector q) - Estimates the distance between two points p, q without considering the map or obstacles between p and q by a heuristic.
 *
 * @author Patrick Loka
 * @version 1.0
 * @see HeuristicStrategy
 * @see map.shortestpath.ShortestPath
 */
public interface Heuristic {

    /**
     * Estimates the distance between two points p, q without considering the map or obstacles between p and q by a heuristic.
     *
     * A valid implementation fulfills following properties:
     * (p,q) >= 0, (p,p) = 0
     * (p,q) == (q,p)
     * (p,z) + (z,q) <= (p,q)
     *
     * @param p,q Points on a Map.
     * @return Estimated distance of p and q without considering the map or obstacles between p and q.
     * @since 1.0
     * @see HeuristicStrategy
     * @see map.shortestpath.ShortestPath
     */
    double estimateDistance(Vector p, Vector q);
}
