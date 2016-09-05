package map.shortestpath;

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

    public ShortestPathResult run(MapFacade map, Vector start, Vector goal, Heuristic heuristic, MovingRule movingRule) {
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
        this.shortestPath = new PreCalculatedShortestPath(new JPSPlusPreCalculationShortestPathPreprocessing(), new BruteForceBoundingBoxesShortestPathPruning());
    }


    /* ------- ShortestPathImplementations ------- */

    private class AStarShortestPath extends ShortestPath {

        protected AStarShortestPath(AbstractShortestPathPruning pruning) {
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

        protected JPSShortestPath(AbstractShortestPathPruning pruning) {
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

            for (Vector subDirection : movingRule.getSubDirections(direction)) {
                if (exploreStrategy(map, candidate, subDirection, cost, goal, movingRule) != null)
                    return new Tuple2<>(candidate, cost);
            }

            return exploreStrategy(map, candidate, direction, cost, goal, movingRule);
        }
    }


    private class PreCalculatedShortestPath extends ShortestPath {

        PreCalculationShortestPathPreprocessing preprocessing;

        PreCalculatedShortestPath(PreCalculationShortestPathPreprocessing preprocessing, AbstractShortestPathPruning pruning) {
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
                    for (Vector dir : movingRule.getSubDirections(direction)) {
                        Tuple2<Vector, Double> NextInDir = exploreStrategy(map, forcedPoint, dir, 0.0, goal, movingRule);
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

    private class NoShortestPathPreprocessing implements AbstractShortestPathPreprocessing {
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

            for (Vector subDirection : movingRule.getSubDirections(direction)) {
                if (exploreStrategy(map, candidate, subDirection, 1.0, goal, movingRule).getArg3()) {
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

    private class NoShortestPathPruning implements AbstractShortestPathPruning {
        @Override
        public void doPreprocessing(MapFacade map, MovingRule movingRule) {
            //Do nothing because no pruning wanted
        }

        @Override
        public boolean prune(Vector candidate, Vector direction, Vector goal) {
            return false; //Never prune anything
        }
    }


    private class BruteForceBoundingBoxesShortestPathPruning extends BoundingBoxesShortestPathPruning {
        @Override
        public void doPreprocessing(MapFacade map, MovingRule movingRule) {
            super.doPreprocessing(map, movingRule);
            for (int x = 0; x < map.getXDim(); x++) {
                for (int y = 0; y < map.getYDim(); y++) {
                    Vector current = new Vector(x, y);
                    if (map.isPassable(current)) {
                        boolean[][] reachedPoints = new boolean[map.getXDim()][map.getYDim()];
                        reachedPoints[current.getX()][current.getY()] = true;
                        HashMap<Vector, BoundingBox> directions = new HashMap<>();
                        PriorityQueue<Tuple2<Tuple3<Vector, Vector, Double>, BoundingBox>> priorityQueue = new PriorityQueue<>((p, q) -> {
                            if (p.getArg1().getArg3() > q.getArg1().getArg3()) return 1;
                            return -1;
                        });
                        for (Vector direction : movingRule.getAllDirections()) {
                            BoundingBox boundingBox = new BoundingBox(current);
                            Vector candidate = current.add(direction);
                            directions.put(direction, boundingBox);
                            if (map.isPassable(candidate))
                                priorityQueue.add(new Tuple2<>(new Tuple3<>(candidate, direction, Math.sqrt(Math.abs(direction.getX()) + Math.abs(direction.getY()))), boundingBox));
                        }

                        while (!priorityQueue.isEmpty()) {
                            Tuple2<Tuple3<Vector, Vector, Double>, BoundingBox> nextEntry = priorityQueue.poll();
                            final Vector nextVector = nextEntry.getArg1().getArg1();
                            final Vector nextDirection = nextEntry.getArg1().getArg2();
                            final BoundingBox nextBoundingBox = nextEntry.getArg2();
                            // TODO: Punkte mit gleicher Entfernung relevant?
                            if (!reachedPoints[nextVector.getX()][nextVector.getY()]) {
                                reachedPoints[nextVector.getX()][nextVector.getY()] = true;
                                nextBoundingBox.add(nextVector);
                                priorityQueue.addAll(explore(map, nextVector, nextDirection, nextEntry.getArg1().getArg3(), nextBoundingBox, movingRule));
                            }
                        }

                        for (Vector incommingDirection : movingRule.getAllDirections()) {
                            if (map.isPassable(current.sub(incommingDirection))) {
                                Collection<Vector> outgoingDirections = movingRule.getForcedDirections(map, current, incommingDirection);
                                outgoingDirections.addAll(movingRule.getSubDirections(incommingDirection));
                                outgoingDirections.add(incommingDirection);
                                for (Vector outgoingDirection : outgoingDirections) {
                                    unionBoundingBox(current, incommingDirection, directions.get(outgoingDirection));
                                }
                            }
                        }
                    }
                }
            }
        }

        private Collection<Tuple2<Tuple3<Vector, Vector, Double>, BoundingBox>> explore(MapFacade map, Vector current, Vector direction, double cost, BoundingBox bb, MovingRule movingRule) {
            List<Tuple2<Tuple3<Vector, Vector, Double>, BoundingBox>> reachablePoints = new ArrayList<>();

            for (Vector forcedDirection : movingRule.getForcedDirections(map, current, direction)) {
                Vector candidate = current.add(forcedDirection);
                if (map.isPassable(candidate))
                    reachablePoints.add(new Tuple2<>(new Tuple3(candidate, forcedDirection, cost + Math.sqrt(Math.abs(forcedDirection.getX()) + Math.abs(forcedDirection.getY()))), bb));
            }

            for (Vector subDirection : movingRule.getSubDirections(direction)) {
                Vector candidate = current.add(subDirection);
                if (map.isPassable(candidate))
                    reachablePoints.add(new Tuple2<>(new Tuple3(candidate, subDirection, cost + Math.sqrt(Math.abs(subDirection.getX()) + Math.abs(subDirection.getY()))), bb));
            }

            Vector candidate = current.add(direction);
            if (map.isPassable(candidate))
                reachablePoints.add(new Tuple2<>(new Tuple3(candidate, direction, cost + Math.sqrt(Math.abs(direction.getX()) + Math.abs(direction.getY()))), bb));

            return reachablePoints;
        }

        ;
    }


    /* ------- HelperMathods ------- */

    private Collection<Vector> getDirectionsJPS(MapFacade map, Vector currentPoint, Vector predecessor, MovingRule movingRule) {
        if (predecessor != null) {
            Collection<Vector> directions = new ArrayList<>();
            Vector direction = movingRule.getDirection(currentPoint, predecessor);
            directions.add(direction);
            directions.addAll(movingRule.getForcedDirections(map, currentPoint, direction));
            directions.addAll(movingRule.getSubDirections(direction));
            return directions;
        } else {
            return movingRule.getAllDirections();
        }
    }
}