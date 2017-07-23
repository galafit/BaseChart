import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by galafit on 21/7/17.
 */
public class Scroll {
    private ScrollModel scrollModel;
    private ScrollPainter scrollPainter;
    private double pointsPerUnit;

    public Scroll() {
        scrollModel = new ScrollModel();
        scrollPainter = new ScrollPainter(scrollModel);
    }

    public void setPointsPerUnit(double pointsPerUnit) {
        this.pointsPerUnit = pointsPerUnit;
    }

    public ScrollModel getScrollModel() {
        return scrollModel;
    }

    public boolean isMouseInsideScroll(int mouseX, int mouseY) {
        return scrollPainter.isMouseInsideScroll(mouseX, mouseY);
    }

    public void moveScroll(int shift) {
        scrollPainter.moveScroll(shift);
    }

    public void setScrollPosition(int mousePosition) {
       scrollPainter.setScrollPosition(mousePosition);
    }

    public void draw(Graphics2D g2d, Rectangle area) {
        double viewportWidth = area.getWidth() / pointsPerUnit;
        scrollModel.setViewportWidth(viewportWidth);
        scrollPainter.draw(g2d, area);
    }
}
