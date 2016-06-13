package map;

import exception.InvalidCoordinateException;
import exception.MapInitialisationException;
import util.RandomUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by paloka on 01.06.16.
 */
class MapFactory {
    private final int[][] NEIGHS_ALL = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    // TODO: remove those members
    private int[][] rooms;
    private CeellType[][] map;


    Map createEmptyMap(int xDim, int yDim) throws MapInitialisationException {
        return new Map(xDim, yDim, true);
    }

    Map createRandomMap(int xDim, int yDim, double pPassable) throws MapInitialisationException {
        Map map = new Map(xDim, yDim, false);
        for (int x = 0; x < xDim; x++) {
            for (int y = 0; y < yDim; y++) {
                if (RandomUtil.getRandomDouble() < pPassable) {
                    try {
                        map.switchPassable(x, y);
                    } catch (InvalidCoordinateException e) {
                        e.printStackTrace();
                        //Todo: MapFactory.createRandomMap - InvalidCoordinateException
                    }
                }
            }
        }
        return map;
    }

    public Map createMazeMap(int xDim, int yDim) throws MapInitialisationException {
        // init structure
        initCellType(xDim, yDim);

        // design choice! ignore the last row / column if its even in the algorithm
        xDim = xDim - (xDim + 1) % 2;
        yDim = yDim - (yDim + 1) % 2;

        // gen maze with no dead ends at first
        genFloors(xDim, yDim, CCEmpty());

        return close(xDim, yDim);
    }

    public Map createMazeRoomMap(int xDim, int yDim) throws MapInitialisationException {
        // init structure
        initCellType(xDim, yDim);

        // design choice! ignore the last row / column if its even in the algorithm
        xDim = xDim - (xDim + 1) % 2;
        yDim = yDim - (yDim + 1) % 2;

        // gen rooms
        // TODO: user input for isRoom number upper bound!
        int roomNum = genRooms(xDim, yDim, 5);

        // gen maze with no dead ends at first
        genFloors(xDim, yDim, CCEachRoom(roomNum));

        return close(xDim, yDim);
    }

    public Map createSingleRoomMap(int xDim, int yDim) throws MapInitialisationException {
        // init structure
        initCellType(xDim, yDim);

        // design choice! ignore the last row / column if its even in the algorithm
        xDim = xDim - (xDim + 1) % 2;
        yDim = yDim - (yDim + 1) % 2;

        // gen rooms
        int roomNum = genRooms(xDim, yDim);

        // gen maze with no dead ends at first
        genFloors(xDim, yDim, CCEachRoom(roomNum));
        clearDeadEndFloors(xDim, yDim);

        return close(xDim, yDim);
    }

    public Map createDoubleRoomMap(int xDim, int yDim) throws MapInitialisationException {
        // init structure
        initCellType(xDim, yDim);

        // design choice! ignore the last row / column if its even in the algorithm
        xDim = xDim - (xDim + 1) % 2;
        yDim = yDim - (yDim + 1) % 2;

        // gen rooms
        int roomNum = genRooms(xDim, yDim);

        // gen maze with no dead ends at first
        genFloors(xDim, yDim, CCEachRoom(roomNum));
        clearDeadEndFloors(xDim, yDim);

        // add another maze for loops
        genFloors(xDim, yDim, CCEachRoom(roomNum));

        // remove the dead ends finally
        clearDeadEndFloors(xDim, yDim);

        return close(xDim, yDim);
    }

    public Map createLoopRoomMap(int xDim, int yDim) throws MapInitialisationException {
        // init structure
        initCellType(xDim, yDim);

        // design choice! ignore the last row / column if its even in the algorithm
        xDim = xDim - (xDim + 1) % 2;
        yDim = yDim - (yDim + 1) % 2;

        // gen rooms
        int roomNum = genRooms(xDim, yDim);

        // gen maze with no dead ends at first
        genFloors(xDim, yDim, CCEachRoom(roomNum));
        clearDeadEndFloors(xDim, yDim);

        // add another maze for loops
        genFloors(xDim, yDim, CCEachDeadEndRoom(roomNum));

        // remove the dead ends finally
        clearDeadEndFloors(xDim, yDim);

        return close(xDim, yDim);
    }


    private void initCellType(int xDim, int yDim)  {
        map = new CeellType[xDim][yDim];
        for (int x = 0; x < xDim; x++) {
            for (int y = 0; y < yDim; y++) {
                map[x][y] = new CeellType();
                map[x][y].setObstacle();
            }
        }
    }

