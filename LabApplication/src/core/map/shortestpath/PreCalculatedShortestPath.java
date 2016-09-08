package core.map.shortestpath;

import core.map.MapFacade;
import core.map.movingrule.MovingRule;
import core.util.Tuple2;
import core.util.Tuple3;
import core.util.Vector;

import java.util.Collection;

/**
 * Created by paloka on 07.09.16.
 */
class PreCalculatedShortestPath extends ShortestPath {

    PreCalculationShortestPathPreprocessing preprocessing;

    PreCalculatedShortestPath(PreCalculationShortestPathPreprocessing preprocessing, ShortestPathPruning pruning) {
        super(preprocessing, pruning);
        this.preprocessing = preprocessing;
    }

    @Override
    public Collection<Vector> getDirectionsStrategy(MapFacade map, Vector currentPoint, Vector predecessor, MovingRule movingRule) {
        return preprocessing.getDirectionsStrategy(map, currentPoint, predecessor, movingRule);
    }

    @Override
    public Tuple2<Vector, Double> exploreStrategy(MapFacade map, Vector currentPoint, Vector direction, Double cost, Vector goal, MovingRule movingRule) {
        Tuple3<Vector, Double, Boolean> preprocessedPoint = preprocessing.getPreprocessing(currentPoint, direction);
        if (preprocessedPoint != null) {
            Vector candidate = preprocessedPoint.getArg1();
            int deltaXToGoal = direction.getX() * (goal.getX() - currentPoint.getX());
            int deltaXToCandidate = direction.getX() * (candidate.getX() - currentPoint.getX());
            deltaXToGoal = (deltaXToGoal <= 0 || deltaXToGoal > deltaXToCandidate) ? 0 : deltaXToGoal;
            int deltaYToGoal = direction.getY() * (goal.getY() - currentPoint.getY());
            int deltaYToCandidate = direction.getY() * (candidate.getY() - currentPoint.getY());
            deltaYToGoal = (deltaYToGoal <= 0 || deltaYToGoal > deltaYToCandidate) ? 0 : deltaYToGoal;
            int minDistanceToGoal = Math.min(deltaXToGoal, deltaYToGoal);
            int maxDistanceToGoal = Math.max(deltaXToGoal, deltaYToGoal);
            int steps = minDistanceToGoal > 0 ? minDistanceToGoal : (maxDistanceToGoal > 0 ? maxDistanceToGoal : 0);
            if (steps > 0) {
                Vector forcedPoint = currentPoint.add(direction.mult(steps));
                if (forcedPoint.equals(goal))
                    return new Tuple2<>(forcedPoint, cost + steps * Math.sqrt(Math.abs(direction.getX())) + Math.abs(direction.getY()));
                for (Vector subordinatedDirection : movingRule.getSubordinatedDirections(direction)) {
                    Tuple2<Vector, Double> NextInDir = exploreStrategy(map, forcedPoint, subordinatedDirection, 0.0, goal, movingRule);
                    if (NextInDir != null && goal.equals(NextInDir.getArg1())) {
                        return new Tuple2<>(forcedPoint, cost + steps * Math.sqrt(Math.abs(direction.getX()) + Math.abs(direction.getY())));
                    }
                }
            }
            if (preprocessedPoint.getArg3()) return new Tuple2<>(candidate, cost + preprocessedPoint.getArg2());
        }
        return null;
    }
}
