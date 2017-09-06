package configuration;

import java.awt.*;

/**
 * Created by galafit on 18/8/17.
 */
public class TextStyle {
    public int fontSize = 12;
    public Color fontColor = Color.BLACK;
    // Logical fonts: Serif, SansSerif, Monospaced, Dialog, and DialogInput.
    public String fontName = Font.SANS_SERIF;
    public boolean isBold = false;
    public boolean isItalic = false;

    public Font getFont() {
        int style;

        if(isBold && isItalic){
            style = Font.BOLD + Font.ITALIC;
        } else if(isBold){
            style = Font.BOLD;
        } else if(isItalic){
            style = Font.ITALIC;
        } else {
            style = Font.PLAIN;
        }
        return new Font(fontName, style, fontSize);
    }
}
