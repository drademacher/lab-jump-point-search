package map.shortestpath;

import map.MapFacade;
import map.movingRule.MovingRule;
import util.Coordinate;

import java.util.Collection;

/**
 * Created by paloka on 21.07.16.
 */
interface AbstractGetDirectionsStrategy {
    abstract Collection<Coordinate> getDirectionsStrategy(MapFacade map, Coordinate currentPoint, Coordinate predecessor, MovingRule movingRule);
}
