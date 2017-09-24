package configuration.general;

import java.awt.*;

/**
 * Created by galafit on 14/9/17.
 */
public class LineConfig {
    public int width = 1;
    public LineStroke stroke;
    public Color color;

    public boolean isVisible() {
        if(width > 0) {
            return true;
        }
        return false;
    }

    public Stroke getStroke() {
        if(stroke == null) {
            return new BasicStroke(width);
        }
        return stroke.getStroke(width);
    }

}
