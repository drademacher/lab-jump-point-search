package map;

import exception.InvalidCoordinateException;
import exception.MapInitialisationException;
import util.Coordinate;
import util.DisjointSet;
import util.RandomUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

class MapFactory {
    private final int[][] NEIGHS_ALL = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    Map createEmptyMap(Coordinate dimension) throws MapInitialisationException {
        return new Map(dimension, true);
    }

    Map createRandomMap(Coordinate dimension, double pPassable) throws MapInitialisationException {
        Map map = new Map(dimension, false);
        for (int x = 0; x < map.getXDim(); x++) {
            for (int y = 0; y < map.getYDim(); y++) {
                if (RandomUtil.getRandomDouble() < pPassable) {
                    try {
                        map.switchPassable(new Coordinate(x,y));
                    } catch (InvalidCoordinateException e) {
                        e.printStackTrace();
                        //Todo: MapFactory.createRandomMap - InvalidCoordinateException
                    }
                }
            }
        }
        return map;
    }

    Map createMazeMap(Coordinate dimension) throws MapInitialisationException {
        // init structure
        CellType[][] map = initCellType(dimension.getX(), dimension.getY());

        // gen maze with no dead ends at first
        genFloors(map, CCEmpty());

        return close(map, dimension);
    }

    Map createMazeRoomMap(Coordinate dimension) throws MapInitialisationException {

        // init structure
        CellType[][] map = initCellType(dimension.getX(), dimension.getY());

        // gen rooms
        // TODO: user input for isRoom number upper bound!
        int userInput = 5;
        ArrayList<Integer[]> rooms = genRooms(map, userInput);

        // gen maze with no dead ends at first
        genFloors(map, CCEachRoom(map, rooms));

        return close(map, dimension);
    }

    Map createSingleRoomMap(Coordinate dimension) throws MapInitialisationException {

        // init structure
        CellType[][] map = initCellType(dimension.getX(), dimension.getY());

        // gen rooms
        ArrayList<Integer[]> rooms = genRooms(map);

        // gen maze with no dead ends at first
        genFloors(map, CCEachRoom(map, rooms));
        clearDeadEndFloors(map);

        return close(map, dimension);
    }

    Map createDoubleRoomMap(Coordinate dimension) throws MapInitialisationException {

        // init structure
        CellType[][] map = initCellType(dimension.getX(), dimension.getY());

        // gen rooms
        ArrayList<Integer[]> rooms = genRooms(map);

        // gen maze with no dead ends at first
        genFloors(map, CCEachRoom(map, rooms));
        clearDeadEndFloors(map);

        // add another maze for loops
        genFloors(map, CCEachRoom(map, rooms));

        // remove the dead ends finally
        clearDeadEndFloors(map);

        return close(map, dimension);
    }

    Map createLoopRoomMap(Coordinate dimension) throws MapInitialisationException {

        // init structure
        CellType[][] map = initCellType(dimension.getX(), dimension.getY());

        // gen rooms
        ArrayList<Integer[]> rooms = genRooms(map);

        // gen maze with no dead ends at first
        genFloors(map, CCEachRoom(map, rooms));
        clearDeadEndFloors(map);

        // add another maze for loops
        genFloors(map, CCEachDeadEndRoom(map, rooms));

        // remove the dead ends finally
        clearDeadEndFloors(map);

        return close(map, dimension);
    }


