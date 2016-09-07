package map.shortestpath;

import map.MapFacade;
import map.movingRule.MovingRule;

/**
 * Created by paloka on 31.08.16.
 */
interface ShortestPathPreprocessing {
    void doPreprocessing(MapFacade map, MovingRule movingRule);
}
