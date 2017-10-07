package base.chart;

import base.axis.Axis;
import base.config.ChartConfig;
import base.config.ScrollConfig;
import base.config.axis.AxisConfig;
import base.config.axis.Orientation;
import base.config.general.Margin;
import base.Range;

import java.awt.*;

/**
 * Created by galafit on 3/10/17.
 */
public class BaseChartWithPreview {
    private Axis topXAxis;
    private Axis bottomXAxis;
    private boolean isPreviewEnable = true;
    private double scrollValue = 0;
    private BaseChart chart;
    private BaseChart preview;
    private ChartConfig chartConfig;
    private Rectangle area;
    private long chartWidth;
    private int viewportWidth = -1;
    private ScrollConfig scrollConfig = new ScrollConfig();
    private Rectangle chartArea;
    private Rectangle previewArea;

    public BaseChartWithPreview(ChartConfig chartConfig, Rectangle area) {
        chart = new BaseChart(chartConfig, area);
    }


    public BaseChartWithPreview(ChartConfig chartConfig, ChartConfig previewConfig, Rectangle area, long chartWidth) {
        this.chartConfig = chartConfig;
        this.area = area;
        this.chartWidth = chartWidth;
        int chartWeight = chartConfig.getSumWeight();
        int previewWeight = previewConfig.getSumWeight();

        int chartHeight = area.height * chartWeight / (chartWeight + previewWeight);
        int previewHeight = area.height * previewWeight / (chartWeight + previewWeight);
        chartArea = new Rectangle(area.x, area.y, area.width, chartHeight);
        previewArea = new Rectangle(area.x, area.y + chartHeight, area.width, previewHeight);

        if (chartConfig.getMargin() != null && previewConfig.getMargin() != null) {
            int left = Math.max(chartConfig.getMargin().left(), previewConfig.getMargin().left());
            int right = Math.max(chartConfig.getMargin().right(), previewConfig.getMargin().right());
            chartConfig.setMargin(new Margin(chartConfig.getMargin().top(), right, chartConfig.getMargin().bottom(), left));
            previewConfig.setMargin(new Margin(previewConfig.getMargin().top(), right, previewConfig.getMargin().bottom(), left));
            viewportWidth = area.width - left - right;
        }

        BaseChart chart = new BaseChart(chartConfig, chartArea);
        preview = new BaseChart(previewConfig, previewArea);

        Range chartMinMax = Range.max(chart.getBottomAxisExtremes(), chart.getTopAxisExtremes());
        Range previewMinMax = Range.max(preview.getBottomAxisExtremes(), preview.getTopAxisExtremes());

        previewMinMax = Range.max(previewMinMax, chartMinMax);
        preview.setBottomAxisExtremes(previewMinMax);
        preview.setTopAxisExtremes(previewMinMax);


        topXAxis = new Axis(new AxisConfig(Orientation.TOP));
        bottomXAxis = new Axis(new AxisConfig(Orientation.BOTTOM));
        topXAxis.setMinMax(previewMinMax);
        bottomXAxis.setMinMax(previewMinMax);
        topXAxis.setStartEnd(0, chartWidth);
        bottomXAxis.setStartEnd(0, chartWidth);


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
        chart = new BaseChart(chartConfig, chartArea);
    }

    public void draw(Graphics2D g2d) {
        chart.draw(g2d);
        if (preview != null) {
            preview.draw(g2d);
        }
    }
}
