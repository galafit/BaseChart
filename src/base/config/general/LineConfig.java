package base.config.general;

import java.awt.*;

/**
 * Created by galafit on 14/9/17.
 */
public class LineConfig {
    private int width = 1;
    private LineStroke stroke;
    private Color color;

    public boolean isVisible() {
        if(getWidth() > 0) {
            return true;
        }
        return false;
    }



    public Stroke getStroke() {
        if(stroke == null) {
            return new BasicStroke(getWidth());
        }
        return stroke.getStroke(getWidth());
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
