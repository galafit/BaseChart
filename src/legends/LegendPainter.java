package legends;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by hdablin on 11.08.17.
 */
public class LegendPainter {
    private List<LegendItem> items;
    private int fontSize = 14;
    private Color fontColor = Color.LIGHT_GRAY;
    private String font = Font.SANS_SERIF;
    private LegendPosition legendPosition = LegendPosition.BOTTOM_CENTER ;
    private int maxStringWidth = 0;
    private ArrayList<Integer> itemsPerStringList = new ArrayList<Integer>();
    Graphics2D g2;

    public LegendPainter(List<LegendItem> items, Graphics2D g2, int areaWidth) {
        this.items = items;
        this.g2 = g2;
        itemsToStrings(areaWidth);
    }

    public boolean isTop(){
        return legendPosition.isTop();
    }

    public int getLegendHeight(){
        return getStringHeight(g2) * itemsPerStringList.size()  + getInterLineSpace() * (itemsPerStringList.size() -1) + getPadding() * 2;
    }

    private void itemsToStrings(int areaWidth){
        if (items.size() == 0){
            return;
        }
        this.g2.setFont(new Font(font, Font.PLAIN, fontSize));
        int stringWidth = 0;
        int itemCounter = 0;
        for (int i = 0; i < items.size(); i++) {
            int itemWidth = getStringWidth(g2, items.get(i).getLabel()) + getColorMarkerSize()  + getColorMarkerPadding();
            if (itemCounter > 0 && stringWidth + itemCounter  * getItemPadding()  + itemWidth + getPadding() * 2 > areaWidth){
                int resultantStringWidth = stringWidth + (itemCounter - 1) * getItemPadding();
                maxStringWidth = Math.max(maxStringWidth, resultantStringWidth);
                itemsPerStringList.add(itemCounter);
                stringWidth = 0;
                itemCounter = 0;
            }
            itemCounter++;
            stringWidth += itemWidth;
        }
        int resultantStringWidth = stringWidth + (itemCounter - 1) * getItemPadding();
        maxStringWidth = Math.max(maxStringWidth, resultantStringWidth);
        itemsPerStringList.add(itemCounter);
    }

    public void draw(Rectangle area){
        int y = area.y + getPadding();
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(area.x,area.y,area.width,area.height);
        this.g2.setFont(new Font(font, Font.PLAIN, fontSize));
        int itemCounter = 0;
        for (int numberOfItems : itemsPerStringList) {
            int x;
            if (legendPosition == LegendPosition.BOTTOM_LEFT || legendPosition == LegendPosition.TOP_LEFT){
                x = area.x + getPadding();
            } else if (legendPosition == LegendPosition.BOTTOM_RIGHT || legendPosition == LegendPosition.TOP_RIGHT) {
                x = area.x + area.width - maxStringWidth - getPadding();
                x = Math.max(area.x + getPadding(), x);
            } else {
                x = (area.x + area.width) / 2 - maxStringWidth / 2;
                x = Math.max(area.x + getPadding(), x);
            }
            g2.setColor(Color.gray);
            g2.fillRect(x, y, maxStringWidth, getStringHeight(g2));
            for (int i = 0; i < numberOfItems; i++) {
                LegendItem legendItem = items.get(itemCounter);
                g2.setColor(legendItem.getColor());
                g2.fillRect(x,y + (getStringHeight(g2) - getColorMarkerSize()) / 2 + 1,getColorMarkerSize(),getColorMarkerSize());
                x +=  getColorMarkerSize() + getColorMarkerPadding();
                g2.setColor(fontColor);
                g2.drawString(legendItem.getLabel(),x,y + getStringAscent(g2));
                x +=  getStringWidth(g2, legendItem.getLabel()) + getItemPadding();
                itemCounter++;
            }
            y += getStringHeight(g2) + getInterLineSpace();
        }

    }

    private int getStringWidth(Graphics2D g2, String string) {
        FontMetrics fm = g2.getFontMetrics();
        return  fm.stringWidth(string);

    }

    private  int getInterLineSpace() {
        return (int)(fontSize * 0.2);
    }

    private int getPadding(){
        return (int)(fontSize * 0.5);
    }

    private int getItemPadding(){
        return (int)(fontSize * 1);
    }

    private int getStringHeight(Graphics2D g2) {
        return g2.getFontMetrics().getHeight();
    }

    private int getStringAscent(Graphics2D g2) {
        return g2.getFontMetrics().getAscent();
    }

    private int getColorMarkerSize(){
        return (int)(fontSize * 0.8);
    }

    private int getColorMarkerPadding(){
        return (int)(fontSize * 0.5);
    }

}

