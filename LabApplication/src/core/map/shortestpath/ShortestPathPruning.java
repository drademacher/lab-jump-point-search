package core.map.shortestpath;

import core.util.Vector;

/**
 * ShortestPathPruning does some preprocessing to decide at runtime for finding a shortest path on a grid map if a certain
 * point on the map reached from a certain direction can be part of the shortest path to a given goal.
 *
 * @author Patrick Loka
 * @version 1.0
 * @since 1.0
 */
interface ShortestPathPruning extends ShortestPathPreprocessing {

    /**
     * Decides whether it is possible for a certain point reached from a certain direction can be part of the sortest path
     * to a given goal. If yes, keep the point and return false, if no, prune the point by returning true.<br>
     *
     * @param candidate Point on the map reached by a shortest path search.
     * @param direction Direction in which candidate is reached.
     * @param goal Goalpoint of the searched shortest path.
     * @return True, if the candidate should be pruned, else false.
     * @since 1.0
     */
    boolean prune(Vector candidate, Vector direction, Vector goal);
}
