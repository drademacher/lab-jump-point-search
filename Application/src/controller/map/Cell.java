package controller.map;


import controller.map.Type;

public class Cell {
    private int x;
    private int y;
    private Type type;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.type = Type.OBSTACLE;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;

    }
}
