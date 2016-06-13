package util;

/**
 * Created by paloka on 03.06.16.
 */
//Todo: weg refactoren if possible
public class Tuple3<A, B, C> extends Tuple2<A, B> {

    private C arg3;

    public Tuple3(A arg1, B arg2, C arg3) {
        super(arg1, arg2);
        this.arg3 = arg3;
    }

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
