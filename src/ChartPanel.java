
import base.ScrollableChart;

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
    // во сколько раз растягивается или сжимается ось при автозуме
    private double defaultZoom = 2;
    private int pastX;
    private int pastY;
    private boolean isPressedInsideScroll;
    private ScrollableChart chart;
    private List<Integer> xAxisList = new ArrayList<>();
    private List<Integer> yAxisList = new ArrayList<>();
    private List<Integer> yAxisListPreview = new ArrayList<>();
    private ChartWithDataManager chartDataManager;

    public ChartPanel(ChartConfig config) {
        chartDataManager = new ChartWithDataManager(config, new Rectangle(0, 0, 500, 500));
        chart = chartDataManager.getChartWithPreview();
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)) {
                    if(chart.chartHoverOn(e.getX(), e.getY(), chart.getChartSelectedTraceIndex())) {
                        repaint();
                    }
                } else {
                    int dy = pastY - e.getY();
                    int dx = e.getX() - pastX;
                    pastX = e.getX();
                    pastY = e.getY();
                    if(isPressedInsideScroll) {
                        if(chart.translateScrolls(dx)) {
                            repaint();
                        }
                    } else {
                        if (e.isAltDown()
                                || e.isControlDown()
                               // || e.isShiftDown()
                                || e.isMetaDown()) { // zoomChartY
                            zoomY(dy);
                            repaint();
                        } else { // tranlate X and Y
                           // translateX(dx);
                            translateY(dy);
                            repaint();
                        }
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
                    autoscaleY();
                    autoscaleX();
                    repaint();
                }
                if (e.getClickCount() == 1) {
                    if(chart.selectChartTrace(e.getX(), e.getY())) {
                        repaint();
                    }
                    if(chart.setScrollsPosition(e.getX(), e.getY())) {
                        repaint();
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)) {
                    if(chart.chartHoverOn(e.getX(), e.getY(), chart.getChartSelectedTraceIndex())) {
                        repaint();
                    }
                } else {
                    pastX = e.getX();
                    pastY = e.getY();
                    if(chart.isPointInsideScroll(e.getX(), e.getY())) {
                        isPressedInsideScroll = true;
                    } else {
                        isPressedInsideScroll = false;
                        updateXAxisList(e.getX(), e.getY());
                        updateYAxisList(e.getX(), e.getY());
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(chart.chartHoverOff()) {
                    repaint();
                }
            }
        });

        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                e.consume(); // avoid the event to be triggered twice
                updateXAxisList(e.getX(), e.getY());
                if (e.isAltDown()
                        || e.isControlDown()
                    //    || e.isShiftDown() // JAVA BUG on MAC!!!!
                        || e.isMetaDown()) { // zoomChartX
                    zoomX(e.getWheelRotation());
                    repaint();
                } else { // translateScrolls X
                    translateX(e.getWheelRotation() * scrollPointsPerRotation);
                    repaint();
                }
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
               chart.setArea(getBounds());
               repaint();
            }
        });
    }


    private void updateXAxisList(int x, int y) {
        int selectedTraceIndex = chart.getChartSelectedTraceIndex();
        xAxisList = new ArrayList<>(1);
        if(selectedTraceIndex >= 0) {
            xAxisList.add(chart.getChartTraceXIndex(selectedTraceIndex));
        } else {
           int xAxisIndex = chart.getChartXIndex(x, y);
           if(xAxisIndex >= 0) {
               xAxisList.add(chart.getChartXIndex(x, y));
           }
        }
    }

    private void updateXAxisList() {
        int selectedTraceIndex = chart.getChartSelectedTraceIndex();
        if(selectedTraceIndex >= 0) {
            xAxisList = new ArrayList<>(1);
            xAxisList.add(chart.getChartTraceXIndex(selectedTraceIndex));
        } else {
            xAxisList = new ArrayList<>(chart.getChartNumberOfXAxis());
            for (int i = 0; i < chart.getChartNumberOfXAxis(); i++) {
                xAxisList.add(i) ;
            }
        }
    }

    private void updateYAxisList(int x, int y) {
        if(chart.isPointInsideChart(x, y)) {
            int chartSelectedTraceIndex = chart.getChartSelectedTraceIndex();
            yAxisList = new ArrayList<>(1);
            if(chartSelectedTraceIndex >= 0) {
                yAxisList.add(chart.getChartTraceYIndex(chartSelectedTraceIndex));
            } else {
                int yAxisIndex = chart.getChartYIndex(x, y);
                if(yAxisIndex >= 0) {
                    yAxisList.add(yAxisIndex);
                }
            }
            yAxisListPreview = new ArrayList<>(0);
        }

        if(chart.isPointInsidePreview(x, y)) {
            int previewSelectedTraceIndex = chart.getPreviewSelectedTraceIndex();
            yAxisListPreview = new ArrayList<>(1);
            if(previewSelectedTraceIndex >= 0) {
                yAxisListPreview.add(chart.getPreviewTraceYIndex(previewSelectedTraceIndex));
            } else {
                int yAxisIndex = chart.getPreviewYIndex(x, y);
                if(yAxisIndex >= 0)
                    yAxisListPreview.add(yAxisIndex);
            }
            yAxisList = new ArrayList<>(0);
        }
    }

    private void updateYAxisList() {
        int chartSelectedTraceIndex = chart.getChartSelectedTraceIndex();
        if(chartSelectedTraceIndex >= 0) {
            yAxisList = new ArrayList<>(1);
            yAxisList.add(chart.getChartTraceYIndex(chartSelectedTraceIndex));
        } else {
            yAxisList = new ArrayList<>(chart.getChartNumberOfYAxis());
            for (int i = 0; i < chart.getChartNumberOfYAxis(); i++) {
                yAxisList.add(i) ;
            }
        }

        int previewSelectedTraceIndex = chart.getPreviewSelectedTraceIndex();
        if(previewSelectedTraceIndex >= 0) {
            yAxisListPreview = new ArrayList<>(1);
            yAxisListPreview.add(chart.getPreviewTraceYIndex(previewSelectedTraceIndex));
        } else {
            yAxisListPreview = new ArrayList<>(chart.getPreviewNumberOfYAxis());
            for (int i = 0; i < chart.getPreviewNumberOfYAxis(); i++) {
                yAxisListPreview.add(i) ;
            }
        }
    }


    private void translateY(int dy) {
        for (Integer yAxisIndex : yAxisList) {
            chart.translateChartY(yAxisIndex, dy);
        }
        for (Integer yAxisIndex : yAxisListPreview) {
            chart.translatePreviewY(yAxisIndex, dy);
        }
    }

    private void translateX(int dx) {
        for (Integer xAxisIndex : xAxisList) {
            chart.translateChartX(xAxisIndex, dx);
        }
    }

    private void zoomY(int dy) {
        for (Integer yAxisIndex : yAxisList) {
            // scaling relative to the stack
            double zoomFactor = 1 + defaultZoom * dy / chart.getChartYStartEnd(yAxisIndex).length();
            chart.zoomChartY(yAxisIndex, zoomFactor);
        }
        for (Integer yAxisIndex : yAxisListPreview) {
            // scaling relative to the stack
            double zoomFactor = 1 + defaultZoom * dy / chart.getChartYStartEnd(yAxisIndex).length();
            chart.zoomPreviewY(yAxisIndex, zoomFactor);
        }
    }

    private void zoomX(int dx) {
        double zoomFactor = 1 + defaultZoom * dx / 100;
        for (Integer xAxisIndex : xAxisList) {
            chart.zoomChartX(xAxisIndex, zoomFactor);
        }
    }

    private void autoscaleX() {
        for (Integer xAxisIndex : xAxisList) {
            chart.autoScaleChartX(xAxisIndex);
        }
    }

    private void autoscaleY() {
        for (Integer yAxisIndex : yAxisList) {
            chart.autoScaleChartY(yAxisIndex);
        }
        for (Integer yAxisIndex : yAxisListPreview) {
            chart.autoScalePreviewY(yAxisIndex);
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        chart.draw((Graphics2D) g);
    }

    public void update() {
        chartDataManager.update();
        repaint();
    }
}
