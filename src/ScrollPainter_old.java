import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by hdablin on 02.07.17.
 */
class ScrollPainter_old {
    private ScrollModel scrollModel;
    private Rectangle paintingArea;
    private Color cursorColor = Color.RED;

    public ScrollPainter_old(ScrollModel scrollModel) {
        this.scrollModel = scrollModel;
    }

    private double scrollPositionToViewportPosition(double scrollPosition) {
        return scrollModel.getMin() + scrollPosition * (scrollModel.getMax() - scrollModel.getMin()) / paintingArea.getWidth();
    }

    private double getScrollWidth() {
        return paintingArea.getWidth() * scrollModel.getExtent() / (scrollModel.getMax() - scrollModel.getMin());
    }

    private double getScrollPosition() {
        return (scrollModel.getValue() - scrollModel.getMin()) * paintingArea.getWidth() / (scrollModel.getMax() - scrollModel.getMin());
    }

    public boolean isMouseInsideScroll(int mouseX, int mouseY) {
        if (paintingArea != null) {
            double cursorAreaX = paintingArea.getX() + getScrollPosition();
            double cursorAreaY = paintingArea.getY();
            Rectangle2D cursorArea = new Rectangle2D.Double(cursorAreaX, cursorAreaY, getScrollWidth(), paintingArea.getHeight());
            return cursorArea.contains(mouseX, mouseY);
        }
        return false;
    }


    public void moveScroll(int shift) {
        int newScrollPosition = (int)getScrollPosition() + shift;
        double viewportPosition = scrollPositionToViewportPosition(newScrollPosition);
        scrollModel.setValue(viewportPosition);

    }

    public void setScrollPosition(int mousePosition) {
        if (paintingArea != null) {
            double newScrollPosition = mousePosition - paintingArea.getX();
            double viewportPosition = scrollPositionToViewportPosition(newScrollPosition);
            scrollModel.setValue(viewportPosition);
        }
    }

    public void draw(Graphics2D g2d, Rectangle area) {
        paintingArea = area;
        g2d.setColor(cursorColor);
        double cursorX = area.getX() + getScrollPosition();
        double cursorY = area.getY();
        double cursorHeight = area.height;
        g2d.draw(new Rectangle2D.Double(cursorX, cursorY, getScrollWidth(), cursorHeight));
    }
}
