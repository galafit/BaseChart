package base.config;

import base.BColor;

/**
 * Created by galafit on 19/8/17.
 */
public class CrosshairConfig {
    private BColor lineColor = new BColor(250, 250, 250);
    private int lineWidth = 1;

    public BColor getLineColor() {
        return lineColor;
    }

    public void setLineColor(BColor lineColor) {
        this.lineColor = lineColor;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }
}
