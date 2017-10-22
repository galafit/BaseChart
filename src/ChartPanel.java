import base.chart.ChangeListener;
import base.chart.ChartEventListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by hdablin on 23.06.17.
 */
public class ChartPanel extends JPanel {
    private Chart chart;

    public ChartPanel(Chart chart) {
        this.chart = chart;
        this.chart.addChangeListener(new ChangeListener() {
            @Override
            public void update() {
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                chart.mouseMoved(e.getX(), e.getY());

            }
        });

        addMouseListener(new ClickListener());

        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                chart.mouseWheelMoved(e.getX(), e.getY(), e.getWheelRotation());
            }
        });
    }

    public void singleClickAction(MouseEvent e) {
        chart.mouseDoubleClicked(e.getX(), e.getY());
    }
    public void doubleClickAction(MouseEvent e) {
        chart.mouseClicked(e.getX(), e.getY());
        if (chart.isMouseInsidePreview(e.getX(), e.getY()) && !chart.isMouseInsideScroll(e.getX(), e.getY())) {
            chart.moveScroll(e.getX(), e.getY());
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        chart.draw((Graphics2D) g);
    }

    /**
     * This class distinguish between single and double click
     */
    class ClickListener extends MouseAdapter  {
        private int clickInterval = 300; //ms
        MouseEvent lastEvent;
        private Timer clickTimer;

        public ClickListener() {
            Object defaultClickInterval = Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");
            if(defaultClickInterval != null) {
                clickInterval = (Integer) defaultClickInterval;
            }
        }

        /**
         * If we do not want to be limited by the desktop configuration we can
         * not use e.getClickCount()==2 but instead
         * choose the interval max between clicks and  handle by oneself the count
         */
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() > 2) return;
            if (e.getClickCount() == 1) {
                lastEvent = e;
                clickTimer = new Timer(clickInterval, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        singleClickAction(lastEvent);
                        System.out.println("single click: ");
                    }
                });
                clickTimer.setRepeats(false);
                clickTimer.start();

            }
            if (e.getClickCount() == 2) {
                clickTimer.stop();
                doubleClickAction(e);
                System.out.println("double click: ");
            }
        }
    }
}
