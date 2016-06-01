package model.map;

import exception.MapInitialisationException;

/**
 * Created by paloka on 01.06.16.
 */
public class MapFactory {

    public static Map createEmptyMap(int xDim, int yDim) throws MapInitialisationException {
        return new Map(xDim,yDim,true);
    }
}
