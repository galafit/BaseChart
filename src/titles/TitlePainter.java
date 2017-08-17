package titles;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by hdablin on 17.08.17.
 */
public class TitlePainter {
    private String chartTitle;
    private int fontSize = 40;
    private Color fontColor = Color.LIGHT_GRAY;
    private String font = Font.SANS_SERIF;
    private String[] words;
    private ArrayList<String> strings ;

    public TitlePainter(String chartTitle) {
        this.chartTitle = chartTitle;
        words = chartTitle.split(" ");
    }

    public int getTitleHeight(Graphics2D g2, int areaWidth){
        g2.setFont(new Font(font, Font.PLAIN, fontSize));
        formStrings(g2, areaWidth);
        return getStringHeight(g2) * strings.size() + getInterLineSpace() * (strings.size() - 1) + getPadding() * 2;
    }

    private void formStrings(Graphics2D g2, int areaWidth){
        strings = new ArrayList<String>();
        if (words.length == 0){
            return;
        }
        String resultantString = words[0];
        for (int i = 1; i < words.length; i++) {
            if (getStringWidth(g2, resultantString + " "+ words[i]) + getPadding() * 2 > areaWidth){
                strings.add(resultantString);
                resultantString = words[i];
            } else {
                resultantString += " " + words[i];
            }
        }
        strings.add(resultantString);
    }

    public void draw(Graphics2D g2, Rectangle area){
        if (strings == null){
            formStrings(g2, area.width);
        }
        g2.setFont(new Font(font, Font.PLAIN, fontSize));
        g2.setColor(fontColor);
        int y = area.y + getPadding();
        for (String string : strings) {
            int x = (area.x + area.width) / 2 - getStringWidth(g2, string) / 2;
            if (x < area.x + getPadding()) {
                x = area.x + getPadding();
            }
            g2.drawString(string,x,y + getStringAscent(g2));
            y += getInterLineSpace() + getStringHeight(g2);
        }

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
        g2.setFont(new Font(font, Font.PLAIN, fontSize));
        return g2.getFontMetrics().getHeight();
    }

    private int getStringAscent(Graphics2D g2) {
        return g2.getFontMetrics().getAscent();
    }

    private int getStringWidth(Graphics2D g2, String string) {
        FontMetrics fm = g2.getFontMetrics();
        return  fm.stringWidth(string);

    }



}
