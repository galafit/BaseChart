package tooltips;

import java.awt.*;
import java.awt.FontMetrics;

/**
 * Created by hdablin on 02.08.17.
 */
public class TooltipPainter {
    Color backgroundColor = new Color(200, 200, 200);
    Color borderColor = new Color(100, 100, 100);
    int borderWidth = 1;
    Color fontColor = Color.BLACK;
    String font = Font.SANS_SERIF;
    int fontSize = 14;
    int x_offset = 10;
    int y_offset = 15;

    public void draw(Graphics2D g2d, Rectangle area, TooltipInfo tooltipInfo){
        Font tooltipFont = new Font(font, Font.PLAIN, fontSize);
        g2d.setFont(tooltipFont);
        Dimension tooltipDimention = getTextSize(g2d, tooltipInfo);
        Rectangle tooltipArea = new Rectangle(tooltipInfo.getX() + x_offset,tooltipInfo.getY() + y_offset,tooltipDimention.width,tooltipDimention.height);
        g2d.setColor(backgroundColor);
        g2d.fillRect(tooltipArea.x, tooltipArea.y, tooltipArea.width, tooltipArea.height);
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(borderWidth));
        g2d.drawRect(tooltipArea.x, tooltipArea.y, tooltipArea.width, tooltipArea.height);
        g2d.setStroke(new BasicStroke(1));
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
        int y = area.y  + padding.top();

        for (int i = 0; i < tooltipInfo.getAmountOfItems(); i++) {
            drawItem(g2, x, y, tooltipInfo.getItem(i));
           // g2.drawRect(x - padding.left(), y, area.width, stringHeght);
            y += (lineSpace + stringHeght);
        }
    }

    private void drawItem(Graphics2D g2, int x, int y, TooltipItem tooltipItem){
        g2.setColor(tooltipItem.getMarkColor());
        int string_y = y + getStringAscent(g2);
        int colorMarkerSize = getColorMarkerSize();
        g2.fillRect(x,y + (getStringHeight(g2) - colorMarkerSize) / 2 + 1, colorMarkerSize, colorMarkerSize);
        x = x + colorMarkerSize + getColorMarkerPadding();
        g2.setColor(fontColor);
        g2.setFont(new Font(font, Font.PLAIN, fontSize));
        String labelString = tooltipItem.getLabel() + ":  ";
        g2.drawString(labelString, x, string_y);
        x = x + getStringWidth(g2, labelString);
        g2.setFont(new Font(font, Font.BOLD, fontSize));
        g2.drawString(tooltipItem.getValue(),x, string_y);
    }


    private int getColorMarkerSize(){
        return (int)(fontSize * 0.8);
    }

    private int getColorMarkerPadding(){
        return (int)(fontSize * 0.5);
    }

    private int getItemWidth(Graphics2D g2d, TooltipItem tooltipItem){
        String string = tooltipItem.getLabel() + ":  " + tooltipItem.getValue();
        int itemWidth = getStringWidth(g2d, string) + getColorMarkerPadding() + getColorMarkerSize();
        return itemWidth;
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

    private int getStringAscent(Graphics2D g2) {
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
