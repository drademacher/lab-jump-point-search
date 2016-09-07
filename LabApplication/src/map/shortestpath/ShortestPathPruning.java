package map.shortestpath;

import util.Vector;

/**
 * Created by paloka on 31.08.16.
 */
interface ShortestPathPruning extends ShortestPathPreprocessing {
    boolean prune(Vector candidate, Vector direction, Vector goal);
}