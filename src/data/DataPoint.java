package data;

/**
 * Created by galafit on 15/7/17.
 */
public class DataPoint<Y> {
    private double x;
    private Y y;

    public DataPoint(double x, Y y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public Y getY() {
        return y;
    }
}
