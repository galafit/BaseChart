/**
 * Created by hdablin on 05.04.17.
 */
public class Point2d implements DataItem {
    private double x;
    private double y;

    public Point2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }
}
