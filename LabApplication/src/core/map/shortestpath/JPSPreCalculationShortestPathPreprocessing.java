package core.map.shortestpath;

import core.map.MapFacade;
import core.map.movingrule.MovingRule;
import core.util.Tuple3;
import core.util.Vector;

import java.util.ArrayList;
import java.util.Collection;

/**
 * JPS implementation of PreCalculationShortestPathPreprocessing
 *
 * @author Parick Loka
 * @vesion 1.0
 * @since 1.0
 */
class JPSPreCalculationShortestPathPreprocessing extends PreCalculationShortestPathPreprocessing {

    @Override
    public Collection<Vector> getDirectionsStrategy(MapFacade map, Vector currentPoint, Vector predecessor, MovingRule movingRule) {
        if (predecessor != null) {
            Collection<Vector> directions = new ArrayList<>();
            Vector direction = predecessor.getDirectionTo(currentPoint);
            directions.add(direction);
            directions.addAll(movingRule.getForcedDirections(map, currentPoint, direction));
            directions.addAll(movingRule.getSubordinatedDirections(direction));
            return directions;
        } else {
            return movingRule.getAllDirections();
        }
    }

    @Override
    public Tuple3<Vector, Double, Boolean> exploreStrategy(MapFacade map, Vector currentPoint, Vector direction, Double cost, Vector goal, MovingRule movingRule) {
        if (getPreprocessing(currentPoint, direction) != null) {
            Tuple3<Vector, Double, Boolean> preprocessedPoint = getPreprocessing(currentPoint, direction);
            return new Tuple3<>(preprocessedPoint.getArg1(), preprocessedPoint.getArg2() + cost, preprocessedPoint.getArg3());
        }

        Vector candidate = currentPoint.add(direction);
        if (!map.isPassable(candidate) || movingRule.isCornerCut(map, currentPoint, direction)) {
            return new Tuple3<>(currentPoint, cost, false);
        }

        Double stepCost = Math.sqrt(Math.abs(direction.getX()) + Math.abs(direction.getY()));
        if (movingRule.getForcedDirections(map, candidate, direction).size() > 0) {
            putPreprocessing(currentPoint, direction, new Tuple3<>(candidate, stepCost, true));
            return new Tuple3<>(candidate, cost + stepCost, true);
        }

        for (Vector subordinatedDirection : movingRule.getSubordinatedDirections(direction)) {
            if (exploreStrategy(map, candidate, subordinatedDirection, 1.0, goal, movingRule).getArg3()) {
                putPreprocessing(currentPoint, direction, new Tuple3<>(candidate, stepCost, true));
                return new Tuple3<>(candidate, cost + stepCost, true);
            }
        }

        Tuple3<Vector, Double, Boolean> result = exploreStrategy(map, candidate, direction, cost + stepCost, goal, movingRule);
        putPreprocessing(currentPoint, direction, new Tuple3<>(result.getArg1(), result.getArg2() - cost, result.getArg3()));
        return result;
    }
}
