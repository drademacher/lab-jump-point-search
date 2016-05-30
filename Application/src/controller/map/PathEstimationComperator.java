package controller.map;

import controller.heuristic.GridHeuristic;

import java.util.Comparator;

/**
 * Created by paloka on 25.05.16.
 */
public class PathEstimationComperator implements Comparator<PathCoordinate> {

    Coordinate goalPoint;
    GridHeuristic gridHeuristic;

    public PathEstimationComperator(Coordinate goalPoint, GridHeuristic gridHeuristic) {
        this.goalPoint  = goalPoint;
        this.gridHeuristic = gridHeuristic;
    }

    @Override
    public int compare(PathCoordinate c1, PathCoordinate c2) {
        if(c1.getPathLength()+ gridHeuristic.estimate(c1,goalPoint)<c2.getPathLength()+ gridHeuristic.estimate(c2,goalPoint)) return 1;
        return -1;
    }
}
