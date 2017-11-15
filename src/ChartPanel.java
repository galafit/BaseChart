
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
    private Chart chart;
    private int pastX;
    private int pastY;
    private boolean isPressedInsideScroll;

    private List<Integer> xAxisList = new ArrayList<>();
    private List<Integer> yAxisList = new ArrayList<>();
    private List<Integer> yAxisListPreview = new ArrayList<>();

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
                    int dx = e.getX() - pastX;
                    pastX = e.getX();
                    pastY = e.getY();
                    if(isPressedInsideScroll) {
                        if(translateScrollBar(dx)) {
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
                            translateX(dx);
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
                    if(moveScrollBar(e.getX(), e.getY())) {
                        repaint();
                    }
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
                if(hoverOff()) {
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
        int selectedTraceIndex = chart.getChartSelectedTraceIndex();
        if(selectedTraceIndex >= 0) {
            xAxisList = new ArrayList<>(1);
            xAxisList.add(chart.getChartTraceXIndex(selectedTraceIndex));
        } else {
            xAxisList = chart.getChartStackXIndexes(x, y);
        }
    }

    private void updateXAxisList() {
        int selectedTraceIndex = chart.getChartSelectedTraceIndex();
        if(selectedTraceIndex >= 0) {
            xAxisList = new ArrayList<>(1);
            xAxisList.add(chart.getChartTraceXIndex(selectedTraceIndex));
        } else {
            xAxisList = chart.getChartXIndexes();
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
                if(yAxisIndex >= 0)
                    yAxisList.add(yAxisIndex);
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
            yAxisList = chart.getChartYIndexes();
        }

        int previewSelectedTraceIndex = chart.getPreviewSelectedTraceIndex();
        if(previewSelectedTraceIndex >= 0) {
            yAxisListPreview = new ArrayList<>(1);
            yAxisListPreview.add(chart.getPreviewTraceYIndex(previewSelectedTraceIndex));
        } else {
            yAxisListPreview = chart.getPreviewYIndexes();
        }
    }

    private boolean moveScrollBar(int x, int y) {
        return chart.moveScroll(x, y);
    }

    private boolean translateScrollBar(int dx) {
         return chart.translateScroll(dx);
    }

    public void translateY(int dy) {
        for (Integer yAxisIndex : yAxisList) {
            chart.translateChartY(yAxisIndex, dy);
        }
        for (Integer yAxisIndex : yAxisListPreview) {
            chart.translatePreviewY(yAxisIndex, dy);
        }
    }

    public void translateX(int dx) {
        for (Integer xAxisIndex : xAxisList) {
            chart.translateChartX(xAxisIndex, dx);
        }
    }

    public void zoomY(int dy) {
        for (Integer yAxisIndex : yAxisList) {
            // scaling relative to the stack
            double zoomFactor = 1 + defaultZoom * dy / chart.getChartYRange(yAxisIndex).length();
            chart.zoomChartY(yAxisIndex, zoomFactor);
        }
        for (Integer yAxisIndex : yAxisListPreview) {
            // scaling relative to the stack
            double zoomFactor = 1 + defaultZoom * dy / chart.getChartYRange(yAxisIndex).length();
            chart.zoomPreviewY(yAxisIndex, zoomFactor);
        }
    }

    public void zoomX(int dx) {
        double zoomFactor = 1 + defaultZoom * dx / 100;
        for (Integer xAxisIndex : xAxisList) {
            chart.zoomChartX(xAxisIndex, zoomFactor);
        }
    }

    public void autoscaleX() {
        for (Integer xAxisIndex : xAxisList) {
            chart.autoscaleChartX(xAxisIndex);
        }
    }

    public void autoscaleY() {
        for (Integer yAxisIndex : yAxisList) {
            chart.autoscaleChartY(yAxisIndex);
        }
        for (Integer yAxisIndex : yAxisListPreview) {
            chart.autoscalePreviewY(yAxisIndex);
        }
    }

    public boolean hoverOff() {
        return chart.chartHoverOff();
    }

    public boolean hoverOn(int x, int y) {
        return chart.chartHoverOn(x, y);
    }



}
