package map.shortestpath;

import map.MapFacade;
import map.movingrule.MovingRule;
import util.BoundingBox;
import util.Tuple2;
import util.Tuple3;
import util.Vector;

import java.util.*;

/**
 * Created by paloka on 01.08.16.
 */
abstract class BoundingBoxesShortestPathPruning implements ShortestPathPruning {
    private HashMap<Vector, HashMap<Vector, BoundingBox>> boundingBoxes  = new HashMap<>();

    protected void unionBoundingBox(Vector currentPoint, Vector direction, BoundingBox newBB){
        if(newBB==null) return;
        this.boundingBoxes.putIfAbsent(currentPoint,new HashMap<>());
        BoundingBox oldBB = this.boundingBoxes.get(currentPoint).putIfAbsent(direction,new BoundingBox(newBB));
        if(oldBB!=null) this.boundingBoxes.get(currentPoint).get(direction).union(newBB);
    }

    @Override
    public boolean prune(Vector candidate, Vector direction, Vector goal){
        if(this.boundingBoxes.get(candidate)==null || this.boundingBoxes.get(candidate).get(direction)==null){
            System.out.println("candidate: "+candidate+"\t direction: "+direction+"\t No BoundingBox found");
            return false;
        }
        System.out.println("\t candidate: "+candidate+"\t direction: "+direction+"\t BoundingBox: "+this.boundingBoxes.get(candidate).get(direction)+"\t"+this.boundingBoxes.get(candidate).get(direction).isInBoundingBox(goal));
        return !this.boundingBoxes.get(candidate).get(direction).isInBoundingBox(goal);
    }

    @Override
    public void doPreprocessing(MapFacade map, MovingRule movingRule) {
        this.boundingBoxes  = new HashMap<>();
        for (int x = 0; x < map.getXDim(); x++) {
            for (int y = 0; y < map.getYDim(); y++) {
                Vector currentPoint = new Vector(x, y);
                if (map.isPassable(currentPoint)) {
                    boolean[][] reachedPoints = new boolean[map.getXDim()][map.getYDim()];
                    reachedPoints[currentPoint.getX()][currentPoint.getY()] = true;
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
                        final Vector nextVector = nextEntry.getArg1().getArg1();
                        final Vector nextDirection = nextEntry.getArg1().getArg2();
                        final BoundingBox nextBoundingBox = nextEntry.getArg2();
                        // TODO: Punkte mit gleicher Entfernung relevant?
                        if (!reachedPoints[nextVector.getX()][nextVector.getY()]) {
                            reachedPoints[nextVector.getX()][nextVector.getY()] = true;
                            nextBoundingBox.add(nextVector);
                            priorityQueue.addAll(explore(map, nextVector, nextDirection, nextEntry.getArg1().getArg3(), nextBoundingBox, movingRule));
                        }
                    }

                    buildBoundingBoxes(map,movingRule,currentPoint,outgoingDirectionBoundingBoxes);
                }
            }
        }
    }


    /* ------- Helper ------- */

    private Collection<Tuple2<Tuple3<Vector, Vector, Double>, BoundingBox>> explore(MapFacade map, Vector currentPoint, Vector direction, double cost, BoundingBox bb, MovingRule movingRule) {
        List<Tuple2<Tuple3<Vector, Vector, Double>, BoundingBox>> reachablePoints = new ArrayList<>();

        for (Vector forcedDirection : movingRule.getForcedDirections(map, currentPoint, direction)) {
            Vector candidate = currentPoint.add(forcedDirection);
            if (map.isPassable(candidate) && !movingRule.isCornerCut(map,currentPoint,forcedDirection))
                reachablePoints.add(new Tuple2<>(new Tuple3(candidate, forcedDirection, cost + Math.sqrt(Math.abs(forcedDirection.getX()) + Math.abs(forcedDirection.getY()))), bb));
        }

        for (Vector subordinatedDirection : movingRule.getSubordinatedDirections(direction)) {
            Vector candidate = currentPoint.add(subordinatedDirection);
            if (map.isPassable(candidate) && !movingRule.isCornerCut(map,currentPoint,subordinatedDirection))
                reachablePoints.add(new Tuple2<>(new Tuple3(candidate, subordinatedDirection, cost + Math.sqrt(Math.abs(subordinatedDirection.getX()) + Math.abs(subordinatedDirection.getY()))), bb));
        }

        Vector candidate = currentPoint.add(direction);
        if (map.isPassable(candidate) && !movingRule.isCornerCut(map,currentPoint,direction))
            reachablePoints.add(new Tuple2<>(new Tuple3(candidate, direction, cost + Math.sqrt(Math.abs(direction.getX()) + Math.abs(direction.getY()))), bb));

        return reachablePoints;
    }

    abstract void buildBoundingBoxes(MapFacade map, MovingRule movingRule, Vector currentPoint, HashMap<Vector, BoundingBox> outgoingDirectionBoundingBoxes);
}
