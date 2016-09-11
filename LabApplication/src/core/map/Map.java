package core.map;

import core.exception.InvalidCoordinateException;
import core.exception.MapInitialisationException;
import core.util.Vector;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The Map implements all MapFacade operations.<br>
 * <br>
 * Map holds a 2 dimensional boolean array with the information whether a point [x][y] is passable or not.
 * Map holds xDim,yDim as the dimension of this array.<br>
 * <br>
 * Map implements getXDim,getYDim by returning the xDim,yDim attribute.<br>
 * <br>
 * Map implements isPassable(coordinate) by looking up whether the array field with the same x,y values as the coordinate is true or false.
 *
 * @author Patrick Loka
 * @version 1.0
 * @see MapFacade
 * @see MapController
 * @see MapFactory
 * @since 1.0
 */
class Map implements MapFacade {

    private final boolean[][] map;
    private int xDim;
    private int yDim;

    /**
     * Initialisation of a Map instance of a given dimension and a default value.
     *
     * @param dimension         Dimension of the new Map
     * @param areFieldsPassable Initial value for all points on the map.
     * @throws MapInitialisationException Thrown if the input dimension has negative values.
     * @see MapFactory
     * @since 1.0
     */
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

    /**
     * {@inheritDoc}
     *
     * @return xDim
     * @since 1.0
     */
    @Override
    public int getXDim() {
        return xDim;
    }

    /**
     * {@inheritDoc}
     *
     * @return yDim
     * @since 1.0
     */
    @Override
    public int getYDim() {
        return yDim;
    }

    /**
     * {@inheritDoc}
     * <br>
     * Implementation: Looks up the value of coordinate in map array and returns value.
     * Returns false if coordinate is no valid input for map array.
     *
     * @param coordinate point to check whether it is allowed to pass or not
     * @return value of coordinate in map array or false.
     * @since 1.0
     */
    @Override
    public boolean isPassable(Vector coordinate) {
        try {
            isValideCoordinate(coordinate);
            return map[coordinate.getX()][coordinate.getY()];
        } catch (InvalidCoordinateException e) {
            return false;
        }
    }

    /**
     * Switches the passable state in map array of a given point.
     * After running this method, a given passable field becomes unpassable and an given unpassable field becomes passable.
     * If coordinate is no valid input for map array, an InvalidCoordinateException is thrown.
     *
     * @param coordinate Point where passable state should be switched.
     * @throws InvalidCoordinateException Thrown if coordinate is no valid input for map array.
     * @see MapController
     * @see MapFacade
     * @since 1.0
     */
    void switchPassable(Vector coordinate) throws InvalidCoordinateException {
        isValideCoordinate(coordinate);
        map[coordinate.getX()][coordinate.getY()] = !map[coordinate.getX()][coordinate.getY()];
    }


    /* ------- Persistence ------- */

    /**
     * Saves the current state of the Map instance as .map file.
     *
     * @param file filename of the file the map should be saved. To generate a valid file it should be type .map.
     * @since 1.0
     */
    void save(File file) {
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
        if (coordinate.getX() < 0 || coordinate.getY() < 0 || coordinate.getX() >= this.xDim || coordinate.getY() >= this.yDim)
            throw new InvalidCoordinateException(coordinate);
    }
}
