package configuration.general;

import java.awt.*;

/**
 * Created by galafit on 14/9/17.
 */
public class MarkConfig {
    public Color color;
    public int size;

    public boolean isMarksVisible() {
        return (size > 0) ? true: false;
    }
}
