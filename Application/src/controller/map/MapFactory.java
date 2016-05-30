package controller.map;

/**
 * Created by paloka on 30.05.16.
 */
public class MapFactory {


    public static Map createEmptyMap(int n, int m) {
        return new Map(n, m);
    }

    public static Map createExampleMap(int n, int m) {
        //Todo: createExampleMap not implemented yet
        throw new NullPointerException();
    }
}
