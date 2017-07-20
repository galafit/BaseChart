package graphs;

import java.awt.*;

/**
 * Created by galafit on 20/7/17.
 */
public class GraphSettings {
    protected Color lineColor = Color.GRAY;
    protected Color fillColor = Color.GRAY;
    protected int lineWidth = 1;
    protected int pointRadious = 1;

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }
}
