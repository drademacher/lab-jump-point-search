package map.shortestpath;

import map.MapFacade;
import map.movingrule.MovingRule;
import util.Vector;

/**
 * Created by paloka on 07.09.16.
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
