package core.map.shortestpath;

import core.map.MapFacade;
import core.map.movingrule.MovingRule;

/**
 * ShortestPathPreprocessing provides a method to call for preprocess finding shortest path on a grid.
 *
 * @author Patrick Loka
 * @version 1.0
 * @since 1.0
 */
interface ShortestPathPreprocessing {

    /**
     * Preprocesses the input map considering the given moving rules. ShortestPathCalculator calls this method in preprocessing step.<br>
     * <br>
     * A valid implementation saves some precalculations and uses this information when searching a shortest path on the
     * given map with the given moving rules later for efficiency at runtime.<br>
     *
     * @param map Given grid map to preprocess.
     * @param movingRule Allowed movements on the given grid map.
     * @since 1.0
     */
    void doPreprocessing(MapFacade map, MovingRule movingRule);
}
