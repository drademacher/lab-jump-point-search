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
}
