package legends;

import graphs.Graph;

import java.awt.*;
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
    private LegendPosition legendPosition = LegendPosition.TOP_RIGHT;

    public LegendPainter(List<LegendItem> items) {
        this.items = items;
    }

    public boolean isTop(){
        return legendPosition.isTop();
    }

    public int getLegendHeight(Graphics2D g2, Rectangle area){
        if (items.size() == 0){
            return 0;
        }
        g2.setFont(new Font(font, Font.PLAIN, fontSize));
        int stringCounter = 1;
        int totalStringWidth = 0;
        for (int i = 0; i < items.size(); i++) {
            int stringWidth = getStringWidth(g2, items.get(i).getLabel()) + getColorMarkerSize()  + getColorMarkerPadding();
           /* if (stringWidth + getPadding() * 2 > area.width) {
                stringCounter ++;
                totalStringWidth = 0;
            } */
            totalStringWidth = totalStringWidth +  stringWidth;
            if (totalStringWidth > stringWidth && totalStringWidth + getPadding() * 2 > area.width){
                stringCounter ++;
                totalStringWidth = 0;
            }else{
                totalStringWidth = totalStringWidth +  getItemPadding();
            }
        }
        int height = getStringHeight(g2) * stringCounter + getPadding() * 2 + getInterLineSpace() * (stringCounter -1);
        return height;
    }

    public void draw(Graphics2D g2, Rectangle area){
        int xStart = getPadding() + area.x;
        int yStart = getPadding() + area.y;
        int x = xStart;
        int y = yStart;

        g2.setColor(Color.CYAN);
        g2.fillRect(area.x,area.y,area.width,area.height);
        g2.setFont(new Font(font, Font.PLAIN, fontSize));
        int fullStringWidth = 0;
        for (int i = 0; i < items.size(); i++) {
            int stringWidth = getStringWidth(g2, items.get(i).getLabel());
            stringWidth = stringWidth + getColorMarkerSize()  + getColorMarkerPadding() + getPadding() ;
            fullStringWidth = fullStringWidth + stringWidth;
            if (x != xStart && x + stringWidth > area.width){
                y = y + getStringHeight(g2) + getInterLineSpace();
                x = xStart;
                fullStringWidth = 0;
            }
            g2.setColor(items.get(i).getColor());
            int currentX = x;
            if (legendPosition == LegendPosition.BOTTOM_RIGHT || legendPosition == LegendPosition.TOP_RIGHT) {
                currentX = area.x + area.width - stringWidth - x;
            } else if (legendPosition == LegendPosition.BOTTOM_LEFT || legendPosition == LegendPosition.TOP_LEFT){
                currentX = x;
            } else {
                currentX = (area.width - 2 * getPadding() - fullStringWidth) / 2 + x;
            }

            g2.fillRect(currentX,y + (getStringHeight(g2) - getColorMarkerSize()) / 2 + 1,getColorMarkerSize(),getColorMarkerSize());
            currentX = currentX + getColorMarkerSize() + getColorMarkerPadding();
            g2.setColor(fontColor);
            g2.drawString(items.get(i).getLabel(),currentX,y + getStringAscent(g2));
            x = x + getColorMarkerSize() + getColorMarkerPadding() + getStringWidth(g2, items.get(i).getLabel()) + getItemPadding();
            fullStringWidth = fullStringWidth + getItemPadding();
        }

    }

    private int getStringWidth(Graphics2D g2, String string) {
        FontMetrics fm = g2.getFontMetrics();
        return  fm.stringWidth(string);

    }

    private  int getInterLineSpace() {
        return (int)(fontSize * 2);
    }

    private int getPadding(){
        return (int)(fontSize * 1.5);
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
