package controller.grid;

import controller.direction.Direction;
import controller.heuristic.NoDirectPathException;

/**
 * Created by paloka on 25.05.16.
 */
class ReachedGoalPointField extends JumpPointField {

    ReachedGoalPointField(int x, int y, PathCoordinate predecessor, Direction arrivalDirection) throws NoDirectPathException {
        super(x, y, predecessor, arrivalDirection);
    }

    @Override
    public FieldType getFieldType() {
        return FieldType.GOAL_POINT;
    }

    @Override
    public boolean isGoal() {
        return true;
    }
}
