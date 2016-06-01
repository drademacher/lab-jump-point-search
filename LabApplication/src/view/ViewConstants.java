package view;

import java.util.Random;

/**
 * Created by paloka on 01.06.16.
 */
public abstract class ViewConstants {
    //Todo: Random does not belong to ViewConstants
    public static final Random RANDOM = new Random();

    //Todo: Depend field size on Zoom lvl in ViewHolder
    public static final int FIELD_SIZE = 15;

    //Todo: Window dimension?
    public static final int WINDOW_WIDTH = 1200;
    public static final int WINDOW_HEIGHT = 800;
}
