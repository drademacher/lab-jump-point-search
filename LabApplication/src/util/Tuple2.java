package util;

/**
 * Created by paloka on 03.06.16.
 */

//Todo: weg refactoren if possible
public class Tuple2<A,B>  {

    private A arg1;
    private B arg2;

    public Tuple2(A arg1, B arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public A getArg1() {
        return arg1;
    }

    public B getArg2() {
        return arg2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple2<?, ?> tuple2 = (Tuple2<?, ?>) o;

        if (arg1 != null ? !arg1.equals(tuple2.arg1) : tuple2.arg1 != null) return false;
        return arg2 != null ? arg2.equals(tuple2.arg2) : tuple2.arg2 == null;

    }

    @Override
    public int hashCode() {
        int result = arg1 != null ? arg1.hashCode() : 0;
        result = 31 * result + (arg2 != null ? arg2.hashCode() : 0);
        return result;
    }
}
