package map.shortestpath;

import map.MapFacade;
import map.movingRule.MovingRule;
import util.Coordinate;
import util.MathUtil;
import util.Tuple2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by paloka on 08.06.16.
 */
public class ShortestPathStrategy {

    private ShortestPath shortestPath;


    /* ------- ShortestPath Getter & Setter ------- */

    public ShortestPath getShortestPath(){
        return this.shortestPath;
    }

    public void setShortestPathAStar(){
        this.shortestPath   = new ShortestPathAStar();
    }

    public void setShortestPathJPS(){
        this.shortestPath   = new ShortestPathJPS();
    }

    public void setShortestPathJPSPlus() {
        this.shortestPath   = new ShortestPathPreprocessed(new ShortestPathJPS());
    }


    /* ------- ShortestPathStrategy ------- */

    private class ShortestPathAStar extends ShortestPath {
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
    }


    private class ShortestPathJPS extends ShortestPath {
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
    }


    private class ShortestPathPreprocessed extends ShortestPath {
        private ShortestPath shortestPathPreprocessingStrategy;
        private HashMap<Coordinate, HashMap<Coordinate, Tuple2<Coordinate,Double>>> preprocessing;

        ShortestPathPreprocessed(ShortestPath shortestPathPreprocessingStrategy){
            this.shortestPathPreprocessingStrategy = new ShortestPath() {
                @Override
                protected Collection<Coordinate> getDirectionsStrategy(MapFacade map, Coordinate currentPoint, Coordinate predecessor, MovingRule movingRule) {
                    return shortestPathPreprocessingStrategy.getDirectionsStrategy(map,currentPoint,predecessor,movingRule);
                }

                @Override
                protected Tuple2<Coordinate, Double> exploreStrategy(MapFacade map, Coordinate currentPoint, Coordinate direction, Double cost, Coordinate goal, MovingRule movingRule) {
                    if(preprocessing!=null && preprocessing.get(currentPoint)!=null && preprocessing.get(currentPoint).get(direction)!=null){
                        Tuple2<Coordinate,Double> candidate  = preprocessing.get(currentPoint).get(direction);
                        return new Tuple2<>(candidate.getArg1(),cost+candidate.getArg2());
                    }
                    Tuple2<Coordinate, Double> candidate = shortestPathPreprocessingStrategy.exploreStrategy(map,currentPoint,direction,cost,goal,movingRule);
                    if(candidate!=null) {
                        if(preprocessing==null) preprocessing   = new HashMap<>();
                        preprocessing.putIfAbsent(currentPoint, new HashMap<>());
                        preprocessing.get(currentPoint).put(direction,new Tuple2<>(candidate.getArg1(),candidate.getArg2()-cost));
                    }
                    return candidate;
                }
            };
        }

        @Override
        protected Collection<Coordinate> getDirectionsStrategy(MapFacade map, Coordinate currentPoint, Coordinate predecessor, MovingRule movingRule) {
            return shortestPathPreprocessingStrategy.getDirectionsStrategy(map,currentPoint,predecessor,movingRule);
        }

        @Override
        protected Tuple2<Coordinate, Double> exploreStrategy(MapFacade map, Coordinate currentPoint, Coordinate direction, Double cost, Coordinate goal, MovingRule movingRule) {
            if(this.preprocessing!=null && this.preprocessing.get(currentPoint)!=null && this.preprocessing.get(currentPoint).get(direction)!=null){
                Tuple2<Coordinate,Double> candidate  = preprocessing.get(currentPoint).get(direction);
                return new Tuple2<>(candidate.getArg1(),cost+candidate.getArg2());
            }
            return null;
        }
    }
}