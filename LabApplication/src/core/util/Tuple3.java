package core.util;

/**
 * Tuple3 is a 3-Tuple of three mate values of generic attribute types.
 *
 * @param <A> Attribute Type of Argument arg1.
 * @param <B> Attribute Type of Argument arg2.
 * @param <C> Attribute Type of Argument arg3.
 * @author Patrick Loka
 * @version 1.0
 * @since 1.0
 */
public class Tuple3<A, B, C> extends Tuple2<A, B> {

    private C arg3;

    /**
     * Init a Tuple3 with the given parameters.
     *
     * @param arg1 set as arg1 of Tuple3.
     * @param arg2 set as arg2 of Tuple3.
     * @param arg3 set as arg3 of Tuple3.
     * @since 1.0
     */
    public Tuple3(A arg1, B arg2, C arg3) {
        super(arg1, arg2);
        this.arg3 = arg3;
    }

    /**
     * Returns arg3 of the Tuple.
     *
     * @return arg3
     * @since 1.0
     */
    public C getArg3() {
        return arg3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Tuple3<?, ?, ?> tuple3 = (Tuple3<?, ?, ?>) o;

        return arg3 != null ? arg3.equals(tuple3.arg3) : tuple3.arg3 == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (arg3 != null ? arg3.hashCode() : 0);
        return result;
    }
}
