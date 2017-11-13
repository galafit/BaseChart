package base.painters;

import base.config.*;
import base.config.general.Margin;
import base.legend.LegendItem;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by hdablin on 11.08.17.
 */
public class LegendPainter {
    private List<LegendItem> items;
    private LegendConfig legendConfig;
    private Rectangle[] itemAreas;
    private int selectedItem = 0;

    public LegendPainter(List<LegendItem> items, LegendConfig legendConfig) {
        this.items = items;
        this.legendConfig = legendConfig;
        itemAreas = new Rectangle[items.size()];
    }

    private void createAreas(Graphics2D g2, int legendWidth) {
        for (int i = 0; i < items.size(); i++) {
           itemAreas[i] = new Rectangle(0, 0, getItemWidth(g2, items.get(i)), getItemHeight(g2));
        }

     /*   Margin margin = legendConfig.getMargin();
        int x_start;
        if (legendConfig.getPosition() == Position.BOTTOM_LEFT || legendConfig.getPosition() == Position.TOP_LEFT) {
            x_start = area.x + margin.left();
        } else if (legendConfig.getPosition() == Position.BOTTOM_RIGHT || legendConfig.getPosition() == Position.TOP_RIGHT) {
            x_start = area.x + area.width - maxStringWidth - margin.right();
            x_start = Math.max(area.x + margin.left(), x_start);
        } else {
            x_start = (area.x + area.width) / 2 - maxStringWidth / 2;
            x_start = Math.max(area.x + margin.left(), x_start);
        }*/

        int x = 0;
        int y = 0;
        for (Rectangle itemArea : itemAreas) {
            if(x + itemArea.width >= legendWidth) {
               x = 0;
               y += itemArea.height;
                itemArea.x = x;
                itemArea.y = y;
            } else {
                itemArea.x = x;
                itemArea.y = y;
                x += itemArea.width + getInterItemSpace();
            }
        }
    }

    public int getLegendHeight(Graphics2D g2, int legendWidth) {
        if (!legendConfig.isVisible() || items.size() == 0) {
            return 0;
        }
        g2.setFont(legendConfig.getTextStyle().getFont());
        createAreas(g2,  legendWidth);
        Rectangle lastItemArea = itemAreas[itemAreas.length - 1];
        return lastItemArea.y + lastItemArea.height;
    }


    public void draw(Graphics2D g2, Rectangle area) {
        if (!legendConfig.isVisible() || items.size() == 0) {
            return;
        }
        g2.setFont(legendConfig.getTextStyle().getFont());
        AffineTransform originTransform = g2.getTransform();
        g2.translate(area.x, area.y);
        for (int i = 0; i < items.size(); i++) {
            if(i == selectedItem) {
                drawItem(i, g2, true);
            } else {
                drawItem(i, g2, false);
            }

        }
        g2.setTransform(originTransform);
    }

    private void drawItem(int itemIndex, Graphics2D g2, boolean isSelected) {
        Rectangle itemRect = itemAreas[itemIndex];
        LegendItem item = items.get(itemIndex);
        // draw background
        g2.setColor(legendConfig.getBackground());
        g2.fillRect(itemRect.x, itemRect.y, itemRect.width, itemRect.height);
        // draw border
        g2.setStroke(new BasicStroke(legendConfig.getBorderWidth()));
        g2.setColor(legendConfig.getBorderColor());
        if(isSelected) {
            g2.setColor(item.getColor());
        }
        g2.drawRect(itemRect.x, itemRect.y, itemRect.width, itemRect.height);
        // draw item color marker
        int x = itemRect.x + getPadding();
        int y = itemRect.y + getPadding();
        g2.setColor(item.getColor());
        g2.fillRect(x, y, getColorMarkerSize(), itemRect.height - 2 *getPadding());
        // draw item
        x += getColorMarkerSize() + getColorMarkerPadding();
        y = itemRect.y + getPadding() + getStringAscent(g2, legendConfig.getTextStyle().getFont());
        //g2.setColor(legendConfig.getTextStyle().getFontColor());
        if(isSelected) {
           // g2.setColor(item.getColor());
        }
        g2.drawString(item.getLabel(), x, y);
    }

    private int getItemWidth(Graphics2D g2, LegendItem item) {
        return getStringWidth(g2, legendConfig.getTextStyle().getFont(), item.getLabel())
                + getColorMarkerSize() + getColorMarkerPadding()
                + 2 * getPadding();

    }

    private int getItemHeight(Graphics2D g2) {
        return getStringHeight(g2, legendConfig.getTextStyle().getFont())
                + 2 * getPadding();

    }

    private int getPadding() {
        return (int) (legendConfig.getTextStyle().getFontSize() * 0.2);
    }

    private int getInterItemSpace() {
        return (int) (legendConfig.getTextStyle().getFontSize() * 0);
    }

    private int getColorMarkerSize() {
        return (int) (legendConfig.getTextStyle().getFontSize() * 0);
    }

    private int getColorMarkerPadding() {
        return (int) (legendConfig.getTextStyle().getFontSize() * 0.5);
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

