package map.shortestpath;

import map.MapFacade;
import map.heuristic.Heuristic;
import map.movingRule.MovingRule;
import util.Coordinate;
import util.MathUtil;
import util.Tuple2;
import util.Tuple3;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by paloka on 08.06.16.
 */
public class ShortestPathStrategy {

    private ShortestPath shortestPath;

    /* ------- ShortestPath Operations ------- */

    public void preprocess(MapFacade map, MovingRule movingRule){
        this.shortestPath.doPreprocessing(map,movingRule);
    }

    public ShortestPathResult run(MapFacade map, Coordinate start, Coordinate goal, Heuristic heuristic, MovingRule movingRule){
        return this.shortestPath.findShortestPath(map,start,goal,heuristic,movingRule);
    }


    /* ------- ShortestPath Setter ------- */

    public void setShortestPathAStar(){
        this.shortestPath   = new ShortestPathAStar();
    }

    public void setShortestPathJPS(){
        this.shortestPath   = new ShortestPathJPS();
    }

    public void setShortestPathJPSPlus() {
        this.shortestPath   = new ShortestPathPreprocessed(new ShortestPathJPSPlusPreprocessing());
    }

    public void setShortestPathJPSBB() {
        this.shortestPath   = new ShortestPathPreprocessed(new ShortestPathJPSBBPreprocessing());
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
            return getDirectionsJPS(map,currentPoint,predecessor,movingRule);
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

        ShortestPathPreprocessed(ShortestPathPreprocessing preprocessing){
            this.preprocessing          = preprocessing;
        }

        @Override
        public Collection<Coordinate> getDirectionsStrategy(MapFacade map, Coordinate currentPoint, Coordinate predecessor, MovingRule movingRule) {
            return this.preprocessing.getDirectionsStrategy(map,currentPoint,predecessor,movingRule);
        }

        @Override
        public Tuple2<Coordinate, Double> exploreStrategy(MapFacade map, Coordinate currentPoint, Coordinate direction, Double cost, Coordinate goal, MovingRule movingRule) {
            Tuple3<Coordinate,Double,Boolean> preprocessedPoint  = this.preprocessing.getPreprocessing(currentPoint,direction);
            if(preprocessedPoint!=null) {
                Coordinate candidate = preprocessedPoint.getArg1();
                int deltaXToGoal        = direction.getX()*(goal.getX()-currentPoint.getX());
                int deltaXToCandidate   = direction.getX()*(candidate.getX()-currentPoint.getX());
                deltaXToGoal            = deltaXToGoal<=0 || deltaXToGoal>deltaXToCandidate?0:deltaXToGoal;
                int deltaYToGoal        = direction.getY()*(goal.getY()-currentPoint.getY());
                int deltaYToCandidate   = direction.getY()*(candidate.getY()-currentPoint.getY());
                deltaYToGoal            = deltaYToGoal<=0 || deltaYToGoal>deltaYToCandidate?0:deltaYToGoal;
                int minDistanceToGoal   = Math.min(deltaXToGoal,deltaYToGoal);
                int maxDistanceToGoal   = Math.max(deltaXToGoal,deltaYToGoal);
                int steps               = minDistanceToGoal>0?minDistanceToGoal:(maxDistanceToGoal>0?maxDistanceToGoal:0);
                if(steps>0){
                    Coordinate forcedPoint  = currentPoint.add(direction.mult(steps));
                    if(forcedPoint.equals(goal)) return new Tuple2<>(forcedPoint,cost + steps*Math.sqrt(Math.abs(direction.getX()))+Math.abs(direction.getY()));
                    for(Coordinate dir:movingRule.getSubDirections(direction)){
                        Tuple2<Coordinate,Double> NextInDir  = exploreStrategy(map,forcedPoint,dir,0.0,goal,movingRule);
                        if(NextInDir!=null && goal.equals(NextInDir.getArg1())){
                            return new Tuple2<>(forcedPoint,cost+steps*Math.sqrt(Math.abs(direction.getX())+Math.abs(direction.getY())));
                        }
                    }
                }
                if (preprocessedPoint.getArg3()) return new Tuple2<>(candidate, cost + preprocessedPoint.getArg2());
            }
            return null;
        }

        @Override
        protected boolean prune(Coordinate candidate, Coordinate direction, Coordinate goal){
            return this.preprocessing.prune(candidate,direction,goal);
        }

        @Override
        public void doPreprocessing(MapFacade map, MovingRule movingRule) {
            this.preprocessing.doPreprocessing(map,movingRule);
        }
    }


    /* ------- ShortestPathPreprocessing ------- */

    private class ShortestPathJPSPlusPreprocessing extends ShortestPathPreprocessing {
        @Override
        public Collection<Coordinate> getDirectionsStrategy(MapFacade map, Coordinate currentPoint, Coordinate predecessor, MovingRule movingRule){
            return getDirectionsJPS(map,currentPoint,predecessor,movingRule);
        }

