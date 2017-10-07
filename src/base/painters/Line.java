package base.painters;

import java.awt.*;

/**
 * Created by galafit on 10/9/17.
 */
public class Line {
    private int xStart;
    private int yStart;
    private int xEnd;
    private int yEnd;

    public Line(int xStart, int yStart, int xEnd, int yEnd) {
        this.xStart = xStart;
        this.yStart = yStart;
        this.xEnd = xEnd;
        this.yEnd = yEnd;
    }

    public void draw(Graphics g) {
       g.drawLine(xStart, yStart, xEnd, yEnd);
    }

    @Override
    public String toString() {
        return "Line{" +
                "xStart=" + xStart +
                ", yStart=" + yStart +
                ", xEnd=" + xEnd +
                ", yEnd=" + yEnd +
                '}';
    }
}
