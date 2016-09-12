package core.map.shortestpath;

import core.map.MapFacade;
import core.map.movingrule.MovingRule;
import core.util.BoundingBox;
import core.util.Vector;

import java.util.HashMap;

/**
 * AStar implementation of BoundingBoxesShortestPathPruning
 *
 * @author Patrick Loka
 * @version 1.0
 * @since 1.0
 */
class AStarBoundingBoxesShortestPathPruning extends BoundingBoxesShortestPathPruning {
    @Override
    void buildBoundingBoxes(MapFacade map, MovingRule movingRule, Vector currentPoint, HashMap<Vector, BoundingBox> outgoingDirectionBoundingBoxes) {
        movingRule.getAllDirections().stream().filter(incomingDirection -> map.isPassable(currentPoint.sub(incomingDirection))).forEach(incomingDirection -> {
            movingRule.getAllDirections().stream()
                    .filter(outgoingDirection -> Math.abs(incomingDirection.getX() + outgoingDirection.getX()) + Math.abs(incomingDirection.getY() + outgoingDirection.getY()) > 1).forEach(outgoingDirection -> {
                unionBoundingBox(currentPoint, incomingDirection, outgoingDirectionBoundingBoxes.get(outgoingDirection));
            });
        });
    }
}
