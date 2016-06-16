package shortestpath;

import exception.InvalidCoordinateException;
import map.MapFacade;
import util.Coordinate;
import util.Tuple2;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by paloka on 08.06.16.
 */
public class ShortestPathAlgorithmFactory {

    public ShortestPathAlgorithm createAStar() {
        return new ShortestPathAlgorithm() {
            @Override
            protected Collection<Tuple2<Coordinate, Double>> getOpenListCandidates(MapFacade map, Coordinate currentPoint) {
                int x = currentPoint.getX();
                int y = currentPoint.getY();

                boolean[][] neighborsPassable = getNeighborsPassable(map, x, y);
                Collection<Tuple2<Coordinate, Double>> candidates = makeAStarCandidates(x, y, neighborsPassable);
                return candidates;
            }
        };
    }

    public ShortestPathAlgorithm createJPS() {
        return new ShortestPathAlgorithm() {
            @Override
            protected Collection<Tuple2<Coordinate, Double>> getOpenListCandidates(MapFacade map, Coordinate currentPoint) {
                int x = currentPoint.getX();
                int y = currentPoint.getY();

                boolean[][] neighborsPassable = getNeighborsPassable(map, x, y);
                Collection<Tuple2<Coordinate, Double>> candidates = makeJPSCandidates(x, y, neighborsPassable);
                return candidates;
            }
        };
    }

    private boolean[][] getNeighborsPassable(MapFacade map, int x, int y) {
        boolean[][] neighborsPassable = new boolean[3][3];
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                try {
                    neighborsPassable[i + 1][j + 1] = map.isPassable(new Coordinate(x + i, y + j));
                } catch (InvalidCoordinateException e) {
                    neighborsPassable[i + 1][j + 1] = false;
                }
            }
        }
        return neighborsPassable;
    }

    private Collection<Tuple2<Coordinate, Double>> makeAStarCandidates(int x, int y, boolean[][] neighborsPassable) {
        Collection<Tuple2<Coordinate, Double>> candidates = new ArrayList<>();
        if (neighborsPassable[0][1]) candidates.add(new Tuple2<>(new Coordinate(x - 1, y), 1.0));
        if (neighborsPassable[2][1]) candidates.add(new Tuple2<>(new Coordinate(x + 1, y), 1.0));
        if (neighborsPassable[1][0]) candidates.add(new Tuple2<>(new Coordinate(x, y - 1), 1.0));
        if (neighborsPassable[1][2]) candidates.add(new Tuple2<>(new Coordinate(x, y + 1), 1.0));
        if (neighborsPassable[0][0] && neighborsPassable[1][0] && neighborsPassable[0][1])
            candidates.add(new Tuple2<>(new Coordinate(x - 1, y - 1), Math.sqrt(2.0)));
        if (neighborsPassable[2][2] && neighborsPassable[1][2] && neighborsPassable[2][1])
            candidates.add(new Tuple2<>(new Coordinate(x + 1, y + 1), Math.sqrt(2.0)));
        if (neighborsPassable[0][2] && neighborsPassable[1][2] && neighborsPassable[0][1])
            candidates.add(new Tuple2<>(new Coordinate(x - 1, y + 1), Math.sqrt(2.0)));
        if (neighborsPassable[2][0] && neighborsPassable[1][0] && neighborsPassable[2][1])
            candidates.add(new Tuple2<>(new Coordinate(x + 1, y - 1), Math.sqrt(2.0)));
        return candidates;
    }


    private Collection<Tuple2<Coordinate, Double>> makeJPSCandidates(int x, int y, boolean[][] neighborsPassable) {
        Collection<Tuple2<Coordinate, Double>> candidates = new ArrayList<>();
        // TODO: make candidates here
        return candidates;
    }
}
