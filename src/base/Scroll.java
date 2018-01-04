package base;

import base.config.ScrollConfig;
import base.scales.Scale;

import java.text.MessageFormat;
import java.util.*;
import java.util.List;


/**
 * Created by galafit on 21/7/17.
 */
public class Scroll {
    private Scale scale;
    private ScrollConfig scrollConfig;
    private float value;
    private float extent;
    private List<ScrollListener> eventListeners = new ArrayList<ScrollListener>();


    public Scroll(float scrollExtent, ScrollConfig scrollConfig, Scale scale) {
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

    public void setExtent(float scrollExtent) {
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
        float scrollStart = scale.scale(value);
        float scrollEnd = scale.scale(value + extent);
        int scrollWidth = Math.max(scrollConfig.getScrollMinWidth(), (int) (scrollEnd - scrollStart));
        if (scrollStart + scrollConfig.getScrollMinWidth() > getEnd()) { // prevent that actually thin scroll moves outside screen
            scrollStart = getEnd() - scrollConfig.getScrollMinWidth();
        }
        return new Range(scrollStart, scrollStart + scrollWidth);
    }

    public float getExtent() {
        return extent;
    }

    public float getPosition() {
        return scale.scale(value);
    }

    /**
     * @return true if value was changed and false if newValue = current scroll value
     */
    public boolean setPosition(float x) {
        float value = scale.invert(x);
        return setValue(value);
    }

    public float getValue() {
        return value;
    }

    public float getWidth() {
        float scrollStart = scale.scale(value);
        float scrollEnd = scale.scale(value + extent);
        return scrollEnd - scrollStart;
    }


    /**
     * @return true if value was changed and false if newValue = current scroll value
     */
    public boolean setValue(float newValue) {
        float oldValue = value;
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
        float min = minMaxRange.start();
        float max = minMaxRange.end();
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

    private float getMin() {
        return scale.getDomain()[0];
    }

    private float getMax() {
        return scale.getDomain()[scale.getDomain().length - 1];
    }

    private float getStart() {
        return scale.getRange()[0];
    }

    private float getEnd() {
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

    public void draw(BCanvas canvas, BRectangle area) {
        canvas.setColor(scrollConfig.getScrollColor());
        canvas.setStroke(new BStroke(1));
        Range scrollRange = getScrollRange();
        canvas.drawRect((int)scrollRange.start(), area.y, (int) scrollRange.length(), area.height);
    }
}
