package painters;

import configuration.CrosshairConfig;

import java.awt.*;

/**
 * Created by galafit on 19/8/17.
 */
public class CrosshairPainter {
    private CrosshairConfig crosshairConfig;

    public CrosshairPainter(CrosshairConfig crosshairConfig) {
        this.crosshairConfig = crosshairConfig;
    }

    public void draw(Graphics2D g2, Rectangle area, int x, int y){
        g2.setStroke(new BasicStroke(crosshairConfig.getLineWidth()));
        g2.setColor(crosshairConfig.getLineColor());
        g2.drawLine(x,area.y, x,area.y + area.height);
    }
}
