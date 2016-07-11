package shortestpath.movingRule;

import map.MapFacade;
import util.Coordinate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by paloka on 09.07.16.
 */
public class MovingRuleStrategy {

    private MovingRule movingRule;

    public MovingRule getMovingRule(){
        return this.movingRule;
    }

    //Todo: MovingRuleOrthogonalOnly: Bug!
    public void setMovingRuleOrthogonalOnly(){
        this.movingRule = new MovingRule() {
            @Override
            public Collection<Coordinate> getAllDirections() {
                Collection<Coordinate> directions   = new ArrayList<>();
                directions.add(new Coordinate(0,-1));
                directions.add(new Coordinate(1,0));
                directions.add(new Coordinate(0,1));
                directions.add(new Coordinate(-1,0));
                return directions;
            }

            @Override
            public Collection<Coordinate> getForcedDirections(MapFacade map, Coordinate currentPoint, Coordinate direction) {
                Collection<Coordinate> forcedDirections = new ArrayList<>();
                int dirX    = direction.getX();
                int dirY    = direction.getY();
                int curX    = currentPoint.getX();
                int curY    = currentPoint.getY();

                if (dirY == 0) {
                    if(!map.isPassable(new Coordinate(curX+(-1)*dirX,curY-1))
                            && map.isPassable(new Coordinate(curX,curY-1))) {
                        forcedDirections.add(new Coordinate(0,-1));
                    }
                    if(!map.isPassable(new Coordinate(curX+(-1)*dirX,curY+1))
                            && map.isPassable(new Coordinate(curX,curY+1))){
                        forcedDirections.add(new Coordinate(0,1));
                    }
                }
                if (dirX == 0) {
                    if(!map.isPassable(new Coordinate(curX-1,curY+(-1)*dirY))
                            && map.isPassable(new Coordinate(curX-1,curY))){
                        forcedDirections.add(new Coordinate(-1,0));
                    }
                    if(!map.isPassable(new Coordinate(curX+1,curY+(-1)*dirY))
                            && map.isPassable(new Coordinate(curX+1,curY))){
                        forcedDirections.add(new Coordinate(1,0));
                    }
                }
                return forcedDirections;
            }

            @Override
            public Collection<Coordinate> getSubDirections(Coordinate direction){
                Collection<Coordinate> subDirections    = new ArrayList<>();
                if(direction.getX()==0){
                    subDirections.add(new Coordinate(1,0));
                    subDirections.add(new Coordinate(-1,0));
                }
                return subDirections;
            }
        };
    }

    //Todo: MovingRuleOrthogonalOnly: Bug!
    public void setMovingRuleCornerCutting(){
        this.movingRule = new MovingRule() {
            @Override
            public Collection<Coordinate> getForcedDirections(MapFacade map, Coordinate currentPoint, Coordinate direction) {
                Collection<Coordinate> forcedDirections = new ArrayList<>();
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
                        if(!map.isPassable(new Coordinate(curX+1,curY)) && map.isPassable(new Coordinate(curX+1,curY+dirY))) forcedDirections.add(new Coordinate(1,dirY));
                        if(!map.isPassable(new Coordinate(curX-1,curY)) && map.isPassable(new Coordinate(curX-1,curY+dirY))) forcedDirections.add(new Coordinate(-1,dirY));
                    }
                    if(dirY==0){
                        if(!map.isPassable(new Coordinate(curX,curY+1)) && map.isPassable(new Coordinate(curX+dirX,curY+1))) forcedDirections.add(new Coordinate(dirX,1));
                        if(!map.isPassable(new Coordinate(curX,curY-1)) && map.isPassable(new Coordinate(curX+dirX,curY-1))) forcedDirections.add(new Coordinate(dirX,-1));
                    }
                }
                return forcedDirections;
            }
        };
    }

    public void setMovingRuleNoCornerCutting(){
        this.movingRule = new MovingRule() {
            @Override
            public Collection<Coordinate> getForcedDirections(MapFacade map, Coordinate currentPoint, Coordinate direction) {
                Collection<Coordinate> forcedDirections = new ArrayList<>();
                int dirX    = direction.getX();
                int dirY    = direction.getY();
                if(Math.abs(dirX)+Math.abs(dirY)==2)    return forcedDirections;

                int curX   = currentPoint.getX();
                int curY   = currentPoint.getY();

                if (dirY == 0) {
                    if(!map.isPassable(new Coordinate(curX-dirX,curY-1))
                            && map.isPassable(new Coordinate(curX,curY-1))) {
                        forcedDirections.add(new Coordinate(0,-1));
                        forcedDirections.add(new Coordinate(dirX,-1));
                    }
                    if(!map.isPassable(new Coordinate(curX-dirX,curY+1))
                            && map.isPassable(new Coordinate(curX,curY+1))){
                        forcedDirections.add(new Coordinate(0,1));
                        forcedDirections.add(new Coordinate(dirX,1));
                    }
                }
                if (dirX == 0) {
                    if(!map.isPassable(new Coordinate(curX-1,curY-dirY))
                            && map.isPassable(new Coordinate(curX-1,curY))){
                        forcedDirections.add(new Coordinate(-1,0));
                        forcedDirections.add(new Coordinate(-1,dirY));
                    }
                    if(!map.isPassable(new Coordinate(curX+1,curY-dirY))
                            && map.isPassable(new Coordinate(curX+1,curY))){
                        forcedDirections.add(new Coordinate(1,0));
                        forcedDirections.add(new Coordinate(1,dirY));
                    }
                }
                return forcedDirections;
            }

            @Override
            public boolean isCornerCut(MapFacade map, Coordinate currentPoint, Coordinate direction) {
                for(Coordinate subDirection:getSubDirections(direction)){
                    if(!map.isPassable(currentPoint.add(subDirection)))   return true;
                }
                return false;
            }
        };
    }
}