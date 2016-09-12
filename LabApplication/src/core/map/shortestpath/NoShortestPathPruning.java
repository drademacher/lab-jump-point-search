package core.map.shortestpath;

import core.map.MapFacade;
import core.map.movingrule.MovingRule;
import core.util.Vector;

/**
 * Implementation of ShortestPathPruning if no pruning is wanted.
 *
 * @author Patrick Loka
 * @version 1.0
 * @since 1.0
 */
class NoShortestPathPruning implements ShortestPathPruning {
    @Override
    public void doPreprocessing(MapFacade map, MovingRule movingRule) {
        //Do nothing because no pruning wanted
    }

    @Override
    public boolean prune(Vector candidate, Vector direction, Vector goal) {
        return false; //Never prune anything
    }
}
