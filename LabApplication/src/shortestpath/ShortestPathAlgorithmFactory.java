package shortestpath;

import exception.InvalidCoordinateException;
import map.MapFacade;
import util.Coordinate;
import util.MathUtil;
import util.Tuple2;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by paloka on 08.06.16.
 */
public class ShortestPathAlgorithmFactory {

    /* ------- ShortestPathAlgorithmFactory ------- */

    public ShortestPathAlgorithm createAStar() {
        return new ShortestPathAlgorithm() {
            @Override
            protected Collection<Tuple2<Coordinate, Double>> getOpenListCandidates(MapFacade map, Coordinate currentPoint, Coordinate predecessor, Coordinate goal) {
                return makeAStarCandidates(currentPoint , getNeighborsPassable(map, currentPoint));
            }
        };
    }

    public ShortestPathAlgorithm createJPS() {
        return new ShortestPathAlgorithm() {
            @Override
            protected Collection<Tuple2<Coordinate, Double>> getOpenListCandidates(MapFacade map, Coordinate currentPoint, Coordinate predecessor, Coordinate goal) {
                return makeJPSCandidates(map, currentPoint, predecessor, goal);
            }
        };
    }


    /* ------- AStar ------- */

    private Collection<Tuple2<Coordinate, Double>> makeAStarCandidates(Coordinate currentPoint, boolean[][] neighborsPassable) {
        int x = currentPoint.getX();
        int y = currentPoint.getY();

        Collection<Tuple2<Coordinate, Double>> candidates = new ArrayList<>();
        if (neighborsPassable[0][1]) candidates.add(new Tuple2<>(new Coordinate(x - 1, y), 1.0));
        if (neighborsPassable[2][1]) candidates.add(new Tuple2<>(new Coordinate(x + 1, y), 1.0));
        if (neighborsPassable[1][0]) candidates.add(new Tuple2<>(new Coordinate(x, y - 1), 1.0));
        if (neighborsPassable[1][2]) candidates.add(new Tuple2<>(new Coordinate(x, y + 1), 1.0));
        if (neighborsPassable[0][0] && neighborsPassable[1][0] && neighborsPassable[0][1])
            candidates.add(new Tuple2<>(new Coordinate(x - 1, y - 1), MathUtil.SQRT2));
        if (neighborsPassable[2][2] && neighborsPassable[1][2] && neighborsPassable[2][1])
            candidates.add(new Tuple2<>(new Coordinate(x + 1, y + 1), MathUtil.SQRT2));
        if (neighborsPassable[0][2] && neighborsPassable[1][2] && neighborsPassable[0][1])
            candidates.add(new Tuple2<>(new Coordinate(x - 1, y + 1), MathUtil.SQRT2));
        if (neighborsPassable[2][0] && neighborsPassable[1][0] && neighborsPassable[2][1])
            candidates.add(new Tuple2<>(new Coordinate(x + 1, y - 1), MathUtil.SQRT2));

        return candidates;
    }


    /* ------- JPS ------- */

    private Collection<Tuple2<Coordinate, Double>> makeJPSCandidates(MapFacade map, Coordinate currentPoint, Coordinate predecessor, Coordinate goal) {
        Collection<Tuple2<Coordinate, Double>> candidates = new ArrayList<>();
        Collection<Coordinate> directions = new ArrayList<>();
        if(predecessor!=null){
            Coordinate direction = getDirection(currentPoint, predecessor);
            directions.add(direction);
            int val = Math.abs(direction.getX()) + Math.abs(direction.getY());
            if (val == 1) {
                boolean[][] neighborsPassable   = getNeighborsPassable(map, currentPoint);
                if (direction.getY() == 0) {
                    if (!neighborsPassable[(-1) * direction.getX() + 1][0]) {
                        directions.add(new Coordinate(0,-1));
                        directions.add(new Coordinate(direction.getX(),-1));
                    }
                    if (!neighborsPassable[(-1) * direction.getX() + 1][2]) {
                        directions.add(new Coordinate(0,1));
                        directions.add(new Coordinate(direction.getX(),1));
                    }
                }
                if (direction.getX() == 0) {
                    if (!neighborsPassable[0][(-1) * direction.getY() + 1]){
                        directions.add(new Coordinate(-1,0));
                        directions.add(new Coordinate(-1,direction.getY()));
                    }
                    if (!neighborsPassable[2][(-1) * direction.getY() + 1]){
                        directions.add(new Coordinate(1,0));
                        directions.add(new Coordinate(1,direction.getY()));
                    }
                }
            }else{
                directions.add(new Coordinate(direction.getX(), 0));
                directions.add(new Coordinate(0, direction.getY()));
            }
        }else{
            directions.add(new Coordinate(0,-1));
            directions.add(new Coordinate(1,-1));
            directions.add(new Coordinate(1,0));
            directions.add(new Coordinate(1,1));
            directions.add(new Coordinate(0,1));
            directions.add(new Coordinate(-1,1));
            directions.add(new Coordinate(-1,0));
            directions.add(new Coordinate(-1,-1));
        }

        for(Coordinate dir:directions) {
            Tuple2<Coordinate, Double> candidate = exploreDirection(map, currentPoint, 0.0, dir, goal);
            if (candidate != null) candidates.add(candidate);
        }

        return candidates;
    }

    private Tuple2<Coordinate, Double> exploreDirection(MapFacade map, Coordinate currentPoint, Double cost, Coordinate direction, Coordinate goal) {
        Coordinate next = currentPoint.add(direction);
        int val = Math.abs(direction.getX()) + Math.abs(direction.getY());
        cost += Math.sqrt(val);
        boolean[][] neighborsPassable = getNeighborsPassable(map, currentPoint);

        if (!neighborsPassable[1][1]) {
            return null;
        }

        if (val==2 && (!neighborsPassable[-1*direction.getX()+1][1] || !neighborsPassable[1][-1*direction.getY()+1]))   return null;

        if(next.equals(goal)) return new Tuple2<>(next,cost);

        if (val == 1) {
            if (direction.getY() == 0) {
                if (!neighborsPassable[(-1) * direction.getX() + 1][0] && neighborsPassable[1][0]) {
                    return new Tuple2<>(next, cost);
                }
                if (!neighborsPassable[(-1) * direction.getX() + 1][2] && neighborsPassable[1][2]) {
                    return new Tuple2<>(next, cost);
                }
            }
            if (direction.getX() == 0) {
                if (!neighborsPassable[0][(-1) * direction.getY() + 1] && neighborsPassable[0][1])
                    return new Tuple2<>(next, cost);
                if (!neighborsPassable[2][(-1) * direction.getY() + 1] && neighborsPassable[2][1])
                    return new Tuple2<>(next, cost);
            }
        } else {
            if(exploreDirection(map, next, cost, new Coordinate(direction.getX(),0), goal) != null)     return new Tuple2<>(next,cost);
            if(exploreDirection(map, next, cost, new Coordinate(0, direction.getY()), goal) != null)    return new Tuple2<>(next,cost);
        }

        return exploreDirection(map, next, cost, direction, goal);
    }


    /* ------- Helper ------- */

    private boolean[][] getNeighborsPassable(MapFacade map, Coordinate currentPoint) {
        int x = currentPoint.getX();
        int y = currentPoint.getY();

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

    private Coordinate getDirection(Coordinate currentPoint, Coordinate predecessor) {
        return new Coordinate((int)Math.signum(currentPoint.getX() - predecessor.getX()), (int)Math.signum(currentPoint.getY() - predecessor.getY()));
    }
}
