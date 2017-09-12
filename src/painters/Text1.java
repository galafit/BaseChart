package painters;
import configuration.TextAnchor;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created by galafit on 10/9/17.
 */
public class Text1 {
    private String text;
    private int x;
    private int y;
    private FontMetrics fm;
    private TextAnchor hTextAnchor;
    private TextAnchor vTextAnchor;
    private int angle = 0;

    public Text1(String string, int x, int y) {
        this(string, x, y, TextAnchor.START, TextAnchor.START, 0, null);

    }

    public Text1(String string, int x, int y, TextAnchor hTextAnchor, TextAnchor vTextAnchor, int angle, FontMetrics fm) {
        text = string;
        this.x = x;
        this.y = y;
        this.hTextAnchor = hTextAnchor;
        this.vTextAnchor = vTextAnchor;
        this.angle = angle;
        this.fm = fm;
    }

    public void draw(Graphics2D g) {
        int x = this.x;
        int y = this.y;
        if(angle == 0)  {
            if(hTextAnchor == TextAnchor.MIDDLE) {
                x -= fm.stringWidth(text) / 2;
            }
            if(hTextAnchor == TextAnchor.END) {
                x -= fm.stringWidth(text);
            }
            if(vTextAnchor == TextAnchor.MIDDLE) {
                y = y + fm.getHeight()/2 - fm.getDescent();
            }
            if(vTextAnchor == TextAnchor.END) {
                y = y + fm.getAscent();
            }
            g.drawString(text, x, y);
            return;
        }
        AffineTransform initialTransform = g.getTransform();
        AffineTransform transform = new AffineTransform();
        transform.translate(x, y);
        transform.rotate(Math.toRadians(angle));
        x = 0;
        y = 0;


        if(vTextAnchor == TextAnchor.MIDDLE) {
            transform.translate(fm.stringWidth(text)/2, 0);
        }
        if(vTextAnchor == TextAnchor.END) {
           transform.translate(fm.stringWidth(text), 0);
        }
        if(hTextAnchor == TextAnchor.END) {
            transform.translate(0, + fm.getAscent());
        }
        if(hTextAnchor == TextAnchor.MIDDLE) {
            transform.translate(0, +  (fm.getHeight()/2 - fm.getDescent()));
        }
        g.setTransform(transform);
        g.drawString(text, x, y);
        g.setTransform(initialTransform);


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
                Point p = new Point(400, 300);
                Point p1 = new Point(400, 600);
                String str = "-0823TestSBrgÑ";

                g.setFont(new Font("San-Serif", Font.PLAIN, 30));
                FontMetrics fm = g.getFontMetrics();
                int strWidth = fm.stringWidth(str);
                int strAscent = fm.getAscent();

                painters.Text1 text1 = new Text1(str, p.x, p.y, TextAnchor.START, TextAnchor.START, -90, fm);
                painters.Text1 text2 = new Text1(str, p.x, p.y, TextAnchor.END, TextAnchor.START, -90, fm);
              //  painters.Text1 text3 = new painters.Text(str, p1.x, p1.y, TextAnchor.MIDDLE, TextAnchor.MIDDLE, fm);
                Graphics2D g2  = (Graphics2D) g;
                text1.draw(g2);
                text2.draw(g2);

                g.setColor(Color.black);
                g.drawLine(p.x, p.y - 600, p.x, p.y + 600);
                g.drawLine(p.x - 300, p.y, p.x + 300, p.y);
                g.drawLine(p1.x - 300, p1.y, p1.x + 300, p1.y);
            }
        });
        frame.setVisible(true);
    }
}
