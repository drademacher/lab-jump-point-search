package map;

import exception.InvalideCoordinateException;
import exception.MapInitialisationException;

import java.util.Arrays;

/**
 * Created by paloka on 01.06.16.
 */
class Map implements MapFacade {

    private final boolean[][] map;
    private int xDim;
    private int yDim;

    Map(int xDim, int yDim, boolean areFieldsPassable) throws MapInitialisationException {
        if(xDim<1||yDim<1) throw new MapInitialisationException(xDim,yDim);
        this.xDim   = xDim;
        this.yDim   = yDim;
        this.map    = new boolean[xDim][yDim];
        if(areFieldsPassable){
            for(boolean[] col:map)  Arrays.fill(col,true);
        }
    }


    /* ------- Getter & Setter ------- */

    @Override
    public int getXDim() {
        return xDim;
    }

    @Override
    public int getYDim() {
        return yDim;
    }

    @Override
    public boolean isPassable(int x, int y) throws InvalideCoordinateException {
        isValideCoordinate(x,y);
        return map[x][y];
    }

    void switchPassable(int x, int y) throws InvalideCoordinateException {
        isValideCoordinate(x,y);
        map[x][y]   = !map[x][y];
    }


    /* ------- Helper ------- */

    private void isValideCoordinate(int x, int y) throws InvalideCoordinateException {
        if(x<0||y<0||x>=xDim||y>=yDim) throw new InvalideCoordinateException(x,y);
    }
}
