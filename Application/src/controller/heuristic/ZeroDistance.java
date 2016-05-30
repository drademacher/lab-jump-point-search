package controller.heuristic;

/**
 * Created by paloka on 27.05.16.
 */
public class ZeroDistance extends GridHeuristic {

    @Override
    public double estimate(int x1, int y1, int x2, int y2) {
        return 0;
    }
}
