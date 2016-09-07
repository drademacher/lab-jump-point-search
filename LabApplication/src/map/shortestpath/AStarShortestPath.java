package map.shortestpath;

import map.MapFacade;
import map.movingRule.MovingRule;
import util.MathUtil;
import util.Tuple2;
import util.Vector;

import java.util.Collection;

/**
 * Created by paloka on 07.09.16.
 */
class AStarShortestPath extends ShortestPath {

    private ShortestPathStrategy shortestPathStrategy;

    protected AStarShortestPath(ShortestPathStrategy shortestPathStrategy, ShortestPathPruning pruning) {
        super(new NoShortestPathPreprocessing(), pruning);
        this.shortestPathStrategy = shortestPathStrategy;
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
