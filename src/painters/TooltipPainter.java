package painters;

import configuration.Margin;
import configuration.TooltipConfig;
import tooltips.TooltipInfo;
import tooltips.TooltipItem;

import java.awt.*;
import java.awt.FontMetrics;

/**
 * Created by hdablin on 02.08.17.
 */
public class TooltipPainter {
    private TooltipConfig tooltipConfig;
    private int x_offset = 10;
    private int y_offset = 15;
    private String separator = ":  ";

    public TooltipPainter(TooltipConfig tooltipConfig) {
        this.tooltipConfig = tooltipConfig;
    }

    public void draw(Graphics2D g2d, Rectangle area, TooltipInfo tooltipInfo){
        g2d.setFont(tooltipConfig.textStyle.getFont());
        Dimension tooltipDimension = getTextSize(g2d, tooltipInfo);
        int tooltipAreaX = tooltipInfo.getX() + x_offset;
        int tooltipAreaY = tooltipInfo.getY() - tooltipDimension.height - y_offset;
        if (tooltipAreaX + tooltipDimension.width > area.width + area.x){
            tooltipAreaX = tooltipInfo.getX() - x_offset - tooltipDimension.width;
        }
        if (tooltipAreaX < area.x){
            tooltipAreaX = area.x;
        }
        if (tooltipAreaY < area.y){
            tooltipAreaY = tooltipInfo.getY() + y_offset;
        }
        if (tooltipAreaY + tooltipDimension.height > area.y + area.height ){
            tooltipAreaY = area.y + area.height - tooltipDimension.height;
        }
        Rectangle tooltipArea = new Rectangle(tooltipAreaX,tooltipAreaY,tooltipDimension.width,tooltipDimension.height);
        g2d.setColor(tooltipConfig.background);
        g2d.fillRect(tooltipArea.x, tooltipArea.y, tooltipArea.width, tooltipArea.height);
        g2d.setColor(tooltipConfig.borderColor);
        g2d.setStroke(new BasicStroke(tooltipConfig.borderWidth));
        g2d.drawRect(tooltipArea.x, tooltipArea.y, tooltipArea.width, tooltipArea.height);
        drawTooltipInfo(g2d, tooltipArea, tooltipInfo);
    }


    /**
     * https://stackoverflow.com/questions/27706197/how-can-i-center-graphics-drawstring-in-java
     */
    private void drawTooltipInfo(Graphics2D g2, Rectangle area, TooltipInfo tooltipInfo) {
        Margin margin = tooltipConfig.margin;
        int stringHeght = getStringHeight(g2, tooltipConfig.textStyle.getFont());
        int lineSpace = getInterLineSpace();
        int x = area.x + margin.left();
        int y = area.y  + margin.top();
        drawItem(g2, x,y, tooltipInfo.getHeader());
        y += (lineSpace + stringHeght);
        for (int i = 0; i < tooltipInfo.getAmountOfItems(); i++) {
            drawItem(g2, x, y, tooltipInfo.getItem(i));
            //g2.drawRect(x - margin.left(), y, area.width, stringHeght);
            y += (lineSpace + stringHeght);
        }
    }

    /*
    * http://pawlan.com/monica/articles/texttutorial/other.html
    */
    private void drawItem(Graphics2D g2, int x, int y, TooltipItem tooltipItem){
        int string_y = y + getStringAscent(g2, tooltipConfig.textStyle.getFont());;
        if (tooltipItem.getMarkColor() != null) {
            g2.setColor(tooltipItem.getMarkColor());
            int colorMarkerSize = getColorMarkerSize();
            g2.fillRect(x, y + (getStringHeight(g2, tooltipConfig.textStyle.getFont()) - colorMarkerSize) / 2 + 1, colorMarkerSize, colorMarkerSize);
            x = x + colorMarkerSize + getColorMarkerPadding();
        }
        if (tooltipItem.getLabel() != null) {
            g2.setColor(tooltipConfig.textStyle.fontColor);
            g2.setFont(tooltipConfig.textStyle.getFont());
            String labelString = tooltipItem.getLabel() + separator;
            g2.drawString(labelString, x, string_y);
            x = x + getStringWidth(g2, tooltipConfig.textStyle.getFont(), labelString);
        }
        if (tooltipItem.getValue() != null){
            g2.setColor(tooltipConfig.textStyle.fontColor);
            // font for value is always BOLD!
            g2.setFont(new Font(tooltipConfig.textStyle.fontName, Font.BOLD, tooltipConfig.textStyle.fontSize));
            g2.drawString(tooltipItem.getValue(), x, string_y);
        }
    }


    private int getColorMarkerSize(){
        return (int)(tooltipConfig.textStyle.fontSize * 0.8);
    }

    private int getColorMarkerPadding(){
        return (int)(tooltipConfig.textStyle.fontSize * 0.5);
    }

    private int getItemWidth(Graphics2D g2d, TooltipItem tooltipItem){
        String string = "";
        if (tooltipItem.getValue() != null){
            string = tooltipItem.getValue();
        }
        if (tooltipItem.getLabel() != null){
            string = tooltipItem.getLabel() + separator + string;
        }
        int itemWidth = getStringWidth(g2d, tooltipConfig.textStyle.getFont(), string);
        if (tooltipItem.getMarkColor() != null){
            itemWidth = itemWidth + getColorMarkerPadding() + getColorMarkerSize();
        }

        return itemWidth;
    }

    private Dimension getTextSize(Graphics2D g2, TooltipInfo tooltipInfo) {
        int textWidth = 0;
        int amountOfItems = tooltipInfo.getAmountOfItems();
        for (int i = 0; i < amountOfItems; i++) {
            textWidth = Math.max(textWidth, getItemWidth(g2, tooltipInfo.getItem(i)));
        }
        textWidth = Math.max(textWidth,getItemWidth(g2, tooltipInfo.getHeader()));
        Margin margin = tooltipConfig.margin;
        textWidth += margin.left() + margin.right();
        int textHeight = margin.top() + margin.bottom() + amountOfItems * getStringHeight(g2, tooltipConfig.textStyle.getFont());
        textHeight += getInterLineSpace() * (amountOfItems - 1);
        if (tooltipInfo.getHeader() != null) {
            textHeight += getStringHeight(g2, tooltipConfig.textStyle.getFont()) + getInterLineSpace();
        }
        return new Dimension(textWidth, textHeight);
    }
    
    private  int getInterLineSpace() {
        return (int)(tooltipConfig.textStyle.fontSize * 0.2);
    }

    private int getStringWidth(Graphics2D g2, Font font, String string) {
        return  g2.getFontMetrics(font).stringWidth(string);
    }

    private int getStringHeight(Graphics2D g2, Font font) {
        return g2.getFontMetrics(font).getHeight();
    }

    private int getStringAscent(Graphics2D g2, Font font) {
        return g2.getFontMetrics(font).getAscent();
    }
}
