package core.util;

/**
 * Tuple2 is a 2-Tuple of two mate values of generic attribute types.
 *
 * @param <A> Attribute Type of Argument arg1.
 * @param <B> Attribute Type of Argument arg2.
 * @author Patrick Loka
 * @version 1.0
 * @since 1.0
 */
public class Tuple2<A, B> {

    private A arg1;
    private B arg2;

    /**
     * Init a Tuple2 with the given parameters.
     *
     * @param arg1 set as arg1 of Tuple2
     * @param arg2 set as arg2 of Tuple2
     * @since 1.0
     */
    public Tuple2(A arg1, B arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    /**
     * Returns arg1 of the Tuple.
     *
     * @return arg1
     * @since 1.0
     */
    public A getArg1() {
        return arg1;
    }

    /**
     * Returns arg2 of the Tuple.
     *
     * @return arg2
     * @since 1.0
     */
    public B getArg2() {
        return arg2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple2<?, ?> tuple2 = (Tuple2<?, ?>) o;

        return arg1 != null ? arg1.equals(tuple2.arg1) : tuple2.arg1 == null && (arg2 != null ? arg2.equals(tuple2.arg2) : tuple2.arg2 == null);
    }

    @Override
    public int hashCode() {
        int result = arg1 != null ? arg1.hashCode() : 0;
        result = 31 * result + (arg2 != null ? arg2.hashCode() : 0);
        return result;
    }
}
