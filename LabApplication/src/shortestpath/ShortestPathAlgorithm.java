package shortestpath;

import map.MapFacade;
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

    public ShortestPathResult findShortestPath(MapFacade map, int xStart, int yStart, int xGoal, int yGoal){
        Map<Tuple2<Integer,Integer>,Tuple2<Integer,Integer>> pathPredecessors   = new HashMap<>();

        PriorityQueue<Tuple3<Tuple2<Integer,Integer>,Tuple2<Integer,Integer>,Double>> openList = new PriorityQueue<>((p, q) -> {
            if(p.getArg3()>q.getArg3()) return 1;
            return -1;
        });

        openList.add(new Tuple3<>(new Tuple2<>(xStart, yStart),null,.0));
        while(!openList.isEmpty()){
            Tuple3<Tuple2<Integer,Integer>,Tuple2<Integer,Integer>,Double> current    = openList.poll();
            Tuple2<Integer,Integer> currentPoint    = current.getArg1();
            if(pathPredecessors.get(currentPoint)!=null)  continue;
            pathPredecessors.put(currentPoint,current.getArg2());
            if(currentPoint.getArg1()==xGoal&&currentPoint.getArg2()==yGoal){
                return new ShortestPathResult(xStart,yStart,xGoal,yGoal,
                        openList.stream().map(entry -> entry.getArg1()).collect(Collectors.toList()),
                        pathPredecessors);
            }
            openList.addAll(getOpenListCandidates(map,currentPoint).stream().filter(candidate -> pathPredecessors.get(candidate) == null).map(candidate -> new Tuple3<>(candidate, currentPoint, current.getArg3()+1/*Todo: hier kommt die heutristic rein*/)).collect(Collectors.toList()));
        }
        //Todo: throw NoPathFoundException
        throw new NullPointerException("No Path");
    }

    protected abstract Collection<Tuple2<Integer,Integer>> getOpenListCandidates(MapFacade map, Tuple2<Integer,Integer> point);
}