package core.map.shortestpath;

import core.map.MapFacade;
import core.map.movingrule.MovingRule;

/**
 * Created by paloka on 07.09.16.
 */
class NoShortestPathPreprocessing implements ShortestPathPreprocessing {
    @Override
    public void doPreprocessing(MapFacade map, MovingRule movingRule) {
        //Do nothing because no preprocessing wanted
    }
}
