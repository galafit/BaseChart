package tooltips;

import axis.ScientificNumber;
import com.sun.javafx.tk.*;

import java.awt.*;
import java.awt.FontMetrics;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by hdablin on 02.08.17.
 */
public class TooltipPainter {
    Color backgroundColor = Color.DARK_GRAY;
    Color borderColor = Color.CYAN;
    Color fontColor = Color.WHITE;
    String font = Font.SANS_SERIF;
    int fontSize = 14;

    public void draw(Graphics2D g2d, Rectangle area, Number x, Number y, String string){
        Padding padding = getPadding();
        Font tooltipFont = new Font(font, Font.PLAIN, fontSize);
        g2d.setFont(tooltipFont);

        String[] strings = string.split("<br>");

        Dimension tooltipDimention = getTextSize(g2d, strings);

        Rectangle tooltipArea = new Rectangle(x.intValue() +20,y.intValue() +20,tooltipDimention.width,tooltipDimention.height);
        g2d.setColor(backgroundColor);
        g2d.fillRect(tooltipArea.x, tooltipArea.y, tooltipArea.width, tooltipArea.height);
        g2d.setColor(borderColor);
        g2d.drawRect(tooltipArea.x, tooltipArea.y, tooltipArea.width, tooltipArea.height);

        g2d.setColor(fontColor);
        drawText(g2d, tooltipArea, strings);
    }


    /**
     * https://stackoverflow.com/questions/27706197/how-can-i-center-graphics-drawstring-in-java
     */
    private void drawText(Graphics2D g2, Rectangle area, String[] strings) {
        Padding padding = getPadding();
        int stringHeght = getStringHeight(g2);
        int lineSpace = getInterLineSpace();
        int x = area.x + padding.left();
        int y = area.y +  getAscent(g2)  + padding.top();

        for (String string : strings) {
            g2.drawString(string, x, y);
            y += (lineSpace + stringHeght);
        }
    }
    
    private Dimension getTextSize(Graphics2D g2, String[] strings) {
        int textWidth = 0;
        for (String string : strings) {
            textWidth = Math.max(textWidth, getStringWidth(g2, string));
        }
        Padding padding = getPadding();
        textWidth += padding.left() + padding.right();
        int textHeight = padding.top() + padding.bottom + strings.length * getStringHeight(g2);
        textHeight += getInterLineSpace() * (strings.length - 1);
        return new Dimension(textWidth, textHeight);
    }
    
    private  int getInterLineSpace() {
        return (int)(fontSize * 0.2);
    }
    
    private Padding getPadding() {
          return new Padding((int)(fontSize * 0.8), (int)(fontSize * 0.4), (int)(fontSize * 0.8), (int)(fontSize * 0.4));
    }

    private Rectangle2D getStringBounds(Graphics2D g2, String string, Font font) {
        TextLayout layout = new TextLayout(string, font, g2.getFontRenderContext());
        Rectangle2D labelBounds = layout.getBounds();
        return labelBounds;
        /*FontRenderContext frc = g2.getFontRenderContext();
        return g2.getFont().getStringBounds(string, frc);*/
    }
    private int getStringWidth(Graphics2D g2, String string) {
        FontMetrics fm = g2.getFontMetrics();
        return  fm.stringWidth(string);

    }

    private int getStringHeight(Graphics2D g2) {
        return g2.getFontMetrics().getHeight();
    }

    private int getAscent(Graphics2D g2) {
        return g2.getFontMetrics().getAscent();
    }
    
    class Padding {
        int left;
        int right;
        int top;
        int bottom;

        public Padding(int left, int top, int right, int bottom) {
            this.left = left;
            this.right = right;
            this.top = top;
            this.bottom = bottom;
        }

        public int left() {
            return left;
        }

        public int right() {
            return right;
        }

        public int top() {
            return top;
        }

        public int bottom() {
            return bottom;
        }
    }
}
