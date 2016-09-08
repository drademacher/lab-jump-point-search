package core.map.shortestpath;

import core.map.MapFacade;
import core.map.movingrule.MovingRule;
import core.util.Vector;
import core.util.Tuple3;

import java.util.HashMap;

/**
 * Created by paloka on 21.07.16.
 */
abstract class PreCalculationShortestPathPreprocessing implements ExploreStrategy, ShortestPathPreprocessing {

    private HashMap<Vector, HashMap<Vector,Tuple3<Vector,Double,Boolean>>> preprocessing    = new HashMap<>();

    @Override
    public void doPreprocessing(MapFacade map, MovingRule movingRule) {
        this.preprocessing  = new HashMap<>();
        for(int x=0;x<map.getXDim();x++){
            for(int y=0;y<map.getYDim();y++){
                Vector current  = new Vector(x,y);
                if(map.isPassable(current)){
                    for(Vector direction:movingRule.getAllDirections()){
                        this.exploreStrategy(map, current, direction, 0.0, null, movingRule);
                    }
                }
            }
        }
    }

    protected Tuple3<Vector,Double,Boolean> getPreprocessing(Vector currentPoint, Vector direction){
        if(this.preprocessing.get(currentPoint)==null)  return null;
        return this.preprocessing.get(currentPoint).get(direction);
    }

    protected void putPreprocessing(Vector currentPoint, Vector direction, Tuple3<Vector,Double,Boolean> nextPoint){
        preprocessing.putIfAbsent(currentPoint,new HashMap<>());
        preprocessing.get(currentPoint).put(direction,nextPoint);
    }

    @Override
    public abstract Tuple3<Vector, Double,Boolean> exploreStrategy(MapFacade map, Vector currentPoint, Vector direction, Double cost, Vector goal, MovingRule movingRule);
}