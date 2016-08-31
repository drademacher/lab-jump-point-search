package map.movingRule;

import map.MapFacade;
import util.Vector;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by paloka on 09.07.16.
 */
public abstract class MovingRule {

    public Vector getDirection(Vector currentPoint, Vector predecessor) {
        return new Vector((int)Math.signum(currentPoint.getX() - predecessor.getX()), (int)Math.signum(currentPoint.getY() - predecessor.getY()));
    }

    public Collection<Vector> getAllDirections(){
        Collection<Vector> directions   = new ArrayList<>();
        directions.add(new Vector(0,-1));
        directions.add(new Vector(1,-1));
        directions.add(new Vector(1,0));
        directions.add(new Vector(1,1));
        directions.add(new Vector(0,1));
        directions.add(new Vector(-1,1));
        directions.add(new Vector(-1,0));
        directions.add(new Vector(-1,-1));
        return directions;
    }

    public abstract Collection<Vector> getForcedDirections(MapFacade map, Vector currentPoint, Vector direction);

    public Collection<Vector> getSubDirections(Vector direction){
        Collection<Vector> subDirections    = new ArrayList<>();
        if(Math.abs(direction.getX())+Math.abs(direction.getY())==2){
            subDirections.add(new Vector(direction.getX(),0));
            subDirections.add(new Vector(0,direction.getY()));
        }
        return subDirections;
    }

    public boolean isCornerCut(MapFacade map, Vector currentPoint, Vector direction){
        return false;
    }
}
