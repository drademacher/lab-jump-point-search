package core.util;

/**
 * Vector models a 2 dimensional Vector. It can be used as a coordinate, a dimension bounds, a direction or anything you can think of by vectors.
 * The meaning of a vector should be documented and/or be given by the attribute name.
 *
 * @author Patrick Loka
 * @version 1.0
 * @since 1.0
 */
public class Vector {

    private int x;
    private int y;

    /**
     * Init Vector instance with the input params.
     *
     * @param x init x value
     * @param y init y value
     * @since 1.0
     */
    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Provides current x value of the vector.
     *
     * @return x
     * @since 1.0
     */
    public int getX() {
        return x;
    }

    /**
     * Provides current y value of the vector.
     *
     * @return y
     * @since 1.0
     */
    public int getY() {
        return y;
    }

    /**
     * Provides vector addition.
     *
     * @param addend vector, which should be added.
     * @return A new Vector instance representing the sum of this vector and the addend vector.
     * @since 1.0
     */
    public Vector add(Vector addend) {
        return new Vector(x + addend.getX(), y + addend.getY());
    }

    /**
     * Provides vector subtraction.
     *
     * @param subtrahend vector, which should be subtracted.
     * @return A new Vector instance representing the difference between this vector as minor and the input as subtrahend vector.
     * @since 1.0
     */
    public Vector sub(Vector subtrahend) {
        return new Vector(x - subtrahend.getX(), y - subtrahend.getY());
    }

    /**
     * Provides vector multiplication with a single integer value.
     *
     * @param factor multiplication factor.
     * @return A new Vector instance representing the product of the input factor and this vector.
     * @since 1.0
     */
    public Vector mult(int factor) {
        return new Vector(x * factor, y * factor);
    }

    /**
     * Calculates the direction to reach the input vector.
     *
     * @param goal point to go from this vector in resulting direction.
     * @return direction d: this vector + sd = goal, for s steps in direction d.
     * @since 1.0
     */
    public Vector getDirectionTo(Vector goal) {
        Vector dir = goal.sub(this);
        return new Vector((int) Math.signum(dir.getX()), (int) Math.signum(dir.getY()));
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
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
