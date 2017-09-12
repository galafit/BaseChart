package painters;

import configuration.TextAnchor;

import javax.swing.*;
import java.awt.*;

/**
 * Created by galafit on 10/9/17.
 */
public class Text {
    private String text;
    private int x;
    private int y;

    public Text(String text, int x, int y) {
        this.text = text;
        this.x = x;
        this.y = y;
    }

    public Text(String string, int x, int y, TextAnchor hTextAnchor, TextAnchor vTextAnchor, FontMetrics fm) {
        text = string;
        if(hTextAnchor == TextAnchor.MIDDLE) {
            x -= fm.stringWidth(string) / 2;
        }
        if(hTextAnchor == TextAnchor.END) {
            x -= fm.stringWidth(string);
        }
        if(vTextAnchor == TextAnchor.MIDDLE) {
            y = y + fm.getHeight()/2 - fm.getDescent();
        }
        if(vTextAnchor == TextAnchor.END) {
            y = y + fm.getAscent();
        }
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        g.drawString(text, x, y);
    }

    public String getText() {
        return text;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     *
     * Test method to see how text positioning works
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(800, 300));
        frame.add(new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Point p = new Point(400, 100);
                Point p1 = new Point(400, 200);
                String str = "-0823TestSBrgÑ";

                g.setFont(new Font("San-Serif", Font.PLAIN, 40));
                FontMetrics fm = g.getFontMetrics();
                int strWidth = fm.stringWidth(str);
                int strAscent = fm.getAscent();

                Text text1 = new Text(str, p.x, p.y, TextAnchor.START, TextAnchor.END, fm);
                Text text2 = new Text(str, p.x, p.y, TextAnchor.END, TextAnchor.START, fm);
                Text text3 = new Text(str, p1.x, p1.y, TextAnchor.MIDDLE, TextAnchor.MIDDLE, fm);

                g.setColor(Color.gray);
                g.fillRect(text1.getX(), text1.getY() - fm.getAscent(),strWidth , strAscent);
                g.fillRect(text2.getX(), text2.getY() - fm.getAscent(),strWidth , strAscent);
                g.fillRect(text3.getX(), text3.getY() - fm.getAscent(),strWidth , strAscent);

                g.setColor(Color.RED);
                text1.draw(g);
                text2.draw(g);
                text3.draw(g);

                g.setColor(Color.black);
                g.drawLine(p.x, p.y - 100, p.x, p.y + 200);
                g.drawLine(p.x - 300, p.y, p.x + 300, p.y);
                g.drawLine(p1.x - 300, p1.y, p1.x + 300, p1.y);
            }
        });
        frame.setVisible(true);
    }
}
