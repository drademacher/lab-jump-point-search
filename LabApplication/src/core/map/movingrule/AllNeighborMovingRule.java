package core.map.movingrule;

import core.map.MapFacade;
import core.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * AllNeighborMovingRule implements all MovingRule operations.<br>
 * <br>
 * AllNeighborMovingRule implements getForcedDirections(map,currentPoint,direction) of MovingRule by defining every point behind an obstacle, placed aside to the currentPoint, as a forced point.<br>
 * <br>
 * All orthogonal and diagonal movements are allowed as long as the reached point is passable.
 *
 * @author Patrick Loka
 * @version 1.0
 * @see MovingRule
 * @see MovingRuleStrategy
 * @since 1.0
 */
class AllNeighborMovingRule extends MovingRule {

    /**
     * {@inheritDoc}
     * <br>
     * Implementation: Define every point behind an obstacle, placed aside to the currentPoint, as a forced point.
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
        int curX = currentPoint.getX();
        int curY = currentPoint.getY();

        if (Math.abs(dirX) + Math.abs(dirY) == 2) {
            forcedDirections.addAll(getSubordinatedDirections(direction).stream()
                    .filter(subDir -> !map.isPassable(currentPoint.sub(subDir)) && map.isPassable(currentPoint.add(direction).sub(subDir).sub(subDir)))
                    .map(subDir -> direction.sub(subDir).sub(subDir))
                    .collect(Collectors.toList()));
        } else {
            if (dirX == 0) {
                if (!map.isPassable(new Vector(curX + 1, curY)) && map.isPassable(new Vector(curX + 1, curY + dirY)))
                    forcedDirections.add(new Vector(1, dirY));
                if (!map.isPassable(new Vector(curX - 1, curY)) && map.isPassable(new Vector(curX - 1, curY + dirY)))
                    forcedDirections.add(new Vector(-1, dirY));
            }
            if (dirY == 0) {
                if (!map.isPassable(new Vector(curX, curY + 1)) && map.isPassable(new Vector(curX + dirX, curY + 1)))
                    forcedDirections.add(new Vector(dirX, 1));
                if (!map.isPassable(new Vector(curX, curY - 1)) && map.isPassable(new Vector(curX + dirX, curY - 1)))
                    forcedDirections.add(new Vector(dirX, -1));
            }
        }
        return forcedDirections;
    }
}
