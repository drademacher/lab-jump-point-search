package map.shortestpath;

import map.MapFacade;
import map.movingRule.MovingRule;
import util.Coordinate;
import util.MathUtil;
import util.Tuple2;
import util.Tuple3;

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

    public void preprocess(MapFacade map, MovingRule movingRule){
        this.shortestPath.doPreprocessing(map,movingRule);
    }

    public void setShortestPathAStar(){
        this.shortestPath   = new ShortestPathAStar();
    }

    public void setShortestPathJPS(){
        this.shortestPath   = new ShortestPathJPS();
    }

    public void setShortestPathJPSPlus() {
        this.shortestPath   = new ShortestPathPreprocessed(new ShortestPathJPSPreprocessing(),new ShortestPathJPS());
    }


    /* ------- ShortestPathStrategy ------- */

    private class ShortestPathAStar extends ShortestPath {
        @Override
        public Collection<Coordinate> getDirectionsStrategy(MapFacade map, Coordinate currentPoint, Coordinate predecessor, MovingRule movingRule) {
            return movingRule.getAllDirections();
        }

        @Override
        public Tuple2<Coordinate, Double> exploreStrategy(MapFacade map, Coordinate currentPoint, Coordinate direction, Double cost, Coordinate goal, MovingRule movingRule) {
            Coordinate candidate    = currentPoint.add(direction);
            if(!map.isPassable(candidate) || movingRule.isCornerCut(map, currentPoint, direction)){
               return null;
            }
            return new Tuple2<>(candidate,Math.abs(direction.getX())+Math.abs(direction.getY())<2?1:MathUtil.SQRT2);
        }
    }


    private class ShortestPathJPS extends ShortestPath {
        @Override
        public Collection<Coordinate> getDirectionsStrategy(MapFacade map, Coordinate currentPoint, Coordinate predecessor, MovingRule movingRule) {
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
        public Tuple2<Coordinate, Double> exploreStrategy(MapFacade map, Coordinate currentPoint, Coordinate direction, Double cost, Coordinate goal, MovingRule movingRule) {
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
        ShortestPathPreprocessing preprocessing;
        AbstractGetDirectionsStrategy getDirectionsStrategy;

        ShortestPathPreprocessed(ShortestPathPreprocessing preprocessing, AbstractGetDirectionsStrategy getDirectionsStrategy){
            this.preprocessing          = preprocessing;
            this.getDirectionsStrategy  = getDirectionsStrategy;
        }

        @Override
        public Collection<Coordinate> getDirectionsStrategy(MapFacade map, Coordinate currentPoint, Coordinate predecessor, MovingRule movingRule) {
            return this.getDirectionsStrategy.getDirectionsStrategy(map,currentPoint,predecessor,movingRule);
        }

        //Todo: test for goalpoint
        @Override
        public Tuple2<Coordinate, Double> exploreStrategy(MapFacade map, Coordinate currentPoint, Coordinate direction, Double cost, Coordinate goal, MovingRule movingRule) {
            Tuple3<Coordinate,Double,Boolean> candidate  = this.preprocessing.getPreprocessing(currentPoint,direction);

            if(candidate!=null && candidate.getArg3()) return new Tuple2<>(candidate.getArg1(),cost+candidate.getArg2());
            return null;
        }

        @Override
        public void doPreprocessing(MapFacade map, MovingRule movingRule) {
            this.preprocessing.doPreprocessing(map,movingRule);
        }
    }


    /* ------- ShortestPathPreprocessing ------- */

    private class ShortestPathJPSPreprocessing extends ShortestPathPreprocessing {
        @Override
        public Tuple3<Coordinate,Double,Boolean> exploreStrategy(MapFacade map, Coordinate currentPoint, Coordinate direction, Double cost, Coordinate goal, MovingRule movingRule){
            if(getPreprocessing(currentPoint,direction)!=null){
                Tuple3<Coordinate,Double,Boolean> preprocessedPoint  = preprocessing.get(currentPoint).get(direction);
                return new Tuple3<>(preprocessedPoint.getArg1(), preprocessedPoint.getArg2() + cost, preprocessedPoint.getArg3());
            }

            Coordinate candidate = currentPoint.add(direction);
            if(!map.isPassable(candidate) || movingRule.isCornerCut(map, currentPoint, direction))    return new Tuple3<>(candidate,cost,false);

            Double NewCost = cost + Math.sqrt(Math.abs(direction.getX()) + Math.abs(direction.getY()));
            if(movingRule.getForcedDirections(map,candidate,direction).size()>0){
                preprocessing.putIfAbsent(currentPoint,new HashMap<>());
                Tuple3<Coordinate,Double,Boolean> result    = new Tuple3<>(candidate,NewCost,true);
                preprocessing.get(currentPoint).put(direction,result);
                return result;
            }

            for(Coordinate subDirection:movingRule.getSubDirections(direction)){
                if(exploreStrategy(map, candidate, subDirection, 1.0, goal, movingRule).getArg3()){
                    preprocessing.putIfAbsent(currentPoint,new HashMap<>());
                    Tuple3<Coordinate,Double,Boolean> result    = new Tuple3<>(candidate,NewCost,true);
                    preprocessing.get(currentPoint).put(direction,result);
                    return result;
                }
            }

            Tuple3<Coordinate,Double,Boolean> result    = exploreStrategy(map, candidate, direction, NewCost, goal, movingRule);
            preprocessing.putIfAbsent(currentPoint,new HashMap<>());
            preprocessing.get(currentPoint).put(direction,new Tuple3<>(result.getArg1(),result.getArg2()-cost,result.getArg3()));
            return result;
        }
    }
}