package base.chart;

import base.Range;
import base.axis.AxisType;
import base.config.ScrollConfig;
import base.scales.Scale;
import base.scales.ScaleLinear;

import java.awt.*;
import java.text.MessageFormat;


/**
 * Created by galafit on 21/7/17.
 */
public class Scroll {
    private Scale scale;
    private ScrollConfig scrollConfig;
    private double scrollValue = 0;
    // two extents because we will scroll two axis (xTop and xBottom)
    private double scrollExtent1;
    private double scrollExtent2;

    public Scroll(ScrollConfig scrollConfig, double scrollExtent1, double scrollExtent2, AxisType axisType) {
        scale = new ScaleLinear();
        if(axisType == AxisType.LOGARITHMIC) {
            // scale = new ScaleLogarithmic;
        }
        this.scrollConfig = scrollConfig;
        this.scrollExtent1 = scrollExtent1;
        this.scrollExtent2 = scrollExtent2;
    }

    public void setScrollExtent1(double scrollExtent1) {
        if(scrollExtent1 > getMax() - getMin()) {
            scrollExtent1 = getMax() - getMin();
        }
        this.scrollExtent1 = scrollExtent1;
        checkBounds();
    }

    public void setScrollExtent2(double scrollExtent2) {
        if(scrollExtent2 > getMax() - getMin()) {
            scrollExtent2 = getMax() - getMin();
        }
        this.scrollExtent2 = scrollExtent2;
        checkBounds();

    }

    public double getScrollExtent1() {
        return scrollExtent1;
    }

    public double getScrollExtent2() {
        return scrollExtent2;
    }

    private Rectangle getScrollRectangle1(Rectangle area) {
        double scrollStart = scale.scale(scrollValue);
        double scrollEnd = scale.scale(scrollValue + scrollExtent1);
        int scrollWidth = Math.max(scrollConfig.getScrollMinWidth(), (int)(scrollEnd - scrollStart));
        if(scrollStart + scrollConfig.getScrollMinWidth() > getEnd()) { // prevent that actually thin scroll moves outside screen
            scrollStart = getEnd() - scrollConfig.getScrollMinWidth();
        }
        return new Rectangle((int)scrollStart, area.y, scrollWidth, area.height);
    }

    private Rectangle getScrollRectangle2(Rectangle area) {
        double scrollStart = scale.scale(scrollValue);
        double scrollEnd = scale.scale(scrollValue + scrollExtent2);
        int scrollWidth = Math.max(scrollConfig.getScrollMinWidth(), (int)(scrollEnd - scrollStart));
        if(scrollStart + scrollConfig.getScrollMinWidth() > getEnd()) { // prevent that actually thin scroll moves outside screen
            scrollStart = getEnd() - scrollConfig.getScrollMinWidth();
        }
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

    public void shiftScroll(int dx) {
        int x = (int)scale.scale(scrollValue);
        int newX = x + dx;
        moveScroll(scale.invert(newX));
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

    public void setStartEnd(Range startEnd) {
        scale.setRange(startEnd.start(), startEnd.end());
    }

    double getMin() {
        return scale.getDomain()[0];
    }

    double getMax() {
        return scale.getDomain()[scale.getDomain().length -1];
    }

    double getStart() {
        return scale.getRange()[0];
    }

    double getEnd() {
        return scale.getRange()[scale.getRange().length -1];
    }


    private void checkBounds() {
        if(scrollValue + Math.min(scrollExtent1, scrollExtent2) > getMax()) {
            scrollValue = getMax() - Math.min(scrollExtent1, scrollExtent2);
        }
        if(scrollValue < getMin()) {
            scrollValue = getMin();
        }
    }

    /**
     * @return true if scrollValue was changed and false if newValue = current scroll value
     */
    public boolean moveScroll(double newValue) {
        double oldValue = scrollValue;
        scrollValue = newValue;
        checkBounds();
        if(scrollValue != oldValue) {
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

    public boolean isMouseInsideScroll(int x, int y, Rectangle area) {
        return getScrollRectangle1(area).contains(x, y) || getScrollRectangle2(area).contains(x, y) ;
    }

    public void draw(Graphics2D g2, Rectangle area) {
        g2.setColor(scrollConfig.getScrollColor());
        g2.setStroke(new BasicStroke(1));
        g2.draw(getScrollRectangle1(area));
        g2.draw(getScrollRectangle2(area));
    }
}
