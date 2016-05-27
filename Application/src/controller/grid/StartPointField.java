package controller.grid;

/**
 * Created by paloka on 25.05.16.
 */
class StartPointField extends GridPointField {

    StartPointField(int x, int y) {
        super(x, y);
    }

    @Override
    public FieldType getFieldType() {
        return FieldType.START_POINT;
    }

    @Override
    public boolean isVisited() {
        return true;
    }

    @Override
    public double getPathLength() {
        return 0;
    }
}
