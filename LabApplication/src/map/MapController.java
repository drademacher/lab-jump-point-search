package map;

import exception.InvalideCoordinateException;
import exception.MapInitialisationException;

public class MapController {

    private Map map;
    private MapFactory mapFactory = new MapFactory();


    /* ------- Factory ------- */

    public MapFacade setEmptyMap(int xDim, int yDim) throws MapInitialisationException {
        this.map    = mapFactory.createEmptyMap(xDim,yDim);
        return this.map;
    }

    public MapFacade setRandomMap(int xDim, int yDim, double pPassable) throws MapInitialisationException {
        this.map    = mapFactory.createRandomMap(xDim,yDim,pPassable);
        return this.map;
    }


    /* ------- Map Operations ------- */

    public MapFacade switchPassable(int x, int y) throws InvalideCoordinateException {
        this.map.switchPassable(x,y);
        return this.map;
    }
}
