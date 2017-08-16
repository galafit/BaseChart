package legends;

import graphs.Graph;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by hdablin on 11.08.17.
 */
public class LegendPainter {
    private List<LegendItem> items;
    private int fontSize = 14;
    Color fontColor = Color.LIGHT_GRAY;
    String font = Font.SANS_SERIF;
    Color borderColor = new Color(100, 100, 100);
    private LegendPosition legendPosition = LegendPosition.BOTTOM_CENTER;

    public LegendPainter(List<LegendItem> items) {
        this.items = items;
    }

    public boolean isTop(){
        return legendPosition.isTop();
    }

    public int getLegendHeight(Graphics2D g2, Rectangle area){
        List<StringInfo> strings = separateStrings(g2, area.width);
        int height = getStringHeight(g2) * strings.size() + getPadding() * 2 + getInterLineSpace() * (strings.size() -1);
        return height;
    }

    private List<StringInfo> separateStrings(Graphics2D g2, int areaWidth){
        if (items.size() == 0){
            return null;
        }
        g2.setFont(new Font(font, Font.PLAIN, fontSize));
        int stringWidth = 0;
        int itemCounter = 0;
        ArrayList<StringInfo> strings = new ArrayList<StringInfo>();
        StringInfo stringInfo = new StringInfo();
        for (int i = 0; i < items.size(); i++) {
            int itemWidth = getStringWidth(g2, items.get(i).getLabel()) + getColorMarkerSize()  + getColorMarkerPadding();
            itemCounter ++;
            if (itemCounter > 1 && stringWidth + (itemCounter -1) * getItemPadding() + getPadding() * 2 + itemWidth> areaWidth){
                stringWidth = 0;
                strings.add(stringInfo);
                stringInfo = new StringInfo();
            }
            stringInfo.addItem(items.get(i));
            stringWidth +=  itemWidth;
            stringInfo.setStringWidth(stringWidth + (itemCounter -1) * getItemPadding());
        }
        strings.add(stringInfo);
        return strings;
    }

    public void draw(Graphics2D g2, Rectangle area){

        int yStart = getPadding() + area.y;
        int y = yStart;

        g2.setColor(Color.CYAN);
        g2.fillRect(area.x,area.y,area.width,area.height);
        g2.setFont(new Font(font, Font.PLAIN, fontSize));

        List<StringInfo> strings = separateStrings(g2, area.width);
        for (StringInfo string : strings) {
            int x;
            if (legendPosition == LegendPosition.BOTTOM_RIGHT || legendPosition == LegendPosition.TOP_RIGHT) {
                x = area.x + area.width - string.getStringWidth() - getPadding();
            } else if (legendPosition == LegendPosition.BOTTOM_LEFT || legendPosition == LegendPosition.TOP_LEFT){
                x = area.x + getPadding();
            } else {
                x = (area.x + area.width) / 2 - string.getStringWidth() / 2;
            }
            for (LegendItem legendItem : string.getItems()) {
                g2.setColor(legendItem.getColor());
                g2.fillRect(x,y + (getStringHeight(g2) - getColorMarkerSize()) / 2 + 1,getColorMarkerSize(),getColorMarkerSize());
                x +=  getColorMarkerSize() + getColorMarkerPadding();
                g2.setColor(fontColor);
                g2.drawString(legendItem.getLabel(),x,y + getStringAscent(g2));
                x +=  getStringWidth(g2, legendItem.getLabel()) + getItemPadding();
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

class StringInfo{
    private int stringWidth;
    private List<LegendItem> items = new ArrayList<LegendItem>();

    public void addItem(LegendItem item){
        items.add(item);
    }

    public int getStringWidth() {
        return stringWidth;
    }

    public List<LegendItem> getItems() {
        return items;
    }

    public void setStringWidth(int stringWidth) {
        this.stringWidth = stringWidth;
    }
}