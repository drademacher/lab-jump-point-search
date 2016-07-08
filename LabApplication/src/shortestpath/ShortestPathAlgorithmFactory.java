package shortestpath;

import map.MapFacade;
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

    public ShortestPathAlgorithm createAStar(boolean isCornerCuttingAllowed) {
        return new ShortestPathAlgorithm() {
            @Override
            protected Collection<Coordinate> getDirectionsStrategy(MapFacade map, Coordinate currentPoint, Coordinate predecessor) {
                return getAllDirections();
            }

            @Override
            protected Tuple2<Coordinate, Double> exploreStrategy(MapFacade map, Coordinate currentPoint, Coordinate direction, Double cost, Coordinate goal) {
                Coordinate candidate    = currentPoint.add(direction);
                if(!map.isPassable(candidate) || (!isCornerCuttingAllowed && isCornerCut(map, currentPoint, direction))){
                    return null;
                }
                return new Tuple2<>(candidate,Math.abs(direction.getX())+Math.abs(direction.getY())<2?1:MathUtil.SQRT2);
            }
        };
    }

    public ShortestPathAlgorithm createJPS(boolean isCornerCuttingAllowed) {//Todo: isCornerCuttingAllowed muss false sein, weil die ForcedFields für cornerCutting falsch sind! Ich werde da wohl noch ein StrategyPattern bauen...
        return new ShortestPathAlgorithm() {
            @Override
            protected Collection<Coordinate> getDirectionsStrategy(MapFacade map, Coordinate currentPoint, Coordinate predecessor) {
                if(predecessor!=null){
                    Collection<Coordinate> directions = new ArrayList<>();
                    Coordinate direction = getDirection(currentPoint, predecessor);
                    directions.add(direction);
                    directions.addAll(getForcedDirections(map,currentPoint,direction));
                    directions.addAll(getSubDirections(direction));
                    return directions;
                }else{
                    return getAllDirections();
                }
            }

            @Override
            protected Tuple2<Coordinate, Double> exploreStrategy(MapFacade map, Coordinate currentPoint, Coordinate direction, Double cost, Coordinate goal) {
                Coordinate candidate = currentPoint.add(direction);
                if(!map.isPassable(candidate) || (!isCornerCuttingAllowed && isCornerCut(map, currentPoint, direction)))    return null;

                cost += Math.sqrt(Math.abs(direction.getX()) + Math.abs(direction.getY()));
                if(candidate.equals(goal))          return new Tuple2<>(candidate,cost);
                if(getForcedDirections(map,candidate,direction).size()>0)  return new Tuple2<>(candidate, cost);

                for(Coordinate subDirection:getSubDirections(direction)){
                    if(exploreStrategy(map, candidate, subDirection, cost, goal) != null)     return new Tuple2<>(candidate,cost);
                }

                return exploreStrategy(map, candidate, direction, cost, goal);
            }
        };
    }
}