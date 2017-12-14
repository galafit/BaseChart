package base;

import base.config.ScrollConfig;
import base.scales.Scale;

import java.awt.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.List;


/**
 * Created by galafit on 21/7/17.
 */
public class Scroll {
    private Scale scale;
    private ScrollConfig scrollConfig;
    private double value;
    private double extent;
    private List<ScrollListener> eventListeners = new ArrayList<ScrollListener>();


    public Scroll(double scrollExtent, ScrollConfig scrollConfig, Scale scale) {
        this.scale = scale;
        this.scrollConfig = scrollConfig;
        setExtent(scrollExtent);
        value = getMin();
    }

    public void addListener(ScrollListener listener) {
        eventListeners.add(listener);
    }

    private void fireListeners() {
        for (ScrollListener listener : eventListeners) {
            listener.onScrollChanged(value, extent);
        }
    }

    public void setExtent(double scrollExtent) {
        if (scrollExtent > getMax() - getMin() || scrollExtent <= 0) {
            scrollExtent = getMax() - getMin();
        }
        if(this.extent != scrollExtent) {
            this.extent = scrollExtent;
            checkBounds();
            fireListeners();
        }
    }


    private Range getScrollRange() {
        double scrollStart = scale.scale(value);
        double scrollEnd = scale.scale(value + extent);
        int scrollWidth = Math.max(scrollConfig.getScrollMinWidth(), (int) (scrollEnd - scrollStart));
        if (scrollStart + scrollConfig.getScrollMinWidth() > getEnd()) { // prevent that actually thin scroll moves outside screen
            scrollStart = getEnd() - scrollConfig.getScrollMinWidth();
        }
        return new Range(scrollStart, scrollStart + scrollWidth);
    }

    public double getExtent() {
        return extent;
    }

    public double getPosition() {
        return scale.scale(value);
    }

    /**
     * @return true if value was changed and false if newValue = current scroll value
     */
    public boolean setPosition(double x) {
        double value = scale.invert(x);
        return setValue(value);
    }

    public double getValue() {
        return value;
    }

    public double getWidth() {
        double scrollStart = scale.scale(value);
        double scrollEnd = scale.scale(value + extent);
        return scrollEnd - scrollStart;
    }


    /**
     * @return true if value was changed and false if newValue = current scroll value
     */
    public boolean setValue(double newValue) {
        double oldValue = value;
        value = newValue;
        checkBounds();
        if (value != oldValue) {
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

    private double getMin() {
        return scale.getDomain()[0];
    }

    private double getMax() {
        return scale.getDomain()[scale.getDomain().length - 1];
    }

    private double getStart() {
        return scale.getRange()[0];
    }

    private double getEnd() {
        return scale.getRange()[scale.getRange().length - 1];
    }

    private void checkBounds() {
        if (value + extent > getMax()) {
            value = getMax() - extent;
        }
        if (value < getMin()) {
            value = getMin();
        }
    }


    public boolean isPointInsideScroll(int x) {
        return getScrollRange().contains(x);
    }

    public void draw(Graphics2D g2, Rectangle area) {
        g2.setColor(scrollConfig.getScrollColor());
        g2.setStroke(new BasicStroke(1));
        Range scrollRange = getScrollRange();
        g2.draw(new Rectangle((int) scrollRange.start(), area.y, (int) scrollRange.length(), area.height));
    }
}
