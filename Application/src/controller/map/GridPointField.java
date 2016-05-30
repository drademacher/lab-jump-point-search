package controller.map;

/**
 * Created by paloka on 25.05.16.
 */
class GridPointField extends Field {

    GridPointField(int x, int y) {
        super(x, y);
    }

    @Override
    public FieldType getFieldType() {
        return FieldType.GRID_POINT;
    }

    @Override
    public boolean isPassable() {
        return true;
    }
}
