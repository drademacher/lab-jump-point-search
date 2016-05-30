package view;

import controller.map.Map;

import java.util.Random;

public class Global {
    private static Global ourInstance = new Global();

    public final Random rnd = new Random();
    public final int size = 12;
    public int n;
    public int m;

    public int xSizeVis;
    public int ySizeVis;
    
    public Map map;

    private Global() {

    }

    public static Global getInstance() {
        return ourInstance;
    }
}
