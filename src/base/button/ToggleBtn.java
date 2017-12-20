package base.button;

import base.config.general.Margin;
import base.config.general.TextStyle;

import java.awt.*;

/**
 * Created by galafit on 18/12/17.
 */
public class ToggleBtn {
    private BtnModel model = new BtnModel();
    private Color color = Color.BLACK;
    private String label = "";
    private Color background = Color.LIGHT_GRAY;
    private boolean isVisible = true;
    private TextStyle textStyle = new TextStyle();
    private Margin margin = new Margin((int)(textStyle.getFontSize() * 0),
            (int)(textStyle.getFontSize() * 1),
            (int)(textStyle.getFontSize() * 0.5),
            (int)(textStyle.getFontSize() * 1));
    private Rectangle bounds;

    public ToggleBtn(Color color, String label) {
        this.color = color;
        this.label = label;
    }

    public void toggle() {
        if(model.isSelected()) {
            model.setSelected(false);
        } else {
            model.setSelected(true);
        }
    }

    public boolean contains(int x, int y) {
        if(bounds != null && bounds.contains(x, y)) {
            return true;
        }
        return false;
    }

    public void addListener(StateListener listener) {
        model.addListener(listener);
    }

    public BtnModel getModel() {
        return model;
    }

    public void setLocation(int x, int y, Graphics2D g2) {
        if(bounds == null) {
            createBounds(g2);
        }
        bounds.x = x;
        bounds.y = y;
    }

    public Rectangle getBounds(Graphics2D g2) {
        if(bounds == null) {
            createBounds(g2);
        }
        return bounds;
    }

    private void createBounds(Graphics2D g2) {
        bounds = new Rectangle(0, 0, getItemWidth(g2), getItemHeight(g2));
    }


    public void draw(Graphics2D g2) {
        if(bounds == null) {
            createBounds(g2);
        }
        // draw background
        g2.setColor(background);
        g2.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        g2.setColor(color);
        if(model.isSelected()) {
            // draw border
            g2.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
            // draw selection marker
            int x = bounds.x + getPadding();
            int y = bounds.y + bounds.height/2;

            int x1 = bounds.x + getPadding() + getColorMarkerSize()/2;
            int y1 = bounds.y + bounds.height - getPadding();

            int x2 = bounds.x + getPadding() + getColorMarkerSize();
            int y2 = bounds.y + getPadding();

            g2.drawLine(x, y, x1, y1);
            g2.drawLine(x1, y1, x2, y2);
        }

        // draw item
        int x = bounds.x + getPadding() + getColorMarkerSize() + getColorMarkerPadding();
        int y = bounds.y + getPadding() + getStringAscent(g2, textStyle.getFont());

        g2.drawString(label, x, y);
    }

    private int getItemWidth(Graphics2D g2) {
        return getStringWidth(g2, textStyle.getFont(), label)
                + getColorMarkerSize() + getColorMarkerPadding()
                + 2 * getPadding();

    }

    private int getItemHeight(Graphics2D g2) {
        return getStringHeight(g2, textStyle.getFont())
                + 2 * getPadding();

    }

    private int getPadding() {
        return (int) (textStyle.getFontSize() * 0.2);
    }

    private int getColorMarkerSize() {
        return (int) (textStyle.getFontSize() * 0.8);
    }

    private int getColorMarkerPadding() {
        return (int) (textStyle.getFontSize() * 0.5);
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

    public void setBackground(Color background) {
        this.background = background;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public void setTextStyle(TextStyle textStyle) {
        this.textStyle = textStyle;
    }

    public void setMargin(Margin margin) {
        this.margin = margin;
    }
}
