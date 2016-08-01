package map.shortestpath;

import map.MapFacade;
import map.movingRule.MovingRule;
import util.Coordinate;
import util.Tuple3;

import java.util.HashMap;

/**
 * Created by paloka on 21.07.16.
 */
abstract class ShortestPathPreprocessing implements AbstractExploreStrategy {

    private HashMap<Coordinate, HashMap<Coordinate,Tuple3<Coordinate,Double,Boolean>>> preprocessing    = new HashMap<>();

    void doPreprocessing(MapFacade map, MovingRule movingRule) {
        this.preprocessing  = new HashMap<>();
        for(int x=0;x<map.getXDim();x++){
            for(int y=0;y<map.getYDim();y++){
                for(Coordinate direction:movingRule.getAllDirections()){
                    this.exploreStrategy(map, new Coordinate(x,y), direction, 0.0, null, movingRule);
                }
            }
        }
    }

    protected Tuple3<Coordinate,Double,Boolean> getPreprocessing(Coordinate currentPoint, Coordinate direction){
        if(this.preprocessing.get(currentPoint)==null)  return null;
        return this.preprocessing.get(currentPoint).get(direction);
    }

    protected void putPreprocessing(Coordinate currentPoint, Coordinate direction, Tuple3<Coordinate,Double,Boolean> nextPoint){
        preprocessing.putIfAbsent(currentPoint,new HashMap<>());
        preprocessing.get(currentPoint).put(direction,nextPoint);
    }

    protected boolean prune(Coordinate candidate, Coordinate direction, Coordinate goal){
        return false;
    }

    @Override
    public abstract Tuple3<Coordinate, Double,Boolean> exploreStrategy(MapFacade map, Coordinate currentPoint, Coordinate direction, Double cost, Coordinate goal, MovingRule movingRule);
}
