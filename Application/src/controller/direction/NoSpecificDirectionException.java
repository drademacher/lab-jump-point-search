package controller.direction;

/**
 * Created by paloka on 27.05.16.
 */
public class NoSpecificDirectionException extends Exception {
    NoSpecificDirectionException(){
        super("No specific Direction given");
    }
}
