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
    private double scrollValue;
    private double scrollExtent;
    private List<ScrollListener> eventListeners = new ArrayList<ScrollListener>();


    public Scroll(double scrollExtent, ScrollConfig scrollConfig, Scale scale) {
        this.scale = scale;
        this.scrollConfig = scrollConfig;
        setExtent(scrollExtent);
        scrollValue = getMin();
    }

    public void addListener(ScrollListener listener) {
        eventListeners.add(listener);
    }

    private void fireListeners() {
        for (ScrollListener listener : eventListeners) {
            listener.onScrollChanged(scrollValue, scrollExtent);
        }
    }

    public void setExtent(double scrollExtent) {
        if (scrollExtent > getMax() - getMin() || scrollExtent <= 0) {
            scrollExtent = getMax() - getMin();
        }
        if(this.scrollExtent != scrollExtent) {
            this.scrollExtent = scrollExtent;
            checkBounds();
            fireListeners();
        }
    }


    private Range getScrollRange() {
        double scrollStart = scale.scale(scrollValue);
        double scrollEnd = scale.scale(scrollValue + scrollExtent);
        int scrollWidth = Math.max(scrollConfig.getScrollMinWidth(), (int) (scrollEnd - scrollStart));
        if (scrollStart + scrollConfig.getScrollMinWidth() > getEnd()) { // prevent that actually thin scroll moves outside screen
            scrollStart = getEnd() - scrollConfig.getScrollMinWidth();
        }
        return new Range(scrollStart, scrollStart + scrollWidth);
    }


    public double getRation() {
        return (getMax() - getMin()) / scrollExtent;
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

    public double getValue() {
        return scrollValue;
    }

    public double getExtent() {
        return scrollExtent;
    }


    private void checkBounds() {
        if (scrollValue + scrollExtent > getMax()) {
            scrollValue = getMax() - scrollExtent;
        }
        if (scrollValue < getMin()) {
            scrollValue = getMin();
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
