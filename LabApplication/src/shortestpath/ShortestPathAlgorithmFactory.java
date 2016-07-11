package shortestpath;

import map.MapFacade;
import shortestpath.movingRule.MovingRule;
import util.Coordinate;
import util.MathUtil;
import util.Tuple2;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by paloka on 08.06.16.
 */
public class ShortestPathAlgorithmFactory {

    /* ------- ShortestPathAlgorithmFactory ------- */

    public ShortestPathAlgorithm createAStar() {
        return new ShortestPathAlgorithm() {
            @Override
            protected Collection<Coordinate> getDirectionsStrategy(MapFacade map, Coordinate currentPoint, Coordinate predecessor, MovingRule movingRule) {
                return movingRule.getAllDirections();
            }

            @Override
            protected Tuple2<Coordinate, Double> exploreStrategy(MapFacade map, Coordinate currentPoint, Coordinate direction, Double cost, Coordinate goal, MovingRule movingRule) {
                Coordinate candidate    = currentPoint.add(direction);
                if(!map.isPassable(candidate) || movingRule.isCornerCut(map, currentPoint, direction)){
                    return null;
                }
                return new Tuple2<>(candidate,Math.abs(direction.getX())+Math.abs(direction.getY())<2?1:MathUtil.SQRT2);
            }
        };
    }

    public ShortestPathAlgorithm createJPS() {
        return new ShortestPathAlgorithm() {
            @Override
            protected Collection<Coordinate> getDirectionsStrategy(MapFacade map, Coordinate currentPoint, Coordinate predecessor, MovingRule movingRule) {
                if(predecessor!=null){
                    Collection<Coordinate> directions = new ArrayList<>();
                    Coordinate direction = movingRule.getDirection(currentPoint, predecessor);
                    directions.add(direction);
                    directions.addAll(movingRule.getForcedDirections(map,currentPoint,direction));
                    directions.addAll(movingRule.getSubDirections(direction));
                    return directions;
                }else{
                    return movingRule.getAllDirections();
                }
            }

            @Override
            protected Tuple2<Coordinate, Double> exploreStrategy(MapFacade map, Coordinate currentPoint, Coordinate direction, Double cost, Coordinate goal, MovingRule movingRule) {
                Coordinate candidate = currentPoint.add(direction);
                if(!map.isPassable(candidate) || movingRule.isCornerCut(map, currentPoint, direction))    return null;

                cost += Math.sqrt(Math.abs(direction.getX()) + Math.abs(direction.getY()));
                if(candidate.equals(goal))          return new Tuple2<>(candidate,cost);
                if(movingRule.getForcedDirections(map,candidate,direction).size()>0)  return new Tuple2<>(candidate, cost);

                for(Coordinate subDirection:movingRule.getSubDirections(direction)){
                    if(exploreStrategy(map, candidate, subDirection, cost, goal, movingRule) != null)     return new Tuple2<>(candidate,cost);
                }

                return exploreStrategy(map, candidate, direction, cost, goal, movingRule);
            }
        };
    }

    public ShortestPathAlgorithm createJPSplus(final PreProcessing preProcessing) {
        return new ShortestPathAlgorithm() {
            @Override
            protected Collection<Coordinate> getDirectionsStrategy(MapFacade map, Coordinate currentPoint, Coordinate predecessor, MovingRule movingRule) {
                // TODO: remove direct code duplicate
                if (predecessor!=null) {
                    Collection<Coordinate> directions = new ArrayList<>();
                    Coordinate direction = movingRule.getDirection(currentPoint, predecessor);
                    directions.add(direction);
                    directions.addAll(movingRule.getForcedDirections(map,currentPoint,direction));
                    directions.addAll(movingRule.getSubDirections(direction));
                    return directions;
                } else {
                    return movingRule.getAllDirections();
                }
            }

            @Override
            protected Tuple2<Coordinate, Double> exploreStrategy(MapFacade map, Coordinate currentPoint, Coordinate direction, Double cost, Coordinate goal, MovingRule movingRule) {
                Tuple2<Coordinate, Double> candidate = preProcessing.get(currentPoint).get(direction);
                if(candidate.equals(goal))          return candidate;
                return new Tuple2<>(candidate.getArg1(), candidate.getArg2() + cost);
            }
        };
    }
}