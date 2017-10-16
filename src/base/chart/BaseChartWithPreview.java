package base.chart;

import base.DataSet;
import base.config.ChartConfig;
import base.config.ScrollConfig;
import base.config.general.Margin;
import base.Range;

import java.awt.*;

/**
 * Created by galafit on 3/10/17.
 */
public class BaseChartWithPreview {
    private BaseChart chart;
    private BaseChart preview;
    private ChartConfig chartConfig;
    private ScrollConfig scrollConfig = new ScrollConfig();
    private Rectangle chartArea;
    private Rectangle previewArea;

    public BaseChartWithPreview(ChartConfig chartConfig, Rectangle area) {
        chart = new BaseChart(chartConfig, area);
    }


    public BaseChartWithPreview(ChartConfig chartConfig, ChartConfig previewConfig, Rectangle area, long chartWidth) {
        this.chartConfig = chartConfig;
        int chartWeight = chartConfig.getSumWeight();
        int previewWeight = previewConfig.getSumWeight();

        int chartHeight = area.height * chartWeight / (chartWeight + previewWeight);
        int previewHeight = area.height * previewWeight / (chartWeight + previewWeight);
        chartArea = new Rectangle(area.x, area.y, area.width, chartHeight);
        previewArea = new Rectangle(area.x, area.y + chartHeight, area.width, previewHeight);

        chart = new BaseChart(chartConfig, chartArea);
        preview = new BaseChart(previewConfig, previewArea);

        Range minMax = Range.max(chart.getTracesXExtremes(), preview.getTracesXExtremes());
        preview.setBottomAxisExtremes(minMax);
        preview.setTopAxisExtremes(minMax);
        if(chartWidth > 0) {
            double extent = (minMax.end() - minMax.start()) * area.width / chartWidth;
            preview.createScroll(scrollConfig, extent);
        } else {
            double extentTop = (minMax.end() - minMax.start()) * area.width / chart.getPreferredTopAxisLength();
            double extentBottom = (minMax.end() - minMax.start()) * area.width / chart.getPreferredBottomAxisLength();
            preview.createScroll(scrollConfig, extentBottom, extentTop);
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



    public boolean hover(int mouseX, int mouseY) {
        //return chart.hover(mouseX, mouseY) || preview.hover(mouseX, mouseY);
        return chart.hover(mouseX, mouseY);
    }


    /**
     * @return true if scrollValue was changed and false if new scroll value = current scroll value
     */
    public boolean moveScroll(int mouseX, int mouseY) {
        boolean isScrollMoved = preview.moveScroll(mouseX, mouseY);
        if(isScrollMoved) {
            chart.setBottomAxisExtremes(getScrollExtremes(0));
            chart.setTopAxisExtremes(getScrollExtremes(1));
        }
        return isScrollMoved;
    }

    /**
     * @return true if scrollValue was changed and false if new scroll value = current scroll value
     */
    public boolean moveScroll(double newScrollValue) {
        boolean isScrollMoved = preview.moveScroll(newScrollValue);
        if(isScrollMoved) {
            chart.setBottomAxisExtremes(getScrollExtremes(0));
            chart.setTopAxisExtremes(getScrollExtremes(1));
        }
        return isScrollMoved;
    }

    public Range getScrollExtremes(int xAxisIndex) {
        if(xAxisIndex == 0) {
            return preview.getScrollExtremes1();
        }
        return preview.getScrollExtremes2();
    }

    public boolean isMouseInsideScroll(int mouseX, int mouseY) {
        if(preview == null) {
            return false;
        }
        return preview.isMouseInsideScroll(mouseX, mouseY);
    }

    public boolean isMouseInsidePreview(int mouseX, int mouseY) {
        if(preview == null) {
            return false;
        }
        return preview.isMouseInsideChart(mouseX, mouseY);
    }

    public boolean isMouseInsideChart(int mouseX, int mouseY) {
        return chart.isMouseInsideChart(mouseX, mouseY);
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
        }
    }
}
