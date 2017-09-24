package configuration.axis;

import configuration.general.TextStyle;

/**
 * Created by galafit on 14/9/17.
 */
public class TitleConfig {
    public TextStyle textStyle = new TextStyle();
    public int padding = (int)(0.4 * textStyle.fontSize);
    public boolean isVisible = true;
}
