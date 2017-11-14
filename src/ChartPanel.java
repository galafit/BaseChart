
import base.Range;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hdablin on 23.06.17.
 */
public class ChartPanel extends JPanel {
    int scrollPointsPerRotation = 30;
    // во сколько раз растягивается или сжимается ось при автозуме
    private double defaultZoom = 2;
    private Chart chart;
    private int pastX;
    private int pastY;

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
            xAxisList = chart.getStackXAxisIndexes(x, y);
        }
    }

    private void updateXAxisList() {
        int selectedTraceIndex = chart.getSelectedTraceIndex();
        if(selectedTraceIndex >= 0) {
            xAxisList = new ArrayList<>(1);
            xAxisList.add(chart.getTraceXAxisIndex(selectedTraceIndex));
        } else {
            xAxisList = chart.getXAxisIndexes();
        }
    }

    private void updateYAxisList(int x, int y) {
        int selectedTraceIndex = chart.getSelectedTraceIndex();
        yAxisList = new ArrayList<>(1);
        if(selectedTraceIndex >= 0) {
            yAxisList.add(chart.getTraceYAxisIndex(selectedTraceIndex));
        } else {
            yAxisList.add(chart.getYAxisIndex(x, y));
        }
    }

    private void updateYAxisList() {
        int selectedTraceIndex = chart.getSelectedTraceIndex();
        if(selectedTraceIndex >= 0) {
            yAxisList = new ArrayList<>(1);
            yAxisList.add(chart.getTraceYAxisIndex(selectedTraceIndex));
        } else {
            yAxisList = chart.getYAxisIndexes();
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
            // scaling relative to the stack
            double zoomFactor = 1 + defaultZoom * dy / chart.getYAxisRange(yAxisIndex).length();
            chart.zoomY(yAxisIndex, zoomFactor);
        }
    }

    public void zoomX(int dx) {
        double zoomFactor = 1 + defaultZoom * dx / 100;
        for (Integer xAxisIndex : xAxisList) {
            chart.zoomX(xAxisIndex, zoomFactor);
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
}
