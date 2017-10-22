package base.chart;

import base.Range;
import base.config.ScrollConfig;
import base.scales.Scale;

import java.awt.*;
import java.text.MessageFormat;


/**
 * Created by galafit on 21/7/17.
 */
public class Scroll {
    private Scale scale;
    private ScrollConfig scrollConfig;
    private double scrollValue = 0;
    // two extents because we will scroll two scale (xTop and xBottom)
    private double scrollExtent1;
    private double scrollExtent2;

    public Scroll(ScrollConfig scrollConfig, double scrollExtent1, double scrollExtent2, Scale scale) {
        this.scale = scale;
        this.scrollConfig = scrollConfig;
        this.scrollExtent1 = scrollExtent1;
        this.scrollExtent2 = scrollExtent2;
    }


    private Rectangle getScrollRectangle(Rectangle area) {
        double scrollStart = scale.scale(scrollValue);
        double scrollEnd = scale.scale(scrollValue + getScrollExtent());

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
        double value = scale.invert(mouseX);
        return moveScroll(value);
    }

    public void setMinMax(Range minMaxRange) {
        if(minMaxRange == null) {
            return;
        }
        double min = minMaxRange.start();
        double max = minMaxRange.end();
        if (min > max){
            String errorMessage = "Error during setMinMax(). Expected Min < Max. Min = {0}, Max = {1}.";
            String formattedError = MessageFormat.format(errorMessage,min,max);
            throw new IllegalArgumentException(formattedError);
        }
        scale.setDomain(min, max);
    }

    public void setStartEnd(double start, double end) {
        scale.setRange(start, end);
    }

    double getMin() {
        return scale.getDomain()[0];
    }

    double getMax() {
        return scale.getDomain()[scale.getDomain().length -1];
    }


    /**
     * @return true if scrollValue was changed and false if newValue = current scroll value
     */
    public boolean moveScroll(double newValue) {

        if(newValue < getMin()) {
            newValue = getMin();
        }
        if(newValue + getScrollExtent() > getMax()) {
            newValue = getMax() - getScrollExtent();
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
