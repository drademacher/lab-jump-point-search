package map.shortestpath;

import util.Coordinate;

import java.util.HashMap;

/**
 * Created by paloka on 01.08.16.
 */
abstract class ShortestPathBoundingBoxesPreprocessing extends ShortestPathPreprocessing {
    private HashMap<Coordinate, HashMap<Coordinate,BoundingBox>> boundingBoxes  = new HashMap<>();

    protected void addBoundingBox(Coordinate currentPoint, Coordinate direction, BoundingBox newBB){
        this.boundingBoxes.putIfAbsent(currentPoint,new HashMap<>());
        BoundingBox oldBB = this.boundingBoxes.get(currentPoint).putIfAbsent(direction,newBB);
        if(oldBB!=null)   oldBB.add(newBB);
    }

    protected void addBoundingBox(Coordinate currentPoint, Coordinate direction, Coordinate bBPoint){
        addBoundingBox(currentPoint,direction,new BoundingBox(bBPoint.getX(),bBPoint.getX(),bBPoint.getY(),bBPoint.getY()));
    }

    protected BoundingBox getBoundingBox(Coordinate currentPoint, Coordinate direction){
        if(this.boundingBoxes.get(currentPoint)==null)  return null;
        return this.boundingBoxes.get(currentPoint).get(direction);
    }

    @Override
    protected boolean prune(Coordinate candidate, Coordinate direction, Coordinate goal){
        if(this.boundingBoxes.get(candidate)==null || this.boundingBoxes.get(candidate).get(direction)==null){
            System.out.println("candidate: "+candidate+"\t direction: "+direction+"\t No BoundingBox found");
            return true;
        }
        System.out.println("\t candidate: "+candidate+"\t direction: "+direction+"\t BoundingBox: "+this.boundingBoxes.get(candidate).get(direction)+"\t"+this.boundingBoxes.get(candidate).get(direction).isInBoundingBox(goal));
        return !this.boundingBoxes.get(candidate).get(direction).isInBoundingBox(goal);
    }
}
