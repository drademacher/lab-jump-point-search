package map.shortestpath;

import map.MapFacade;
import map.movingRule.MovingRule;
import util.Vector;

import java.util.HashMap;

/**
 * Created by paloka on 01.08.16.
 */
abstract class BoundingBoxesShortestPathPruning implements AbstractShortestPathPruning {
    private HashMap<Vector, HashMap<Vector,BoundingBox>> boundingBoxes  = new HashMap<>();

    @Override
    public void doPreprocessing(MapFacade map, MovingRule movingRule) {
        this.boundingBoxes  = new HashMap<>();
    }

    protected void unionBoundingBox(Vector currentPoint, Vector direction, BoundingBox newBB){
        if(newBB==null) return;
        this.boundingBoxes.putIfAbsent(currentPoint,new HashMap<>());
        BoundingBox oldBB = this.boundingBoxes.get(currentPoint).putIfAbsent(direction,new BoundingBox(newBB));
        if(oldBB!=null) this.boundingBoxes.get(currentPoint).get(direction).union(newBB);
    }

    @Override
    public boolean prune(Vector candidate, Vector direction, Vector goal){
        if(this.boundingBoxes.get(candidate)==null || this.boundingBoxes.get(candidate).get(direction)==null){
            System.out.println("candidate: "+candidate+"\t direction: "+direction+"\t No BoundingBox found");
            return false;
        }
        System.out.println("\t candidate: "+candidate+"\t direction: "+direction+"\t BoundingBox: "+this.boundingBoxes.get(candidate).get(direction)+"\t"+this.boundingBoxes.get(candidate).get(direction).isInBoundingBox(goal));
        return !this.boundingBoxes.get(candidate).get(direction).isInBoundingBox(goal);
    }
}
