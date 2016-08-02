package util;

/**
 * Created by paloka on 13.06.16.
 */
public class Coordinate {

    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Coordinate add(Coordinate adding) {
        return new Coordinate(x + adding.getX(), y + adding.getY());
    }

    public Coordinate sub(Coordinate substracting) {
        return new Coordinate(x - substracting.getX(), y - substracting.getY());
    }

    public Coordinate mult(int multiplicator){
        return new Coordinate(x*multiplicator,y*multiplicator);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;

        if (x != that.x) return false;
        return y == that.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString(){
        return "("+x+","+y+")";
    }
}
