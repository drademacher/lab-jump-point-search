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
            protected Collection<Tuple2<Tuple2<Integer, Integer>, Double>> getOpenListCandidates(MapFacade map, Tuple2<Integer,Integer> point) {
                int x   = point.getArg1();
                int y   = point.getArg2();
                Collection<Tuple2<Tuple2<Integer, Integer>, Double>> candidates   = new ArrayList<>();
                boolean[][] neighborsPassable   = new boolean[3][3];
                for(int i=-1;i<2;i++){
                    for(int j=-1;j<2;j++){
                        try {
                            neighborsPassable[i+1][j+1] = map.isPassable(x+i,y+j);
                        } catch (InvalidCoordinateException e) {
                            neighborsPassable[i+1][j+1] = false;
                        }
                    }
                }
                if(neighborsPassable[0][1])                                                     candidates.add(new Tuple2<>(new Tuple2<>(x-1, y),1.0));
                if(neighborsPassable[2][1])                                                     candidates.add(new Tuple2<>(new Tuple2<>(x+1, y),1.0));
                if(neighborsPassable[1][0])                                                     candidates.add(new Tuple2<>(new Tuple2<>(x,y-1),1.0));
                if(neighborsPassable[1][2])                                                     candidates.add(new Tuple2<>(new Tuple2<>(x,y+1),1.0));
                if(neighborsPassable[0][0]&&neighborsPassable[1][0]&&neighborsPassable[0][1])   candidates.add(new Tuple2<>(new Tuple2<>(x-1,y-1), Math.sqrt(2.0)));
                if(neighborsPassable[2][2]&&neighborsPassable[1][2]&&neighborsPassable[2][1])   candidates.add(new Tuple2<>(new Tuple2<>(x+1,y+1), Math.sqrt(2.0)));
                if(neighborsPassable[0][2]&&neighborsPassable[1][2]&&neighborsPassable[0][1])   candidates.add(new Tuple2<>(new Tuple2<>(x-1,y+1), Math.sqrt(2.0)));
                if(neighborsPassable[2][0]&&neighborsPassable[1][0]&&neighborsPassable[2][1])   candidates.add(new Tuple2<>(new Tuple2<>(x+1,y-1), Math.sqrt(2.0)));
                return candidates;
            }
        };
    }
}
