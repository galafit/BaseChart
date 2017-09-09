package configuration;

import axis.LineStyle;

import java.awt.*;

/**
 * Created by galafit on 5/9/17.
 */
public class LinesConfig {
    public Color axisLineColor = Color.GRAY;
    public Color gridColor = new Color(50, 50, 50);
    public Color minorGridColor = new Color(25, 25, 25);

    public int axisLineWidth = 1;
    public int gridLineWidth = 1;
    public int minorGridLineWidth = 0;

    public LineStyle gridLineStyle = LineStyle.SOLID;
    public LineStyle minorGridLineStyle = LineStyle.SOLID;

    public int minorGridCounter = 5; // minor grid divider

    public boolean isGridVisible() {
        return (gridLineWidth > 0) ? true : false;
    }

    public boolean isMinorGridVisible() {
        return (minorGridLineWidth > 0) ? true : false;
    }

    public boolean isAxisLineVisible() {
        return (axisLineWidth > 0) ? true : false;
    }
}
