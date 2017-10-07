package base.config.axis;

import base.config.general.TextStyle;

/**
 * Created by galafit on 14/9/17.
 */
public class TitleConfig {
    private TextStyle textStyle = new TextStyle();
    private int padding = (int)(0.4 * getTextStyle().getFontSize());
    private boolean isVisible = true;

    public TextStyle getTextStyle() {
        return textStyle;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }
}
