package base.painters;

import base.config.general.TextAnchor;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created by galafit on 10/9/17.
 */
public class Text {
    private String text;
    private int x;
    private int y;
    private int rotationAngle = 0;
    private int translationX, translationY;

    public Text(String text, int x, int y) {
        this.text = text;
        this.x = x;
        this.y = y;
    }

    public Text(String string, int x, int y, TextAnchor hTextAnchor, TextAnchor vTextAnchor, FontMetrics fm) {
        if(string != null && !string.isEmpty()) {
            text = string;
            this.x = x;
            this.y = y;
            if(hTextAnchor == TextAnchor.MIDDLE) {
                this.x -= fm.stringWidth(string) / 2;
            }
            if(hTextAnchor == TextAnchor.END) {
                this.x -= fm.stringWidth(string);
            }
            if(vTextAnchor == TextAnchor.MIDDLE) {
                this.y +=  fm.getHeight()/2 - fm.getDescent();
            }
            if(vTextAnchor == TextAnchor.END) {
                this.y +=  fm.getAscent();
            }
        }
    }

    public Text(String string, int x, int y, TextAnchor hTextAnchor, TextAnchor vTextAnchor, int rotationAngle, FontMetrics fm) {
        if(string != null && !string.isEmpty()) {
            text = string;
            this.rotationAngle = rotationAngle;
            this.x = x;
            this.y = y;

            if(vTextAnchor == TextAnchor.MIDDLE) {
                translationX =  - fm.stringWidth(text)/2;
            }
            if(vTextAnchor == TextAnchor.END) {
                translationX = - fm.stringWidth(text);
            }
            if(hTextAnchor == TextAnchor.MIDDLE) {
                translationY = + (fm.getHeight()/2 - fm.getDescent());
            }
            if(hTextAnchor == TextAnchor.END) {
                translationY = + fm.getAscent();
            }
        }
    }

    public void draw(Graphics2D g2) {
        if(text != null && !text.isEmpty()) {
            if(rotationAngle != 0) {
                AffineTransform initialTransform = g2.getTransform();
                g2.rotate(Math.toRadians(rotationAngle), x, y);
                g2.translate(translationX, translationY);
                g2.drawString(text, x, y);
                g2.setTransform(initialTransform);
            } else {
                g2.drawString(text, x, y);
            }

        }
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
        frame.setSize(new Dimension(800, 800));
        frame.add(new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Point p = new Point(400, 200);
                Point p1 = new Point(400, 600);
                String str = "Hello-068";

                g.setFont(new Font("San-Serif", Font.PLAIN, 20));
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
                Graphics2D g2  = (Graphics2D) g;
                text1.draw(g2);
                text2.draw(g2);
                text3.draw(g2);

                Text rotatedText1 = new Text(str, p.x, p.y, TextAnchor.START, TextAnchor.END, 90, fm);
                Text rotatedText2 = new Text(str, p.x, p.y, TextAnchor.START, TextAnchor.END, -90, fm);
                Text rotatedText3 = new Text(str, p1.x, p1.y, TextAnchor.MIDDLE, TextAnchor.MIDDLE, 90, fm);

                g.setColor(Color.BLUE);
                rotatedText1.draw(g2);
                rotatedText2.draw(g2);
                rotatedText3.draw(g2);

                g.setColor(Color.black);
                g.drawLine(p.x, p.y - 600, p.x, p.y + 600);
                g.drawLine(p.x - 300, p.y, p.x + 300, p.y);
                g.drawLine(p1.x - 300, p1.y, p1.x + 300, p1.y);
            }
        });
        frame.setVisible(true);
    }
}
