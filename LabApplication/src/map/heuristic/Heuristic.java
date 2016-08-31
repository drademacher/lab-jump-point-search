package map.heuristic;

import util.Vector;

/**
 * Created by paloka on 10.06.16.
 */
public interface Heuristic {

    double estimateDistance(Vector p, Vector q);
}
