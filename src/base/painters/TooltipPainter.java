package base.painters;

import base.config.general.Margin;
import base.config.TooltipConfig;
import base.tooltips.TooltipInfo;
import base.tooltips.InfoItem;

import java.awt.*;

/**
 * Created by hdablin on 02.08.17.
 */
public class TooltipPainter {
    private TooltipConfig tooltipConfig;
    private int x, y;
    private int y_offset = 2;
    private String separator = "  ";
    private TooltipInfo tooltipInfo;

    public TooltipPainter(TooltipConfig tooltipConfig) {
        this.tooltipConfig = tooltipConfig;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setTooltipInfo(TooltipInfo tooltipInfo) {
        this.tooltipInfo = tooltipInfo;
    }

    public void draw(Graphics2D g2d, Rectangle area) {
        g2d.setFont(tooltipConfig.getTextStyle().getFont());
        Dimension tooltipDimension  = getTextSize(g2d, tooltipInfo);
        int tooltipAreaX = x - tooltipDimension.width / 2;
        int tooltipAreaY = y - tooltipDimension.height - y_offset;
        if (tooltipAreaX + tooltipDimension.width > area.x + area.width){
            tooltipAreaX = area.x + area.width - tooltipDimension.width;
        }
        if (tooltipAreaX < area.x){
            tooltipAreaX = area.x;
        }
        if (tooltipAreaY < area.y){
            tooltipAreaY = y + y_offset;
        }
        if (tooltipAreaY + tooltipDimension.height > area.y + area.height ){
            tooltipAreaY = area.y + area.height - tooltipDimension.height;
        }

        Rectangle tooltipArea = new Rectangle(tooltipAreaX, tooltipAreaY, tooltipDimension.width, tooltipDimension.height);
        g2d.setColor(tooltipConfig.getBackground());
        g2d.fillRect(tooltipArea.x, tooltipArea.y, tooltipArea.width, tooltipArea.height);
        g2d.setColor(tooltipConfig.getBorderColor());
        g2d.setStroke(new BasicStroke(tooltipConfig.getBorderWidth()));
        g2d.drawRect(tooltipArea.x, tooltipArea.y, tooltipArea.width, tooltipArea.height);
        drawTooltipInfo(g2d, tooltipArea, tooltipInfo);
    }


    /**
     * https://stackoverflow.com/questions/27706197/how-can-i-center-graphics-drawstring-in-java
     */
    private void drawTooltipInfo(Graphics2D g2, Rectangle area, TooltipInfo tooltipInfo) {
        Margin margin = tooltipConfig.getMargin();
        int stringHeght = getStringHeight(g2, tooltipConfig.getTextStyle().getFont());
        int lineSpace = getInterLineSpace();
        int x = area.x + margin.left();
        int y = area.y + margin.top();
        if (tooltipInfo.getHeader() != null) {
            drawItem(g2, x, y, tooltipInfo.getHeader());
            y += (lineSpace + stringHeght);
        }

        for (int i = 0; i < tooltipInfo.getAmountOfItems(); i++) {
            drawItem(g2, x, y, tooltipInfo.getItem(i));
            //g2.drawRect(x - margin.left(), y, area.width, stringHeght);
            y += (lineSpace + stringHeght);
        }
    }

    /*
    * http://pawlan.com/monica/articles/texttutorial/other.html
    */
    private void drawItem(Graphics2D g2, int x, int y, InfoItem infoItem) {

        int string_y = y + getStringAscent(g2, tooltipConfig.getTextStyle().getFont());
        ;
        if (infoItem.getMarkColor() != null) {
            g2.setColor(infoItem.getMarkColor());
            int colorMarkerSize = getColorMarkerSize();
            g2.fillRect(x, y + (getStringHeight(g2, tooltipConfig.getTextStyle().getFont()) - colorMarkerSize) / 2 + 1, colorMarkerSize, colorMarkerSize);
            x = x + colorMarkerSize + getColorMarkerPadding();
        }
        if (infoItem.getLabel() != null) {
            g2.setColor(tooltipConfig.getTextStyle().getFontColor());
            g2.setFont(tooltipConfig.getTextStyle().getFont());
            String labelString = infoItem.getLabel() + separator;
            g2.drawString(labelString, x, string_y);
            x = x + getStringWidth(g2, tooltipConfig.getTextStyle().getFont(), labelString);
        }
        if (infoItem.getValue() != null) {
            g2.setColor(tooltipConfig.getTextStyle().getFontColor());
            // font for value is always BOLD!
            g2.setFont(new Font(tooltipConfig.getTextStyle().getFontName(), Font.BOLD, tooltipConfig.getTextStyle().getFontSize()));
            g2.drawString(infoItem.getValue(), x, string_y);
        }
    }


    private int getColorMarkerSize() {
        return (int) (tooltipConfig.getTextStyle().getFontSize() * 0.8);
    }

    private int getColorMarkerPadding() {
        return (int) (tooltipConfig.getTextStyle().getFontSize() * 0.5);
    }

    private int getItemWidth(Graphics2D g2d, InfoItem infoItem) {
        String string = "";
        if (infoItem.getValue() != null) {
            string = infoItem.getValue();
        }
        if (infoItem.getLabel() != null) {
            string = infoItem.getLabel() + separator + string;
        }
        int itemWidth = getStringWidth(g2d, tooltipConfig.getTextStyle().getFont(), string);
        if (infoItem.getMarkColor() != null) {
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
        if (tooltipInfo.getHeader() != null) {
            textWidth = Math.max(textWidth, getItemWidth(g2, tooltipInfo.getHeader()));
        }
        Margin margin = tooltipConfig.getMargin();
        textWidth += margin.left() + margin.right();
        int textHeight = margin.top() + margin.bottom() + amountOfItems * getStringHeight(g2, tooltipConfig.getTextStyle().getFont());
        textHeight += getInterLineSpace() * (amountOfItems - 1);
        if (tooltipInfo.getHeader() != null) {
            textHeight += getStringHeight(g2, tooltipConfig.getTextStyle().getFont()) + getInterLineSpace();
        }
        return new Dimension(textWidth, textHeight);
    }

    private int getInterLineSpace() {
        return (int) (tooltipConfig.getTextStyle().getFontSize() * 0.2);
    }

    private int getStringWidth(Graphics2D g2, Font font, String string) {
        return g2.getFontMetrics(font).stringWidth(string);
    }

    private int getStringHeight(Graphics2D g2, Font font) {
        return g2.getFontMetrics(font).getHeight();
    }

    private int getStringAscent(Graphics2D g2, Font font) {
        return g2.getFontMetrics(font).getAscent();
    }
}
