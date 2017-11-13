
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hdablin on 23.06.17.
 */
public class ChartPanel extends JPanel {
    int scrollPointsPerRotation = 10;
    private Chart chart;
    private int pastX;
    private int pastY;

    private boolean isRightButton = false;
    private List<Integer> xAxisList = new ArrayList<>();
    private List<Integer> yAxisList = new ArrayList<>();

    public ChartPanel(Chart chart) {
        this.chart = chart;

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)) {
                    if(hoverOn(e.getX(), e.getY())) {
                        repaint();
                    }
                } else {
                    int dy = pastY - e.getY();
                    int dx = pastX - e.getX();
                    pastX = e.getX();
                    pastY = e.getY();
                    if (e.isAltDown()
                            || e.isControlDown()
                            || e.isShiftDown()
                            || e.isMetaDown()) { // zoomY

                        zoomY(dy);
                        repaint();
                    } else { // tranlate X and Y
                        translateX(dx);
                        translateY(dy);
                        repaint();
                    }
                }
             }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    updateXAxisList();
                    updateYAxisList();
                    autoscaleX();
                    autoscaleY();
                    repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)) {
                    if(hoverOn(e.getX(), e.getY())) {
                        repaint();
                    }
                } else {
                    pastX = e.getX();
                    pastY = e.getY();
                    updateXAxisList(e.getX(), e.getY());
                    updateYAxisList(e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(hoverOff()) {
                    repaint();
                }
            }
        });

        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                updateXAxisList(e.getX(), e.getY());
                if (e.isAltDown()
                        || e.isControlDown()
                        || e.isShiftDown()
                        || e.isMetaDown()) { // zoomX

                    zoomX(e.getWheelRotation());
                    repaint();
                } else { // translate X
                    translateX(e.getWheelRotation() * scrollPointsPerRotation);
                    repaint();
                }
            }
        });

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        chart.draw((Graphics2D) g);
    }

    private void updateXAxisList(int x, int y) {
        int selectedTraceIndex = chart.getSelectedTraceIndex();
        if(selectedTraceIndex >= 0) {
            xAxisList = new ArrayList<>(1);
            xAxisList.add(chart.getTraceXAxisIndex(selectedTraceIndex));
        } else {
            xAxisList = chart.getStackXAxisUsedIndexes(x, y);
        }
    }

    private void updateXAxisList() {
        int selectedTraceIndex = chart.getSelectedTraceIndex();
        if(selectedTraceIndex >= 0) {
            xAxisList = new ArrayList<>(1);
            xAxisList.add(chart.getTraceXAxisIndex(selectedTraceIndex));
        } else {
            xAxisList = chart.getXAxisUsedIndexes();
        }
    }

    private void updateYAxisList(int x, int y) {
        int selectedTraceIndex = chart.getSelectedTraceIndex();
        if(selectedTraceIndex >= 0) {
            yAxisList = new ArrayList<>(1);
            yAxisList.add(chart.getTraceYAxisIndex(selectedTraceIndex));
        } else {
            yAxisList = chart.getStackYAxisUsedIndexes(x, y);
        }
    }

    private void updateYAxisList() {
        int selectedTraceIndex = chart.getSelectedTraceIndex();
        if(selectedTraceIndex >= 0) {
            yAxisList = new ArrayList<>(1);
            yAxisList.add(chart.getTraceYAxisIndex(selectedTraceIndex));
        } else {
            yAxisList = chart.getYAxisUsedIndexes();
        }
    }


    public void translateY(int dy) {
        for (Integer yAxisIndex : yAxisList) {
            chart.translateY(yAxisIndex, dy);
        }
    }

    public void translateX(int dx) {
        for (Integer xAxisIndex : xAxisList) {
            chart.translateX(xAxisIndex, dx);
        }
    }

    public void zoomY(int dy) {
        for (Integer yAxisIndex : yAxisList) {
            chart.zoomY(yAxisIndex, dy);
        }
    }

    public void zoomX(int dx) {
        for (Integer xAxisIndex : xAxisList) {
            chart.zoomX(xAxisIndex, dx);
        }
    }

    public void autoscaleX() {
        for (Integer xAxisIndex : xAxisList) {
            chart.autoscaleXAxis(xAxisIndex);
        }
    }

    public void autoscaleY() {
        for (Integer yAxisIndex : yAxisList) {
            chart.autoscaleYAxis(yAxisIndex);
        }
    }


    public boolean hoverOff() {
        return chart.hoverOff();
    }

    public boolean hoverOn(int x, int y) {
        return chart.hoverOn(x, y);
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
