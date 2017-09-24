package painters;

import configuration.general.Margin;
import configuration.general.TextStyle;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by hdablin on 17.08.17.
 */
public class TitlePainter {
    private TextStyle titleStyle;
    private String[] words = new String[0];
    private ArrayList<String> strings;

    public TitlePainter(String chartTitle, TextStyle titleStyle) {
        if(chartTitle != null) {
            words = chartTitle.split(" ");
            this.titleStyle = titleStyle;
        }
    }

    public int getTitleHeight(Graphics2D g2, int areaWidth){
        if(words.length == 0) {
            return 0;
        }
        g2.setFont(titleStyle.getFont());
        formStrings(g2, areaWidth);
        return getStringHeight(g2, titleStyle.getFont()) * strings.size()
                + getInterLineSpace() * (strings.size() - 1)
                + getMargin().top() + getMargin().bottom();
    }

    private void formStrings(Graphics2D g2, int areaWidth){
        strings = new ArrayList<String>();
        StringBuilder resultantString = new StringBuilder(words[0]);
        for (int i = 1; i < words.length; i++) {
            if (getStringWidth(g2, titleStyle.getFont(),  resultantString + " "+ words[i]) + getMargin().left() + getMargin().right() > areaWidth){
                strings.add(resultantString.toString());
                resultantString = new StringBuilder(words[i]);
            } else {
                resultantString.append(" ").append(words[i]);
            }
        }
        strings.add(resultantString.toString());
    }

    public void draw(Graphics2D g2, Rectangle area){
        if(words.length == 0) {
            return;
        }
        g2.setFont(titleStyle.getFont());
        g2.setColor(titleStyle.fontColor);
        if (strings == null){
            formStrings(g2, area.width);
        }
        int y = area.y + getMargin().top();
        for (String string : strings) {
            int x = (area.x + area.width) / 2 - getStringWidth(g2, titleStyle.getFont(), string) / 2;
            if (x < area.x + getMargin().left()) {
                x = area.x + getMargin().left();
            }
            g2.drawString(string,x,y + getStringAscent(g2, titleStyle.getFont()));
            y += getInterLineSpace() + getStringHeight(g2, titleStyle.getFont());
        }

    }


    private  int getInterLineSpace() {
        return (int)(titleStyle.fontSize * 0.2);
    }

    private Margin getMargin(){
        return new Margin((int)(titleStyle.fontSize * 0),
                (int)(titleStyle.fontSize * 0.5),
                (int)(titleStyle.fontSize * 0.5),
                (int)(titleStyle.fontSize * 0.5));
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
