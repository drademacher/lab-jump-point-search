package core.map.shortestpath;

import core.map.MapFacade;
import core.map.movingrule.MovingRule;

/**
 * Implementation of ShortestPathPreprocessing if no Preprocessing is wanted.
 *
 * @author Patrick Loka
 * @version 1.0
 * @since 1.0
 */
class NoShortestPathPreprocessing implements ShortestPathPreprocessing {
    @Override
    public void doPreprocessing(MapFacade map, MovingRule movingRule) {
        //Do nothing because no preprocessing wanted
    }
}
