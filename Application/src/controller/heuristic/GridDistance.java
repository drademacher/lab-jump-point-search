package controller.heuristic;

/**
 * Created by paloka on 27.05.16.
 */
public class GridDistance extends GridHeuristic {

    private static double SQRT2 = Math.sqrt(2);

    @Override
    public double estimate(int x1, int y1, int x2, int y2) {
        int deltaX  = absoluteDifference(x1,x2);
        int deltaY  = absoluteDifference(y1,y2);
        return absoluteDifference(deltaX,deltaY)+SQRT2*(deltaX<deltaY?deltaX:deltaY);
    }

    public static double calculateDirectPath(int x1, int y1, int x2, int y2) throws NoDirectPathException {
        if(x1==x2)  return absoluteDifference(y1,y2);
        if(y1==y2)  return absoluteDifference(x1,x2);
        int deltaX  = absoluteDifference(x1,x2);
        int deltaY  = absoluteDifference(y1,y2);
        if(deltaX-deltaY==0)  return SQRT2*deltaX;
        throw new NoDirectPathException(x1,y1,x2,y2);
    }

    private static int absoluteDifference(int x, int y){
        return Math.abs(x-y);
    }
}
