package configuration;

import java.awt.*;

/**
 * Created by galafit on 18/8/17.
 */
public class LegendConfig {
    public boolean isVisible = false;
    public TextStyle textStyle = new TextStyle();
    public Position position = Position.TOP_CENTER;
    public Color background = Color.WHITE;
    public Color borderColor = Color.LIGHT_GRAY;
    public int borderWidth = 0;
    public Margin margin = new Margin((int)(textStyle.fontSize * 0),
            (int)(textStyle.fontSize * 1),
            (int)(textStyle.fontSize * 0.5),
            (int)(textStyle.fontSize * 1));
}
