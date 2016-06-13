package map;

/**
 * Created by danny on 13.06.2016.
 */
public class CeellType {
    private int val;

    public void setRoom() {
        val = 1;
    }

    public void setFloor() {
        val = 2;
    }

    public void setObstacle() {
        val = -1;
    }

    public boolean isRoom() {
        return val == 1;
    }

    public boolean isFloor() {
        return val == 2;
    }


    public boolean isObstacle() {
        return val == -1;
    }
}
