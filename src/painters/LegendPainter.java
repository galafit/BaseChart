package painters;

import configuration.*;
import legend.LegendItem;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by hdablin on 11.08.17.
 */
public class LegendPainter {
    private List<LegendItem> items;
    private int maxStringWidth = 0;
    private ArrayList<Integer> itemsPerStringList;
    private LegendConfig legendConfig;

    public LegendPainter(List<LegendItem> items, LegendConfig legendConfig) {
        this.items = items;
        this.legendConfig = legendConfig;
    }

    public boolean isTop() {
        return legendConfig.position.isTop();
    }

    public int getLegendHeight(Graphics2D g2, int areaWidth) {
        if (!legendConfig.isVisible || items.size() == 0) {
            return 0;
        }
        g2.setFont(legendConfig.textStyle.getFont());
        itemsToStrings(g2, areaWidth);
        return getStringHeight(g2, legendConfig.textStyle.getFont()) * itemsPerStringList.size()
                + getInterLineSpace() * (itemsPerStringList.size() - 1)
                + legendConfig.margin.top() + legendConfig.margin.bottom();
    }

    private void itemsToStrings(Graphics2D g2, int areaWidth) {
        itemsPerStringList = new ArrayList<Integer>();
        int sumItemsWidth = getItemWidth(g2, items.get(0));
        int itemCounter = 1;
        for (int i = 1; i < items.size(); i++) {
            int currentItemWidth = getItemWidth(g2, items.get(i));
            if (sumItemsWidth + currentItemWidth + itemCounter * getInterItemSpace() + legendConfig.margin.left() + legendConfig.margin.right() > areaWidth) {
                maxStringWidth = Math.max(maxStringWidth, sumItemsWidth + (itemCounter - 1) * getInterItemSpace());
                itemsPerStringList.add(itemCounter);
                sumItemsWidth = currentItemWidth;
                itemCounter = 1;
            } else {
                itemCounter++;
                sumItemsWidth += currentItemWidth;
            }

        }
        maxStringWidth = Math.max(maxStringWidth, sumItemsWidth + (itemCounter - 1) * getInterItemSpace());
        itemsPerStringList.add(itemCounter);
    }

    private int getItemWidth(Graphics2D g2, LegendItem item) {
        return getStringWidth(g2, legendConfig.textStyle.getFont(), item.getLabel()) + getColorMarkerSize() + getColorMarkerPadding();

    }

    public void draw(Graphics2D g2, Rectangle area) {
        if (!legendConfig.isVisible || items.size() == 0) {
            return;
        }

        g2.setFont(legendConfig.textStyle.getFont());
        Margin margin = legendConfig.margin;
        int x_start;
        if (legendConfig.position == Position.BOTTOM_LEFT || legendConfig.position == Position.TOP_LEFT) {
            x_start = area.x + margin.left();
        } else if (legendConfig.position == Position.BOTTOM_RIGHT || legendConfig.position == Position.TOP_RIGHT) {
            x_start = area.x + area.width - maxStringWidth - margin.right();
            x_start = Math.max(area.x + margin.left(), x_start);
        } else {
            x_start = (area.x + area.width) / 2 - maxStringWidth / 2;
            x_start = Math.max(area.x + margin.left(), x_start);
        }

        // draw background
        int legendHeight = getStringHeight(g2, legendConfig.textStyle.getFont()) * itemsPerStringList.size()
                + getInterLineSpace() * (itemsPerStringList.size() - 1)
                + margin.top() + margin.bottom();
        int legendWidth = maxStringWidth + margin.left() + margin.right();
        g2.setColor(legendConfig.background);
        g2.fillRect(x_start - margin.left(), area.y, legendWidth, legendHeight);

        // draw border
        g2.setStroke(new BasicStroke(legendConfig.borderWidth));
        g2.setColor(legendConfig.borderColor);
        g2.drawRect(x_start - margin.left(), area.y, legendWidth, legendHeight);

        int itemCounter = 0;
        int y = area.y + margin.top();
        for (int numberOfItems : itemsPerStringList) {
            int x = x_start;

            // g2.setColor(legendConfig.getBackground());
            // g2.fillRect(x, y, maxStringWidth, getStringHeight(g2));

            for (int i = 0; i < numberOfItems; i++) {
                LegendItem legendItem = items.get(itemCounter);
                g2.setColor(legendItem.getColor());
                g2.fillRect(x, y + (getStringHeight(g2, legendConfig.textStyle.getFont()) - getColorMarkerSize()) / 2 + 1, getColorMarkerSize(), getColorMarkerSize());
                x += getColorMarkerSize() + getColorMarkerPadding();
                g2.setColor(legendConfig.textStyle.fontColor);
                g2.drawString(legendItem.getLabel(), x, y + getStringAscent(g2, legendConfig.textStyle.getFont()));
                x += getStringWidth(g2, legendConfig.textStyle.getFont(), legendItem.getLabel()) + getInterItemSpace();
                itemCounter++;
            }
            y += getStringHeight(g2, legendConfig.textStyle.getFont()) + getInterLineSpace();
        }

    }

     private int getInterLineSpace() {
        return (int) (legendConfig.textStyle.fontSize * 0.2);
    }

    private int getInterItemSpace() {
        return (int) (legendConfig.textStyle.fontSize * 1);
    }

    private int getColorMarkerSize() {
        return (int) (legendConfig.textStyle.fontSize * 0.8);
    }

    private int getColorMarkerPadding() {
        return (int) (legendConfig.textStyle.fontSize * 0.5);
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

