package data_old;

/**
 * Created by galafit on 15/7/17.
 */
public class XYPoint<Y> {
    private Number x;
    private Y y;

    public XYPoint(Number x, Y y) {
        this.x = x;
        this.y = y;
    }

    public Number getX() {
        return x;
    }

    public Y getY() {
        return y;
    }
}
