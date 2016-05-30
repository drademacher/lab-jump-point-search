package controller.grid;

/**
 * Created by paloka on 27.05.16.
 */
public abstract class PathCoordinate extends Coordinate {

    PathCoordinate(int x, int y) {
        super(x, y);
    }

    public boolean hasShorterPathAs(PathCoordinate coordinate){
        return this.getPathLength()<coordinate.getPathLength();
    }

    public double getPathLength(){
        return Double.POSITIVE_INFINITY;
    };


}
