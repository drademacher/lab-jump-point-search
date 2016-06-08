package shortestpath;

import exception.InvalidCoordinateException;
import map.MapFacade;
import util.Tuple2;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by paloka on 08.06.16.
 */
public class ShortestPathAlgorithmFactory {

    public ShortestPathAlgorithm createAStar(){
        return new ShortestPathAlgorithm() {
            @Override
            protected Collection<Tuple2<Integer, Integer>> getOpenListCandidates(MapFacade map, Tuple2<Integer,Integer> point) {
                Collection<Tuple2<Integer,Integer>> neighbors   = new ArrayList<>();
                try {
                    if(map.isPassable(point.getArg1(),point.getArg2()+1)) neighbors.add(new Tuple2<>(point.getArg1(),point.getArg2()+1));
                } catch (InvalidCoordinateException e) {
                    e.printStackTrace();
                }
                try {
                    if(map.isPassable(point.getArg1(),point.getArg2()-1)) neighbors.add(new Tuple2<>(point.getArg1(),point.getArg2()-1));
                } catch (InvalidCoordinateException e) {
                    e.printStackTrace();
                }
                try {
                    if(map.isPassable(point.getArg1()+1,point.getArg2())) neighbors.add(new Tuple2<>(point.getArg1()+1,point.getArg2()));
                } catch (InvalidCoordinateException e) {
                    e.printStackTrace();
                }
                try {
                    if(map.isPassable(point.getArg1()-1,point.getArg2())) neighbors.add(new Tuple2<>(point.getArg1()-1,point.getArg2()));
                } catch (InvalidCoordinateException e) {
                    e.printStackTrace();
                }
                return neighbors;
            }
        };
    }
}
