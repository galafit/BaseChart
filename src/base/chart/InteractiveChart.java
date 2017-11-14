package base.chart;

import base.DataSet;
import base.Range;
import base.config.ChartConfig;
import base.config.general.Margin;
import base.painters.CrosshairPainter;
import base.painters.LegendPainter;
import base.painters.TitlePainter;
import base.painters.TooltipPainter;
import base.scales.Scale;
import base.tooltips.TooltipInfo;

import java.awt.*;
import java.util.List;

/**
 * Created by galafit on 27/10/17.
 */
public class InteractiveChart {
    private SimpleChart chart;

    private LegendPainter legendPainter;
    private TitlePainter titlePainter;
    private Rectangle titleArea;
    private Rectangle legendArea;
    private Rectangle fullArea;
    private ChartConfig chartConfig;
    private boolean isDirty = true;

    private CrosshairPainter crosshairPainter;
    private TooltipPainter tooltipPainter;
    private TooltipInfo tooltipInfo;

    private int selectedTraceIndex = -1;
    private int hoverIndex = -1;


    public InteractiveChart(ChartConfig chartConfig, Rectangle area) {
        fullArea = area;
        this.chartConfig = chartConfig;
        chart = new SimpleChart(chartConfig);
        tooltipPainter = new TooltipPainter(chartConfig.getTooltipConfig());
        crosshairPainter = new CrosshairPainter(chartConfig.getCrosshairConfig());
        titlePainter = new TitlePainter(chartConfig.getTitle(), chartConfig.getTitleTextStyle());
        legendPainter = new LegendPainter(chart.getTracesInfo(), chartConfig.getLegendConfig());
    }

    private TooltipInfo getTooltipInfo() {
        TooltipInfo tooltipInfo = null;
        if (selectedTraceIndex >= 0 && hoverIndex >= 0) {
            tooltipInfo = new TooltipInfo();
            tooltipInfo.addItems(chart.getDataInfo(selectedTraceIndex, hoverIndex));
            Point dataPosition = chart.getDataPosition(selectedTraceIndex, hoverIndex);
            tooltipInfo.setXY(dataPosition.x, dataPosition.y);
        }
        return tooltipInfo;
    }

    public int getTraceAmount() {
        return chart.getTraceAmount();
    }

    public DataSet getTraceData(int traceIndex) {
        return chart.getTraceData(traceIndex);
    }

    public void setTraceData(DataSet data, int traceIndex) {
        chart.setTraceData(data, traceIndex);
    }

    Margin getMargin(Graphics2D g2) {
        if (isDirty) {
            calculateAreas(g2);
        }
        return chart.getMargin(g2);
    }

    void setMargin(Graphics2D g2, Margin margin) {
        if (isDirty) {
            calculateAreas(g2);
        }
        chart.setMargin(g2, margin);
    }

    Rectangle getGraphArea() {
        return chart.getGraphArea();
    }

    Scale getBottomScale() {
        return chart.getBottomAxis().getScale();
    }

    public void setArea(Rectangle area, Graphics2D g2) {
        fullArea = area;
        isDirty = true;
    }

    private void calculateAreas(Graphics2D g2) {
        int titleHeight = titlePainter.getTitleHeight(g2, fullArea.width);
        int legendHeight = legendPainter.getLegendHeight(g2, fullArea.width);

        if (chartConfig.getLegendConfig().isTop()) {

        }
        titleArea = new Rectangle(fullArea.x, fullArea.y, fullArea.width, titleHeight);
        Rectangle chartArea;
        if (chartConfig.getLegendConfig().isTop()) {
            legendArea = new Rectangle(fullArea.x, fullArea.y + titleHeight, fullArea.width, legendHeight);
            chartArea = new Rectangle(fullArea.x, fullArea.y + titleHeight + legendHeight, fullArea.width, fullArea.height - titleHeight - legendHeight);
        } else {
            legendArea = new Rectangle(fullArea.x, fullArea.y + fullArea.height - legendHeight, fullArea.width, legendHeight);
            chartArea = new Rectangle(fullArea.x, fullArea.y + titleHeight, fullArea.width, fullArea.height - titleHeight - legendHeight);
        }
        chart.setArea(chartArea);
        isDirty = false;
    }

    public void draw(Graphics2D g2) {
        if (isDirty) {
            calculateAreas(g2);
        }

        g2.setColor(chartConfig.getMarginColor());
        g2.fill(fullArea);

        chart.draw(g2);
        tooltipInfo = getTooltipInfo();
        if (tooltipInfo != null) {
            tooltipPainter.draw(g2, chart.getFullArea(), tooltipInfo);
            crosshairPainter.draw(g2, chart.getGraphArea(), tooltipInfo.getX(), tooltipInfo.getY());
        }
        titlePainter.draw(g2, titleArea);
        legendPainter.draw(g2, legendArea);
    }


    int getPreferredTopAxisLength() {
        return chart.getPreferredTopAxisLength();
    }

    int getPreferredBottomAxisLength() {
        return chart.getPreferredBottomAxisLength();
    }

    Range getTracesXExtremes() {
        return chart.getTracesXExtremes();
    }

    void setXAxisExtremes(int xAxisIndex, Range minMax) {
        chart.setXAxisExtremes(xAxisIndex, minMax);
    }

    /**=======================Base methods to interact==========================**/

    public int getSelectedTraceIndex() {
        return selectedTraceIndex;
    }

    public Range getYAxisRange(int yAxisIndex) {
        return chart.getYAxisRange(yAxisIndex);
    }

    public int getYAxisIndex(int x, int y) {
        return chart.getYAxisIndex(x, y);
    }

    public List<Integer> getStackXAxisIndexes(int x, int y) {
        return chart.getStackXAxisIndexes(x, y);
    }

    public List<Integer> getYAxisIndexes() {
        return chart.getYAxisIndexes();
    }

    public List<Integer> getXAxisIndexes() {
        return chart.getXAxisIndexes();
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

    public void autoscaleXAxis(int xAxisIndex) {
        chart.autoscaleXAxis(xAxisIndex);
    }

    public void autoscaleYAxis(int yAxisIndex) {
        chart.autoscaleYAxis(yAxisIndex);
    }

    public int getTraceYAxisIndex(int traceIndex) {
        return chart.getTraceYAxisIndex(traceIndex);
    }

    public int getTraceXAxisIndex(int traceIndex) {
        return chart.getTraceXAxisIndex(traceIndex);
    }


    public boolean hoverOff() {
        if (hoverIndex >= 0) {
            hoverIndex = -1;
            return true;
        }
        return false;
    }

    public boolean hoverOn(int x, int y) {
        if (selectedTraceIndex < 0) {
            return false;
        }
        int nearestIndex = chart.getTraceData(selectedTraceIndex).findNearestData(chart.xPositionToValue(selectedTraceIndex, x));
        if (hoverIndex != nearestIndex) {
            hoverIndex = nearestIndex;
            return true;
        }
        return false;
    }

}
