package configuration;

import java.awt.*;

/**
 * Created by galafit on 19/8/17.
 */
public class TooltipConfig {
    private TextStyle textStyle = new TextStyle();
    private Color background = new Color(220, 220, 220);
    private Color borderColor = new Color(100, 100, 100);
    private int borderWidth = 1;
    private Padding padding;


    public TextStyle getTextStyle() {
        return textStyle;
    }

    public void setTextStyle(TextStyle textStyle) {
        this.textStyle = textStyle;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    public void setPadding(Padding padding) {
        this.padding = padding;
    }

    public Padding getPadding(){
        if(padding != null) {
            return padding;
        }
        return new Padding((int)(getTextStyle().getFontSize() * 0.4),
                (int)(getTextStyle().getFontSize() * 0.8),
                (int)(getTextStyle().getFontSize() * 0.4),
                (int)(getTextStyle().getFontSize() * 0.8));
    }
}
