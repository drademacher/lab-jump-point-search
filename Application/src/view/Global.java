package view;

import grid.Map;

import java.util.Random;

public class Global {
    private static Global ourInstance = new Global();

    public final Random rnd = new Random();
    public final int size = 15;
    public int n;
    public int m;
    public Map map;

    private Global() {

    }

    public static Global getInstance() {
        return ourInstance;
    }
}
