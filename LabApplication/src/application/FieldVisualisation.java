package application;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Created by paloka on 01.06.16.
 */
enum FieldVisualisation {
    GOAL_POINT("#DE2C2C"),
    GRID_POINT("#E8E8E8"),
    JUMP_POINT("#90C3D4"),
    START_POINT("#2CDE38"),
    OBSTACLE_POINT("#454545"),
    VISITED_POINT("#BABABA"),
    PATH_POINT("#DEDE2C");

    private Color color;

    FieldVisualisation(String paint) {
        color = (Color) Paint.valueOf(paint);
    }

    Color getColor() {
        return color;
    }
}
