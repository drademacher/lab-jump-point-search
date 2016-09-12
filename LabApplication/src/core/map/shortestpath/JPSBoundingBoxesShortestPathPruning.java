package core.map.shortestpath;

import core.map.MapFacade;
import core.map.movingrule.MovingRule;
import core.util.BoundingBox;
import core.util.Vector;

import java.util.Collection;
import java.util.HashMap;

/**
 * JPS implementation of BoundingBoxesShortestPathPruning
 *
 * @author Patrick Loka
 * @version 1.0
 * @since 1.0
 */
class JPSBoundingBoxesShortestPathPruning extends BoundingBoxesShortestPathPruning {
    @Override
    void buildBoundingBoxes(MapFacade map, MovingRule movingRule, Vector currentPoint, HashMap<Vector, BoundingBox> outgoingDirectionBoundingBoxes) {
        movingRule.getAllDirections().stream().filter(incomingDirection -> map.isPassable(currentPoint.sub(incomingDirection))).forEach(incomingDirection -> {
            Collection<Vector> outgoingDirections = movingRule.getForcedDirections(map, currentPoint, incomingDirection);
            outgoingDirections.addAll(movingRule.getSubordinatedDirections(incomingDirection));
            outgoingDirections.add(incomingDirection);
            for (Vector outgoingDirection : outgoingDirections) {
                unionBoundingBox(currentPoint, incomingDirection, outgoingDirectionBoundingBoxes.get(outgoingDirection));
            }
        });
    }
}
