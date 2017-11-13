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

    public ChartWithPreview(ChartConfig chartConfig, Rectangle area) {
        chart = new InteractiveChart(chartConfig, area);
    }

    public ChartWithPreview(ChartConfig chartConfig, ChartConfig previewConfig, Rectangle area, long chartWidth) {
        int chartWeight = chartConfig.getSumWeight();
        int previewWeight = previewConfig.getSumWeight();

        int chartHeight = area.height * chartWeight / (chartWeight + previewWeight);
        int previewHeight = area.height * previewWeight / (chartWeight + previewWeight);
        chartArea = new Rectangle(area.x, area.y, area.width, chartHeight);
        previewArea = new Rectangle(area.x, area.y + chartHeight, area.width, previewHeight);

        chart = new InteractiveChart(chartConfig, chartArea);
        preview = new InteractiveChart(previewConfig, previewArea);

        Range minMax = Range.max(chart.getTracesXExtremes(), preview.getTracesXExtremes());
        preview.setBottomAxisExtremes(minMax);
        preview.setTopAxisExtremes(minMax);
        if(chartWidth > 0) {
            double extent = (minMax.end() - minMax.start()) * area.width / chartWidth;
            scroll = new Scroll(scrollConfig, extent, extent, preview.getBottomScale());

        } else {
            double extentTop = (minMax.end() - minMax.start()) * area.width / chart.getPreferredTopAxisLength();
            double extentBottom = (minMax.end() - minMax.start()) * area.width / chart.getPreferredBottomAxisLength();
            scroll = new Scroll(scrollConfig, extentBottom, extentTop,  preview.getBottomScale());
        }
        chart.setBottomAxisExtremes(getScrollExtremes(0));
        chart.setTopAxisExtremes(getScrollExtremes(1));
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
    public boolean moveScroll(int mouseX, int mouseY) {
        if(!previewArea.contains(mouseX, mouseY)) {
           return false;
        }
        boolean isScrollMoved = scroll.moveScroll(mouseX, mouseY);
        if(isScrollMoved) {
            chart.setBottomAxisExtremes(getScrollExtremes(0));
            chart.setTopAxisExtremes(getScrollExtremes(1));
        }
        return isScrollMoved;
    }

    /**
     * @return true if scrollValue was changed and false if newValue = current scroll value
     */
    public boolean moveScroll(double newValue) {
        boolean isScrollMoved = scroll.moveScroll(newValue);
        if(isScrollMoved) {
            chart.setBottomAxisExtremes(getScrollExtremes(0));
            chart.setTopAxisExtremes(getScrollExtremes(1));
        }
        return isScrollMoved;
    }


    public boolean isMouseInsideScroll(int mouseX, int mouseY) {
        if(preview == null) {
            return false;
        }
        return scroll.isMouseInsideScroll(mouseX, mouseY, preview.getGraphArea());
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

        chart.draw(g2d);
        if (preview != null) {
            preview.draw(g2d);
            scroll.draw(g2d, preview.getGraphArea());
        }
    }

    /**=======================Base methods to interact==========================**/

    public int getSelectedTraceIndex() {
        return chart.getSelectedTraceIndex();
    }

    public int getTraceYAxisIndex(int traceIndex) {
        return chart.getTraceYAxisIndex(traceIndex);
    }

    public int getTraceXAxisIndex(int traceIndex) {
        return chart.getTraceXAxisIndex(traceIndex);
    }


    public List<Integer> getStackYAxisUsedIndexes(int x, int y) {
        return chart.getStackYAxisUsedIndexes(x, y);
    }

    public List<Integer> getStackXAxisUsedIndexes(int x, int y) {
        return chart.getStackXAxisUsedIndexes(x, y);
    }

    public List<Integer> getYAxisUsedIndexes() {
        return chart.getYAxisUsedIndexes();
    }

    public List<Integer> getXAxisUsedIndexes() {
        return chart.getXAxisUsedIndexes();
    }

    public void zoomY(int yAxisIndex, double zoomFactor) {
        chart.zoomY(yAxisIndex, zoomFactor);
    }

    public void zoomX(int xAxisIndex, double zoomFactor) {
        chart.zoomX(xAxisIndex, zoomFactor);
    }

    public void translateY(int yAxisIndex, int dy) {
        chart.translateY(yAxisIndex, dy);
    }

    public void translateX(int xAxisIndex, int dx) {
        chart.translateX(xAxisIndex, dx);
    }

    public void zoomY(int yAxisIndex, int dy) {
        chart.zoomY(yAxisIndex, dy);
    }

    public void zoomX(int xAxisIndex, int dx) {
        chart.zoomX(xAxisIndex, dx);
    }

    public void autoscaleXAxis(int xAxisIndex) {
        chart.autoscaleXAxis(xAxisIndex);
    }

    public void autoscaleYAxis(int yAxisIndex) {
        chart.autoscaleYAxis(yAxisIndex);
    }

    public boolean hoverOff() {
       return chart.hoverOff();
    }

    public boolean hoverOn(int x, int y) {
       return chart.hoverOn(x, y);
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
