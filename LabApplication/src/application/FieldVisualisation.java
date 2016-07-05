package application;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Created by paloka on 01.06.16.
 */
enum FieldVisualisation {
    GOAL_POINT("#D90000"),
    GRID_POINT("#FFFFFF"),
    JUMP_POINT("#595959"),
    START_POINT("#006AD9"),
    OBSTACLE_POINT("#212121"),
    VISITED_POINT("#B3B3B3"),
    PATH_POINT("#2BD942");

    // TODO: use color #D92BD3 for fields of jump points

    private Color color;

    FieldVisualisation(String paint) {
        color = (Color) Paint.valueOf(paint);
    }

    Color getColor() {
        return color;
    }
}
