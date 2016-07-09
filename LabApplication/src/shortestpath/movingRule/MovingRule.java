package shortestpath.movingRule;

import map.MapFacade;
import util.Coordinate;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by paloka on 09.07.16.
 */
public abstract class MovingRule {

    public Coordinate getDirection(Coordinate currentPoint, Coordinate predecessor) {
        return new Coordinate((int)Math.signum(currentPoint.getX() - predecessor.getX()), (int)Math.signum(currentPoint.getY() - predecessor.getY()));
    }

    public Collection<Coordinate> getAllDirections(){
        Collection<Coordinate> directions   = new ArrayList<>();
        directions.add(new Coordinate(0,-1));
        directions.add(new Coordinate(1,-1));
        directions.add(new Coordinate(1,0));
        directions.add(new Coordinate(1,1));
        directions.add(new Coordinate(0,1));
        directions.add(new Coordinate(-1,1));
        directions.add(new Coordinate(-1,0));
        directions.add(new Coordinate(-1,-1));
        return directions;
    }

    public abstract Collection<Coordinate> getForcedDirections(MapFacade map, Coordinate currentPoint, Coordinate direction);

    public Collection<Coordinate> getSubDirections(Coordinate direction){
        Collection<Coordinate> subDirections    = new ArrayList<>();
        if(Math.abs(direction.getX())+Math.abs(direction.getY())==2){
            subDirections.add(new Coordinate(direction.getX(),0));
            subDirections.add(new Coordinate(0,direction.getY()));
        }
        return subDirections;
    }

    public boolean isCornerCut(MapFacade map, Coordinate currentPoint, Coordinate direction){
        return false;
    }
}
