import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

/**
 * Created by hdablin on 02.08.17.
 */
public class Tooltip {
    Color backgroundColor = Color.DARK_GRAY;
    Color borderColor = Color.CYAN;
    Color fontColor = Color.WHITE;
    String font = Font.SANS_SERIF;
    int fontSize = 12;
    int padding = 4;

    public void draw(Graphics2D g2d, Rectangle area, double x, double y, String string){
        Font tooltipFont = new Font(font, Font.PLAIN, fontSize);
        int tooltipWidth = getStringWidth(g2d,string,tooltipFont) + 2 * padding;
        int tooltipHeight = getStringHeight(g2d,string,tooltipFont) + 2 * padding;

        Rectangle tooltipArea = new Rectangle((int)x,(int)y,tooltipWidth,tooltipHeight);
        g2d.setColor(borderColor);
        g2d.drawRect(tooltipArea.x, tooltipArea.y, tooltipArea.width, tooltipArea.height);
        g2d.setColor(backgroundColor);
        g2d.fillRect(tooltipArea.x, tooltipArea.y, tooltipArea.width, tooltipArea.height);
        g2d.setColor(fontColor);
        g2d.drawString(string, tooltipArea.x + padding, tooltipArea.y + padding);
    }

    private Rectangle2D getStringBounds(Graphics2D g, String string, Font font) {
        TextLayout layout = new TextLayout(string, font, g.getFontRenderContext());
        Rectangle2D labelBounds = layout.getBounds();
        return labelBounds;
    }

    private int getStringWidth(Graphics2D g, String label, Font font) {
        return (int)Math.round(getStringBounds(g, label, font).getWidth());
    }

    private int getStringHeight(Graphics2D g, String label, Font font) {
        //return (int)(g.getFontMetrics().getStringBounds(label,(Graphics2D)(g)).getHeight());
        return (int)Math.round(getStringBounds(g, label, font).getHeight());
    }
}
