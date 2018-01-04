package base.config;

import base.BColor;

/**
 * Created by galafit on 14/9/17.
 */
public class MarkConfig {
    private BColor color;
    private int size = 4;

    public boolean isMarksVisible() {
        return (getSize() > 0) ? true: false;
    }

    public BColor getColor() {
        return color;
    }

    public void setColor(BColor color) {
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
