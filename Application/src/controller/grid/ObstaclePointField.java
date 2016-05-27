package controller.grid;

/**
 * Created by paloka on 27.05.16.
 */
public class ObstaclePointField extends Field {

    ObstaclePointField(int x, int y) {
        super(x, y);
    }

    @Override
    public FieldType getFieldType() {
        return FieldType.OBSTACLE_POINT;
    }

    @Override
    public boolean isPassable() {
        return false;
    }
}