    Map loadMap(File file) throws MapInitialisationException {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            br.readLine();  //Skip type
            int yDim = Integer.valueOf(br.readLine().substring(7));  //Read height
            int xDim = Integer.valueOf(br.readLine().substring(6));  //Read width
            Map map = new Map(new Coordinate(xDim,yDim), false); //init Map without passable fields
            br.readLine();  //Skip map
            String currentLine;
            for (int y = 0; (currentLine = br.readLine()) != null; y++) { //Read in MapRow
                for (int x = 0; x < currentLine.length(); x++) {
                    if (currentLine.charAt(x) == '.' || currentLine.charAt(x) == 'G' || currentLine.charAt(x) == 'S') {
                        map.switchPassable(new Coordinate(x,y));    //Mark passable fields
                    }
                }
            }
            return map;
        } catch (Exception e) {
            throw new MapInitialisationException();
        }
    }


    /* ------- Helper for Room Generation ------- */

    private CellType[][] initCellType(int xDim, int yDim) {
        // design choice! ignore the last row / column if its even in the algorithm
        xDim = xDim - (xDim + 1) % 2;
        yDim = yDim - (yDim + 1) % 2;

        CellType[][] map = new CellType[xDim][yDim];
        for (int x = 0; x < xDim; x++) {
            for (int y = 0; y < yDim; y++) {
                map[x][y] = new CellType();
                map[x][y].setObstacle();
            }
        }

        return map;
    }

    private Map close(CellType[][] map, Coordinate dimension) throws MapInitialisationException {
        Map res = new Map(dimension, false);
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                if (!map[x][y].isObstacle()) try {
                    res.switchPassable(new Coordinate(x, y));
                } catch (InvalidCoordinateException e) {
                    // TODO: no idea
                    e.printStackTrace();
                }
            }
        }
        return res;
    }

    /**
     * internal function which generated a random number of rooms the limit is
     * just an upper bound and its not expected to be reached
     */
    private ArrayList<Integer[]> genRooms(CellType[][] map) {
        return genRooms(map, map.length * map[0].length / 25);
    }

    private ArrayList<Integer[]> genRooms(CellType[][] map, int limit) {
        ArrayList<Integer[]> rooms = new ArrayList<>();

        // declare vars
        int xLen, yLen, xStart, yStart;

        // try to put up a new isRoom in each iteration
        for (int i = 0; i < limit; i++) {
            // make sure to have valid isRoom sizes
            // random num n transforms into 2n+1 -> odd
            do {
                xLen = (int) (4 + 2 * RandomUtil.getRandomGaussian());
                xLen = 2 * xLen + 1;
                yLen = (int) (4 + 2 * RandomUtil.getRandomGaussian());
                yLen = 2 * yLen + 1;
            } while (xLen < 4 || yLen < 4);

            // gen a position in the level
            // increment number if its even -> odd
            xStart = RandomUtil.getRandomInteger(map.length - xLen);
            xStart = xStart + (xStart + 1) % 2;
            yStart = RandomUtil.getRandomInteger(map[0].length - yLen);
            yStart = yStart + (yStart + 1) % 2;

            // check whether the position is valid
            if (!checkRoom(map, xStart, yStart, xLen, yLen)) {
                continue;
            }

            // place isRoom
            for (int x = xStart; x < xStart + xLen; x++) {
                for (int y = yStart; y < yStart + yLen; y++) {
                    map[x][y].setRoom();
                }
            }

            // insert isRoom into memory
            rooms.add(new Integer[]{xStart, yStart, xLen, yLen});
        }

        // return updated builder object
        return rooms;
    }

    /**
     * helper function to check for valid isRoom coordinates
     */
    private boolean checkRoom(CellType[][] map, int xStart, int yStart, int xLen, int yLen) {
        // be sure to only check for odd numbers (xStart, yStart are odd)
        for (int i = 0; i <= xLen; i += 2) {
            for (int j = 0; j <= yLen; j += 2) {
                if (xStart + i < 0 || xStart + i >= map.length - 1 || yStart + j < 0 || yStart + j >= map[0].length - 1) {
                    return false;
                }


                if (!map[xStart + i][yStart + j].isObstacle()) {
                    return false;
                }
            }
        }

        // no collision -> valid placement
        return true;
    }

    /**
     * create a disjoint set with no connected components at all
     */
    private DisjointSet<CellType> CCEmpty() {
        return new DisjointSet<>();
    }

    /**
     * create a disjoint set with a connected component for each isRoom
     */
    private DisjointSet<CellType> CCEachRoom(CellType[][] map, ArrayList<Integer[]> rooms) {
        // create connected compontents
        DisjointSet<CellType> cc = new DisjointSet<>();


        for (Integer[] room : rooms) {
            cc.makeSet(map[room[0]][room[1]]);

            for (int x = room[0]; x < room[0] + room[2]; x += 2) {
                for (int y = room[1]; y < room[1] + room[3]; y += 2) {
                    if (x == room[0] && y == room[1])
                        continue;

                    cc.makeSet(map[x][y]);
                    cc.union(map[room[0]][room[1]], map[x][y]);
                }
            }
        }

        return cc;
    }


    /**
     * create a disjoint set with a connected component for each isRoom
     */
    private DisjointSet<CellType> CCEachDeadEndRoom(CellType[][] map, ArrayList<Integer[]> rooms) {
        // add for rooms which only one floor connection a connected component
        DisjointSet<CellType> cc = new DisjointSet<>();


        for (Integer[] room : rooms) {
            // declare variables
            final int xStart = room[0], xLen = room[2], yStart = room[1],
                    yLen = room[3];

            // check if isRoom only has one floor connection
            int count = 0;
            for (int x = 0; x < xLen; x++) {
                if (map[xStart + x][yStart - 1].isFloor())
                    count++;

                if (map[xStart + x][yStart + yLen].isFloor())
                    count++;
            }

            for (int y = 0; y < yLen; y++) {
                if (map[xStart - 1][yStart + y].isFloor())
                    count++;

                if (map[xStart + xLen][yStart + y].isFloor())
                    count++;
            }

            // skip the isRoom on break condition
            if (count > 1) {
                continue;
            }

            // create a connected component for each isRoom
            cc.makeSet(map[xStart][yStart]);
            for (int x = xStart; x < xStart + xLen; x += 2) {
                for (int y = yStart; y < yStart + yLen; y += 2) {
                    if (x == xStart && y == yStart)
                        continue;

                    cc.makeSet(map[x][y]);
                    cc.union(map[xStart][yStart], map[x][y]);
                }
            }
        }

        return cc;
    }

    /**
     * internal function to generate a maze around the rooms
     * <p>
     * this is done by a FloodFill algorithm instead of some over engineering
     * with MST
     */
    private void genFloors(CellType[][] map, DisjointSet<CellType> cc) {
        ArrayList<int[]> q = new ArrayList<>();

        for (int i = 1; i < map.length; i += 2) {
            for (int j = 1; j < map[0].length; j += 2) {
                // fill in fixed cells on odd / odd coordinates
                if (map[i][j].isObstacle()) {
                    map[i][j].setFloor();
                    cc.makeSet(map[i][j]);
                }

                // queue neighbours when one corner is not a isRoom
                if (i + 2 < map.length && !map[i + 1][j].isRoom())
                    q.add(new int[]{i, j, i + 2, j});
                if (j + 2 < map[0].length && !map[i][j + 1].isRoom())
                    q.add(new int[]{i, j, i, j + 2});
            }
        }

        // choose connector in a random order
        Collections.shuffle(q, RandomUtil.getRANDOM());

        for (int[] e : q) {
            // rename array
            final int x1 = e[0], y1 = e[1], x2 = e[2], y2 = e[3];

            if (cc.findSet(map[x1][y1]) == null)
                continue;

            if (cc.findSet(map[x2][y2]) == null)
                continue;

            // check if two cells are already connected
            if (cc.findSet(map[x1][y1]) == cc.findSet(map[x2][y2]))
                continue;

            // merge two components by adding a connector
            cc.union(map[x1][y1], map[x2][y2]);
            map[(x1 + x2) / 2][(y1 + y2) / 2].setFloor();
        }
    }

    /**
     * internal function which deletes all dead ends of a maze
     */
    private void clearDeadEndFloors(CellType[][] map) {
        int count;
        boolean repeat = true, deadend[][];

        while (repeat) {
            // fresh init for single execution of elimination
            deadend = new boolean[map.length][map[0].length];
            repeat = false;

            // dead end iff 3 neighbours are walls
            for (int x = 0; x < map.length; x++) {
                for (int y = 0; y < map[0].length; y++) {
                    if (map[x][y].isObstacle()) {
                        continue;
                    }

                    count = 0;
                    for (int j = 0; j < 4; j++) {
                        if (map[x + NEIGHS_ALL[j][0]][y + NEIGHS_ALL[j][1]].isObstacle())
                            count++;
                    }

                    if (count >= 3) {
                        deadend[x][y] = true;
                        repeat = true;
                    }
                }
            }

            // remove dead ends
            for (int x = 0; x < map.length; x++) {
                for (int y = 0; y < map[0].length; y++) {
                    if (deadend[x][y]) {
                        map[x][y].setObstacle();
                    }
                }
            }
        }
    }

    class CellType {
        private int val;

        void setRoom() {
            val = 1;
        }

        void setFloor() {
            val = 2;
        }

        void setObstacle() {
            val = -1;
        }

        boolean isRoom() {
            return val == 1;
        }

        boolean isFloor() {
            return val == 2;
        }

        boolean isObstacle() {
            return val == -1;
        }
    }
}
