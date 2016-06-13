package shortestpath;

/**
 * Created by paloka on 10.06.16.
 */
public interface Heuristic {

    double estimateDistance(int x1, int y1, int x2, int y2);
}
