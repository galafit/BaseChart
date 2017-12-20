package base.painters;

import base.config.CrosshairConfig;

import java.awt.*;

/**
 * Created by galafit on 19/8/17.
 */
public class CrosshairPainter {
    private CrosshairConfig crosshairConfig;
    private int x, y;

    public CrosshairPainter(CrosshairConfig crosshairConfig) {
        this.crosshairConfig = crosshairConfig;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics2D g2, Rectangle area){
        g2.setStroke(new BasicStroke(crosshairConfig.getLineWidth()));
        g2.setColor(crosshairConfig.getLineColor());
        g2.drawLine(x,area.y, x,area.y + area.height);
        g2.drawLine(area.x, y, area.x + area.width,y);
    }
}
