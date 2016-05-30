package controller.map;

/**
 * Created by paloka on 25.05.16.
 */
class GoalPointField extends GridPointField {

    GoalPointField(int x, int y) {
        super(x, y);
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
