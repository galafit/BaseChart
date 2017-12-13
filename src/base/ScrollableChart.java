package base;

import base.config.ScrollableChartConfig;
import base.config.SimpleChartConfig;
import base.config.general.Margin;
import java.awt.*;
import java.util.*;


/**
 * Created by galafit on 3/10/17.
 */
public class ScrollableChart {
    private SimpleChart chart;
    private SimpleChart preview;
    private Rectangle chartArea;
    private Rectangle previewArea;
    private Map<Integer, Scroll> scrolls = new Hashtable<Integer, Scroll>(2);
    private ScrollableChartConfig config;

    private boolean isDirty = true;


    public ScrollableChart(ScrollableChartConfig config, Rectangle area) {
        this.config = config;
        SimpleChartConfig chartConfig = config.getChartConfig();
        Set<Integer> scrollableAxis = config.getXAxisWithScroll();
        calculateAreas(area);
        if(!isPreviewEnable()) {
            chart = new SimpleChart(chartConfig, chartArea);
        } else {
            SimpleChartConfig previewConfig = config.getPreviewConfig();
            chart = new SimpleChart(chartConfig, chartArea);
            if(config.getPreviewMinMax() == null) {
                Range previewMinMax = null;
                for (Integer xAxisIndex : scrollableAxis) {
                    previewMinMax = Range.max(previewMinMax, chart.getXMinMax(xAxisIndex));
                }
                config.setPreviewMinMax(previewMinMax);
            }

            preview = new SimpleChart(previewConfig, previewArea);

            for (Integer xAxisIndex : scrollableAxis) {
                Scroll scroll = new Scroll(config.getScrollExtent(xAxisIndex), config.getScrollConfig(), preview.getXAxisScale(0));
                scrolls.put(xAxisIndex, scroll);
                Range scrollRange = new Range(scroll.getValue(), scroll.getValue() + scroll.getExtent());
                chart.setXMinMax(xAxisIndex,  scrollRange);
                scroll.addListener(new ScrollListener() {
                    @Override
                    public void onScrollChanged(double scrollValue, double scrollExtent) {
                        chart.setXMinMax(xAxisIndex, new Range(scrollValue, scrollValue + scrollExtent));
                    }
                });
            }
        }
    }

    public void draw(Graphics2D g2d) {
        if(preview != null) {
            if(isDirty) {
                Margin chartMargin = chart.getMargin(g2d);
                Margin previewMargin = preview.getMargin(g2d);
                if (chartMargin.left() != previewMargin.left() || chartMargin.right() != previewMargin.right()) {
                    int left = Math.max(chartMargin.left(), previewMargin.left());
                    int right = Math.max(chartMargin.right(), previewMargin.right());
                    chartMargin = new Margin(chartMargin.top(), right, chartMargin.bottom(), left);
                    previewMargin = new Margin(previewMargin.top(), right, previewMargin.bottom(), left);
                    chart.setMargin(g2d, chartMargin);
                    preview.setMargin(g2d, previewMargin);
                }
            }
            preview.draw(g2d);
            for (Integer key : scrolls.keySet()) {
                scrolls.get(key).draw(g2d, preview.getGraphArea(g2d));
            }
        }
        chart.draw(g2d);
    }

    private void calculateAreas(Rectangle area) {
        if (!isPreviewEnable()) {
            chartArea = area;
        } else {
            int chartWeight = config.getChartConfig().getSumWeight();
            int previewWeight = config.getPreviewConfig().getSumWeight();
            int chartHeight = area.height * chartWeight / (chartWeight + previewWeight);
            int previewHeight = area.height * previewWeight / (chartWeight + previewWeight);
            chartArea = new Rectangle(area.x, area.y, area.width, chartHeight);
            previewArea = new Rectangle(area.x, area.y + chartHeight, area.width, previewHeight);
        }
    }

    public void setArea(Rectangle area) {
        calculateAreas(area);
        chart.setArea(chartArea);
        if(preview != null) {
            preview.setArea(previewArea);
        }
        isDirty = true;
    }

    /**
     * =======================Base methods to interact with chart==========================
     **/

    public void setChartData(ArrayList<DataSet> data) {
        chart.setData(data);
    }

    public int getChartSelectedTraceIndex() {
        return chart.getSelectedTraceIndex();
    }

    public Range getChartYStartEnd(int yAxisIndex) {
        return chart.getYStartEnd(yAxisIndex);
    }

