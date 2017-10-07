package base.painters;

import base.axis.Axis;
import base.config.ScrollConfig;

import java.awt.*;


/**
 * Created by galafit on 21/7/17.
 */
public class ScrollPainter {
    private Axis axis;
    private double scrollValue;
    private double scrollExtent;
    private ScrollConfig scrollConfig;


    public ScrollPainter(double scrollValue, double scrollExtent, ScrollConfig scrollConfig, Axis axis) {
        this.axis = axis;
        this.scrollValue = scrollValue;
        this.scrollExtent = scrollExtent;
        this.scrollConfig = scrollConfig;
    }

    private Rectangle getScrollRectangle(Rectangle area) {
        double scrollStart = axis.scale(scrollValue);
        double scrollEnd = axis.scale(scrollValue + scrollExtent);

        int scrollWidth = Math.max(scrollConfig.getScrollMinWidth(), (int)(scrollEnd - scrollStart));
        return new Rectangle((int)scrollStart, area.y, scrollWidth, area.height);
    }

    public double calculateScrollValue(int mouseX) {
        double value = axis.invert(mouseX);
        if(value < axis.getMin()) {
            value = axis.getMin();
        }
        if(value + scrollExtent > axis.getMax()) {
            value = axis.getMax() - scrollExtent;
        }
        return value;
    }

    public boolean isMouseInsideScroll(int mouseX, int mouseY, Rectangle area) {
        return getScrollRectangle(area).contains(mouseX, mouseY);
    }

    public void draw(Graphics2D g2, Rectangle area) {
        g2.setColor(scrollConfig.getScrollColor());
        g2.setStroke(new BasicStroke(1));
        g2.draw(getScrollRectangle(area));
    }
}
