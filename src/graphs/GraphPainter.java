package graphs;

import axis.Axis;
import data.XYSet;

import java.awt.*;

/**
 * Created by galafit on 20/7/17.
 */
public abstract class GraphPainter<Y> {
    private GraphSettings settings = new GraphSettings();

    public GraphSettings getSettings() {
        return settings;
    }

    public void setSettings(GraphSettings settings) {
        this.settings = settings;
    }

    public abstract void draw(XYSet<Y> dataPoints, Graphics2D g, Rectangle area, Axis xAxis, Axis yAxis);

    public abstract void drawHover(Graphics2D g, Rectangle area, Axis xAxis, Axis yAxis);

}
