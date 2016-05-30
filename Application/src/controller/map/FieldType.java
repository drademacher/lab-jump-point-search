package controller.map;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Created by paloka on 25.05.16.
 */
public enum FieldType {
    GOAL_POINT("#D4A190"),
    GRID_POINT("#E8E8E8"),
    JUMP_POINT("#90C3D4"),
    START_POINT("#A1D490"),
    OBSTACLE_POINT("#454545"),
    VISITED_POINT("#BABABA");

    private Color color;

    FieldType(String paint) {
        color = (Color) Paint.valueOf(paint);
    }

    public Color getColor() {
        return color;
    }
}
