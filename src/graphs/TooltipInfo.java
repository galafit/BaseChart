package graphs;

/**
 * Created by hdablin on 02.08.17.
 */
public class TooltipInfo {
    private String string;
    private double x,y;

    public TooltipInfo(String string, double x, double y) {
        this.string = string;
        this.x = x;
        this.y = y;
    }

    public String getString() {
        return string;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
