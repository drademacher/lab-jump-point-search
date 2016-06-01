package exception;

/**
 * Created by paloka on 01.06.16.
 */
public class InvalideCoordinateException extends Exception{
    public InvalideCoordinateException(int x, int y){
        super("Field is not part of the map: ("+x+","+y+")");
    }
}
