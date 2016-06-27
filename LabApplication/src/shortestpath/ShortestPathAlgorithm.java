package shortestpath;

import map.MapFacade;
import util.Coordinate;
import util.Tuple2;
import util.Tuple3;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

/**
 * Created by paloka on 06.06.16.
 */
public abstract class ShortestPathAlgorithm {

    public ShortestPathResult findShortestPath(MapFacade map, Coordinate start, Coordinate goal, Heuristic heuristic) {
        Map<Coordinate, Coordinate> pathPredecessors = new HashMap<>();

        PriorityQueue<Tuple3<Coordinate, Coordinate, Tuple2<Double,Double>>> openList = new PriorityQueue<>((p, q) -> {
            if (p.getArg3().getArg1() + p.getArg3().getArg2() > q.getArg3().getArg1() + q.getArg3().getArg2()) return 1;
            return -1;
        });

        openList.add(new Tuple3<>(start, null, new Tuple2<>(.0,heuristic.estimateDistance(start,goal))));
        while (!openList.isEmpty()) {
            Tuple3<Coordinate, Coordinate, Tuple2<Double, Double>> currentPath = openList.poll();
            Coordinate currentPoint         = currentPath.getArg1();
            Coordinate currentPredecessor   = currentPath.getArg2();
            double pathDistance             = currentPath.getArg3().getArg1();
            if (pathPredecessors.get(currentPoint) != null)     continue;
            pathPredecessors.put(currentPoint, currentPredecessor);
            if (currentPoint.equals(goal)) {
                return new ShortestPathResult(start, goal,
                        openList.stream().map(entry -> entry.getArg1()).collect(Collectors.toList()),
                        pathPredecessors);
            }
            openList.addAll(getOpenListCandidates(map, currentPoint, currentPredecessor, goal).stream().
                    filter(candidate -> pathPredecessors.get(candidate.getArg1()) == null).
                    map(candidate -> new Tuple3<>(candidate.getArg1(), currentPoint, new Tuple2<>(pathDistance + candidate.getArg2(),heuristic.estimateDistance(candidate.getArg1(),goal)))).
                    collect(Collectors.toList()));
        }
        //Todo: throw NoPathFoundException
        throw new NullPointerException("No Path");
    }

    protected abstract Collection<Tuple2<Coordinate, Double>> getOpenListCandidates(MapFacade map, Coordinate currentPoint, Coordinate predecessor, Coordinate goal);
}