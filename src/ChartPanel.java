import base.chart.ChangeListener;

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
        chart.mouseClicked(e.getX(), e.getY());
    }

    public void doubleClickAction(MouseEvent e) {
        chart.mouseDoubleClicked(e.getX(), e.getY());
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
        private MouseEvent lastEvent;
        private long lastTime;
        private Timer clickTimer;

        public ClickListener() {
            Object defaultClickInterval = Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");
            if(defaultClickInterval != null) {
                clickInterval = (Integer) defaultClickInterval;
            }
        }

        public ClickListener(int clickInterval) {
            this.clickInterval = clickInterval;
        }

        /**
         * As we do not want to be limited by the desktop configuration we do
         * not use e.getClickCount()==2 but instead
         * use clickInterval and and  handle the count by by ourselves
         */
        public void mouseClicked(MouseEvent e) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTime >= clickInterval) { // single click
                lastEvent = e;
                lastTime = currentTime;
                clickTimer = new Timer(clickInterval, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        singleClickAction(lastEvent);
                       // System.out.println("single click: ");
                    }
                });
                clickTimer.setRepeats(false);
                clickTimer.start();

            } else { // double click
                lastTime = 0;
                clickTimer.stop();
                doubleClickAction(e);
                //System.out.println("double click: ");
            }
        }
    }
}