        @Override
        public Tuple3<Coordinate,Double,Boolean> exploreStrategy(MapFacade map, Coordinate currentPoint, Coordinate direction, Double cost, Coordinate goal, MovingRule movingRule){
            if(getPreprocessing(currentPoint,direction)!=null){
                Tuple3<Coordinate,Double,Boolean> preprocessedPoint  = getPreprocessing(currentPoint,direction);
                return new Tuple3<>(preprocessedPoint.getArg1(), preprocessedPoint.getArg2() + cost, preprocessedPoint.getArg3());
            }

            Coordinate candidate = currentPoint.add(direction);
            if(!map.isPassable(candidate) || movingRule.isCornerCut(map, currentPoint, direction)){
                return new Tuple3<>(currentPoint,cost,false);
            }

            Double stepCost = Math.sqrt(Math.abs(direction.getX()) + Math.abs(direction.getY()));
            if(movingRule.getForcedDirections(map,candidate,direction).size()>0){
                putPreprocessing(currentPoint,direction,new Tuple3<>(candidate,stepCost,true));
                return new Tuple3<>(candidate,cost+stepCost,true);
            }

            for(Coordinate subDirection:movingRule.getSubDirections(direction)){
                if(exploreStrategy(map, candidate, subDirection, 1.0, goal, movingRule).getArg3()){
                    putPreprocessing(currentPoint,direction,new Tuple3<>(candidate,stepCost,true));
                    return new Tuple3<>(candidate,cost+stepCost,true);
                }
            }

            Tuple3<Coordinate,Double,Boolean> result    = exploreStrategy(map, candidate, direction, cost+stepCost, goal, movingRule);
            putPreprocessing(currentPoint,direction,new Tuple3<>(result.getArg1(),result.getArg2()-cost,result.getArg3()));
            return result;
        }
    }


    private class ShortestPathJPSBBPreprocessing extends ShortestPathBoundingBoxesPreprocessing{
        @Override
        public Collection<Coordinate> getDirectionsStrategy(MapFacade map, Coordinate currentPoint, Coordinate predecessor, MovingRule movingRule){
            return getDirectionsJPS(map,currentPoint,predecessor,movingRule);
        }

        //Todo: JPS+BB Buggy
        @Override
        public Tuple3<Coordinate,Double,Boolean> exploreStrategy(MapFacade map, Coordinate currentPoint, Coordinate direction, Double cost, Coordinate goal, MovingRule movingRule){
            if(getPreprocessing(currentPoint,direction)!=null){
                Tuple3<Coordinate,Double,Boolean> preprocessedPoint  = getPreprocessing(currentPoint,direction);
                return new Tuple3<>(preprocessedPoint.getArg1(), preprocessedPoint.getArg2() + cost, preprocessedPoint.getArg3());
            }

            Coordinate candidate = currentPoint.add(direction);
            if(!map.isPassable(candidate) || movingRule.isCornerCut(map, currentPoint, direction)){
                return new Tuple3<>(currentPoint,cost,false);
            }
            addBoundingBox(candidate,direction,candidate);

            Double stepCost = Math.sqrt(Math.abs(direction.getX()) + Math.abs(direction.getY()));
            boolean candidateIsJP = false;
            Collection<Coordinate> forcedDirections = movingRule.getForcedDirections(map,candidate,direction);
            if(forcedDirections.size()>0){
                for(Coordinate dir:forcedDirections){
                    exploreStrategy(map,candidate,dir,0.0,null,movingRule);
                    addBoundingBox(candidate,direction,getBoundingBox(candidate.add(dir),dir));
                }
                candidateIsJP = true;
            }

            for(Coordinate subDirection:movingRule.getSubDirections(direction)){
                Tuple3<Coordinate,Double,Boolean> subResult = exploreStrategy(map, candidate, subDirection, 1.0, goal, movingRule);
                addBoundingBox(candidate,direction,getBoundingBox(candidate.add(subDirection),subDirection));
                if(subResult.getArg3()) candidateIsJP = true;
            }

            Tuple3<Coordinate,Double,Boolean> result    = exploreStrategy(map, candidate, direction, cost+stepCost, goal, movingRule);
            addBoundingBox(candidate,direction,getBoundingBox(candidate.add(direction),direction));

            if(candidateIsJP){
                putPreprocessing(currentPoint,direction,new Tuple3<>(candidate,stepCost,true));
                return new Tuple3<>(candidate,cost+stepCost,true);
            }
            putPreprocessing(currentPoint,direction,new Tuple3<>(result.getArg1(),result.getArg2()-cost,result.getArg3()));
            return result;
        }
    }


    /* ------- GetDirectionStrategíes ------- */

    private Collection<Coordinate> getDirectionsJPS(MapFacade map, Coordinate currentPoint, Coordinate predecessor, MovingRule movingRule){
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
}