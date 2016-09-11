package core.map.movingrule;

import core.map.MapFacade;
import core.util.Vector;

import java.util.ArrayList;
import java.util.Collection;

/**
 * UncutNeighborMovingRule implements all MovingRule operations and overrides one operation.<br>
 * <br>
 * UncutNeighborMovingRule implements getForcedDirections(map,currentPoint,direction) of MovingRule by defining the two points behind an obstacle, placed diagonal behind to the currentPoint, as a forced points.<br>
 * <br>
 * UncutNeighborMovingRule overrides isCornerCut(map,currentPoint,direction) by forbidding Corner Cutting as an accepted movement on the map.<br>
 * <br>
 * All orthogonal movements are allowed as long as the reached point is passable.
 * All diagonal movements are allowed as long as the reached point is passable and no obstacle is cut.
 *
 * @author Patrick Loka
 * @version 1.0
 * @see MovingRule
 * @see MovingRuleStrategy
 * @since 1.0
 */
class UncutNeighborMovingRule extends MovingRule {

    /**
     * {@inheritDoc}
     * <br>
     * Implementation: Define every two points behind an obstacle, placed diagonal behind to the currentPoint, as a forced points.
     *
     * @param map          map to apply moving rules.
     * @param currentPoint A passable point on the map.
     * @param direction    The direction vector in {-1,0,1}^2 in which the currentPoint was reached.
     * @return A set of all directions d: currentPoint + d = forced point.
     * @since 1.0
     */
    @Override
    public Collection<Vector> getForcedDirections(MapFacade map, Vector currentPoint, Vector direction) {
        Collection<Vector> forcedDirections = new ArrayList<>();
        int dirX = direction.getX();
        int dirY = direction.getY();
        if (Math.abs(dirX) + Math.abs(dirY) == 2) return forcedDirections;

        int curX = currentPoint.getX();
        int curY = currentPoint.getY();

        if (dirY == 0) {
            if (!map.isPassable(new Vector(curX - dirX, curY - 1))
                    && map.isPassable(new Vector(curX, curY - 1))) {
                forcedDirections.add(new Vector(0, -1));
                forcedDirections.add(new Vector(dirX, -1));
            }
            if (!map.isPassable(new Vector(curX - dirX, curY + 1))
                    && map.isPassable(new Vector(curX, curY + 1))) {
                forcedDirections.add(new Vector(0, 1));
                forcedDirections.add(new Vector(dirX, 1));
            }
        }
        if (dirX == 0) {
            if (!map.isPassable(new Vector(curX - 1, curY - dirY))
                    && map.isPassable(new Vector(curX - 1, curY))) {
                forcedDirections.add(new Vector(-1, 0));
                forcedDirections.add(new Vector(-1, dirY));
            }
            if (!map.isPassable(new Vector(curX + 1, curY - dirY))
                    && map.isPassable(new Vector(curX + 1, curY))) {
                forcedDirections.add(new Vector(1, 0));
                forcedDirections.add(new Vector(1, dirY));
            }
        }
        return forcedDirections;
    }

    /**
     * {@inheritDoc}
     * <br>
     * Implementation(Corner Cutting is forbidden): A Corner is cut, iff there is an obstacle in any subordinatedDirection.
     *
     * @param map          map to apply moving rules.
     * @param currentPoint A passable point on the map.
     * @param direction    The direction vector in {-1,0,1}^2 in which the currentPoint is leaving.
     * @return True, if a corner is cut by moving in input direction by leaving currentPoint. Otherwise return false.
     * @since 1.0
     */
    @Override
    public boolean isCornerCut(MapFacade map, Vector currentPoint, Vector direction) {
        for (Vector subordinatedDirection : getSubordinatedDirections(direction)) {
            if (!map.isPassable(currentPoint.add(subordinatedDirection))) return true;
        }
        return false;
    }
}
