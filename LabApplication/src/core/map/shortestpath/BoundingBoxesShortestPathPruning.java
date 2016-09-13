package core.map.shortestpath;

import core.map.MapFacade;
import core.map.movingrule.MovingRule;
import core.util.BoundingBox;
import core.util.Tuple2;
import core.util.Tuple3;
import core.util.Vector;

import java.util.*;

/**
 * BoundingBoxesShortestPathPruning uses BoundingBoxes to decide whether certain points can be on a shortest path to a given goal or not.<br>
 * <br>
 * BoundingBoxesShortestPathPruning implements doPreprocessing by building BoundingBoxes for each passable point with each possible
 * incoming direction on a grid map containing every reachable point.<br>
 * <br>
 * BoundingBoxesShortestPathPruning implements prune by checking whether the goalpoint is inside the asked BoundingBox or outside
 *
 * @author Patrick Loka
 * @version 1.0
 * @since 1.0
 */
abstract class BoundingBoxesShortestPathPruning implements ShortestPathPruning {
    private HashMap<Vector, HashMap<Vector, BoundingBox>> boundingBoxes = new HashMap<>();

    /**
     * Union another Bounding Box to the stored one. If no Bounding Box is stored jet, set the new Bounding Box as stored Bounding Box.
     *
     * @param currentPoint key point
     * @param direction key direction
     * @param newBB Bounding Box, which should be stored or added with params currentPoint and direction.
     * @since 1.0
     */
    protected void unionBoundingBox(Vector currentPoint, Vector direction, BoundingBox newBB) {
        if (newBB == null) return;
        this.boundingBoxes.putIfAbsent(currentPoint, new HashMap<>());
        BoundingBox oldBB = this.boundingBoxes.get(currentPoint).putIfAbsent(direction, new BoundingBox(newBB));
        if (oldBB != null) this.boundingBoxes.get(currentPoint).get(direction).union(newBB);
    }

    /**
     * {@inheritDoc}
     * <br>
     * Implementation: checks whether the stored bounding box containing to the given direction and point contains the goalpoint, else prune.
     *
     * @param candidate key point.
     * @param direction key direction.
     * @param goal Goalpoint of the searched shortest path.
     * @return False, if the stored bounding box containing to the given direction and point contains the goal point, else true.
     * @since 1.0
     */
    @Override
    public boolean prune(Vector candidate, Vector direction, Vector goal) {
        if (this.boundingBoxes.get(candidate) == null || this.boundingBoxes.get(candidate).get(direction) == null) {
            //Todo: System.out.println("candidate: "+candidate+"\t direction: "+direction+"\t No BoundingBox found");
            return false;
        }
        //Todo: System.out.println("\t candidate: "+candidate+"\t direction: "+direction+"\t BoundingBox: "+this.boundingBoxes.get(candidate).get(direction)+"\t"+this.boundingBoxes.get(candidate).get(direction).isInBoundingBox(goal));
        return !this.boundingBoxes.get(candidate).get(direction).isInBoundingBox(goal);
    }

