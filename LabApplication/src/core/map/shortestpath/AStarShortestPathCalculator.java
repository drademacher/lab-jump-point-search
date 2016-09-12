package core.map.shortestpath;

import core.map.MapFacade;
import core.map.movingrule.MovingRule;
import core.util.MathUtil;
import core.util.Tuple2;
import core.util.Vector;

import java.util.Collection;

/**
 * AStar implementation of ShortestPathCalculator
 *
 * @author Patrick Loka
 * @version 1.0
 * @since 1.0
 */
class AStarShortestPathCalculator extends ShortestPathCalculator {

    protected AStarShortestPathCalculator(ShortestPathPruning pruning) {
        super(new NoShortestPathPreprocessing(), pruning);
    }

    @Override
    public Collection<Vector> getDirectionsStrategy(MapFacade map, Vector currentPoint, Vector predecessor, MovingRule movingRule) {
        return movingRule.getAllDirections();
    }

    @Override
    public Tuple2<Vector, Double> exploreStrategy(MapFacade map, Vector currentPoint, Vector direction, Double cost, Vector goal, MovingRule movingRule) {
        Vector candidate = currentPoint.add(direction);
        if (!map.isPassable(candidate) || movingRule.isCornerCut(map, currentPoint, direction)) {
            return null;
        }
        return new Tuple2<>(candidate, Math.abs(direction.getX()) + Math.abs(direction.getY()) < 2 ? 1 : MathUtil.SQRT2);
    }
}
