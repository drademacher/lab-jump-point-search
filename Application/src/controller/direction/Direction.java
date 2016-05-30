package controller.direction;

import controller.map.Coordinate;

import java.util.List;

/**
 * Created by paloka on 27.05.16.
 */
public abstract class Direction {

    public abstract DirectionType getDirectionType();
    public abstract List<Coordinate> getForcedNeighbors(int x, int y);
    public abstract List<Coordinate> getNeighborsInSubdirections(int x, int y) throws NoSpecificDirectionException;
    public abstract Coordinate getNeighborInDirection(int x, int y) throws NoSpecificDirectionException;
}
