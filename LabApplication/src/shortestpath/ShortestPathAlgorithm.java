package shortestpath;

import map.MapFacade;
import shortestpath.heuristic.Heuristic;
import util.Coordinate;
import util.Tuple2;
import util.Tuple3;

import java.util.*;
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
            System.out.println("(" +currentPoint.getX() + "," + currentPoint.getY() + ")");
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


    /* ------- Algorithm Substeps ------- */

    private Collection<Tuple2<Coordinate, Double>> getOpenListCandidates(MapFacade map, Coordinate currentPoint, Coordinate predecessor, Coordinate goal){
        Collection<Coordinate> directions = getDirectionsStrategy(map, currentPoint, predecessor);

        Collection<Tuple2<Coordinate, Double>> candidates = new ArrayList<>();
        for(Coordinate dir:directions) {
            Tuple2<Coordinate, Double> candidate = exploreStrategy(map, currentPoint, dir, 0.0, goal);
            if (candidate != null) candidates.add(candidate);
        }
        return candidates;
    }


    /* ------- Algorithm Strategies ------- */

    protected abstract Collection<Coordinate> getDirectionsStrategy(MapFacade map, Coordinate currentPoint, Coordinate predecessor);
    protected abstract Tuple2<Coordinate, Double> exploreStrategy(MapFacade map, Coordinate currentPoint, Coordinate direction, Double cost, Coordinate goal);


    /* ------- Helper ------- */

    protected Coordinate getDirection(Coordinate currentPoint, Coordinate predecessor) {
        return new Coordinate((int)Math.signum(currentPoint.getX() - predecessor.getX()), (int)Math.signum(currentPoint.getY() - predecessor.getY()));
    }

    protected Collection<Coordinate> getAllDirections(){
        Collection<Coordinate> directions   = new ArrayList<>();
        directions.add(new Coordinate(0,-1));
        directions.add(new Coordinate(1,-1));
        directions.add(new Coordinate(1,0));
        directions.add(new Coordinate(1,1));
        directions.add(new Coordinate(0,1));
        directions.add(new Coordinate(-1,1));
        directions.add(new Coordinate(-1,0));
        directions.add(new Coordinate(-1,-1));
        return directions;
    }

    protected Collection<Coordinate> getForcedDirections(MapFacade map, Coordinate currentPoint, Coordinate direction){ //Todo: Strategy Pattern for CornerCutting Option
        Collection<Coordinate> forcedDirections = new ArrayList<>();
        int dirX    = direction.getX();
        int dirY    = direction.getY();
        if(Math.abs(dirX)+Math.abs(dirY)==2)    return forcedDirections;

        int curX   = currentPoint.getX();
        int curY   = currentPoint.getY();

        if (dirY == 0) {
            if(!map.isPassable(new Coordinate(curX+(-1)*dirX,curY-1))
                    && map.isPassable(new Coordinate(curX,curY-1))) {
                forcedDirections.add(new Coordinate(0,-1));
                forcedDirections.add(new Coordinate(dirX,-1));
            }
            if(!map.isPassable(new Coordinate(curX+(-1)*dirX,curY+1))
                    && map.isPassable(new Coordinate(curX,curY+1))){
                forcedDirections.add(new Coordinate(0,1));
                forcedDirections.add(new Coordinate(dirX,1));
            }
        }
        if (direction.getX() == 0) {
            if(!map.isPassable(new Coordinate(curX-1,curY+(-1)*dirY))
                    && map.isPassable(new Coordinate(curX-1,curY))){
                forcedDirections.add(new Coordinate(-1,0));
                forcedDirections.add(new Coordinate(-1,dirY));
            }
            if(!map.isPassable(new Coordinate(curX+1,curY+(-1)*dirY))
                    && map.isPassable(new Coordinate(curX+1,curY))){
                forcedDirections.add(new Coordinate(1,0));
                forcedDirections.add(new Coordinate(1,dirY));
            }
        }
        return forcedDirections;
    }

    protected Collection<Coordinate> getSubDirections(Coordinate direction){
        Collection<Coordinate> subDirections    = new ArrayList<>();
        if(Math.abs(direction.getX())+Math.abs(direction.getY())==2){
            subDirections.add(new Coordinate(direction.getX(),0));
            subDirections.add(new Coordinate(0,direction.getY()));
        }
        return subDirections;
    }

    protected boolean isCornerCut(MapFacade map, Coordinate currentPoint, Coordinate direction){
        for(Coordinate subDirection:getSubDirections(direction)){
            if(!map.isPassable(currentPoint.add(subDirection)))   return true;
        }
        return false;
    }
}