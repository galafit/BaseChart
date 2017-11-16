package base.chart;

import base.Range;
import base.axis.AxisType;
import base.config.ScrollConfig;
import base.scales.Scale;
import base.scales.ScaleLinear;

import java.awt.*;
import java.text.MessageFormat;
import java.util.*;


/**
 * Created by galafit on 21/7/17.
 */
public class Scroll {
    private Scale scale;
    private ScrollConfig scrollConfig;
    private double scrollValue = 0;
    // two extents because we will scroll two axis (xTop and xBottom)
    private double scrollExtent0;
    private double scrollExtent1;
    private java.util.List<ScrollListener> eventListeners = new ArrayList<ScrollListener>();


    public Scroll(ScrollConfig scrollConfig, double scrollExtent0, double scrollExtent1, AxisType axisType) {
        scale = new ScaleLinear();
        if(axisType == AxisType.LOGARITHMIC) {
            // scale = new ScaleLogarithmic;
        }
        this.scrollConfig = scrollConfig;
        this.scrollExtent0 = scrollExtent0;
        this.scrollExtent1 = scrollExtent1;
    }

    public void addListener(ScrollListener listener) {
        eventListeners.add(listener);
    }

    private void fireListeners(){
        for (ScrollListener listener : eventListeners) {
            listener.onScrollMoved(scrollValue, scrollExtent0, scrollExtent1);
        }
    }

    public void setScrollExtent0(double scrollExtent0) {
        if(scrollExtent0 > getMax() - getMin()) {
            scrollExtent0 = getMax() - getMin();
        }
        this.scrollExtent0 = scrollExtent0;
        checkBounds();
    }

    public void setScrollExtent1(double scrollExtent1) {
        if(scrollExtent1 > getMax() - getMin()) {
            scrollExtent1 = getMax() - getMin();
        }
        this.scrollExtent1 = scrollExtent1;
        checkBounds();
    }

    public double getScrollExtent0() {
        return scrollExtent0;
    }

    public double getScrollExtent1() {
        return scrollExtent1;
    }


    private Rectangle getScrollRectangle1(Rectangle area) {
        double scrollStart = scale.scale(scrollValue);
        double scrollEnd = scale.scale(scrollValue + scrollExtent0);
        int scrollWidth = Math.max(scrollConfig.getScrollMinWidth(), (int)(scrollEnd - scrollStart));
        if(scrollStart + scrollConfig.getScrollMinWidth() > getEnd()) { // prevent that actually thin scroll moves outside screen
            scrollStart = getEnd() - scrollConfig.getScrollMinWidth();
        }
        return new Rectangle((int)scrollStart, area.y, scrollWidth, area.height);
    }

    private Rectangle getScrollRectangle2(Rectangle area) {
        double scrollStart = scale.scale(scrollValue);
        double scrollEnd = scale.scale(scrollValue + scrollExtent1);
        int scrollWidth = Math.max(scrollConfig.getScrollMinWidth(), (int)(scrollEnd - scrollStart));
        if(scrollStart + scrollConfig.getScrollMinWidth() > getEnd()) { // prevent that actually thin scroll moves outside screen
            scrollStart = getEnd() - scrollConfig.getScrollMinWidth();
        }
        return new Rectangle((int)scrollStart, area.y, scrollWidth, area.height);
    }

    public double getRation() {
        return (getMax() - getMin()) / Math.min(scrollExtent0, scrollExtent1);
    }

    /**
     * @return true if scrollValue was changed and false if newValue = current scroll value
     */
    public boolean moveScrollTo(int x, int y) {
        double value = scale.invert(x);
        return moveScrollTo(value);
    }

    public boolean translateScroll(double translation) {
        double scrollPosition = scale.scale(scrollValue);
        double newScrollPosition = scrollPosition + translation;
        return moveScrollTo(scale.invert(newScrollPosition));
    }

    /**
     * @return true if scrollValue was changed and false if newValue = current scroll value
     */
    public boolean moveScrollTo(double newValue) {
        double oldValue = scrollValue;
        scrollValue = newValue;
        checkBounds();
        if(scrollValue != oldValue) {
            fireListeners();
            return true;
        }
        return false;
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

    public double getValue() {
        return scrollValue;
    }

    private void checkBounds() {
        if(scrollValue + Math.min(scrollExtent0, scrollExtent1) > getMax()) {
            scrollValue = getMax() - Math.min(scrollExtent0, scrollExtent1);
        }
        if(scrollValue < getMin()) {
            scrollValue = getMin();
        }
    }


    public Range getScrollExtremes1() {
        return new Range(scrollValue, scrollValue + scrollExtent0);
    }

    public Range getScrollExtremes2() {
        return new Range(scrollValue, scrollValue + scrollExtent1);
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
