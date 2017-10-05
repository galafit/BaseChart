import axis.Axis;
import configuration.ChartConfig;
import configuration.ScrollConfig;
import configuration.axis.AxisConfig;
import configuration.axis.Orientation;
import configuration.general.Margin;
import data.Range;

import java.awt.*;

/**
 * Created by galafit on 3/10/17.
 */
public class ChartWithPreview implements Drawable {
    private Axis topXAxis;
    private Axis bottomXAxis;
    private boolean isPreviewEnable = true;
    private double scrollValue = 0;
    private Chart chart;
    private Chart preview;
    private ChartConfig chartConfig;
    private Rectangle area;
    private long chartWidth;
    private int viewportWidth = -1;
    private ScrollConfig scrollConfig = new ScrollConfig();

    public ChartWithPreview(ChartConfig chartConfig) {
        chart = new Chart(chartConfig);
    }


    public ChartWithPreview(ChartConfig chartConfig, ChartConfig previewConfig, Rectangle area, long chartWidth) {
        this.chartConfig = chartConfig;
        this.area = area;
        this.chartWidth = chartWidth;
        int chartWeight = chartConfig.getSumWeight();
        int previewWeight = previewConfig.getSumWeight();

        int chartHeight = area.height * chartWeight / (chartWeight + previewWeight);
        int previewHeight = area.height * previewWeight / (chartWeight + previewWeight);
        Rectangle chartRectangle = new Rectangle(area.x, area.y, area.width, chartHeight);
        Rectangle previewRectangle = new Rectangle(area.x, area.y + chartHeight, area.width, previewHeight);
        chartConfig.setArea(chartRectangle);
        previewConfig.setArea(previewRectangle);

        Range minMax = null;
        for (int i = 0; i < chartConfig.getTraceAmount(); i++) {
            minMax = Range.max(minMax, chartConfig.getTraceConfig(i).getData().getDataSet().getXExtremes());
        }
        for (int i = 0; i < previewConfig.getTraceAmount(); i++) {
            minMax = Range.max(minMax, previewConfig.getTraceConfig(i).getData().getDataSet().getXExtremes());
        }

        if(minMax != null) {
            previewConfig.getBottomAxisConfig().setExtremes(minMax.start(), minMax.end());
            previewConfig.getTopAxisConfig().setExtremes(minMax.start(), minMax.end());
        }


        topXAxis = new Axis(new AxisConfig(Orientation.TOP));
        bottomXAxis = new Axis(new AxisConfig(Orientation.BOTTOM));
        topXAxis.setMinMax(minMax);
        bottomXAxis.setMinMax(minMax);
        topXAxis.setStartEnd(0, chartWidth);
        bottomXAxis.setStartEnd(0, chartWidth);

        if (chartConfig.margin != null && previewConfig.margin != null) {
            int left = Math.max(chartConfig.margin.left(), previewConfig.margin.left());
            int right = Math.max(chartConfig.margin.right(), previewConfig.margin.right());
            chartConfig.margin = new Margin(chartConfig.margin.top(), right, chartConfig.margin.bottom(), left);
            previewConfig.margin = new Margin(previewConfig.margin.top(), right, previewConfig.margin.bottom(), left);
            viewportWidth = area.width - left - right;
        }

        preview = new Chart(previewConfig);
        moveScroll(scrollValue);
    }

    public boolean hover(int mouseX, int mouseY) {
        return chart.hover(mouseX, mouseY);
    }

    public void moveScroll(int mouseX, int mouseY) {
        if (preview!= null && preview.isMouseInsideChart(mouseX, mouseY)) {
            double newScrollValue = preview.calculateScrollValue(mouseX);
            moveScroll(newScrollValue);
        }
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


    private void moveScroll(double newScrollValue) {
        double scrollStart = bottomXAxis.scale(scrollValue);
        double scrollExtent = bottomXAxis.invert(scrollStart + viewportWidth) - scrollValue;
        preview.setScroll(newScrollValue, scrollExtent, scrollConfig);
        scrollValue = newScrollValue;
        chartConfig.getBottomAxisConfig().setExtremes(scrollValue, scrollValue + scrollExtent);
        chart = new Chart(chartConfig);
    }

    @Override
    public void draw(Graphics2D g2d) {
        chart.draw(g2d);
        if (preview != null) {
            preview.draw(g2d);
        }
    }
}
