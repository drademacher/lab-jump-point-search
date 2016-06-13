package shortestpath;

import util.Coordinate;

/**
 * Created by paloka on 10.06.16.
 */
public interface Heuristic {

    double estimateDistance(Coordinate p, Coordinate q);
}
