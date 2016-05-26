package grid;


import view.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static grid.Type.FLOOR;
import static grid.Type.OBSTACLE;
import static grid.Type.ROOM;


/**
 * builder pattern class to generate level maps source:
 */

public class LevelBuilder {
    // layout types
    public enum Layout {
        MAZE, MAZE_WITH_ROOMS, SINGLE_CONN_ROOMS, LOOPED_ROOMS, DOUBLE_CONN_ROOMS
    };

    // class constants
    int ROOM_LIMIT = 300;
    final int[][] NEIGHS_ALL = new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
    final int[][] NEIGHS_ODD = new int[][] { { 2, 0 }, { -2, 0 }, { 0, 2 }, { 0, -2 } };

    // setting vars
    private boolean hasRooms = false, hasFloor = true;

    // class members
    private int n;
    private int m;
    private Cell[][] map;
    private Random rnd;
    private Room[] rooms;
    private int roomNum = 0;
    private ArrayList<int[]> floors;

    /**
     * constuctor
     *
     * @param n
     *            the width
     * @param m
     *            the height
     * @param layout
     *            the layout
     */
    public LevelBuilder(int n, int m, Layout layout) {
        n = n - (n + 1) % 2;
        m = m - (m + 1) % 2;
        this.n = n;
        this.m = m;
        map = new Cell[n][m];
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++) {
                map[x][y] = new Cell(x, y);
            }
        }

        // get random object
        rnd = new Random();

        switch (layout) {
            case MAZE:
                // gen maze with no dead ends at first
                genFloors(ccFromNoRooms());

                break;
            case MAZE_WITH_ROOMS:
                // gen rooms
                genRooms(5);

                // gen maze with no dead ends at first
                genFloors(ccFromAllRooms());

                break;

            case SINGLE_CONN_ROOMS:
                // gen rooms
                genRooms(ROOM_LIMIT);

                // gen maze with no dead ends at first
                genFloors(ccFromAllRooms());
                clearDeadends();

                break;

            case LOOPED_ROOMS:
                // gen rooms
                genRooms(ROOM_LIMIT);

                // gen maze with no dead ends at first
                genFloors(ccFromAllRooms());
                clearDeadends();

                // add another maze for loops
                genFloors(ccFromEndRooms());

                // remove the dead ends finally
                clearDeadends();

                break;
            case DOUBLE_CONN_ROOMS:
                // gen rooms
                genRooms(ROOM_LIMIT);

                // gen maze with no dead ends at first
                genFloors(ccFromAllRooms());
                clearDeadends();

                // add another maze for loops
                genFloors(ccFromAllRooms());

                // remove the dead ends finally
                clearDeadends();

                break;
        }

        // complete floors computation for all layouts
        // (every layout so far has floors)
        completeFloors();
    }

    /**
     * internal function which generated a random number of rooms the limit is
     * just an upper bound and its not expected to be reached
     */
    private LevelBuilder genRooms(int limit) {
        // set room flag
        hasRooms = true;

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
                map[x][y].setType(FLOOR);
            }
        }
    }

    /**
     * helper function to check for valid room coordinates
     *
     */
    private boolean checkRoom(int xStart, int yStart, int xLen, int yLen) {
        // be sure to only check for odd numbers (xStart, yStart are odd)
        for (int i = 0; i <= xLen; i += 2) {
            for (int j = 0; j <= yLen; j += 2) {
                if (xStart + i < 0 || xStart + i >= n - 1 || yStart + j < 0 || yStart + j >= m - 1) {
                    return false;
                }


                if (map[xStart + i][yStart + j].getType() != Type.OBSTACLE) {
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
    private DisjointSet<Cell> ccFromAllRooms() {
        // create connected compontents
        DisjointSet<Cell> cc = new DisjointSet<>();

        for (int i = 0; i < roomNum; i++) {
            cc.makeSet(map[rooms[i].getXStart()][rooms[i].getYStart()]);

            for (int x = rooms[i].getXStart(); x < rooms[i].getXStart() + rooms[i].getXLen(); x += 2) {
                for (int y = rooms[i].getYStart(); y < rooms[i].getYStart() + rooms[i].getYLen(); y += 2) {
                    if (x == rooms[i].getXStart() && y == rooms[i].getYStart())
                        continue;

                    cc.makeSet(map[x][y]);
                    cc.union(map[rooms[i].getXStart()][rooms[i].getYStart()], map[x][y]);
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
    private DisjointSet<Cell> ccFromEndRooms() {
        // add for rooms which only one floor connection a connected component
        DisjointSet<Cell> cc = new DisjointSet<>();

        for (int i = 0; i < roomNum; i++) {
            // declare variables
            final int xStart = rooms[i].getXStart(), xLen = rooms[i].getXLen(), yStart = rooms[i].getYStart(),
                    yLen = rooms[i].getYLen();

            // check if room only has one floor connection
            int count = 0;
            for (int x = 0; x < xLen; x++) {
                if (map[xStart + x][yStart - 1].getType() == FLOOR)
                    count++;

                if (map[xStart + x][yStart + yLen].getType() == FLOOR)
                    count++;
            }

            for (int y = 0; y < yLen; y++) {
                if (map[xStart - 1][yStart + y].getType() == FLOOR)
                    count++;

                if (map[xStart + xLen][yStart + y].getType() == FLOOR)
                    count++;
            }

            // skip the room on break condition
            if (count > 1) {
                continue;
            }

            // create a connected component for each room
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
     * create a disjoint set with no connected components at all
     *
     * @return
     */
    private DisjointSet<Cell> ccFromNoRooms() {
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
    private void genFloors(DisjointSet<Cell> cc) {
        ArrayList<int[]> q = new ArrayList<>();

        for (int i = 1; i < n; i += 2) {
            for (int j = 1; j < m; j += 2) {
                // fill in fixed cells on odd / odd coordinates
                if (map[i][j].getType() == OBSTACLE) {
                    map[i][j].setType(FLOOR);
                    cc.makeSet(map[i][j]);
                }

                // queue neighbours when one corner is not a room
                if (i + 2 < n && map[i+1][j].getType() != ROOM)
                    q.add(new int[] { i, j, i + 2, j });
                if (j + 2 < m && map[i][j+1].getType() != ROOM)
                    q.add(new int[] { i, j, i, j + 2 });
            }
        }

        // choose connector in a random order
        Collections.shuffle(q);

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
            map[(x1 + x2) / 2][(y1 + y2) / 2].setType(FLOOR);
        }
    }

    /**
     * internal function which deletes all dead ends of a maze
     *
     * @return
     */
    private void clearDeadends() {
        int count;
        boolean repeat = true, deadend[][];

        while (repeat) {
            // fresh inits for single execution of elemination
            deadend = new boolean[n][m];
            repeat = false;

            // dead end iff 3 neighbours are walls
            for (int x = 0; x < n; x++) {
                for (int y = 0; y < m; y++) {
                    if (map[x][y].getType() == OBSTACLE) {
                        continue;
                    }

                    count = 0;
                    for (int j = 0; j < 4; j++) {
                        if (map[x + NEIGHS_ALL[j][0]][y + NEIGHS_ALL[j][1]].getType() == OBSTACLE)
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
                    if (deadend[x][y])
                        map[x][y].setType(OBSTACLE);
                }
            }
        }
    }

    /**
     * internal helper function
     */
    private void completeFloors() {
        // set floor flag
        hasFloor = true;

        // create new array
        floors = new ArrayList<>();

        // fill in all the floor cells
        for (int i = 1; i < n; i += 2) {
            for (int j = 1; j < m; j += 2) {
                if (map[i][j].getType() == FLOOR) {
                    floors.add(new int[] { i, j });
                }
            }
        }

        // spawn points is a random permuation
        Collections.shuffle(floors);
    }



    /**
     * function which is called finally to make a Map from a MapBuilder
     */
    public Grid create() {
        Grid grid = new Grid(Context.getInstance().n, Context.getInstance().m);
        grid.setObstacle();

        for (int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++) {
                if (map[x][y].getType() == OBSTACLE) {
                    grid.setCell(x, y, OBSTACLE);
                } else {
                    grid.setCell(x, y, Type.FLOOR);
                }
            }
        }
        return grid;
    }
}
