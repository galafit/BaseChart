import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.beans.Transient;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;

public class TestTooltip {

    private static class CirclePanel extends JPanel {
        private Ellipse2D circle1 = new Ellipse2D.Double(0, 0, 20, 20);
        private Ellipse2D circle2 = new Ellipse2D.Double(300, 200, 20, 20);
        private Ellipse2D circle3 = new Ellipse2D.Double(200, 100, 20, 20);

        public CirclePanel() {
            // Register the component on the tooltip manager
            // So that #getToolTipText(MouseEvent) gets invoked when the mouse
            // hovers the component
            ToolTipManager.sharedInstance().registerComponent(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Simple paint of 3 circles on the component
            g.setColor(Color.RED);
            Graphics2D g2 = (Graphics2D) g;
            g2.fill(circle1);
            g2.fill(circle2);
            g2.fill(circle3);
        };

        /**
         * This method is called automatically when the mouse is over the component.
         * Based on the location of the event, we detect if we are over one of
         * the circles. If so, we display some information relative to that circle
         * If the mouse is not over any circle we return the tooltip of the
         * component.
         */
        @Override
        public String getToolTipText(MouseEvent event) {
            Point p = new Point(event.getX(), event.getY());
            String t = tooltipForCircle(p, circle1);
            if (t != null) {
                return t;
            }
            t = tooltipForCircle(p, circle2);
            if (t != null) {
                return t;
            }
            t = tooltipForCircle(p, circle3);
            if (t != null) {
                return t;
            }
            return super.getToolTipText(event);
        }

        @Override
        @Transient
        public Dimension getPreferredSize() {
            // Some size we would like to have
            return new Dimension(350, 350);
        }

        protected String tooltipForCircle(Point p, Ellipse2D circle) {
            // Test to check if the point  is inside circle
            if (circle.contains(p)) {
                // p is inside the circle, we return some information
                // relative to that circle.
                return "Circle: (" + circle.getX() + " " + circle.getY() + ")";
            }
            return null;
        }
    }

    protected void initUI() {
        JFrame frame = new JFrame("Test tooltip");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new CirclePanel();
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new TestTooltip().initUI();
            }
        });
    }

}
