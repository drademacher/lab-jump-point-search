package map;

import exception.InvalideCoordinateException;
import exception.MapInitialisationException;
import util.RandomUtil;

/**
 * Created by paloka on 01.06.16.
 */
class MapFactory {

    Map createEmptyMap(int xDim, int yDim) throws MapInitialisationException {
        return new Map(xDim,yDim,true);
    }

    Map createRandomMap(int xDim, int yDim, double pPassable) throws MapInitialisationException {
        Map map = new Map(xDim,yDim,false);
        for (int x = 0; x < xDim; x++) {
            for (int y = 0; y < yDim; y++) {
                //Todo: implement random singelton to obmit global
                if (RandomUtil.getRandomDouble() < pPassable) {
                    try {
                        map.switchPassable(x, y);
                    } catch (InvalideCoordinateException e) {
                        e.printStackTrace();
                        //Todo: MapFactory.createRandomMap - InvalideCoordinateException
                    }
                }
            }
        }
        return map;
    }
}
