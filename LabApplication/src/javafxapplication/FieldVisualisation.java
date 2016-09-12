package javafxapplication;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Enum type with additional color information for rendering the grid map.
 *
 * @author Danny Rademacher
 * @version 1.0
 * @since 1.0
 */
enum FieldVisualisation {
    GRID_POINT("#FFFFFF"),
    OBSTACLE_POINT("#A1A1A1"),

    CLOSED_LIST("#D9F0FF"),
    OPEN_LIST("#2298E6"),

    GOAL_POINT("#730B19"),
    START_POINT("#730B19"),
    PATH_POINT("#A12D3C");

    private Color color;

    FieldVisualisation(String paint) {
        color = (Color) Paint.valueOf(paint);
    }

    Color getColor() {
        return color;
    }
}
