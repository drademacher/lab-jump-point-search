package application;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Created by paloka on 01.06.16.
 */
enum FieldVisualisation {
    GRID_POINT("#FFFFFF"),
    OBSTACLE_POINT("#999999"),

    VISITED_POINT("#D9F0FF"),
    JUMP_POINT("#2298E6"),

    GOAL_POINT("#A12D3C"),
    START_POINT("#A12D3C"),
    PATH_POINT("#A12D3C");

    // TODO: use color #D92BD3 for fields of jump points

    private Color color;

    FieldVisualisation(String paint) {
        color = (Color) Paint.valueOf(paint);
    }

    Color getColor() {
        return color;
    }
}
