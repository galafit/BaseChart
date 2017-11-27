package base;

import base.config.ScrollConfig;
import base.scales.Scale;

import java.awt.*;
import java.text.MessageFormat;
import java.util.*;


/**
 * Created by galafit on 21/7/17.
 */
public class Scroll {
    private Scale scale;
    private ScrollConfig scrollConfig;
    private double scrollValue;
    // two extents because we will scroll two axis (xTop and xBottom)
    private double scrollExtent0;
    private double scrollExtent1;
    private java.util.List<ScrollListener> eventListeners = new ArrayList<ScrollListener>();


    public Scroll(ScrollConfig scrollConfig, double scrollExtent0, double scrollExtent1, Scale scale) {
        this.scale = scale;
        this.scrollConfig = scrollConfig;
        this.scrollExtent0 = scrollExtent0;
        this.scrollExtent1 = scrollExtent1;
        scrollValue = getMin();
        System.out.println("scroll "+scrollValue);
    }

    public void addListener(ScrollListener listener) {
        eventListeners.add(listener);
    }

    private void fireListeners() {
        for (ScrollListener listener : eventListeners) {
            listener.onScrollChanged(scrollValue, scrollExtent0, scrollExtent1);
        }
    }

    public void setScrollExtent0(double scrollExtent0) {
        if (scrollExtent0 > getMax() - getMin()) {
            scrollExtent0 = getMax() - getMin();
        }
        if(this.scrollExtent0 != scrollExtent0) {
            this.scrollExtent0 = scrollExtent0;
            checkBounds();
            fireListeners();
        }
    }

    public void setScrollExtent1(double scrollExtent1) {
        if (scrollExtent1 > getMax() - getMin()) {
            scrollExtent1 = getMax() - getMin();
        }
        if(this.scrollExtent1 != scrollExtent1) {
            this.scrollExtent1 = scrollExtent1;
            checkBounds();
            fireListeners();
        }
    }

    public double getScrollExtent0() {
        return scrollExtent0;
    }

    public double getScrollExtent1() {
        return scrollExtent1;
    }


    private Range getScrollRange0() {
        if (scrollExtent0 == 0) {
            return null;
        }
        double scrollStart = scale.scale(scrollValue);
        double scrollEnd = scale.scale(scrollValue + scrollExtent0);
        int scrollWidth = Math.max(scrollConfig.getScrollMinWidth(), (int) (scrollEnd - scrollStart));
        if (scrollStart + scrollConfig.getScrollMinWidth() > getEnd()) { // prevent that actually thin scroll moves outside screen
            scrollStart = getEnd() - scrollConfig.getScrollMinWidth();
        }
        return new Range(scrollStart, scrollStart + scrollWidth);
    }

    private Range getScrollRange1() {
        if (scrollExtent1 == 0) {
            return null;
        }
        double scrollStart = scale.scale(scrollValue);
        double scrollEnd = scale.scale(scrollValue + scrollExtent1);
        int scrollWidth = Math.max(scrollConfig.getScrollMinWidth(), (int) (scrollEnd - scrollStart));
        if (scrollStart + scrollConfig.getScrollMinWidth() > getEnd()) { // prevent that actually thin scroll moves outside screen
            scrollStart = getEnd() - scrollConfig.getScrollMinWidth();
        }
        return new Range(scrollStart, scrollStart + scrollWidth);
    }


    public double getMinExtent() {
        double extent = scrollExtent0;
        if(scrollExtent0 == 0) {
            extent = scrollExtent1;
        }
        if(scrollExtent0 > 0 && scrollExtent1 > 0) {
            extent = Math.min(scrollExtent0, scrollExtent1);
        }
        return extent;
    }

    public double getRation() {
        return (getMax() - getMin()) / getMinExtent();
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
        if (scrollValue != oldValue) {
            fireListeners();
            return true;
        }
        return false;
    }

    public void setMinMax(Range minMaxRange) {
        if (minMaxRange == null) {
            return;
        }
        double min = minMaxRange.start();
        double max = minMaxRange.end();
        if (min > max) {
            String errorMessage = "Error during setMinMax(). Expected Min < Max. Min = {0}, Max = {1}.";
            String formattedError = MessageFormat.format(errorMessage, min, max);
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
        return scale.getDomain()[scale.getDomain().length - 1];
    }

    double getStart() {
        return scale.getRange()[0];
    }

    double getEnd() {
        return scale.getRange()[scale.getRange().length - 1];
    }

    public double getValue() {
        return scrollValue;
    }

    private void checkBounds() {
        if (scrollValue + Math.min(scrollExtent0, scrollExtent1) > getMax()) {
            scrollValue = getMax() - Math.min(scrollExtent0, scrollExtent1);
        }
        if (scrollValue < getMin()) {
            scrollValue = getMin();
        }
    }


    public Range getScrollExtremes1() {
        return new Range(scrollValue, scrollValue + scrollExtent0);
    }

    public Range getScrollExtremes2() {
        return new Range(scrollValue, scrollValue + scrollExtent1);
    }

    public boolean isPointInsideScroll(int x) {
        Range scrollRange0 = getScrollRange0();
        Range scrollRange1 = getScrollRange1();
        return (scrollRange0 != null && scrollRange0.contains(x)) || (scrollRange1 != null && getScrollRange1().contains(x));
    }

    public void draw(Graphics2D g2, Rectangle area) {
        g2.setColor(scrollConfig.getScrollColor());
        g2.setStroke(new BasicStroke(1));
        Range scrollRange0 = getScrollRange0();
        if (scrollRange0 != null) {
            g2.draw(new Rectangle((int) scrollRange0.start(), area.y, (int) scrollRange0.length(), area.height));

        }
        Range scrollRange1 = getScrollRange1();
        if (scrollRange1 != null) {
            g2.draw(new Rectangle((int) scrollRange1.start(), area.y, (int) scrollRange1.length(), area.height));

        }
    }
}
