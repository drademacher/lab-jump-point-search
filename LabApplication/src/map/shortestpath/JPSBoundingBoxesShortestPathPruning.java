package map.shortestpath;

import map.MapFacade;
import map.movingrule.MovingRule;
import util.BoundingBox;
import util.Vector;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by paloka on 07.09.16.
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
