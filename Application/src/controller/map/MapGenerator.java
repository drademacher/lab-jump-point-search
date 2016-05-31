package controller.map;


import view.Global;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static controller.map.Type.FLOOR;
import static controller.map.Type.OBSTACLE;
import static controller.map.Type.ROOM;


/**
 * builder pattern class to generate level maps source:
 */

public class MapGenerator {
    // class constants
    private int ROOM_LIMIT = 300;
    private final int[][] NEIGHS_ALL = new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
    // final int[][] NEIGHS_ODD = new int[][] { { 2, 0 }, { -2, 0 }, { 0, 2 }, { 0, -2 } };

    // class members
    private int n;
    private int m;
    // private Field[][] map;
    private Map map;
    private boolean[][] room;
    private Random rnd;
    private Room[] rooms;
    private int roomNum = 0;
    private ArrayList<int[]> floors;

    private void init(int n, int m) throws NotAFieldException {
        map = new Map(n, m);
        room = new boolean[n][m];

        for (int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++) {
                map.setObstacle(x, y);
            }
        }

        // design choice! ignore the last row / column if its even in the algorithm
        this.n = n - (n + 1) % 2;
        this.m = m - (m + 1) % 2;

        // get random object
        rnd = new Random();
    }

    private boolean isFloor(int x, int y) throws NotAFieldException {
        return map.getField(x, y).getFieldType().equals(FieldType.GRID_POINT) && !room[x][y];
    }

    private boolean isRoom(int x, int y) throws NotAFieldException {
        return map.getField(x, y).getFieldType().equals(FieldType.GRID_POINT) && room[x][y];
    }

    private boolean isObstacle(int x, int y) throws NotAFieldException {
        return map.getField(x, y).getFieldType().equals(FieldType.OBSTACLE_POINT);
    }



    public Map genMaze(int n, int m) throws NotAFieldException {
        // init structure
        init(n, m);

        // gen maze with no dead ends at first
        genFloors(ccFromNoRooms());

        // complete floors computation for all layouts
        completeFloors();

        return map;
    }


    public Map genMazeWithRooms(int n, int m) throws NotAFieldException {
        // init structure
        init(n, m);

        // gen rooms
        genRooms(5);

        // gen maze with no dead ends at first
        genFloors(ccFromAllRooms());

        // complete floors computation for all layouts
        completeFloors();

        return map;
    }


    public Map genSingleConnRooms(int n, int m) throws NotAFieldException {
        // init structure
        init(n, m);

        // gen rooms
        genRooms(ROOM_LIMIT);

        // gen maze with no dead ends at first
        genFloors(ccFromAllRooms());
        clearDeadends();

        // complete floors computation for all layouts
        completeFloors();

        return map;
    }


    public Map genLoopedRooms(int n, int m) throws NotAFieldException {
        // init structure
        init(n, m);

        // gen rooms
        genRooms(ROOM_LIMIT);

        // gen maze with no dead ends at first
        genFloors(ccFromAllRooms());
        clearDeadends();

        // add another maze for loops
        genFloors(ccFromEndRooms());

        // remove the dead ends finally
        clearDeadends();

        // complete floors computation for all layouts
        completeFloors();

        return map;
    }



    public Map genDoubleConnRooms(int n, int m) throws NotAFieldException {
        // init structure
        init(n, m);

        // gen rooms
        genRooms(ROOM_LIMIT);

        // gen maze with no dead ends at first
        genFloors(ccFromAllRooms());
        clearDeadends();

        // add another maze for loops
        genFloors(ccFromAllRooms());

        // remove the dead ends finally
        clearDeadends();

        // complete floors computation for all layouts
        completeFloors();

        return map;
    }

    /**
     * internal function which generated a random number of rooms the limit is
     * just an upper bound and its not expected to be reached
     */
    private MapGenerator genRooms(int limit) throws NotAFieldException {
        // init room super array
        rooms = new Room[limit];

        // declare vars
        int xLen, yLen, xStart, yStart;

        // try to put up a new room in each iteration
        for (int i = 0; i < limit; i++) {
            // make sure to have valid room sizes
            // random num n transforms into 2n+1 -> odd
            do {
                xLen = (int) (4 + 2 * rnd.nextGaussian());
                xLen = 2 * xLen + 1;
                yLen = (int) (4 + 2 * rnd.nextGaussian());
                yLen = 2 * yLen + 1;
            } while (xLen < 4 || yLen < 4);

            // gen a position in the level
            // increment number if its even -> odd
            xStart = rnd.nextInt(n - xLen);
            xStart = xStart + (xStart + 1) % 2;
            yStart = rnd.nextInt(m - yLen);
            yStart = yStart + (yStart + 1) % 2;

            // check whether the position is valid
            if (!checkRoom(xStart, yStart, xLen, yLen)) {
                continue;
            }

            // place room
            setNewRoom(xStart, xLen, yStart, yLen);

            // insert room into memory
            rooms[roomNum] = new Room(xStart, xLen, yStart, yLen);
            roomNum++;

        }

        // return updated builder object
        return this;
    }

    private void setNewRoom(int xStart, int xLen, int yStart, int yLen) {
        for (int x = xStart; x < xStart + xLen; x++) {
            for (int y = yStart; y < yStart + yLen; y++) {
                try {
                    map.setField(x, y);
                } catch (NotAFieldException e) {
                    e.printStackTrace();
                }
                room[x][y] = true;
            }
        }
    }

    /**
     * helper function to check for valid room coordinates
     *
     */
    private boolean checkRoom(int xStart, int yStart, int xLen, int yLen) throws NotAFieldException {
        // be sure to only check for odd numbers (xStart, yStart are odd)
        for (int i = 0; i <= xLen; i += 2) {
            for (int j = 0; j <= yLen; j += 2) {
                if (xStart + i < 0 || xStart + i >= n - 1 || yStart + j < 0 || yStart + j >= m - 1) {
                    return false;
                }


                if (!isObstacle(xStart + i, yStart + j)) {
                    return false;
                }
            }
        }

        // no collision -> valid placement
        return true;
    }

    /**
     * create a disjoint set with a connected component for each room
     *
     * @return
     */
    private DisjointSet<Field> ccFromAllRooms() throws NotAFieldException {
        // create connected compontents
        DisjointSet<Field> cc = new DisjointSet<>();

        for (int i = 0; i < roomNum; i++) {
            cc.makeSet(map.getField(rooms[i].getXStart(), rooms[i].getYStart()));

            for (int x = rooms[i].getXStart(); x < rooms[i].getXStart() + rooms[i].getXLen(); x += 2) {
                for (int y = rooms[i].getYStart(); y < rooms[i].getYStart() + rooms[i].getYLen(); y += 2) {
                    if (x == rooms[i].getXStart() && y == rooms[i].getYStart())
                        continue;

                    cc.makeSet(map.getField(x, y));
                    cc.union(map.getField(rooms[i].getXStart(), rooms[i].getYStart()), map.getField(x, y));
                }
            }
        }

        return cc;
    }

    /**
     * create a disjoint set with a connected component for each room
     *
     * @return
     */
    private DisjointSet<Field> ccFromEndRooms() throws NotAFieldException {
        // add for rooms which only one floor connection a connected component
        DisjointSet<Field> cc = new DisjointSet<>();

        for (int i = 0; i < roomNum; i++) {
            // declare variables
            final int xStart = rooms[i].getXStart(), xLen = rooms[i].getXLen(), yStart = rooms[i].getYStart(),
                    yLen = rooms[i].getYLen();

            // check if room only has one floor connection
            int count = 0;
            for (int x = 0; x < xLen; x++) {
                if (isFloor(xStart + x, yStart - 1))
                    count++;

                if (isFloor(xStart + x, yStart + yLen))
                    count++;
            }

            for (int y = 0; y < yLen; y++) {
                if (isFloor(xStart - 1, yStart + y))
                    count++;

                if (isFloor(xStart + xLen, yStart + y))
                    count++;
            }

            // skip the room on break condition
            if (count > 1) {
                continue;
            }

            // create a connected component for each room
            cc.makeSet(map.getField(xStart, yStart));
            for (int x = xStart; x < xStart + xLen; x += 2) {
                for (int y = yStart; y < yStart + yLen; y += 2) {
                    if (x == xStart && y == yStart)
                        continue;

                    cc.makeSet(map.getField(x, y));
                    cc.union(map.getField(xStart, yStart), map.getField(x, y));
                }
            }
        }

        return cc;
    }

    /**
     * create a disjoint set with no connected components at all
     *
     * @return
     */
    private DisjointSet<Field> ccFromNoRooms() {
        return new DisjointSet<>();
    }

    /**
     * internal function to generate a maze around the rooms
     *
     * this is done by a floodfill algorithm instead of some overengineering
     * with MST
     *
     * @return
     */
    private void genFloors(DisjointSet<Field> cc) throws NotAFieldException {
        ArrayList<int[]> q = new ArrayList<>();

        for (int i = 1; i < n; i += 2) {
            for (int j = 1; j < m; j += 2) {
                // fill in fixed cells on odd / odd coordinates
                if (isObstacle(i, j)) {
                    map.setField(i, j);
                    room[i][j] = false;
                    cc.makeSet(map.getField(i, j));
                }

                // queue neighbours when one corner is not a room
                if (i + 2 < n && !isRoom(i+1, j))
                    q.add(new int[] { i, j, i + 2, j });
                if (j + 2 < m && !isRoom(i, j+1))
                    q.add(new int[] { i, j, i, j + 2 });
            }
        }

        // choose connector in a random order
        Collections.shuffle(q);

        for (int[] e : q) {
            // rename array
            final int x1 = e[0], y1 = e[1], x2 = e[2], y2 = e[3];

            if (cc.findSet(map.getField(x1, y1)) == null)
                continue;

            if (cc.findSet(map.getField(x2, y2)) == null)
                continue;

            // check if two cells are already connected
            if (cc.findSet(map.getField(x1, y1)) == cc.findSet(map.getField(x2, y2)))
                continue;

            // merge two components by adding a connector
            cc.union(map.getField(x1, y1), map.getField(x2, y2));
            map.setField((x1 + x2) / 2, (y1 + y2) / 2);
            room[(x1 + x2) / 2][(y1 + y2) / 2] = false;
        }
    }

    /**
     * internal function which deletes all dead ends of a maze
     *
     * @return
     */
    private void clearDeadends() throws NotAFieldException {
        int count;
        boolean repeat = true, deadend[][];

        while (repeat) {
            // fresh inits for single execution of elemination
            deadend = new boolean[n][m];
            repeat = false;

            // dead end iff 3 neighbours are walls
            for (int x = 0; x < n; x++) {
                for (int y = 0; y < m; y++) {
                    if (isObstacle(x, y)) {
                        continue;
                    }

                    count = 0;
                    for (int j = 0; j < 4; j++) {
                        if (isObstacle(x + NEIGHS_ALL[j][0], y + NEIGHS_ALL[j][1]))
                            count++;
                    }

                    if (count >= 3) {
                        deadend[x][y] = true;
                        repeat = true;
                    }
                }
            }

            // remove dead ends
            for (int x = 0; x < n; x++) {
                for (int y = 0; y < m; y++) {
                    if (deadend[x][y]) {
                        map.setObstacle(x, y);
                        room[x][y] = false;
                    }
                }
            }
        }
    }

    /**
     * internal helper function
     */
    private void completeFloors() throws NotAFieldException {
        // create new array
        floors = new ArrayList<>();

        // fill in all the floor cells
        for (int i = 1; i < n; i += 2) {
            for (int j = 1; j < m; j += 2) {
                if (isFloor(i, j)) {
                    floors.add(new int[] { i, j });
                }
            }
        }

        // spawn points is a random permuation
        Collections.shuffle(floors);
    }
}
