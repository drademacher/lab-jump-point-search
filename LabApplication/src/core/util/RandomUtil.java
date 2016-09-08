package core.util;

import java.util.Random;

/**
 * Init a Random object if needed and provides its randomness to the application
 *
 * @author Patrick Loka, Danny Rademacher
 * @version 1.0
 * @since 1.0
 */
public class RandomUtil {

    private static Random RANDOM;

    /**
     * Provides an uniform distributed random double value between 0.0 and 1.0
     *
     * @return an uniform distributed random double value between 0.0 and 1.0
     * @since 1.0
     */
    public static double getRandomDouble() {
        if (RANDOM == null) RANDOM = new Random();
        return RANDOM.nextDouble();
    }

    /**
     * Provides an uniform distributed random int value between 0 and the input parameter d.
     *
     * @param d upper bound for interval the random int value is chosen from.
     * @return an uniform distributed random int value between 0 and d.
     * @since 1.0
     */
    public static int getRandomInteger(int d) {
        if (RANDOM == null) RANDOM = new Random();
        return RANDOM.nextInt(d);
    }

    /**
     * Provides a gaussian distributed random double value between 0.0 and 1.0.
     *
     * @return a gaussian distributed random double value between 0.0 and 1.0.
     * @since 1.0
     */
    public static double getRandomGaussian() {
        if (RANDOM == null) RANDOM = new Random();
        return RANDOM.nextGaussian();
    }

    /**
     * Provides the Random instance.
     *
     * @return the Random instance.
     * @since 1.0
     */
    public static Random getRANDOM() {
        if (RANDOM == null) RANDOM = new Random();
        return RANDOM;
    }
}
