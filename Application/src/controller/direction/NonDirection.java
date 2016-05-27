package controller.direction;

import controller.grid.Coordinate;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by paloka on 27.05.16.
 */
public class NonDirection extends Direction {

    @Override
    public DirectionType getDirectionType() {
        return DirectionType.NON;
    }

    @Override
    public List<Coordinate> getForcedNeighbors(int x, int y){
        List<Coordinate> neighbors = new LinkedList<>();
        neighbors.add(new Coordinate(x-1,y-1));
        neighbors.add(new Coordinate(x-1,y));
        neighbors.add(new Coordinate(x-1,y+1));
        neighbors.add(new Coordinate(x,y-1));
        neighbors.add(new Coordinate(x,y+1));
        neighbors.add(new Coordinate(x+1,y-1));
        neighbors.add(new Coordinate(x+1,y));
        neighbors.add(new Coordinate(x+1,y+1));
        return neighbors;
    }

    @Override
    public List<Coordinate> getNeighborsInSubdirections(int x, int y) throws NoSpecificDirectionException {
        throw new NoSpecificDirectionException();
    }

    @Override
    public Coordinate getNeighborInDirection(int x, int y) throws NoSpecificDirectionException {
        throw new NoSpecificDirectionException();
    }
}
