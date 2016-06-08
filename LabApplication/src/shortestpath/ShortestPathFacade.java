package shortestpath;

import util.Tuple2;

import java.util.Set;

/**
 * Created by paloka on 06.06.16.
 */
public interface ShortestPathFacade {

    Set<Tuple2<Integer,Integer>> getJumppoints();
    Set<Tuple2<Integer,Integer>> getVisitedPoints();
}
