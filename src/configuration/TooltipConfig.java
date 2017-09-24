package configuration;

import configuration.general.Margin;
import configuration.general.TextStyle;

import java.awt.*;

/**
 * Created by galafit on 19/8/17.
 */
public class TooltipConfig {
    public TextStyle textStyle = new TextStyle();
    public Color background = new Color(220, 220, 220);
    public Color borderColor = new Color(100, 100, 100);
    public int borderWidth = 1;
    public Margin margin = new Margin((int)(textStyle.fontSize * 0.4),
            (int)(textStyle.fontSize * 0.8),
            (int)(textStyle.fontSize * 0.4),
            (int)(textStyle.fontSize * 0.8));
}
