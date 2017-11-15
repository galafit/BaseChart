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
    private InteractiveChart chart;
    private InteractiveChart preview;
    private ScrollConfig scrollConfig = new ScrollConfig();
    private Rectangle chartArea;
    private Rectangle previewArea;
    private Scroll scroll;
    private double screenExtent;

    public ChartWithPreview(ChartConfig chartConfig, Rectangle area) {
        chart = new InteractiveChart(chartConfig, area);
    }

    public ChartWithPreview(ChartConfig chartConfig, ChartConfig previewConfig, Rectangle area, double screenExtent) {
        int chartWeight = chartConfig.getSumWeight();
        int previewWeight = previewConfig.getSumWeight();

        int chartHeight = area.height * chartWeight / (chartWeight + previewWeight);
        int previewHeight = area.height * previewWeight / (chartWeight + previewWeight);
        chartArea = new Rectangle(area.x, area.y, area.width, chartHeight);
        previewArea = new Rectangle(area.x, area.y + chartHeight, area.width, previewHeight);

        chart = new InteractiveChart(chartConfig, chartArea);
        preview = new InteractiveChart(previewConfig, previewArea);

        Range minMax = Range.max(chart.getTracesXExtremes(), preview.getTracesXExtremes());

        preview.setXAxisExtremes(0, minMax);
        preview.setXAxisExtremes(1, minMax);
        this.screenExtent = screenExtent;
        scroll = new Scroll(scrollConfig, getPreferredBottomExtent(), getPreferredTopExtent(),  preview.getXAxisType(0));

        scroll.setMinMax(minMax);

        chart.setXAxisExtremes(0, getScrollExtremes(0));
        chart.setXAxisExtremes(1, getScrollExtremes(1));
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
    public boolean moveScroll(double newValue) {
        boolean isScrollMoved = scroll.moveScroll(newValue);
        if(isScrollMoved) {
            chart.setXAxisExtremes(0, getScrollExtremes(0));
            chart.setXAxisExtremes(1, getScrollExtremes(1));
        }
        return isScrollMoved;
    }

    private double getPreferredTopExtent() {
        double extentTop = 0;
        if(screenExtent > 0) {
            extentTop = screenExtent;
        } else {
            double topAxisPreferredLength = chart.getPreferredTopAxisLength();
            if(topAxisPreferredLength > 0) {
                Range minMax = preview.getXAxisMinMax(0);
                extentTop = (minMax.end() - minMax.start()) * previewArea.width / topAxisPreferredLength;
            }
        }
        return extentTop;
    }

    private double getPreferredBottomExtent() {
        double extentBottom = 0;
        if(screenExtent > 0) {
            extentBottom = screenExtent;
        } else {
            Range minMax = preview.getXAxisMinMax(0);
            double bottomAxisPreferredLength = chart.getPreferredBottomAxisLength();
            if(bottomAxisPreferredLength > 0) {
                extentBottom = (minMax.end() - minMax.start()) * previewArea.width / bottomAxisPreferredLength;

            }
        }
        return extentBottom;
    }


    private void shiftChart(int dx) {
        double scrollTranslation = dx/ scroll.getRation();
        scroll.translate(scrollTranslation);
        chart.setXAxisExtremes(0, getScrollExtremes(0));
        chart.setXAxisExtremes(1, getScrollExtremes(1));
    }

    private void zoomScroll(int extentNumber, double zoomFactor) {
        if(extentNumber == 0) {
            scroll.setScrollExtent1(scroll.getScrollExtent1() * zoomFactor);
            chart.setXAxisExtremes(0, getScrollExtremes(0));
        }

        if(extentNumber == 1) {
            scroll.setScrollExtent2(scroll.getScrollExtent2() * zoomFactor);
            chart.setXAxisExtremes(1, getScrollExtremes(1));
        }
    }


    public Range getScrollExtremes(int xAxisIndex) {
        if(xAxisIndex == 0) {
            return scroll.getScrollExtremes1();
        }
        return scroll.getScrollExtremes2();
    }

    public void draw(Graphics2D g2d) {
         Margin chartMargin = chart.getMargin(g2d);
         Margin previewMargin = preview.getMargin(g2d);
         if(chartMargin.left() != previewMargin.left() || chartMargin.right() != previewMargin.right()) {
             int left = Math.max(chartMargin.left(), previewMargin.left());
             int right = Math.max(chartMargin.right(), previewMargin.right());
             chartMargin = new Margin(chartMargin.top(), right, chartMargin.bottom(), left);
             previewMargin = new Margin(previewMargin.top(), right, previewMargin.bottom(), left);
             chart.setMargin(g2d, chartMargin);
             preview.setMargin(g2d, previewMargin);
         }
        scroll.setStartEnd(preview.getXAxisRange(0));


        chart.draw(g2d);
        if (preview != null) {
            preview.draw(g2d);
            scroll.draw(g2d, preview.getGraphArea());
        }
    }

    /**=======================Base methods to interact with chart==========================**/

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
        if(preview == null) {
            chart.zoomX(xAxisIndex, zoomFactor);
        } else {
            zoomScroll(xAxisIndex, zoomFactor);
        }

    }

    public void translateChartY(int yAxisIndex, int dy) {
        chart.translateY(yAxisIndex, dy);
    }

    public void translateChartX(int xAxisIndex, int dx) {
        if(preview == null) {
            chart.translateX(xAxisIndex, dx);
        } else {
            shiftChart(dx);
        }
    }

    public void autoscaleChartX(int xAxisIndex) {
        if(preview == null) {
            chart.autoscaleXAxis(xAxisIndex);
        } else {
            scroll.setScrollExtent1(getPreferredBottomExtent());
            scroll.setScrollExtent2(getPreferredTopExtent());
            chart.setXAxisExtremes(0, getScrollExtremes(0));
            chart.setXAxisExtremes(1, getScrollExtremes(1));
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

    /**=======================Base methods to interact with preview==========================**/
    public boolean isPointInsideScroll(int x, int y) {
        if(preview == null) {
            return false;
        }
        return scroll.isMouseInsideScroll(x, y, preview.getGraphArea());
    }


    public boolean isPointInsidePreview(int x, int y) {
        if(preview == null) {
            return false;
        }
        return previewArea.contains(x, y);
    }

    /**
     * @return true if scrollValue was changed and false if newValue = current scroll value
     */
    public boolean moveScroll(int x, int y) {
        if(!previewArea.contains(x, y)) {
            return false;
        }
        boolean isScrollMoved = scroll.moveScroll(x, y);
        if(isScrollMoved) {
            chart.setXAxisExtremes(0, getScrollExtremes(0));
            chart.setXAxisExtremes(1, getScrollExtremes(1));
        }
        return isScrollMoved;
    }

    public boolean translateScroll(int dx) {
        boolean isScrollMoved = scroll.translate(dx);
        if(isScrollMoved) {
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
        } else if(preview != null && previewArea.contains(mouseX, mouseY) && !isMouseInsideScroll(mouseX, mouseY)) {
            moveScroll(mouseX, mouseY);
        }
        for (ChartEventListener changeListener : chartEventListeners) {
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


    class EventListener implements  ChartEventListener {
        @Override
        public void hoverChanged() {
            for (ChartEventListener changeListener : chartEventListeners) {
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
