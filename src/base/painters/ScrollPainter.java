package base.painters;

import base.Range;
import base.axis.Axis;
import base.config.ScrollConfig;

import java.awt.*;


/**
 * Created by galafit on 21/7/17.
 */
public class ScrollPainter {
    private Axis axis;
    private ScrollConfig scrollConfig;
    private double scrollValue = 0;
    // two extents because we will scroll two axis (xTop and xBottom)
    private double scrollExtent1;
    private double scrollExtent2;

    public ScrollPainter(ScrollConfig scrollConfig, double scrollExtent1, double scrollExtent2, Axis axis) {
        this.axis = axis;
        this.scrollConfig = scrollConfig;
        this.scrollExtent1 = scrollExtent1;
        this.scrollExtent2 = scrollExtent2;
    }

    public ScrollPainter(ScrollConfig scrollConfig, double scrollExtent, Axis axis) {
       this(scrollConfig, scrollExtent, scrollExtent, axis);
    }


    private Rectangle getScrollRectangle(Rectangle area) {
        double scrollStart = axis.scale(scrollValue);
        double scrollEnd = axis.scale(scrollValue + getScrollExtent());

        int scrollWidth = Math.max(scrollConfig.getScrollMinWidth(), (int)(scrollEnd - scrollStart));
        return new Rectangle((int)scrollStart, area.y, scrollWidth, area.height);
    }

    private double getScrollExtent() {
        return Math.min(scrollExtent1, scrollExtent2);
    }

    /**
     * @return true if scrollValue was changed and false if newValue = current scroll value
     */
    public boolean moveScroll(int mouseX, int mouseY) {
        double value = axis.invert(mouseX);
        return moveScroll(value);
    }

    /**
     * @return true if scrollValue was changed and false if newValue = current scroll value
     */
    public boolean moveScroll(double newValue) {
        if(newValue < axis.getMin()) {
            newValue = axis.getMin();
        }
        if(newValue + getScrollExtent() > axis.getMax()) {
            newValue = axis.getMax() - getScrollExtent();
        }
        if(scrollValue != newValue) {
            scrollValue = newValue;
            return true;
        }
        return false;
    }

    public Range getScrollExtremes1() {
        return new Range(scrollValue, scrollValue + scrollExtent1);
    }

    public Range getScrollExtremes2() {
        return new Range(scrollValue, scrollValue + scrollExtent2);
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
