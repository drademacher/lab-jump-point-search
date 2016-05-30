package controller.map;

import controller.direction.Direction;
import controller.direction.NonDirection;
import javafx.scene.paint.Color;

/**
 * Created by paloka on 24.05.16.
 */
public abstract class Field extends PathCoordinate {

    private int x;
    private int y;

    Field(int x, int y) {
        super(x,y);
    }

    public abstract FieldType getFieldType();
    public abstract boolean isPassable();

    public boolean isVisited(){
        return false;
    };

    public boolean isGoal(){
        return false;
    };

    public Coordinate getPredecessorOnPath() throws NoPathException {
        throw new NoPathException(getX(),getY());
    }

    public Direction getArrivalDirection(){
        return new NonDirection();
    }

    public Color getColor(){
        return getFieldType().getColor();
    }
}
