package base.config.general;

import javax.swing.*;
import java.awt.*;

/**
 * Created by galafit on 18/8/17.
 */
public class TextStyle {
    private int fontSize = 12;
    private Color fontColor;
    // Logical fonts: Serif, SansSerif, Monospaced, Dialog, and DialogInput.
    private String fontName; // Font.SANS_SERIF;
    private boolean isBold = false;
    private boolean isItalic = false;

    public TextStyle() {
    }

    public TextStyle(Color fontColor) {
        this.setFontColor(fontColor);
    }

    public Font getFont() {
        int style;
        String name = (getFontName() != null) ? getFontName() : new JLabel().getFont().getFontName();
        if(isBold() && isItalic()){
            style = Font.BOLD + Font.ITALIC;
        } else if(isBold()){
            style = Font.BOLD;
        } else if(isItalic()){
            style = Font.ITALIC;
        } else {
            style = Font.PLAIN;
        }
        return new Font(name, style, getFontSize());
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean isBold) {
        this.isBold = isBold;
    }

    public boolean isItalic() {
        return isItalic;
    }

    public void setItalic(boolean isItalic) {
        this.isItalic = isItalic;
    }
}
