package util;

import java.util.Random;

/**
 * Created by paloka on 02.06.16.
 */
public class RandomUtil {
    //Todo: Random does not belong to ApplicationConstants
    private static final Random RANDOM  = new Random();

    public static double getRandomDouble(){
        return RANDOM.nextDouble();
    }
}
