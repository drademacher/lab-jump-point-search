package map.shortestpath;

import map.MapFacade;
import map.movingRule.MovingRule;
import util.Coordinate;
import util.Tuple2;

/**
 * Created by paloka on 21.07.16.
 */
interface AbstractExploreStrategy {
    Tuple2<Coordinate, Double> exploreStrategy(MapFacade map, Coordinate currentPoint, Coordinate direction, Double cost, Coordinate goal, MovingRule movingRule);
}
