package base.tooltips;

import java.util.ArrayList;

/**
 * Created by hdablin on 02.08.17.
 */
public class TooltipInfo {
    private InfoItem header;
    private ArrayList<InfoItem> items = new ArrayList<InfoItem>();
    int x,y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public InfoItem getHeader() {
        return header;
    }

    public void setHeader(InfoItem header) {
        this.header = header;
    }

    public int getAmountOfItems(){
        return items.size();
    }

    public InfoItem getItem(int index){
        return items.get(index);
    }

    public void addItems(InfoItem... items){
        for (InfoItem item : items) {
            this.items.add(item);
        }
    }
}