    /**
     * {@inheritDoc}
     * <br>
     * Implementation: Calculates Bounding Boxes for each direction and point containing all reachable points.
     *
     * @param map Given grid map to preprocess.
     * @param movingRule Allowed movements on the given grid map.
     * @since 1.0
     */
    @Override
    public void doPreprocessing(MapFacade map, MovingRule movingRule) {
        this.boundingBoxes = new HashMap<>();
        for (int x = 0; x < map.getXDim(); x++) {
            for (int y = 0; y < map.getYDim(); y++) {
                Vector currentPoint = new Vector(x, y);
                if (map.isPassable(currentPoint)) {
                    double[][] reachedPoints = new double[map.getXDim()][map.getYDim()];
                    HashMap<Vector, BoundingBox> outgoingDirectionBoundingBoxes = new HashMap<>();
                    PriorityQueue<Tuple2<Tuple3<Vector, Vector, Double>, BoundingBox>> priorityQueue = new PriorityQueue<>((p, q) -> {
                        if (p.getArg1().getArg3() > q.getArg1().getArg3()) return 1;
                        return -1;
                    });
                    for (Vector direction : movingRule.getAllDirections()) {
                        BoundingBox boundingBox = new BoundingBox(currentPoint);
                        Vector candidate = currentPoint.add(direction);
                        outgoingDirectionBoundingBoxes.put(direction, boundingBox);
                        if (map.isPassable(candidate))
                            priorityQueue.add(new Tuple2<>(new Tuple3<>(candidate, direction, Math.sqrt(Math.abs(direction.getX()) + Math.abs(direction.getY()))), boundingBox));
                    }

                    while (!priorityQueue.isEmpty()) {
                        Tuple2<Tuple3<Vector, Vector, Double>, BoundingBox> nextEntry = priorityQueue.poll();
                        final Vector nextVector     = nextEntry.getArg1().getArg1();
                        final Vector nextDirection  = nextEntry.getArg1().getArg2();
                        final double nextCost       = nextEntry.getArg1().getArg3();
                        final BoundingBox nextBoundingBox = nextEntry.getArg2();
                        if (-nextCost>=reachedPoints[nextVector.getX()][nextVector.getY()]) {
                            reachedPoints[nextVector.getX()][nextVector.getY()] = -nextCost;
                            nextBoundingBox.add(nextVector);
                            priorityQueue.addAll(explore(map, nextVector, nextDirection, nextCost, nextBoundingBox, movingRule));
                        }
                    }

                    buildBoundingBoxes(map, movingRule, currentPoint, outgoingDirectionBoundingBoxes);
                }
            }
        }
    }


    /* ------- Helper ------- */

    private Collection<Tuple2<Tuple3<Vector, Vector, Double>, BoundingBox>> explore(MapFacade map, Vector currentPoint, Vector direction, double cost, BoundingBox bb, MovingRule movingRule) {
        List<Tuple2<Tuple3<Vector, Vector, Double>, BoundingBox>> reachablePoints = new ArrayList<>();

        for (Vector forcedDirection : movingRule.getForcedDirections(map, currentPoint, direction)) {
            Vector candidate = currentPoint.add(forcedDirection);
            if (map.isPassable(candidate) && !movingRule.isCornerCut(map, currentPoint, forcedDirection))
                reachablePoints.add(new Tuple2<>(new Tuple3(candidate, forcedDirection, cost + Math.sqrt(Math.abs(forcedDirection.getX()) + Math.abs(forcedDirection.getY()))), bb));
        }

        for (Vector subordinatedDirection : movingRule.getSubordinatedDirections(direction)) {
            Vector candidate = currentPoint.add(subordinatedDirection);
            if (map.isPassable(candidate) && !movingRule.isCornerCut(map, currentPoint, subordinatedDirection))
                reachablePoints.add(new Tuple2<>(new Tuple3(candidate, subordinatedDirection, cost + Math.sqrt(Math.abs(subordinatedDirection.getX()) + Math.abs(subordinatedDirection.getY()))), bb));
        }

        Vector candidate = currentPoint.add(direction);
        if (map.isPassable(candidate) && !movingRule.isCornerCut(map, currentPoint, direction))
            reachablePoints.add(new Tuple2<>(new Tuple3(candidate, direction, cost + Math.sqrt(Math.abs(direction.getX()) + Math.abs(direction.getY()))), bb));

        return reachablePoints;
    }

    /**
     * Builds one Bounding Box based on the incoming direction based on the moving rules and the bounding boxes of the outgoing directions considering different exploring strategies.
     *
     * @param map given grid map.
     * @param movingRule moving rules for given grid map.
     * @param currentPoint current processed point on the given grid map.
     * @param outgoingDirectionBoundingBoxes One Bounding Box for each outgoing direction from the current point containing all points
     *                                       which are reachable fastest by running in the according outgoing direction.
     * @since 1.0
     */
    abstract void buildBoundingBoxes(MapFacade map, MovingRule movingRule, Vector currentPoint, HashMap<Vector, BoundingBox> outgoingDirectionBoundingBoxes);
}
