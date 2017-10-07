package base.tooltips;

import java.util.ArrayList;

/**
 * Created by hdablin on 02.08.17.
 */
public class TooltipInfo {
    private TooltipItem header;
    private ArrayList<TooltipItem> items = new ArrayList<TooltipItem>();
    int x,y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public TooltipItem getHeader() {
        return header;
    }

    public void setHeader(TooltipItem header) {
        this.header = header;
    }

    public int getAmountOfItems(){
        return items.size();
    }

    public TooltipItem getItem(int index){
        return items.get(index);
    }

    public void addItem(TooltipItem item){
        items.add(item);
    }

    public void setItems(ArrayList<TooltipItem> items){
        this.items = items;
    }
}
