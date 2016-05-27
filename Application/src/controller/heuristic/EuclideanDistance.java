package controller.heuristic;

/**
 * Created by paloka on 27.05.16.
 */
public class EuclideanDistance extends GridHeuristic{

    @Override
    public double estimate(int x1, int y1, int x2, int y2) {
        return Math.sqrt(square(x1-x2)+square(y1-y2));
    }

    private double square(int x){
        return x*x;
    }
}
