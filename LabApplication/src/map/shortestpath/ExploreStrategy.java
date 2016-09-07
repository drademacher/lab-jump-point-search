package map.shortestpath;

import map.MapFacade;
import map.movingrule.MovingRule;
import util.Vector;
import util.Tuple2;

import java.util.Collection;

/**
 * Created by paloka on 21.07.16.
 */
interface ExploreStrategy {
    Tuple2<Vector, Double> exploreStrategy(MapFacade map, Vector currentPoint, Vector direction, Double cost, Vector goal, MovingRule movingRule);
    Collection<Vector> getDirectionsStrategy(MapFacade map, Vector currentPoint, Vector predecessor, MovingRule movingRule);
}
