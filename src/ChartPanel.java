import base.chart.GestureListener;
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
        GestureListener chartMouseListener = chart.getMouseListener();

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                boolean isModified = false;
                if (e.isAltDown() || e.isControlDown() || e.isShiftDown() || e.isMetaDown()) {
                    isModified = true;
                }
                chartMouseListener.onDrag(e.getX(), e.getY(), isModified);
             }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    chartMouseListener.onDoubleClick(e.getX(), e.getY());
                } else if (e.getClickCount() == 1) {
                    chartMouseListener.onClick(e.getX(), e.getY());
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {
                chartMouseListener.onPress(e.getX(), e.getY(), SwingUtilities.isRightMouseButton(e));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                chartMouseListener.onRelease(e.getX(), e.getY());
            }

        });

        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                boolean isModified = false;
                if (e.isAltDown() || e.isControlDown() || e.isShiftDown() || e.isMetaDown()) {
                    isModified = true;
                }
                int rotation = e.getWheelRotation();
                int translation = rotation;
                chartMouseListener.onScroll(translation, isModified);
                if(e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                    System.out.println("unit scroll, rotation "+rotation);
                }
                if(e.getScrollType() == MouseWheelEvent.WHEEL_BLOCK_SCROLL) {
                    System.out.println("block scroll, rotation "+rotation);
                }
            }
        });

        this.chart.addChartListener(new ChartEventListener() {
            @Override
            public void update() {
                repaint();
            }
        });
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        chart.draw((Graphics2D) g);
    }

    /**
     * This class distinguish between single and double click
     */
    class ClickListener extends MouseAdapter {
        private int clickInterval = 300; //ms
        private MouseEvent lastEvent;
        private long lastTime;
        private Timer clickTimer;

        public ClickListener() {
            Object defaultClickInterval = Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");
            if (defaultClickInterval != null) {
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
                        // DO SINGLE CLICK ACTION!
                        // System.out.println("single click: ");
                    }
                });
                clickTimer.setRepeats(false);
                clickTimer.start();

            } else { // double click
                lastTime = 0;
                clickTimer.stop();
                // DO SINGLE CLICK ACTION!
                //System.out.println("double click: ");
            }
        }
    }
}
