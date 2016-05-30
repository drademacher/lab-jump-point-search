package controller.grid;

/**
 * Created by paloka on 25.05.16.
 */

public class NotAFieldException extends Exception {
    NotAFieldException(int x, int y){
        super("Field ("+x+","+y+") does not exist in grid");
    }
}
