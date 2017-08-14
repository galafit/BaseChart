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

    public LegendPainter(List<LegendItem> items) {
        this.items = items;
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
        g2.setColor(Color.CYAN);
        int height = getStringHeight(g2) * stringCounter + getPadding() * 2 + getInterLineSpace() * (stringCounter -1);
        g2.fillRect(area.x, area.y, area.width, height);
        return height;
    }

    public void draw(Graphics2D g2, Rectangle area){

        int xStart = getPadding() + area.x;
        int yStart = getPadding() + area.y;
        int x = xStart;
        int y = yStart;
        g2.setFont(new Font(font, Font.PLAIN, fontSize));
        for (int i = 0; i < items.size(); i++) {
            int stringWidth = getStringWidth(g2, items.get(i).getLabel());
            stringWidth = stringWidth + getColorMarkerSize()  + getColorMarkerPadding() + getPadding() ;
            if (x != xStart && x + stringWidth > area.width){
                y = y + getStringHeight(g2) + getInterLineSpace();
                x = xStart;
            }
            g2.setColor(items.get(i).getColor());
            g2.fillRect(x,y + (getStringHeight(g2) - getColorMarkerSize()) / 2 + 1,getColorMarkerSize(),getColorMarkerSize());
            x = x + getColorMarkerSize() + getColorMarkerPadding();
            g2.setColor(fontColor);
            g2.drawString(items.get(i).getLabel(),x,y + getStringAscent(g2));
            x = x + getStringWidth(g2, items.get(i).getLabel()) + getItemPadding();
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
        return (int)(fontSize * 2.5);
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
