package map.movingRule;

import map.MapFacade;
import util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by paloka on 09.07.16.
 */
public class MovingRuleStrategy {

    private MovingRule movingRule;
    
    
    /* ------- MovingRule Getter & Setter ------- */

    public MovingRule getMovingRule(){
        return this.movingRule;
    }

    public void setMovingRuleBasic(){
        this.movingRule = new MovingRuleBasic();
    }

    public void setMovingRuleNoCornerCutting(){
        this.movingRule = new MovingRuleNoCornerCutting();
    }

    public void setMovingRuleNoDiagonal(){
        this.movingRule = new MovingRuleNoDiagonal();
    }

    
    /* ------- MovingRule Implementations ------- */

    private class MovingRuleBasic extends MovingRule{
        @Override
        public Collection<Vector> getForcedDirections(MapFacade map, Vector currentPoint, Vector direction) {
            Collection<Vector> forcedDirections = new ArrayList<>();
            int dirX    = direction.getX();
            int dirY    = direction.getY();
            int curX   = currentPoint.getX();
            int curY   = currentPoint.getY();

            if(Math.abs(dirX)+Math.abs(dirY)==2){
                forcedDirections.addAll(getSubDirections(direction).stream()
                        .filter(subDir -> !map.isPassable(currentPoint.sub(subDir)) && map.isPassable(currentPoint.add(direction).sub(subDir).sub(subDir)))
                        .map(subDir -> direction.sub(subDir).sub(subDir))
                        .collect(Collectors.toList()));
            }else{
                if(dirX==0){
                    if(!map.isPassable(new Vector(curX+1,curY)) && map.isPassable(new Vector(curX+1,curY+dirY))) forcedDirections.add(new Vector(1,dirY));
                    if(!map.isPassable(new Vector(curX-1,curY)) && map.isPassable(new Vector(curX-1,curY+dirY))) forcedDirections.add(new Vector(-1,dirY));
                }
                if(dirY==0){
                    if(!map.isPassable(new Vector(curX,curY+1)) && map.isPassable(new Vector(curX+dirX,curY+1))) forcedDirections.add(new Vector(dirX,1));
                    if(!map.isPassable(new Vector(curX,curY-1)) && map.isPassable(new Vector(curX+dirX,curY-1))) forcedDirections.add(new Vector(dirX,-1));
                }
            }
            return forcedDirections;
        }
    }

    private class MovingRuleNoCornerCutting extends MovingRule{
        @Override
        public Collection<Vector> getForcedDirections(MapFacade map, Vector currentPoint, Vector direction) {
            Collection<Vector> forcedDirections = new ArrayList<>();
            int dirX    = direction.getX();
            int dirY    = direction.getY();
            if(Math.abs(dirX)+Math.abs(dirY)==2)    return forcedDirections;

            int curX   = currentPoint.getX();
            int curY   = currentPoint.getY();

            if (dirY == 0) {
                if(!map.isPassable(new Vector(curX-dirX,curY-1))
                        && map.isPassable(new Vector(curX,curY-1))) {
                    forcedDirections.add(new Vector(0,-1));
                    forcedDirections.add(new Vector(dirX,-1));
                }
                if(!map.isPassable(new Vector(curX-dirX,curY+1))
                        && map.isPassable(new Vector(curX,curY+1))){
                    forcedDirections.add(new Vector(0,1));
                    forcedDirections.add(new Vector(dirX,1));
                }
            }
            if (dirX == 0) {
                if(!map.isPassable(new Vector(curX-1,curY-dirY))
                        && map.isPassable(new Vector(curX-1,curY))){
                    forcedDirections.add(new Vector(-1,0));
                    forcedDirections.add(new Vector(-1,dirY));
                }
                if(!map.isPassable(new Vector(curX+1,curY-dirY))
                        && map.isPassable(new Vector(curX+1,curY))){
                    forcedDirections.add(new Vector(1,0));
                    forcedDirections.add(new Vector(1,dirY));
                }
            }
            return forcedDirections;
        }

        @Override
        public boolean isCornerCut(MapFacade map, Vector currentPoint, Vector direction) {
            for(Vector subDirection:getSubDirections(direction)){
                if(!map.isPassable(currentPoint.add(subDirection)))   return true;
            }
            return false;
        }
    }

    private class MovingRuleNoDiagonal extends MovingRule{

        @Override
        public Collection<Vector> getAllDirections() {
            Collection<Vector> directions   = new ArrayList<>();
            directions.add(new Vector(0,-1));
            directions.add(new Vector(1,0));
            directions.add(new Vector(0,1));
            directions.add(new Vector(-1,0));
            return directions;
        }
        @Override
        public Collection<Vector> getForcedDirections(MapFacade map, Vector currentPoint, Vector direction) {
            Collection<Vector> forcedDirections = new ArrayList<>();
            int dirX    = direction.getX();
            int dirY    = direction.getY();
            int curX    = currentPoint.getX();
            int curY    = currentPoint.getY();

            if (dirY == 0) {
                if(!map.isPassable(new Vector(curX+(-1)*dirX,curY-1))
                        && map.isPassable(new Vector(curX,curY-1))) {
                    forcedDirections.add(new Vector(0,-1));
                }
                if(!map.isPassable(new Vector(curX+(-1)*dirX,curY+1))
                        && map.isPassable(new Vector(curX,curY+1))){
                    forcedDirections.add(new Vector(0,1));
                }
            }
            if (dirX == 0) {
                if(!map.isPassable(new Vector(curX-1,curY+(-1)*dirY))
                        && map.isPassable(new Vector(curX-1,curY))){
                    forcedDirections.add(new Vector(-1,0));
                }
                if(!map.isPassable(new Vector(curX+1,curY+(-1)*dirY))
                        && map.isPassable(new Vector(curX+1,curY))){
                    forcedDirections.add(new Vector(1,0));
                }
            }
            return forcedDirections;
        }

        @Override
        public Collection<Vector> getSubDirections(Vector direction){
            Collection<Vector> subDirections    = new ArrayList<>();
            if(direction.getX()==0){
                subDirections.add(new Vector(1,0));
                subDirections.add(new Vector(-1,0));
            }
            return subDirections;
        }

    }
}