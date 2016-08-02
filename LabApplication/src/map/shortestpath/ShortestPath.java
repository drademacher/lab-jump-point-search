package map.shortestpath;

import map.MapFacade;
import map.heuristic.Heuristic;
import map.movingRule.MovingRule;
import util.Coordinate;
import util.Tuple2;
import util.Tuple3;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by paloka on 06.06.16.
 */
public abstract class ShortestPath implements AbstractExploreStrategy {

    /* ------- Preprocessing ------- */

    protected void doPreprocessing(MapFacade map, MovingRule movingRule){}

    protected boolean prune(Coordinate candidate, Coordinate direction, Coordinate goal){
        return false;
    }


    /* ------- ShortestPath ------- */

    protected ShortestPathResult findShortestPath(MapFacade map, Coordinate start, Coordinate goal, Heuristic heuristic, MovingRule movingRule) {
        System.out.println("start: "+start+"\t goal: "+goal);

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
            openList.addAll(getOpenListCandidates(map, currentPoint, currentPredecessor, goal, movingRule).stream().
                    filter(candidate -> pathPredecessors.get(candidate.getArg1()) == null).
                    map(candidate -> new Tuple3<>(candidate.getArg1(), currentPoint, new Tuple2<>(pathDistance + candidate.getArg2(),heuristic.estimateDistance(candidate.getArg1(),goal)))).
                    collect(Collectors.toList()));
        }
        //Todo: throw NoPathFoundException
        throw new NullPointerException("No Path");
    }


    /* ------- Algorithm Substeps ------- */

    private Collection<Tuple2<Coordinate, Double>> getOpenListCandidates(MapFacade map, Coordinate currentPoint, Coordinate predecessor, Coordinate goal, MovingRule movingRule){
        Collection<Coordinate> directions = getDirectionsStrategy(map, currentPoint, predecessor, movingRule);

        Collection<Tuple2<Coordinate, Double>> candidates = new ArrayList<>();
        for(Coordinate dir:directions) {
            Tuple2<Coordinate, Double> candidate = exploreStrategy(map, currentPoint, dir, 0.0, goal, movingRule);
            if(candidate!=null) System.out.print("current: "+currentPoint);
            if (candidate != null && !prune(candidate.getArg1(),dir,goal)) candidates.add(candidate);
        }
        return candidates;
    }
}