    public void setChartYMinMax(int yAxisIndex, Range minMax) {
        chart.setYMinMax(yAxisIndex, minMax);
    }

    public Range getChartYMinMax(int yAxisIndex) {
        return chart.getYMinMax(yAxisIndex);
    }

    public int getChartTraceYIndex(int traceIndex) {
        return chart.getTraceYIndex(traceIndex);
    }

    public int getChartTraceXIndex(int traceIndex) {
        return chart.getTraceXIndex(traceIndex);
    }

    public int getChartYIndex(int x, int y) {
        return chart.getYIndex(x, y);
    }

    public int getChartXIndex(int x, int y) {
        return chart.getXIndex(x, y);
    }

    public int getChartNumberOfXAxis() {
        return chart.getNumberOfXAxis();
    }

    public int getChartNumberOfYAxis() {
        return chart.getNumberOfYAxis();
    }

    public void zoomChartY(int yAxisIndex, double zoomFactor) {
        chart.zoomY(yAxisIndex, zoomFactor);
    }

    public void zoomChartX(int xAxisIndex, double zoomFactor) {
        chart.zoomX(xAxisIndex, zoomFactor);
        if (preview != null) {
            chart.zoomX(xAxisIndex, zoomFactor);
            Range minMax = Range.max(chart.getXMinMax(0), chart.getXMinMax(1));
            minMax = Range.max(minMax, preview.getXMinMax(0));
            preview.setXMinMax(0, minMax);
            preview.setXMinMax(1, minMax);
            scrolls.get(xAxisIndex).setExtent(chart.getXMinMax(xAxisIndex).length());
        }
    }

    public void translateChartY(int yAxisIndex, int dy) {
        chart.translateY(yAxisIndex, dy);
    }

    public void translateChartX(int xAxisIndex, int dx) {
        chart.translateX(xAxisIndex, dx);
        if (preview != null) {
            double scrollValue = chart.getXMinMax(xAxisIndex).start();
            setScrollsValue(scrollValue);
            chart.setXMinMax(xAxisIndex, new Range(scrolls.get(xAxisIndex).getValue(), scrolls.get(xAxisIndex).getValue() + scrolls.get(xAxisIndex).getExtent()));
        }
    }

    public void autoScaleChartX(int xAxisIndex) {
        if (preview == null) {
            chart.autoScaleX(xAxisIndex);
        }
    }

    public void autoScaleChartY(int yAxisIndex) {
        chart.autoScaleY(yAxisIndex);
    }

    public boolean chartHoverOff() {
        return chart.hoverOff();
    }

    public boolean chartHoverOn(int x, int y) {
        return chart.hoverOn(x, y);
    }

    public boolean isPointInsideChart(int x, int y) {
        return chartArea.contains(x, y);
    }

    /**
     * =======================Base methods to interact with preview==========================
     **/

    public boolean isPreviewEnable() {
        return config.isPreviewEnable();
    }

    public void setPreviewData(ArrayList<DataSet> data) {
        preview.setData(data);
    }

    /**
     * @return true if scrollValue was changed and false if newValue = current scroll value
     */
    public boolean setScrollsValue(double newValue) {
        boolean scrollsMoved = false;
        for (Integer xAxisIndex : scrolls.keySet()) {
            scrollsMoved = scrolls.get(xAxisIndex).setValue(newValue) || scrollsMoved;
        }
        return scrollsMoved;
    }

    /**
     * @return true if scrollValue was changed and false if newValue = current scroll value
     */
    public boolean setScrollsPosition(double x, double y) {
        if (previewArea == null || !previewArea.contains(x, y)) {
            return false;
        }
        boolean scrollsMoved = false;
        for (Integer key : scrolls.keySet()) {
            scrollsMoved = scrolls.get(key).setPosition(x) || scrollsMoved;
        }
        return scrollsMoved;
    }

    public boolean translateScrolls(int dx) {
        Double maxScrollsPosition = null;
        for (Integer key : scrolls.keySet()) {
            maxScrollsPosition = (maxScrollsPosition == null) ? scrolls.get(key).getPosition() : Math.max(maxScrollsPosition, scrolls.get(key).getPosition());
        }
        return setScrollsPosition(maxScrollsPosition + dx, previewArea.y);
    }


    public Set<Integer> getXAxisWithScroll() {
        return scrolls.keySet();
    }

