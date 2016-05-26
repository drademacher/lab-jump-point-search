package view;

import grid.Grid;

import java.util.Random;

public class Context {
    private static Context ourInstance = new Context();

    public final Random rnd = new Random();
    public final int size = 10;
    public int n;
    public int m;
    public Grid grid;

    private Context() {

    }

    public static Context getInstance() {
        return ourInstance;
    }
}
