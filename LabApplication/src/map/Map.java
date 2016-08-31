package map;

import exception.InvalidCoordinateException;
import exception.MapInitialisationException;
import util.Vector;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by paloka on 01.06.16.
 */
class Map implements MapFacade {

    private final boolean[][] map;
    private int xDim;
    private int yDim;

    Map(Vector dimension, boolean areFieldsPassable) throws MapInitialisationException {
        this.xDim = dimension.getX();
        this.yDim = dimension.getY();
        if (this.xDim < 1 || this.yDim < 1) throw new MapInitialisationException(dimension);
        this.map = new boolean[this.xDim][this.yDim];
        if (areFieldsPassable) {
            for (boolean[] col : this.map) Arrays.fill(col, true);
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
    public boolean isPassable(Vector coordinate){
        try {
            isValideCoordinate(coordinate);
            return map[coordinate.getX()][coordinate.getY()];
        } catch (InvalidCoordinateException e) {
            return false;
        }
    }

    void switchPassable(Vector coordinate) throws InvalidCoordinateException {
        isValideCoordinate(coordinate);
        map[coordinate.getX()][coordinate.getY()] = !map[coordinate.getX()][coordinate.getY()];
    }


    /* ------- Persistence ------- */

    void save(File file){
        ArrayList<String> lines = new ArrayList<>();
        lines.addAll(Arrays.asList("type octile", "height " + this.yDim, "width " + this.xDim, "map"));
        for (int y = 0; y < this.yDim; y++) {
            String line = "";
            for (int x = 0; x < this.xDim; x++) line = line + (this.map[x][y] ? "." : "T");
            lines.add(line);
        }
        try {
            Files.write(file.toPath(), lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            //Todo: Map.save - IOException
            e.printStackTrace();
        }
    }


    /* ------- Helper ------- */

    private void isValideCoordinate(Vector coordinate) throws InvalidCoordinateException {
        if (coordinate.getX() < 0 || coordinate.getY() < 0 || coordinate.getX() >= this.xDim || coordinate.getY() >= this.yDim) throw new InvalidCoordinateException(coordinate);
    }
}
