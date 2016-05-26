package grid;

import java.util.ArrayList;
import java.util.Collections;

public class Room {
    // class member
    private int xStart, xLen, yStart, yLen;
    private ArrayList<int[]> randomCells;

    public Room(int xStart, int xLen, int yStart, int yLen) {
        // setting class members
        this.xStart = xStart;
        this.xLen = xLen;
        this.yStart = yStart;
        this.yLen = yLen;

        // generate random cells
        this.randomCells = new ArrayList<>();

        for (int x = 0; x < xLen; x++) {
            for (int y = 0; y < yLen; y++) {
                this.randomCells.add(new int[] { xStart + x, yStart + y });
            }
        }

        // create a random permutation
        Collections.shuffle(randomCells);
    }

    /**
     * @return the xStart
     */
    public int getXStart() {
        return xStart;
    }

    /**
     * @return the xLen
     */
    public int getXLen() {
        return xLen;
    }

    /**
     * @return the yStart
     */
    public int getYStart() {
        return yStart;
    }

    /**
     * @return the yLen
     */
    public int getYLen() {
        return yLen;
    }

    /**
     * @return the randomCells
     */
    public int[] getUnusedRandomCell() {
        final int[] ret = randomCells.get(0);
        randomCells.remove(0);
        return ret;
    }
}