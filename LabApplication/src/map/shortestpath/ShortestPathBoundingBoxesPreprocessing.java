package map.shortestpath;

import map.MapFacade;
import map.movingRule.MovingRule;
import util.Vector;

import java.util.HashMap;

/**
 * Created by paloka on 01.08.16.
 */
abstract class ShortestPathBoundingBoxesPreprocessing extends ShortestPathPreprocessing {
    private HashMap<Vector, HashMap<Vector,BoundingBox>> boundingBoxes  = new HashMap<>();

    protected void addBoundingBox(Vector currentPoint, Vector direction, BoundingBox newBB){
        this.boundingBoxes.putIfAbsent(currentPoint,new HashMap<>());
        BoundingBox oldBB = this.boundingBoxes.get(currentPoint).putIfAbsent(direction,newBB);
        if(oldBB!=null)   oldBB.add(newBB);
    }

    protected void addBoundingBox(Vector currentPoint, Vector direction, Vector bBPoint){
        addBoundingBox(currentPoint,direction,new BoundingBox(bBPoint.getX(),bBPoint.getX(),bBPoint.getY(),bBPoint.getY()));
    }

    protected BoundingBox getBoundingBox(Vector currentPoint, Vector direction){
        if(this.boundingBoxes.get(currentPoint)==null)  return null;
        return this.boundingBoxes.get(currentPoint).get(direction);
    }

    @Override
    protected void doPreprocessing(MapFacade map, MovingRule movingRule){
        this.boundingBoxes  = new HashMap<>();
        super.doPreprocessing(map,movingRule);
    }

    @Override
    protected boolean prune(Vector candidate, Vector direction, Vector goal){
        if(this.boundingBoxes.get(candidate)==null || this.boundingBoxes.get(candidate).get(direction)==null){
            System.out.println("candidate: "+candidate+"\t direction: "+direction+"\t No BoundingBox found");
            return true;
        }
        System.out.println("\t candidate: "+candidate+"\t direction: "+direction+"\t BoundingBox: "+this.boundingBoxes.get(candidate).get(direction)+"\t"+this.boundingBoxes.get(candidate).get(direction).isInBoundingBox(goal));
        return !this.boundingBoxes.get(candidate).get(direction).isInBoundingBox(goal);
    }
}
