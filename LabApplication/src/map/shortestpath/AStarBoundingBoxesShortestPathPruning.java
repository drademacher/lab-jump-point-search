package map.shortestpath;

import map.MapFacade;
import map.movingrule.MovingRule;
import util.BoundingBox;
import util.Vector;

import java.util.HashMap;

/**
 * Created by paloka on 07.09.16.
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
