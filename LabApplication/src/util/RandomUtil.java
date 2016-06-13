package util;

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
}