    public void addScrollListener(int xAxisIndex, ScrollListener listener) {
        scrolls.get(xAxisIndex).addListener(listener);
    }

    public double getScrollExtent(int xAxisIndex) {
        return scrolls.get(xAxisIndex).getExtent();
    }

    public double getScrollValue(int xAxisIndex) {
        return scrolls.get(xAxisIndex).getValue();
    }

    public int getPreviewNumberOfXAxis() {
        return preview.getNumberOfXAxis();
    }

    public int getPreviewNumberOfYAxis() {
        return preview.getNumberOfYAxis();
    }


    public boolean isPointInsideScroll(int x, int y) {
        for (Integer key : scrolls.keySet()) {
            if(scrolls.get(key).isPointInsideScroll(x)) {
                return true;
            }
        }
        return false;
    }


    public boolean isPointInsidePreview(int x, int y) {
        if (previewArea != null) {
            return previewArea.contains(x, y);
        }
        return false;
    }

    public int getPreviewSelectedTraceIndex() {
        if(preview != null) {
            return preview.getSelectedTraceIndex();
        }
        return -1;
    }

    public void setPreviewMinMax(Range minMax) {
        for (int i = 0; i < preview.getNumberOfXAxis(); i++) {
            preview.setXMinMax(i, minMax);
        }
    }

    public Range getPreviewMinMax() {
        return preview.getXMinMax(0);
    }

    public Range getPreviewYStartEnd(int yAxisIndex) {
        return preview.getYStartEnd(yAxisIndex);
    }

    public void setPreviewYMinMax(int yAxisIndex, Range minMax) {
        chart.setYMinMax(yAxisIndex, minMax);
    }

    public Range getPreviewYMinMax(int yAxisIndex) {
        return chart.getYMinMax(yAxisIndex);
    }

    public int getPreviewTraceYIndex(int traceIndex) {
        return preview.getTraceYIndex(traceIndex);
    }

    public int getPreviewYIndex(int x, int y) {
        if(preview != null) {
            return preview.getYIndex(x, y);
        }
        return -1;
    }


    public void zoomPreviewY(int yAxisIndex, double zoomFactor) {
        if(preview != null) {
            preview.zoomY(yAxisIndex, zoomFactor);
        }
    }

    public void translatePreviewY(int yAxisIndex, int dy) {
        if(preview != null) {
            preview.translateY(yAxisIndex, dy);
        }

    }

    public void autoScalePreviewY(int yAxisIndex) {
        if(preview != null) {
            preview.autoScaleY(yAxisIndex);
        }
    }




/*    @Override
    public void onClick(int mouseX, int mouseY) {
        if(chartArea.contains(mouseX, mouseY)) {
            chart.onClick(mouseX, mouseY);
        } else if(preview != null && previewArea.contains(mouseX, mouseY) && !isPointInsideScroll(mouseX, mouseY)) {
            setScrollsValue(mouseX, mouseY);
        }
        for (ScrollListener changeListener : chartEventListeners) {
           // changeListener.update();
        }
    }

    @Override
    public void onDoubleClick(int mouseX, int mouseY) {
        chart.onDoubleClick(mouseX, mouseY);
    }

    @Override
    public void mouseMoved(int mouseX, int mouseY) {
        chart.mouseMoved(mouseX, mouseY);
    }

    @Override
    public void onScroll(int mouseX, int mouseY, int wheelRotation) {
        chart.mouseWheelMoved(mouseX, mouseY, wheelRotation);
    }


    class EventListener implements  ScrollListener {
        @Override
        public void hoverChanged() {
            for (ScrollListener changeListener : chartEventListeners) {
                changeListener.update();
            }
        }

        @Override
        public void xAxisActionPerformed(int xAxisIndex, int actionDirection) {
            System.out.println(xAxisIndex+" X AxisAction "+ actionDirection);
        }

        @Override
        public void yAxisActionPerformed(int yAxisIndex, int actionDirection) {
            System.out.println(yAxisIndex+" Y AxisAction "+ actionDirection);
        }

        @Override
        public void yAxisResetActionPerformed(int yAxisIndex) {
            System.out.println(yAxisIndex+" Y AxisReset ");
        }

        @Override
        public void xAxisResetActionPerformed(int xAxisIndex) {
            System.out.println(xAxisIndex+" X AxisReset ");
        }
    }*/
}
