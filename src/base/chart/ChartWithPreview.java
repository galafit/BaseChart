package base.chart;

import base.DataSet;
import base.config.ChartConfig;
import base.config.ScrollConfig;
import base.config.general.Margin;
import base.Range;

import java.awt.*;
import java.util.List;


/**
 * Created by galafit on 3/10/17.
 */
public class ChartWithPreview {
    private SimpleChart chart;
    private SimpleChart preview;
    private ScrollConfig scrollConfig = new ScrollConfig();
    private Rectangle chartArea;
    private Rectangle previewArea;
    private Scroll scroll;
    private double bottomExtent;
    private double topExtent;

    public ChartWithPreview(ChartConfig chartConfig, Rectangle area) {
        chart = new SimpleChart(chartConfig, area);
    }

    public ChartWithPreview(ChartConfig chartConfig, ChartConfig previewConfig, Rectangle area) {
        int chartWeight = chartConfig.getSumWeight();
        int previewWeight = previewConfig.getSumWeight();

        int chartHeight = area.height * chartWeight / (chartWeight + previewWeight);
        int previewHeight = area.height * previewWeight / (chartWeight + previewWeight);
        chartArea = new Rectangle(area.x, area.y, area.width, chartHeight);
        previewArea = new Rectangle(area.x, area.y + chartHeight, area.width, previewHeight);

        chart = new SimpleChart(chartConfig, chartArea);
        preview = new SimpleChart(previewConfig, previewArea);

        scroll = new Scroll(scrollConfig, getBottomExtent(), getTopExtent(), preview.getXAxisScale(0));

        //chart.setXAxisExtremes(0, getScrollExtremes(0));
       // chart.setXAxisExtremes(1, getScrollExtremes(1));
    }

    public void addScrollListener(ScrollListener listener) {
        scroll.addListener(listener);
    }

    public void setTraceData(DataSet data, int traceIndex) {
        chart.setTraceData(data, traceIndex);
    }

    public void setPreviewTraceData(DataSet data, int traceIndex) {
        preview.setTraceData(data, traceIndex);
    }

    /**
     * @return true if scrollValue was changed and false if newValue = current scroll value
     */
    public boolean moveScrollTo(double newValue) {
        boolean isScrollMoved = scroll.moveScrollTo(newValue);
        if (isScrollMoved) {
            chart.setXAxisExtremes(0, getScrollExtremes(0));
            chart.setXAxisExtremes(1, getScrollExtremes(1));
        }
        return isScrollMoved;
    }

    public void moveScrollToStart() {
        moveScrollTo(scroll.getMin());
    }

    public void moveScrollToEnd() {
        moveScrollTo(scroll.getMax());
    }

    public double getScrollValue() {
        return scroll.getValue();
    }

    public double getScrollExtent0() {
        return scroll.getScrollExtent0();
    }

    public double getScrollExtent1() {
        return scroll.getScrollExtent1();
    }

    private double calculatePreferredExtent(int xAxisIndex) {
        double preferredExtent = 0;
        for (int i = 0; i < chart.getTraceAmount(); i++) {
            if (chart.getTraceXAxisIndex(i) == xAxisIndex) {
                DataSet traceData = chart.getTraceData(i);
                int preferredTraceLength = (traceData.size() -1) * chart.getPreferredTraceDataMarkSize(i);
                if(traceData.getXExtremes().length() > chart.getXAxisMinMax(xAxisIndex).length()) {
                    double extent = traceData.getXExtremes().length() * chartArea.width / preferredTraceLength ;
                    preferredExtent = Math.max(preferredExtent, extent);
                } else {
                    preferredExtent = chart.getXAxisMinMax(xAxisIndex).length();
                }
            }
        }
        return preferredExtent;
    }

    private double getBottomExtent() {
        if(bottomExtent == 0) {
            bottomExtent = calculatePreferredExtent(0);
        }
        return bottomExtent;
    }

    private double getTopExtent() {
        if(topExtent == 0) {
            topExtent = calculatePreferredExtent(1);
        }
        return topExtent;
    }


    private void translateChart(int dx) {
        double scrollTranslation = dx / scroll.getRation();
        scroll.translateScroll(scrollTranslation);
        chart.setXAxisExtremes(0, getScrollExtremes(0));
        chart.setXAxisExtremes(1, getScrollExtremes(1));
    }


