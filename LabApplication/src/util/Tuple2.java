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
}
