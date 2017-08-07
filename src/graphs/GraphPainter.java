package graphs;

import axis.Axis;
import data.Range;
import data.XYOrderedSet;
import data.XYPoint;
import data.XYSet;

import java.awt.*;

/**
 * Created by galafit on 20/7/17.
 */
public abstract class GraphPainter<Y> {
    protected XYOrderedSet<Y> dataPoints;
    protected int hoverIndex = -1;

    private GraphSettings settings = new GraphSettings();

    public GraphSettings getSettings() {
        return settings;
    }

    public void setSettings(GraphSettings settings) {
        this.settings = settings;
    }

    public abstract void draw(XYSet<Y> dataPoints, Graphics2D g, Rectangle area, Axis xAxis, Axis yAxis);

    public abstract void drawHover(Graphics2D g, Rectangle area, Axis xAxis, Axis yAxis);

    public abstract boolean hover(int mouseX, int mouseY, Axis xAxis, Axis yAxis);

    public XYPoint<Y> getHoverPoint(){
        if (hoverIndex >= 0) {
            return new XYPoint<Y>(dataPoints.getX(hoverIndex), dataPoints.getY(hoverIndex));
        }
        return null;
    }

    public abstract Range getYPixelRange();
}
