package grid;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public enum Type {
    NODE (Paint.valueOf("#C0C0C0")),
    OBSTACLE (Paint.valueOf("#4D4D4D")),
    ERROR (Paint.valueOf("#FF0000"));

    private Color color;

    Type(Paint paint) {
        color = (Color) paint;
    }

    public Color getColor() {
        return color;
    }
}
