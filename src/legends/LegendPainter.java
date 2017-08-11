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
        int stringCounter = 1;
        int markerCounter = 0;
        String string = "";
        for (int i = 0; i < items.size(); i++) {
            string = string + items.get(i).getLabel();
            int stringWidth = getStringWidth(g2, string);
            markerCounter ++;
            stringWidth = stringWidth + getColorMarkerSize() * markerCounter + getColorMarkerPadding() * markerCounter + getPadding() * 2 + (markerCounter -1) * getItemPadding();
            if (stringWidth > area.width){
                stringCounter ++;
                string = "";
                markerCounter = 0;
            }
        }
        return getStringHeight(g2) * stringCounter + getPadding() * 2 + getInterLineSpace() * (stringCounter -1) ;
    }

    public void draw(Graphics2D g2, Rectangle area){

        int x = getPadding() + area.x;
        int y = getPadding() + area.y;
        g2.setFont(new Font(font, Font.PLAIN, fontSize));
        for (int i = 0; i < items.size(); i++) {
            int stringWidth = getStringWidth(g2, items.get(i).getLabel());
            stringWidth = stringWidth + getColorMarkerSize()  + getColorMarkerPadding()  + getPadding();
            if (x + stringWidth > area.width){
                y = y + getStringHeight(g2) + getInterLineSpace();
                x = getPadding() + area.x;
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
        return (int)(fontSize * 0.5);
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
