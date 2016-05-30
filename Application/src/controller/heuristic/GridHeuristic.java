package controller.heuristic;

import controller.grid.Coordinate;

/**
 * Created by paloka on 27.05.16.
 */
public abstract class GridHeuristic {

    public double estimate(Coordinate c1, Coordinate c2){
        return estimate(c1.getX(),c1.getY(),c2.getX(),c2.getY());
    };

    public abstract double estimate(int x1, int y1, int x2, int y2);
}
