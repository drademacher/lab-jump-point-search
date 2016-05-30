package controller.map;

import view.Global;

/**
 * Created by paloka on 30.05.16.
 */
public class MapFactory {


    public static Map createEmptyMap(int n, int m) {
        return new Map(n, m);
    }

    public static Map createExampleMap(int n, int m) {
        Map map =  new Map(n, m);
        try {
            map.setStartPoint(0, 0);
            map.setStartPoint(2, 2);
            map.setGoalPoint(0, 1);
            map.setGoalPoint(5, 10);
            map.setObstacle(4, 4);
        } catch (Exception e) {

        }
        return map;
    }

    public static Map createRandomMap(int n, int m, double prob) {
        Map map = new Map(n, m);
        try {
            for (int x = 0; x < n; x++) {
                for (int y = 0; y < m; y++) {
                    if (Global.getInstance().rnd.nextDouble() <= prob) {
                        //Todo: implement random singelton to obmit global
                        map.setObstacle(x, y);
                    }
                }
            }
        } catch (NotAFieldException e) {}
        return map;
    }

    public static Map createMazeMap(int n, int m) {
        return new MapGenerator().genMaze(n, m);
    }

    public static Map createMazeWithRoomsMap(int n, int m) {
        return new MapGenerator().genMazeWithRooms(n, m);
    }

    public static Map createSingleConnRoomsMap(int n, int m) {
        return new MapGenerator().genSingleConnRooms(n, m);
    }

    public static Map createLoopedRoomsMap(int n, int m) {
        return new MapGenerator().genLoopedRooms(n, m);
    }

    public static Map createDoubleConnRoomsMap(int n, int m) {
        return new MapGenerator().genDoubleConnRooms(n, m);
    }
}
