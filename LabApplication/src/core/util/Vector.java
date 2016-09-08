package core.util;

/**
 * Created by paloka on 13.06.16.
 */
public class Vector {

    private int x;
    private int y;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Vector add(Vector adding) {
        return new Vector(x + adding.getX(), y + adding.getY());
    }

    public Vector sub(Vector substracting) {
        return new Vector(x - substracting.getX(), y - substracting.getY());
    }

    public Vector mult(int multiplicator){
        return new Vector(x*multiplicator,y*multiplicator);
    }

    public Vector getDirectionTo(Vector goal){
        Vector dir  = goal.sub(this);
        return new Vector((int)Math.signum(dir.getX()),(int)Math.signum(dir.getY()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector that = (Vector) o;

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
