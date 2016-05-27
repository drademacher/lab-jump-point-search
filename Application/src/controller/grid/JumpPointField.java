package controller.grid;

import controller.direction.Direction;
import controller.heuristic.GridDistance;
import controller.heuristic.NoDirectPathException;

/**
 * Created by paloka on 25.05.16.
 */
class JumpPointField extends GridPointField{

    protected Coordinate predecessor;
    protected double pathLength;
    protected Direction arrivalDirection;

    JumpPointField(int x, int y, PathCoordinate predecessor, Direction arrivalDirection) throws NoDirectPathException {
        this(x, y, predecessor, predecessor.getPathLength()+GridDistance.calculateDirectPath(x,y,predecessor.getX(),predecessor.getY()), arrivalDirection);
    }

    JumpPointField(int x, int y, Coordinate predecessor, double pathLength, Direction arrivalDirection) {
        super(x, y);
        this.predecessor        = predecessor;
        this.pathLength         = pathLength;
        this.arrivalDirection   = arrivalDirection;
    }

    @Override
    public FieldType getFieldType() {
        return FieldType.JUMP_POINT;
    }

    @Override
    public Coordinate getPredecessorOnPath() {
        return predecessor;
    }

    @Override
    public double getPathLength() {
        return pathLength;
    }

    @Override
    public Direction getArrivalDirection(){
        return arrivalDirection;
    }
}
