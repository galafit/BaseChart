package tooltips;

import axis.ScientificNumber;
import com.sun.javafx.tk.*;
import graphs.Graph;

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

    public void draw(Graphics2D g2d, Rectangle area, TooltipInfo tooltipInfo){
        Padding padding = getPadding();
        Font tooltipFont = new Font(font, Font.PLAIN, fontSize);
        g2d.setFont(tooltipFont);
        Dimension tooltipDimention = getTextSize(g2d, tooltipInfo);
        Rectangle tooltipArea = new Rectangle(tooltipInfo.getX() +20,tooltipInfo.getY() +20,tooltipDimention.width,tooltipDimention.height);
        g2d.setColor(backgroundColor);
        g2d.fillRect(tooltipArea.x, tooltipArea.y, tooltipArea.width, tooltipArea.height);
        g2d.setColor(borderColor);
        g2d.drawRect(tooltipArea.x, tooltipArea.y, tooltipArea.width, tooltipArea.height);
        drawTooltipInfo(g2d, tooltipArea, tooltipInfo);
    }


    /**
     * https://stackoverflow.com/questions/27706197/how-can-i-center-graphics-drawstring-in-java
     */
    private void drawTooltipInfo(Graphics2D g2, Rectangle area, TooltipInfo tooltipInfo) {
        Padding padding = getPadding();
        int stringHeght = getStringHeight(g2);
        int lineSpace = getInterLineSpace();
        int x = area.x + padding.left();
        int y = area.y +  getAscent(g2)  + padding.top();

        for (int i = 0; i < tooltipInfo.getAmountOfItems(); i++) {
            drawItem(g2, x,y, tooltipInfo.getItem(i));
            y += (lineSpace + stringHeght);
        }
    }

    private int getColorMarkerSize(){
        return fontSize / 2;
    }

    private int getColorMarkerPadding(){
        return fontSize / 2;
    }

    private int getItemWidth(Graphics2D g2d, TooltipItem tooltipItem){
        String string = tooltipItem.getLabel() + ": " + tooltipItem.getValue();
        int itemWidth = getStringWidth(g2d, string) + getColorMarkerPadding() + getColorMarkerSize();
        return itemWidth;
    }

    private void drawItem(Graphics2D g2d, int x, int y, TooltipItem tooltipItem){
        g2d.setColor(tooltipItem.getMarkColor());
        g2d.fillRect(x,y - getStringHeight(g2d) / 2 + getColorMarkerSize() / 2, getColorMarkerSize(), getColorMarkerSize());
        x = x + getColorMarkerSize() + getColorMarkerPadding();
        g2d.setColor(fontColor);
        g2d.setFont(new Font(font, Font.PLAIN, fontSize));
        String labelString = tooltipItem.getLabel() + ": ";
        g2d.drawString(labelString, x, y);
        x = x + getStringWidth(g2d, labelString);
        g2d.setFont(new Font(font, Font.BOLD, fontSize));
        g2d.drawString(tooltipItem.getValue(),x,y);
    }
    
    private Dimension getTextSize(Graphics2D g2, TooltipInfo tooltipInfo) {
        int textWidth = 0;
        int amountOfItems = tooltipInfo.getAmountOfItems();
        for (int i = 0; i < amountOfItems; i++) {
            textWidth = Math.max(textWidth, getItemWidth(g2, tooltipInfo.getItem(i)));
        }
        Padding padding = getPadding();
        textWidth += padding.left() + padding.right();
        int textHeight = padding.top() + padding.bottom + amountOfItems * getStringHeight(g2);
        textHeight += getInterLineSpace() * (amountOfItems - 1);
        return new Dimension(textWidth, textHeight);
    }
    
    private  int getInterLineSpace() {
        return (int)(fontSize * 0.2);
    }
    
    private Padding getPadding() {
          return new Padding((int)(fontSize * 0.8), (int)(fontSize * 0.4), (int)(fontSize * 0.8), (int)(fontSize * 0.4));
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