    public Range getScrollExtremes(int xAxisIndex) {
        if (xAxisIndex == 0) {
            return scroll.getScrollExtremes1();
        }
        return scroll.getScrollExtremes2();
    }

    public void draw(Graphics2D g2d) {
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

        chart.draw(g2d);
        if (preview != null) {
            preview.draw(g2d);
            scroll.draw(g2d, preview.getGraphArea(g2d));
        }
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

    public List<Integer> getChartYIndexes() {
        return chart.getYAxisIndexes();
    }

    public List<Integer> getChartXIndexes() {
        return chart.getXAxisIndexes();
    }

    public void zoomChartY(int yAxisIndex, double zoomFactor) {
        chart.zoomY(yAxisIndex, zoomFactor);
    }

    public void zoomChartX(int xAxisIndex, double zoomFactor) {
        if (preview == null) {
            chart.zoomX(xAxisIndex, zoomFactor);
        } else {
            chart.zoomX(xAxisIndex, zoomFactor);
            if(xAxisIndex == 0) {
                scroll.setScrollExtent0(chart.getXAxisMinMax(xAxisIndex).length());
            }
            if(xAxisIndex == 1) {
                scroll.setScrollExtent1(chart.getXAxisMinMax(xAxisIndex).length());
            }

        }
    }


    public void translateChartY(int yAxisIndex, int dy) {
        chart.translateY(yAxisIndex, dy);
    }

    public void translateChartX(int xAxisIndex, int dx) {
        if (preview == null) {
            chart.translateX(xAxisIndex, dx);
        } else {
            translateChart(dx);
        }
    }

    public void autoscaleChartX(int xAxisIndex) {
        if (preview == null) {
            chart.autoscaleXAxis(xAxisIndex);
        } else {
          /*  scroll.setScrollExtent0(getBottomExtent());
            scroll.setScrollExtent1(getTopExtent());
            chart.setXAxisExtremes(0, getScrollExtremes(0));
            chart.setXAxisExtremes(1, getScrollExtremes(1)); */
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
    public boolean isPointInsideScroll(int x, int y) {
        if (preview != null && previewArea.contains(x, y) && scroll.isPointInsideScroll(x)) {
            return true;
        }
        return false;
    }


    public boolean isPointInsidePreview(int x, int y) {
        if (preview == null) {
            return false;
        }
        return previewArea.contains(x, y);
    }

    /**
     * @return true if scrollValue was changed and false if newValue = current scroll value
     */
    public boolean moveScrollTo(int x, int y) {
        if (!previewArea.contains(x, y)) {
            return false;
        }
        boolean isScrollMoved = scroll.moveScrollTo(x, y);
        if (isScrollMoved) {
            chart.setXAxisExtremes(0, getScrollExtremes(0));
            chart.setXAxisExtremes(1, getScrollExtremes(1));
        }
        return isScrollMoved;
    }

    public boolean translateScroll(int dx) {
        boolean isScrollMoved = scroll.translateScroll(dx);
        if (isScrollMoved) {
            chart.setXAxisExtremes(0, getScrollExtremes(0));
            chart.setXAxisExtremes(1, getScrollExtremes(1));
        }
        return isScrollMoved;
    }

    public int getPreviewSelectedTraceIndex() {
        return preview.getSelectedTraceIndex();
    }

    public Range getPreviewYRange(int yAxisIndex) {
        return preview.getYAxisRange(yAxisIndex);
    }

    public int getPreviewTraceYIndex(int traceIndex) {
        return preview.getTraceYAxisIndex(traceIndex);
    }

    public int getPreviewYIndex(int x, int y) {
        return preview.getYAxisIndex(x, y);
    }

    public List<Integer> getPreviewYIndexes() {
        return preview.getYAxisIndexes();
    }

    public void zoomPreviewY(int yAxisIndex, double zoomFactor) {
        preview.zoomY(yAxisIndex, zoomFactor);
    }

    public void translatePreviewY(int yAxisIndex, int dy) {
        preview.translateY(yAxisIndex, dy);
    }

    public void autoscalePreviewY(int yAxisIndex) {
        preview.autoscaleYAxis(yAxisIndex);
    }




/*    @Override
    public void onClick(int mouseX, int mouseY) {
        if(chartArea.contains(mouseX, mouseY)) {
            chart.onClick(mouseX, mouseY);
        } else if(preview != null && previewArea.contains(mouseX, mouseY) && !isPointInsideScroll(mouseX, mouseY)) {
            moveScrollTo(mouseX, mouseY);
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
