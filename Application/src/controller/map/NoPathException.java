package controller.map;

/**
 * Created by paloka on 25.05.16.
 */

public class NoPathException extends Exception {
    NoPathException(int x, int y){
        super("No Path was calculated for field: ("+x+","+y+")");
    }
}
