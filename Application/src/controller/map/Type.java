package controller.map;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public enum Type {
    FLOOR(Paint.valueOf("#E8E8E8")),
    ROOM (Paint.valueOf("#D9D9D9")),
    OBSTACLE (Paint.valueOf("#7D7D7D")),
    ERROR (Paint.valueOf("#FF0000"));

    private Color color;

    Type(Paint paint) {
        color = (Color) paint;
    }

    public Color getColor() {
        return color;
    }
}
