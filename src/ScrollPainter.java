import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by hdablin on 02.07.17.
 */
class ScrollPainter {
    private ScrollModel scrollModel;
    private Rectangle paintingArea;
    private Color cursorColor = Color.RED;

    public ScrollPainter(ScrollModel scrollModel) {
        this.scrollModel = scrollModel;
    }

    private long scrollPositionToViewportPosition(double scrollPosition) {
        long viewportPosition = Math.round(scrollPosition * (scrollModel.getMax() - scrollModel.getMin()) / paintingArea.getWidth());
        return viewportPosition;
    }

    public double getScrollWidth() {
        return paintingArea.getWidth() * scrollModel.getViewportWidth() / (scrollModel.getMax() - scrollModel.getMin());
    }

    public double getScrollPosition() {
        return scrollModel.getViewportPosition() * paintingArea.getWidth() / (scrollModel.getMax() - scrollModel.getMin());
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


    public void moveScrollPosition(int shift) {
        double newScrollPosition = getScrollPosition() + shift;
        scrollModel.setViewportPosition(scrollPositionToViewportPosition(newScrollPosition));
    }

    public void setScrollPosition(int mousePosition) {
        if (paintingArea != null) {
            double newScrollPosition = mousePosition;
            if (newScrollPosition <= paintingArea.getX()) {
                newScrollPosition = 0;
            } else if (newScrollPosition >= paintingArea.getX() + paintingArea.getWidth()) {
                newScrollPosition = paintingArea.getWidth();
            } else {
                newScrollPosition -= paintingArea.getX();
            }
            scrollModel.setViewportPosition(scrollPositionToViewportPosition(newScrollPosition));
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
