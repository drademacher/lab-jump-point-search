package core.map.movingrule;

import core.map.MapFacade;
import core.util.Vector;

import java.util.ArrayList;
import java.util.Collection;

/**
 * OrthogonalNeighborMovingRule implements all MovingRule operations and overrides two operation.<br>
 * <br>
 * OrthogonalNeighborMovingRule implements getForcedDirections(map,currentPoint,direction) of MovingRule by defining the point behind an obstacle, placed diagonal behind to the currentPoint, as a forced point.<br>
 * <br>
 * OrthogonalNeighborMovingRule overrides getAllDirections() by just allowing orthogonal movements.<br>
 * <br>
 * OrthogonalNeighborMovingRule overrides getSubordinatedDirections(direction) by prioritizing the y-movements to the x-movements
 *
 * @author Patrick Loka
 * @version 1.0
 * @see MovingRule
 * @see MovingRuleStrategy
 * @since 1.0
 */
class OrthogonalNeighborMovingRule extends MovingRule{

    /**
     * {@inheritDoc}
     * <br>
     * Implementation: Returns all orthogonal directions in a 2-dimensional space.
     *
     * @return all orthogonal directions in a 2-dimensional space.
     * @since 1.0
     */
    @Override
    public Collection<Vector> getAllDirections() {
        Collection<Vector> directions   = new ArrayList<>();
        directions.add(new Vector(0,-1));
        directions.add(new Vector(1,0));
        directions.add(new Vector(0,1));
        directions.add(new Vector(-1,0));
        return directions;
    }

    /**
     *{@inheritDoc}
     * <br>
     * Implementation: Define every point behind an obstacle, placed diagonal behind to the currentPoint, as a forced point.
     *
     * @param map map to apply moving rules.
     * @param currentPoint A passable point on the map.
     * @param direction The direction vector in {-1,0,1}^2 in which the currentPoint was reached.
     * @return A set of all directions d: currentPoint + d = forced point.
     * @since 1.0
     */
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

    /**
     * {@inheritDoc}
     * <br>
     * Implementation (y-direction first): If the input directions moves on the y-aches, return all x-aches directions.
     * Else return an empty set.
     *
     * @param direction a direction vector in {-1,0,1}^2
     * @return A set of all subordinated directions based on the input direction.
     * @since 1.0
     */
    @Override
    public Collection<Vector> getSubordinatedDirections(Vector direction){
        Collection<Vector> subDirections    = new ArrayList<>();
        if(direction.getX()==0){
            subDirections.add(new Vector(1,0));
            subDirections.add(new Vector(-1,0));
        }
        return subDirections;
    }
}
