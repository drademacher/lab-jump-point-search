package map.shortestpath;

import exception.NoPathFoundExeception;
import map.MapFacade;
import map.heuristic.Heuristic;
import map.movingRule.MovingRule;
import util.Vector;
import util.MathUtil;
import util.Tuple2;
import util.Tuple3;

import java.util.*;

/**
 * Created by paloka on 08.06.16.
 */
public class ShortestPathStrategy {

    private ShortestPath shortestPath;

    /* ------- ShortestPath Operations ------- */

    public void preprocess(MapFacade map, MovingRule movingRule) {
        this.shortestPath.doPreprocessing(map, movingRule);
    }

    public ShortestPathResult run(MapFacade map, Vector start, Vector goal, Heuristic heuristic, MovingRule movingRule) throws NoPathFoundExeception {
        return this.shortestPath.findShortestPath(map, start, goal, heuristic, movingRule);
    }


    /* ------- ShortestPath Setter ------- */

    public void setShortestPathAStar() {
        this.shortestPath = new AStarShortestPath(new NoShortestPathPruning());
    }

    public void setShortestPathJPS() {
        this.shortestPath = new JPSShortestPath(new NoShortestPathPruning());
    }

    public void setShortestPathJPSPlus() {
        this.shortestPath = new PreCalculatedShortestPath(new JPSPlusPreCalculationShortestPathPreprocessing(), new NoShortestPathPruning());
    }

    public void setShortestPathJPSBB() {
        this.shortestPath = new PreCalculatedShortestPath(new JPSPlusPreCalculationShortestPathPreprocessing(), new JPSBoundingBoxesShortestPathPruning());
    }

    public void setShortestPathAStarBB() {
        this.shortestPath   = new AStarShortestPath(new AStarBoundingBoxesShortestPathPruning());
    }


    /* ------- ShortestPathImplementations ------- */

    private class AStarShortestPath extends ShortestPath {

        protected AStarShortestPath(ShortestPathPruning pruning) {
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


    private class JPSShortestPath extends ShortestPath {

        protected JPSShortestPath(ShortestPathPruning pruning) {
            super(new NoShortestPathPreprocessing(), pruning);
        }

        @Override
        public Collection<Vector> getDirectionsStrategy(MapFacade map, Vector currentPoint, Vector predecessor, MovingRule movingRule) {
            return getDirectionsJPS(map, currentPoint, predecessor, movingRule);
        }

        @Override
        public Tuple2<Vector, Double> exploreStrategy(MapFacade map, Vector currentPoint, Vector direction, Double cost, Vector goal, MovingRule movingRule) {
            Vector candidate = currentPoint.add(direction);
            if (!map.isPassable(candidate) || movingRule.isCornerCut(map, currentPoint, direction)) return null;

            cost += Math.sqrt(Math.abs(direction.getX()) + Math.abs(direction.getY()));
            if (candidate.equals(goal)) return new Tuple2<>(candidate, cost);
            if (movingRule.getForcedDirections(map, candidate, direction).size() > 0)
                return new Tuple2<>(candidate, cost);

            for (Vector subordinatedDirection : movingRule.getSubordinatedDirections(direction)) {
                if (exploreStrategy(map, candidate, subordinatedDirection, cost, goal, movingRule) != null)
                    return new Tuple2<>(candidate, cost);
            }

            return exploreStrategy(map, candidate, direction, cost, goal, movingRule);
        }
    }


    private class PreCalculatedShortestPath extends ShortestPath {

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


    /* ------- ShortestPathPreprocessingImplementations ------- */

    private class NoShortestPathPreprocessing implements ShortestPathPreprocessing {
        @Override
        public void doPreprocessing(MapFacade map, MovingRule movingRule) {
            //Do nothing because no preprocessing wanted
        }
    }

    private class JPSPlusPreCalculationShortestPathPreprocessing extends PreCalculationShortestPathPreprocessing {
        @Override
        public Collection<Vector> getDirectionsStrategy(MapFacade map, Vector currentPoint, Vector predecessor, MovingRule movingRule) {
            return getDirectionsJPS(map, currentPoint, predecessor, movingRule);
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


    /* ------- ShortestPathPruningImplementations ------- */

    private class NoShortestPathPruning implements ShortestPathPruning {
        @Override
        public void doPreprocessing(MapFacade map, MovingRule movingRule) {
            //Do nothing because no pruning wanted
        }

        @Override
        public boolean prune(Vector candidate, Vector direction, Vector goal) {
            return false; //Never prune anything
        }
    }


    private class JPSBoundingBoxesShortestPathPruning extends BoundingBoxesShortestPathPruning {
        @Override
        void buildBoundingBoxes(MapFacade map, MovingRule movingRule, Vector currentPoint, HashMap<Vector, BoundingBox> outgoingDirectionBoundingBoxes) {
            movingRule.getAllDirections().stream().filter(incomingDirection -> map.isPassable(currentPoint.sub(incomingDirection))).forEach(incomingDirection -> {
                Collection<Vector> outgoingDirections = movingRule.getForcedDirections(map, currentPoint, incomingDirection);
                    outgoingDirections.addAll(movingRule.getSubordinatedDirections(incomingDirection));
                    outgoingDirections.add(incomingDirection);
                    for (Vector outgoingDirection : outgoingDirections) {
                        unionBoundingBox(currentPoint, incomingDirection, outgoingDirectionBoundingBoxes.get(outgoingDirection));
                    }
                });
            }
        }

    private class AStarBoundingBoxesShortestPathPruning extends BoundingBoxesShortestPathPruning {
        @Override
        void buildBoundingBoxes(MapFacade map, MovingRule movingRule, Vector currentPoint, HashMap<Vector, BoundingBox> outgoingDirectionBoundingBoxes) {
            movingRule.getAllDirections().stream().filter(incomingDirection -> map.isPassable(currentPoint.sub(incomingDirection))).forEach(incomingDirection -> {
                movingRule.getAllDirections().stream().filter(outgoingDirection -> Math.abs(incomingDirection.getX() + outgoingDirection.getX()) + Math.abs(incomingDirection.getY() + outgoingDirection.getY()) > 1).forEach(outgoingDirection -> {
                    unionBoundingBox(currentPoint, incomingDirection, outgoingDirectionBoundingBoxes.get(outgoingDirection));
                });
            });
        }
    }


    /* ------- HelperMathods ------- */

    private Collection<Vector> getDirectionsJPS(MapFacade map, Vector currentPoint, Vector predecessor, MovingRule movingRule) {
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
}