    private Map close(int xDim, int yDim) throws MapInitialisationException {
        Map res = new Map(xDim, yDim, true);
        for (int x = 0; x < xDim; x++) {
            for (int y = 0; y < yDim; y++) {
                if (map[x][y].isObstacle()) try {
                    res.switchPassable(x, y);
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
    private int genRooms(int xDim, int yDim) {
        return genRooms(xDim, yDim, xDim * yDim / 25);
    }

    private int genRooms(int xDim, int yDim, int limit) {
        // init isRoom super array
        rooms = new int[limit][4];

        // declare vars
        int xLen, yLen, xStart, yStart;

        int roomNum = 0;

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
            xStart = RandomUtil.getRandomInteger(xDim - xLen);
            xStart = xStart + (xStart + 1) % 2;
            yStart = RandomUtil.getRandomInteger(yDim - yLen);
            yStart = yStart + (yStart + 1) % 2;

            // check whether the position is valid
            if (!checkRoom(xDim, yDim, xStart, yStart, xLen, yLen)) {
                continue;
            }

            // place isRoom
            for (int x = xStart; x < xStart + xLen; x++) {
                for (int y = yStart; y < yStart + yLen; y++) {
                    map[x][y].setRoom();
                }
            }

            // insert isRoom into memory
            rooms[roomNum] = new int[]{xStart, yStart, xLen, yLen};
            roomNum++;

        }

        // return updated builder object
        return roomNum;
    }

    /**
     * helper function to check for valid isRoom coordinates
     */
    private boolean checkRoom(int xDim, int yDim, int xStart, int yStart, int xLen, int yLen) {
        // be sure to only check for odd numbers (xStart, yStart are odd)
        for (int i = 0; i <= xLen; i += 2) {
            for (int j = 0; j <= yLen; j += 2) {
                if (xStart + i < 0 || xStart + i >= xDim - 1 || yStart + j < 0 || yStart + j >= yDim - 1) {
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
    private DisjointSet<CeellType> CCEmpty() {
        return new DisjointSet<>();
    }

    /**
     * create a disjoint set with a connected component for each isRoom
     */
    private DisjointSet<CeellType> CCEachRoom(int roomNum) {
        // create connected compontents
        DisjointSet<CeellType> cc = new DisjointSet<>();


        for (int i = 0; i < roomNum; i++) {
            cc.makeSet(map[rooms[i][0]][rooms[i][1]]);

            for (int x = rooms[i][0]; x < rooms[i][0] + rooms[i][2]; x += 2) {
                for (int y = rooms[i][1]; y < rooms[i][1] + rooms[i][3]; y += 2) {
                    if (x == rooms[i][0] && y == rooms[i][1])
                        continue;

                    cc.makeSet(map[x][y]);
                    cc.union(map[rooms[i][0]][rooms[i][1]], map[x][y]);
                }
            }
        }

        return cc;
    }

    /**
     * create a disjoint set with a connected component for each isRoom
     */
    private DisjointSet<CeellType> CCEachDeadEndRoom(int roomNum) {
        // add for rooms which only one floor connection a connected component
        DisjointSet<CeellType> cc = new DisjointSet<>();


        for (int i = 0; i < roomNum; i++) {
            // declare variables
            final int xStart = rooms[i][0], xLen = rooms[i][2], yStart = rooms[i][1],
                    yLen = rooms[i][3];

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
    private void genFloors(int xDim, int yDim, DisjointSet<CeellType> cc) {
        ArrayList<int[]> q = new ArrayList<>();

        for (int i = 1; i < xDim; i += 2) {
            for (int j = 1; j < yDim; j += 2) {
                // fill in fixed cells on odd / odd coordinates
                if (map[i][j].isObstacle()) {
                    map[i][j].setFloor();
                    cc.makeSet(map[i][j]);
                }

                // queue neighbours when one corner is not a isRoom
                if (i + 2 < xDim && !map[i+1][j].isRoom())
                    q.add(new int[]{i, j, i + 2, j});
                if (j + 2 < yDim && !map[i][j + 1].isRoom())
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
    private void clearDeadEndFloors(int xDim, int yDim) {
        int count;
        boolean repeat = true, deadend[][];

        while (repeat) {
            // fresh init for single execution of elimination
            deadend = new boolean[xDim][yDim];
            repeat = false;

            // dead end iff 3 neighbours are walls
            for (int x = 0; x < xDim; x++) {
                for (int y = 0; y < yDim; y++) {
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
            for (int x = 0; x < xDim; x++) {
                for (int y = 0; y < yDim; y++) {
                    if (deadend[x][y]) {
                        map[x][y].setObstacle();
                    }
                }
            }
        }
    }

    Map loadMap(File file) throws MapInitialisationException {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            br.readLine();  //Skip type
            int yDim = Integer.valueOf(br.readLine().substring(7));  //Read height
            int xDim = Integer.valueOf(br.readLine().substring(6));  //Read width
            Map map = new Map(xDim, yDim, false); //init Map without passable fields
            br.readLine();  //Skip map
            String currentLine;
            for (int y = 0; (currentLine = br.readLine()) != null; y++) { //Read in MapRow
                for (int x = 0; x < currentLine.length(); x++) {
                    if (currentLine.charAt(x) == '.' || currentLine.charAt(x) == 'G' || currentLine.charAt(x) == 'S') {
                        map.switchPassable(x, y);    //Mark passable fields
                    }
                }
            }
            return map;
        } catch (Exception e) {
            throw new MapInitialisationException();
        }
    }
}
