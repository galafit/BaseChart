package base.config;

import java.awt.*;

/**
 * Created by galafit on 19/8/17.
 */
public class CrosshairConfig {
    private Color lineColor = new Color(250, 250, 250);
    private int lineWidth = 1;

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }
}
