package controller.heuristic;

/**
 * Created by paloka on 27.05.16.
 */
public class NoDirectPathException extends Exception {

    NoDirectPathException(int x1, int x2, int y1, int y2){
        super("There is no direct path between ("+x1+","+y1+") and ("+x2+","+y2+")");
    }
}
