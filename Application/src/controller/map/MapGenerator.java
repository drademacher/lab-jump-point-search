package controller.map;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


/**
 * builder pattern class to generate level maps source:
 */

public class MapGenerator {
    // TODO: remove in final version
    // private int ROOM_LIMIT = 300;
    private final Random RAND = new Random();
    // TODO: keep this constant
    private final int[][] NEIGHS_ALL = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    // TODO: remove those members
    private int n;
    private int m;
    private Map map;
    private int[][] rooms;
    private boolean[][] isRoom;


    private void init(int n, int m) throws NotAFieldException {
        map = new Map(n, m);
        isRoom = new boolean[n][m];


        for (int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++) {
                map.setObstacle(x, y);
            }
        }

        // design choice! ignore the last row / column if its even in the algorithm
        this.n = n - (n + 1) % 2;
        this.m = m - (m + 1) % 2;

    }

    private boolean isFloor(int x, int y) throws NotAFieldException {
        return map.getField(x, y).getFieldType().equals(FieldType.GRID_POINT) && !isRoom[x][y];
    }

    private boolean isRoom(int x, int y) throws NotAFieldException {
        return map.getField(x, y).getFieldType().equals(FieldType.GRID_POINT) && isRoom[x][y];
    }

    private boolean isObstacle(int x, int y) throws NotAFieldException {
        return map.getField(x, y).getFieldType().equals(FieldType.OBSTACLE_POINT);
    }


    Map genMaze(int n, int m) throws NotAFieldException {
        // init structure
        init(n, m);

        // gen maze with no dead ends at first
        genFloors(CCEmpty());

        return map;
    }


    Map genMazeWithRooms(int n, int m) throws NotAFieldException {
        // init structure
        init(n, m);

        // gen rooms
        // TODO: user input for isRoom number upper bound!
        int roomNum = genRooms(5);

        // gen maze with no dead ends at first
        genFloors(CCEachRoom(roomNum));

        return map;
    }


    Map genSingleConnRooms(int n, int m) throws NotAFieldException {
        // init structure
        init(n, m);

        // gen rooms
        int roomNum = genRooms();

        // gen maze with no dead ends at first
        genFloors(CCEachRoom(roomNum));
        clearDeadEndFloors();

        return map;
    }


    Map genLoopedRooms(int n, int m) throws NotAFieldException {
        // init structure
        init(n, m);

        // gen rooms
        int roomNum = genRooms();

        // gen maze with no dead ends at first
        genFloors(CCEachRoom(roomNum));
        clearDeadEndFloors();

        // add another maze for loops
        genFloors(CCEachDeadEndRoom(roomNum));

        // remove the dead ends finally
        clearDeadEndFloors();

        return map;
    }


    Map genDoubleConnRooms(int n, int m) throws NotAFieldException {
        // init structure
        init(n, m);

        // gen rooms
        int roomNum = genRooms();

        // gen maze with no dead ends at first
        genFloors(CCEachRoom(roomNum));
        clearDeadEndFloors();

        // add another maze for loops
        genFloors(CCEachRoom(roomNum));

        // remove the dead ends finally
        clearDeadEndFloors();

        return map;
    }

    /**
     * internal function which generated a random number of rooms the limit is
     * just an upper bound and its not expected to be reached
     */
    private int genRooms() throws NotAFieldException {
        return genRooms(n * m / 25);
    }

    private int genRooms(int limit) throws NotAFieldException {
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
                xLen = (int) (4 + 2 * RAND.nextGaussian());
                xLen = 2 * xLen + 1;
                yLen = (int) (4 + 2 * RAND.nextGaussian());
                yLen = 2 * yLen + 1;
            } while (xLen < 4 || yLen < 4);

            // gen a position in the level
            // increment number if its even -> odd
            xStart = RAND.nextInt(n - xLen);
            xStart = xStart + (xStart + 1) % 2;
            yStart = RAND.nextInt(m - yLen);
            yStart = yStart + (yStart + 1) % 2;

            // check whether the position is valid
            if (!checkRoom(xStart, yStart, xLen, yLen)) {
                continue;
            }

            // place isRoom
            for (int x = xStart; x < xStart + xLen; x++) {
                for (int y = yStart; y < yStart + yLen; y++) {
                    try {
                        map.setField(x, y);
                    } catch (NotAFieldException e) {
                        e.printStackTrace();
                    }
                    isRoom[x][y] = true;
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
     * create a disjoint set with no connected components at all
     */
    private DisjointSet<Field> CCEmpty() {
        return new DisjointSet<>();
    }
    
    /**
     * create a disjoint set with a connected component for each isRoom
     */
    private DisjointSet<Field> CCEachRoom(int roomNum) throws NotAFieldException {
        // create connected compontents
        DisjointSet<Field> cc = new DisjointSet<>();


        for (int i = 0; i < roomNum; i++) {
            cc.makeSet(map.getField(rooms[i][0], rooms[i][1]));

            for (int x = rooms[i][0]; x < rooms[i][0] + rooms[i][2]; x += 2) {
                for (int y = rooms[i][1]; y < rooms[i][1] + rooms[i][3]; y += 2) {
                    if (x == rooms[i][0] && y == rooms[i][1])
                        continue;

                    cc.makeSet(map.getField(x, y));
                    cc.union(map.getField(rooms[i][0], rooms[i][1]), map.getField(x, y));
                }
            }
        }

        return cc;
    }

    /**
     * create a disjoint set with a connected component for each isRoom
     */
    private DisjointSet<Field> CCEachDeadEndRoom(int roomNum) throws NotAFieldException {
        // add for rooms which only one floor connection a connected component
        DisjointSet<Field> cc = new DisjointSet<>();


        for (int i = 0; i < roomNum; i++) {
            // declare variables
            final int xStart = rooms[i][0], xLen = rooms[i][2], yStart = rooms[i][1],
                    yLen = rooms[i][3];

            // check if isRoom only has one floor connection
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

            // skip the isRoom on break condition
            if (count > 1) {
                continue;
            }

            // create a connected component for each isRoom
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
     * internal function to generate a maze around the rooms
     * <p>
     * this is done by a FloodFill algorithm instead of some over engineering
     * with MST
     */
    private void genFloors(DisjointSet<Field> cc) throws NotAFieldException {
        ArrayList<int[]> q = new ArrayList<>();

        for (int i = 1; i < n; i += 2) {
            for (int j = 1; j < m; j += 2) {
                // fill in fixed cells on odd / odd coordinates
                if (isObstacle(i, j)) {
                    map.setField(i, j);
                    isRoom[i][j] = false;
                    cc.makeSet(map.getField(i, j));
                }

                // queue neighbours when one corner is not a isRoom
                if (i + 2 < n && !isRoom(i + 1, j))
                    q.add(new int[]{i, j, i + 2, j});
                if (j + 2 < m && !isRoom(i, j + 1))
                    q.add(new int[]{i, j, i, j + 2});
            }
        }

        // choose connector in a random order
        Collections.shuffle(q, RAND);

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
            isRoom[(x1 + x2) / 2][(y1 + y2) / 2] = false;
        }
    }

    /**
     * internal function which deletes all dead ends of a maze
     */
    private void clearDeadEndFloors() throws NotAFieldException {
        int count;
        boolean repeat = true, deadend[][];

        while (repeat) {
            // fresh init for single execution of elimination
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
                        isRoom[x][y] = false;
                    }
                }
            }
        }
    }
}
