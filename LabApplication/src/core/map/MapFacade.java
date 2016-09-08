package core.map;

import core.util.Vector;

/**
 * Created by paloka on 02.06.16.
 */
public interface MapFacade {

    int getXDim();

    int getYDim();

    boolean isPassable(Vector vector);
}
