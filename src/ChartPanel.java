
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
    private ScrollableChart scrollableChart;
    private List<Integer> xAxisList = new ArrayList<>();
    private List<Integer> yAxisList = new ArrayList<>();
    private List<Integer> yAxisListPreview = new ArrayList<>();
    private ChartWithDataManager chartDataManager;

    public ChartPanel(ChartConfig config) {
        setBackground(config.getChartConfig().getBackground());
        chartDataManager = new ChartWithDataManager(config, new Rectangle(0, 0, 500, 500));
        scrollableChart = chartDataManager.getChartWithPreview();
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)) {
                    if(scrollableChart.chartContains(e.getX(), e.getY())) {
                        if(scrollableChart.chartHoverOn(e.getX(), e.getY(), scrollableChart.getChartSelectedTraceIndex())) {
                            repaint();
                        }
                    }
                    if(scrollableChart.previewContains(e.getX(), e.getY())) {
                        if(scrollableChart.previewHoverOn(e.getX(), e.getY(), scrollableChart.getPreviewSelectedTraceIndex())) {
                            repaint();
                        }
                    }
                } else {
                    int dy = pastY - e.getY();
                    int dx = e.getX() - pastX;
                    pastX = e.getX();
                    pastY = e.getY();
                    if(isPressedInsideScroll) {
                        if(scrollableChart.translateScrolls(dx)) {
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
                    if(scrollableChart.chartContains(e.getX(), e.getY()) && scrollableChart.selectChartTrace(e.getX(), e.getY())) {
                        repaint();
                    }
                    if(scrollableChart.previewContains(e.getX(), e.getY()) && (scrollableChart.selectPreviewTrace(e.getX(), e.getY()) || scrollableChart.setScrollsPosition(e.getX(), e.getY()))) {
                        repaint();
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)) {
                    if(scrollableChart.chartContains(e.getX(), e.getY())) {
                        if(scrollableChart.chartHoverOn(e.getX(), e.getY(), scrollableChart.getChartSelectedTraceIndex())) {
                            repaint();
                        }
                    }
                    if(scrollableChart.previewContains(e.getX(), e.getY())) {
                        if(scrollableChart.previewHoverOn(e.getX(), e.getY(), scrollableChart.getPreviewSelectedTraceIndex())) {
                            repaint();
                        }
                    }
                } else {
                    pastX = e.getX();
                    pastY = e.getY();
                    if(scrollableChart.isPointInsideScroll(e.getX(), e.getY())) {
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
                if(scrollableChart.chartContains(e.getX(), e.getY())) {
                    if(scrollableChart.chartHoverOff()) {
                        repaint();
                    }
                }
                if(scrollableChart.previewContains(e.getX(), e.getY())) {
                    if(scrollableChart.previewHoverOff()) {
                        repaint();
                    }
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
               scrollableChart.setArea(getBounds());
               repaint();
            }
        });
    }


    private void updateXAxisList(int x, int y) {
        int selectedTraceIndex = scrollableChart.getChartSelectedTraceIndex();
        xAxisList = new ArrayList<>(1);
        if(selectedTraceIndex >= 0) {
            xAxisList.add(scrollableChart.getChartTraceXIndex(selectedTraceIndex));
        } else {
           int xAxisIndex = scrollableChart.getChartXIndex(x, y);
           if(xAxisIndex >= 0) {
               xAxisList.add(scrollableChart.getChartXIndex(x, y));
           }
        }
    }

    private void updateXAxisList() {
        int selectedTraceIndex = scrollableChart.getChartSelectedTraceIndex();
        if(selectedTraceIndex >= 0) {
            xAxisList = new ArrayList<>(1);
            xAxisList.add(scrollableChart.getChartTraceXIndex(selectedTraceIndex));
        } else {
            xAxisList = new ArrayList<>(scrollableChart.getChartXAxisCounter());
            for (int i = 0; i < scrollableChart.getChartXAxisCounter(); i++) {
                xAxisList.add(i) ;
            }
        }
    }

    private void updateYAxisList(int x, int y) {
        if(scrollableChart.isPointInsideChart(x, y)) {
            int chartSelectedTraceIndex = scrollableChart.getChartSelectedTraceIndex();
            yAxisList = new ArrayList<>(1);
            if(chartSelectedTraceIndex >= 0) {
                yAxisList.add(scrollableChart.getChartTraceYIndex(chartSelectedTraceIndex));
            } else {
                int yAxisIndex = scrollableChart.getChartYIndex(x, y);
                if(yAxisIndex >= 0) {
                    yAxisList.add(yAxisIndex);
                }
            }
            yAxisListPreview = new ArrayList<>(0);
        }

        if(scrollableChart.previewContains(x, y)) {
            int previewSelectedTraceIndex = scrollableChart.getPreviewSelectedTraceIndex();
            yAxisListPreview = new ArrayList<>(1);
            if(previewSelectedTraceIndex >= 0) {
                yAxisListPreview.add(scrollableChart.getPreviewTraceYIndex(previewSelectedTraceIndex));
            } else {
                int yAxisIndex = scrollableChart.getPreviewYIndex(x, y);
                if(yAxisIndex >= 0)
                    yAxisListPreview.add(yAxisIndex);
            }
            yAxisList = new ArrayList<>(0);
        }
    }

    private void updateYAxisList() {
        int chartSelectedTraceIndex = scrollableChart.getChartSelectedTraceIndex();
        if(chartSelectedTraceIndex >= 0) {
            yAxisList = new ArrayList<>(1);
            yAxisList.add(scrollableChart.getChartTraceYIndex(chartSelectedTraceIndex));
        } else {
            yAxisList = new ArrayList<>(scrollableChart.getChartYAxisCounter());
            for (int i = 0; i < scrollableChart.getChartYAxisCounter(); i++) {
                yAxisList.add(i) ;
            }
        }

        int previewSelectedTraceIndex = scrollableChart.getPreviewSelectedTraceIndex();
        if(previewSelectedTraceIndex >= 0) {
            yAxisListPreview = new ArrayList<>(1);
            yAxisListPreview.add(scrollableChart.getPreviewTraceYIndex(previewSelectedTraceIndex));
        } else {
            yAxisListPreview = new ArrayList<>(scrollableChart.getPreviewYAxisCounter());
            for (int i = 0; i < scrollableChart.getPreviewYAxisCounter(); i++) {
                yAxisListPreview.add(i) ;
            }
        }
    }


    private void translateY(int dy) {
        for (Integer yAxisIndex : yAxisList) {
            scrollableChart.translateChartY(yAxisIndex, dy);
        }
        for (Integer yAxisIndex : yAxisListPreview) {
            scrollableChart.translatePreviewY(yAxisIndex, dy);
        }
    }

    private void translateX(int dx) {
        for (Integer xAxisIndex : xAxisList) {
            scrollableChart.translateChartX(xAxisIndex, dx);
        }
    }

    private void zoomY(int dy) {
        for (Integer yAxisIndex : yAxisList) {
            // scaling relative to the stack
            double zoomFactor = 1 + defaultZoom * dy / scrollableChart.getChartYStartEnd(yAxisIndex).length();
            scrollableChart.zoomChartY(yAxisIndex, zoomFactor);
        }
        for (Integer yAxisIndex : yAxisListPreview) {
            // scaling relative to the stack
            double zoomFactor = 1 + defaultZoom * dy / scrollableChart.getChartYStartEnd(yAxisIndex).length();
            scrollableChart.zoomPreviewY(yAxisIndex, zoomFactor);
        }
    }

    private void zoomX(int dx) {
        double zoomFactor = 1 + defaultZoom * dx / 100;
        for (Integer xAxisIndex : xAxisList) {
            scrollableChart.zoomChartX(xAxisIndex, zoomFactor);
        }
    }

    private void autoscaleX() {
        for (Integer xAxisIndex : xAxisList) {
            scrollableChart.autoScaleChartX(xAxisIndex);
        }
    }

    private void autoscaleY() {
        for (Integer yAxisIndex : yAxisList) {
            scrollableChart.autoScaleChartY(yAxisIndex);
        }
        for (Integer yAxisIndex : yAxisListPreview) {
            scrollableChart.autoScalePreviewY(yAxisIndex);
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        scrollableChart.draw((Graphics2D) g);
    }

    public void update() {
        chartDataManager.update();
        repaint();
    }
}
