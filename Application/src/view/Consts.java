package view;

import java.util.Random;

public class Consts {
    public static final Random rand = new Random();

    public static final int size = 15;

    public static final int windowWidth = 1200;
    public static final int windowHeight = 800;

    public static final int defaultXDim = windowWidth / size - 2;
    public static final int defaultYDim = windowHeight / size - 2;

    /**
     * The caller references the constants using <tt>Consts.EMPTY_STRING</tt>,
     * and so on. Thus, the caller should be prevented from constructing objects of
     * this class, by declaring this private constructor.
     */
    private Consts() {
        //this prevents even the native class from
        //calling this constructor as well :
        throw new AssertionError();
    }
}
