package controller.grid;

import controller.direction.Direction;

/**
 * Created by paloka on 25.05.16.
 */
class VisitedPointField extends JumpPointField {

    VisitedPointField(int x, int y, Coordinate predecessor, double pathLength, Direction arrivalDirection) {
        super(x, y, predecessor, pathLength, arrivalDirection);
    }

    @Override
    public FieldType getFieldType() {
        return FieldType.VISITED_POINT;
    }

    @Override
    public boolean isVisited() {
        return true;
    }
}
