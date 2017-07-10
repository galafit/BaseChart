package main.chart.axis;

import java.awt.*;

/**
 * Created by galafit on 13/6/17.
 */
public class GridSettings {
    private int gridLineWidth = 1;
    private LineStyle gridLineStyle = LineStyle.SOLID;
    private LineStyle minorGridLineStyle = LineStyle.SOLID;
    private int minorGridLineWidth = 0;
    private Color gridColor = new Color(50, 50, 50);
    private Color minorGridColor = new Color(25, 25, 25);
    private int minorGridDivider = 5; // minor grid counter

    public boolean isGridVisible() {
        return (gridLineWidth > 0) ? true : false;
    }

    public boolean isMinorGridVisible() {
        return (minorGridLineWidth > 0) ? true : false;
    }

    public int getGridLineWidth() {
        return gridLineWidth;
    }

    public void setGridLineWidth(int gridLineWidth) {
        this.gridLineWidth = gridLineWidth;
    }

    public LineStyle getGridLineStyle() {
        return gridLineStyle;
    }

    public void setGridLineStyle(LineStyle gridLineStyle) {
        this.gridLineStyle = gridLineStyle;
    }

    public LineStyle getMinorGridLineStyle() {
        return minorGridLineStyle;
    }

    public void setMinorGridLineStyle(LineStyle minorGridLineStyle) {
        this.minorGridLineStyle = minorGridLineStyle;
    }

    public int getMinorGridLineWidth() {
        return minorGridLineWidth;
    }

    public void setMinorGridLineWidth(int minorGridLineWidth) {
        this.minorGridLineWidth = minorGridLineWidth;
    }

    public Color getGridColor() {
        return gridColor;
    }

    public void setGridColor(Color gridColor) {
        this.gridColor = gridColor;
    }

    public Color getMinorGridColor() {
        return minorGridColor;
    }

    public void setMinorGridColor(Color minorGridColor) {
        this.minorGridColor = minorGridColor;
    }

    public int getMinorGridDivider() {
        return minorGridDivider;
    }

    public void setMinorGridDivider(int minorGridDivider) {
        this.minorGridDivider = minorGridDivider;
    }
}
