package core.util;

import java.util.Random;

/**
 * Created by paloka on 02.06.16.
 */
public class RandomUtil {

    private static Random RANDOM;

    public static double getRandomDouble() {
        if (RANDOM == null) RANDOM = new Random();
        return RANDOM.nextDouble();
    }

    public static int getRandomInteger(int d) {
        if (RANDOM == null) RANDOM = new Random();
        return RANDOM.nextInt(d);
    }

    public static double getRandomGaussian() {
        if (RANDOM == null) RANDOM = new Random();
        return RANDOM.nextGaussian();
    }

    public static Random getRANDOM() {
        if (RANDOM == null) RANDOM = new Random();
        return RANDOM;
    }
}
