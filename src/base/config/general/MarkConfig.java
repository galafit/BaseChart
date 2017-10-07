package base.config.general;

import java.awt.*;

/**
 * Created by galafit on 14/9/17.
 */
public class MarkConfig {
    private Color color;
    private int size;

    public boolean isMarksVisible() {
        return (getSize() > 0) ? true: false;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
