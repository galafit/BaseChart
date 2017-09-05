package configuration;

import java.awt.*;

/**
 * Created by galafit on 19/8/17.
 */
public class CrosshairConfig {
    private Color lineColor = Color.GRAY;
    private int lineWidth = 2;

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }
}
