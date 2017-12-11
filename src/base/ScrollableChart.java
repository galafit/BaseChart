package base;

import base.config.ScrollableChartConfig;
import base.config.SimpleChartConfig;
import base.config.general.Margin;
import java.awt.*;
import java.util.*;
import java.util.List;


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

    public ScrollableChart(ScrollableChartConfig config, Rectangle area) {
        this.config = config;
        SimpleChartConfig chartConfig = config.getChartConfig();
        Set<Integer> scrollableAxis = config.getXAxisWithScroll();
        if(!isPreviewEnable()) {
            chart = new SimpleChart(chartConfig, area);
        } else {
            SimpleChartConfig previewConfig = config.getPreviewConfig();
            int chartWeight = chartConfig.getSumWeight();
            int previewWeight = previewConfig.getSumWeight();
            int chartHeight = area.height * chartWeight / (chartWeight + previewWeight);
            int previewHeight = area.height * previewWeight / (chartWeight + previewWeight);
            chartArea = new Rectangle(area.x, area.y, area.width, chartHeight);
            previewArea = new Rectangle(area.x, area.y + chartHeight, area.width, previewHeight);
            chart = new SimpleChart(chartConfig, chartArea);

            if(config.getPreviewMinMax() == null) {
                Range previewMinMax = null;
                for (Integer xAxisIndex : scrollableAxis) {
                    previewMinMax = Range.max(previewMinMax, chart.getXAxisMinMax(xAxisIndex));
                }
                config.setPreviewMinMax(previewMinMax);
            }

            preview = new SimpleChart(previewConfig, previewArea);

            for (Integer xAxisIndex : scrollableAxis) {
                Scroll scroll = new Scroll(config.getScrollExtent(xAxisIndex), config.getScrollConfig(), preview.getXAxisScale(0));
                scrolls.put(xAxisIndex, scroll);
                Range scrollRange = new Range(scroll.getValue(), scroll.getValue() + scroll.getExtent());
                chart.setXAxisExtremes(xAxisIndex,  scrollRange);
                scroll.addListener(new ScrollListener() {
                    @Override
                    public void onScrollChanged(double scrollValue, double scrollExtent) {
                        chart.setXAxisExtremes(xAxisIndex, new Range(scrollValue, scrollValue + scrollExtent));
                    }
                });
            }
        }
    }

    public boolean isPreviewEnable() {
       return config.isPreviewEnable();
    }

    public void setPreviewMinMax(Range minMax) {
        for (int i = 0; i < preview.getNumberOfXAxis(); i++) {
            preview.setXAxisExtremes(i, minMax);
        }
    }

    public Range getPreviewMinMax() {
        return preview.getXAxisMinMax(0);
    }

    public void addScrollsListener(int xAxisIndex, ScrollListener scrollListener) {
        scrolls.get(xAxisIndex).addListener(scrollListener);
    }

    public void setChartData(ArrayList<DataSet> data) {
       chart.setData(data);
    }

    public void setPreviewData(ArrayList<DataSet> data) {
        preview.setData(data);
    }

    /**
     * @return true if scrollValue was changed and false if newValue = current scroll value
     */
    public boolean moveScrollsTo(double newValue) {
        boolean scrollsMoved = false;
        for (Integer xAxisIndex : scrolls.keySet()) {
            scrollsMoved = scrolls.get(xAxisIndex).moveScrollTo(newValue);
        }
        return scrollsMoved;
    }


    public void draw(Graphics2D g2d) {
        if(preview != null) {
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
            preview.draw(g2d);
            for (Integer key : scrolls.keySet()) {
                scrolls.get(key).draw(g2d, preview.getGraphArea(g2d));
            }
        }
        chart.draw(g2d);
    }

    /**
     * =======================Base methods to interact with chart==========================
     **/

    public int getChartSelectedTraceIndex() {
        return chart.getSelectedTraceIndex();
    }

    public Range getChartYRange(int yAxisIndex) {
        return chart.getYAxisRange(yAxisIndex);
    }

    public int getChartTraceYIndex(int traceIndex) {
        return chart.getTraceYAxisIndex(traceIndex);
    }

    public int getChartTraceXIndex(int traceIndex) {
        return chart.getTraceXAxisIndex(traceIndex);
    }

    public int getChartYIndex(int x, int y) {
        return chart.getYAxisIndex(x, y);
    }

    public List<Integer> getChartStackXIndexes(int x, int y) {
        return chart.getStackXAxisIndexes(x, y);
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
            Range minMax = Range.max(chart.getXAxisMinMax(0), chart.getXAxisMinMax(1));
            minMax = Range.max(minMax, preview.getXAxisMinMax(0));
            preview.setXAxisExtremes(0, minMax);
            preview.setXAxisExtremes(1, minMax);
            scrolls.get(xAxisIndex).setExtent(chart.getXAxisMinMax(xAxisIndex).length());
        }
    }

    public void translateChartY(int yAxisIndex, int dy) {
        chart.translateY(yAxisIndex, dy);
    }

    public void translateChartX(int xAxisIndex, int dx) {
        if (preview == null) {
            chart.translateX(xAxisIndex, dx);
        } else {
            double scrollTranslation = dx / scrolls.get(0).getRation();
            for (Integer key : scrolls.keySet()) {
                scrollTranslation = Math.min(scrollTranslation, dx / scrolls.get(key).getRation());
            }
            for (Integer key : scrolls.keySet()) {
                scrolls.get(key).translateScroll(scrollTranslation);
            }
        }
    }

    public void autoscaleChartX(int xAxisIndex) {
        if (preview == null) {
            chart.autoscaleXAxis(xAxisIndex);
        }
    }

    public void autoscaleChartY(int yAxisIndex) {
        chart.autoscaleYAxis(yAxisIndex);
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

    /**
     * @return true if scrollValue was changed and false if newValue = current scroll value
     */
    public boolean moveScrollsTo(int x, int y) {
        if (previewArea != null || !previewArea.contains(x, y)) {
            return false;
        }
        boolean scrollsMoved = false;
        for (Integer key : scrolls.keySet()) {
            scrollsMoved = scrolls.get(key).moveScrollTo(x, y);
        }
        return scrollsMoved;
    }

    public boolean translateScroll(int dx) {
       boolean isScrollMoved = false;
        for (Integer key : scrolls.keySet()) {
            isScrollMoved = scrolls.get(key).translateScroll(dx);
        }
        return isScrollMoved;
    }

    public int getPreviewSelectedTraceIndex() {
        if(preview != null) {
            return preview.getSelectedTraceIndex();
        }
        return -1;
    }

    public Range getPreviewYRange(int yAxisIndex) {
        return preview.getYAxisRange(yAxisIndex);
    }

    public int getPreviewTraceYIndex(int traceIndex) {
        return preview.getTraceYAxisIndex(traceIndex);
    }

    public int getPreviewYIndex(int x, int y) {
        if(preview != null) {
            return preview.getYAxisIndex(x, y);
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

    public void autoscalePreviewY(int yAxisIndex) {
        if(preview != null) {
            preview.autoscaleYAxis(yAxisIndex);
        }
    }




/*    @Override
    public void onClick(int mouseX, int mouseY) {
        if(chartArea.contains(mouseX, mouseY)) {
            chart.onClick(mouseX, mouseY);
        } else if(preview != null && previewArea.contains(mouseX, mouseY) && !isPointInsideScroll(mouseX, mouseY)) {
            moveScrollsTo(mouseX, mouseY);
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
