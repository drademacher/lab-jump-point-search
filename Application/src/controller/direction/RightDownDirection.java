package controller.direction;

import controller.grid.Coordinate;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by paloka on 27.05.16.
 */
public class RightDownDirection extends Direction {

    @Override
    public DirectionType getDirectionType() {
        return DirectionType.RIGHT_DOWN;
    }

    @Override
    public List<Coordinate> getForcedNeighbors(int x, int y) {
        List<Coordinate> neighbors  = new LinkedList<>();
        //Todo: find out forced neighbors
        return neighbors;
    }

    @Override
    public List<Coordinate> getNeighborsInSubdirections(int x, int y) {
        List<Coordinate> neighbors  = new LinkedList<>();
        neighbors.add(new Coordinate(x+1,y));
        neighbors.add(new Coordinate(x,y-1));
        return null;
    }

    @Override
    public Coordinate getNeighborInDirection(int x, int y) {
        return new Coordinate(x+1,y-1);
    }
}
