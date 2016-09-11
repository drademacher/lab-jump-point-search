package core.map.movingrule;

import core.map.MapFacade;
import core.util.Vector;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A MovingRule defines which movements on a grid are allowed an which not.<br>
 * <br>
 * A MovingRule also defines forced- and SubDirections considering the given movement rules.
 *
 * @author Patrick Loka
 * @version 1.0
 * @see MovingRuleStrategy
 * @see AllNeighborMovingRule
 * @see UncutNeighborMovingRule
 * @see OrthogonalNeighborMovingRule
 * @since 1.0
 */
public abstract class MovingRule {

    /**
     * Returns all allowed movement directions.<br>
     * <br>
     * Default Implementation: Returns all orthogonal and diagonal directions in a 2-dimensional space.<br>
     *
     * @return all orthogonal and diagonal directions in a 2-dimensional space.
     * @see OrthogonalNeighborMovingRule
     * @since 1.0
     */
    public Collection<Vector> getAllDirections() {
        Collection<Vector> directions = new ArrayList<>();
        directions.add(new Vector(0, -1));
        directions.add(new Vector(1, -1));
        directions.add(new Vector(1, 0));
        directions.add(new Vector(1, 1));
        directions.add(new Vector(0, 1));
        directions.add(new Vector(-1, 1));
        directions.add(new Vector(-1, 0));
        directions.add(new Vector(-1, -1));
        return directions;
    }

    /**
     * Returns all forced directions based on passed obstacles.<br>
     * <br>
     * A valid implementation returns all forced directions considering the implementation specific definition of a forced point considering the accepted moving rules.<br>
     *
     * @param map          map to apply moving rules.
     * @param currentPoint A passable point on the map.
     * @param direction    The direction vector in {-1,0,1}^2 in which the currentPoint was reached.
     * @return A set of all directions d: currentPoint + d = forced point.
     * @see AllNeighborMovingRule
     * @see UncutNeighborMovingRule
     * @see OrthogonalNeighborMovingRule
     * @since 1.0
     */
    public abstract Collection<Vector> getForcedDirections(MapFacade map, Vector currentPoint, Vector direction);

    /**
     * Returns all subordinated directions of a prioritized input direction considering the accepted moving rules.<br>
     * <br>
     * Default Implementation (diagonal first): If the input is a basis vector, return an empty set.
     * Else return a set of the basis vectors which add up to the input.<br>
     *
     * @param direction a direction vector in {-1,0,1}^2
     * @return A set of all subordinated directions based on the input direction.
     * @see OrthogonalNeighborMovingRule
     * @since 1.0
     */
    public Collection<Vector> getSubordinatedDirections(Vector direction) {
        Collection<Vector> subordinatedDirections = new ArrayList<>();
        if (Math.abs(direction.getX()) + Math.abs(direction.getY()) == 2) {
            subordinatedDirections.add(new Vector(direction.getX(), 0));
            subordinatedDirections.add(new Vector(0, direction.getY()));
        }
        return subordinatedDirections;
    }

    /**
     * Returns true, if a corner is cut without permission by moving in input direction by leaving currentPoint. Otherwise returns false.<br>
     * <br>
     * Default Implementation(Corner Cutting is allowed): Corner is independent of the input never cut.<br>
     *
     * @param map          map to apply moving rules.
     * @param currentPoint A passable point on the map.
     * @param direction    The direction vector in {-1,0,1}^2 in which the currentPoint is leaving.
     * @return false
     * @see UncutNeighborMovingRule
     * @since 1.0
     */
    public boolean isCornerCut(MapFacade map, Vector currentPoint, Vector direction) {
        return false;
    }
}
