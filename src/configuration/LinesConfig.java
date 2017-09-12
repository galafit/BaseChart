package configuration;

import axis.LineStyle;

import java.awt.*;

/**
 * Created by galafit on 5/9/17.
 */
public class LinesConfig {
    public Color axisLineColor = Color.GRAY;
    public Color gridColor =  new Color(100, 100, 100);
    public Color minorGridColor =  new Color(80, 80, 80);

    public int axisLineWidth = 1;
    public int gridLineWidth = 1;
    public int minorGridLineWidth = 1;

    private LineStyle gridLineStyle = LineStyle.SOLID;
    private LineStyle minorGridLineStyle = LineStyle.DOT;

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

    public void setGridLineStyle(LineStyle gridLineStyle) {
        this.gridLineStyle = gridLineStyle;
    }

    public void setMinorGridLineStyle(LineStyle minorGridLineStyle) {
        this.minorGridLineStyle = minorGridLineStyle;
    }

    public Stroke getGridLineStroke() {
        return gridLineStyle.getStroke(gridLineWidth);
    }

    public Stroke getMinorGridLineStroke() {
        return minorGridLineStyle.getStroke(minorGridLineWidth);
    }

    public Stroke getAxisLineStroke() {
        return new BasicStroke(axisLineWidth);
    }
}
