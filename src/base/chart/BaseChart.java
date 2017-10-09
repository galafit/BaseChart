package base.chart;

import base.axis.Axis;
import base.config.ChartConfig;
import base.config.ScrollConfig;
import base.config.general.Margin;
import base.config.traces.TraceConfig;
import base.Range;
import base.legend.LegendItem;
import base.painters.*;
import base.tooltips.TooltipInfo;
import base.tooltips.TooltipItem;
import base.traces.Trace;
import base.traces.TraceRegister;
import data.XYDataSet;


import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by hdablin on 24.03.17.
 */
public class BaseChart {

    private List<Axis> xAxisList = new ArrayList<Axis>(2);
    private List<Axis> yAxisList = new ArrayList<Axis>();
    private List<Trace> traces = new ArrayList<Trace>();
    private boolean isTicksAlignmentEnable = false;

    private TooltipPainter tooltipPainter;
    private boolean isTooltipSeparated = true;
    private TooltipInfo tooltipInfo;
    private LegendPainter legendPainter;
    private TitlePainter titlePainter;
    private CrosshairPainter crosshairPainter;
    private ChartConfig chartConfig;
    private ScrollPainter scrollPainter;
    private boolean isScrollEnabled = false;

    private Rectangle fullArea;
    private Rectangle titleArea;
    private Rectangle legendArea;
    private Rectangle graphArea;
    private Margin margin;


    public BaseChart(ChartConfig chartConfig, Rectangle area) {
        this.chartConfig = chartConfig;
        fullArea = area;
        xAxisList.add(new Axis(chartConfig.getBottomAxisConfig()));
        xAxisList.add(new Axis(chartConfig.getTopAxisConfig()));
        for (int i = 0; i < chartConfig.getStacksAmount(); i++) {
            yAxisList.add(new Axis(chartConfig.getLeftAxisConfig(i)));
            yAxisList.add(new Axis(chartConfig.getRightAxisConfig(i)));
        }

        tooltipPainter = new TooltipPainter(chartConfig.getTooltipConfig());
        crosshairPainter = new CrosshairPainter(chartConfig.getCrosshairConfig());
        titlePainter = new TitlePainter(chartConfig.getTitle(), chartConfig.getTitleTextStyle());
        ArrayList<LegendItem> legendItems = new ArrayList<LegendItem>(chartConfig.getTraceAmount());
        for (int i = 0; i < chartConfig.getTraceAmount() ; i++) {
            TraceConfig traceConfig = chartConfig.getTraceConfig(i);
            Trace trace = TraceRegister.getTrace(traceConfig);
            trace.setXAxis(xAxisList.get(traceConfig.getXAxisIndex()));
            trace.setYAxis(yAxisList.get(traceConfig.getYAxisIndex()));
            LegendItem[] items = trace.getLegendItems();
            for (LegendItem item : items) {
                legendItems.add(item);
            }
            traces.add(trace);
        }
        legendPainter = new LegendPainter(legendItems, chartConfig.getLegendConfig());
    }

    Range getTracesXExtremes() {
        Range xRange = null;
        for (Trace trace : traces) {
            xRange = Range.max(xRange, trace.getXExtremes());
        }
        return xRange;
    }

    Margin getMargin(Graphics2D g2) {
        if(margin == null) {
            calculateMarginsAndAreas(g2, chartConfig.getMargin());
        }
        return margin;
    }

    void setMargin(Graphics2D g2, Margin margin) {
        calculateMarginsAndAreas(g2, margin);
        xAxisList.get(0).update();
        xAxisList.get(1).update();
        for (Axis axis : yAxisList) {
           axis.update();
        }
    }


    void setTopAxisExtremes(Range minMax) {
        xAxisList.get(1).setMinMax(minMax);
        xAxisList.get(1).setAutoScale(false);
    }

    void setBottomAxisExtremes(Range minMax) {
        xAxisList.get(0).setMinMax(minMax);
        xAxisList.get(0).setAutoScale(false);
    }

    private void setXAxisDomain() {
        for (int i = 0; i < xAxisList.size(); i++) {
            Axis xAxis = xAxisList.get(i);
            if (xAxis.isAutoScale()) {
                Range xRange = null;
                for (Trace trace : traces) {
                    if(trace.getXAxis() == xAxis) {
                        xRange = Range.max(xRange, trace.getXExtremes());
                    }
                }
                xAxis.setMinMax(xRange);
            } else {
                xAxis.setMinMax(chartConfig.getXAxisConfig(i).getExtremes());
            }
        }
    }

    private void setYAxisDomain() {
        for (int i = 0; i < yAxisList.size(); i++) {
            Axis yAxis = yAxisList.get(i);
            if (yAxis.isAutoScale()) {
                Range yRange = null;
                for (Trace trace : traces) {
                    if(trace.getYAxis() == yAxis) {
                        yRange = Range.max(yRange, trace.getYExtremes());
                    }
                }
                yAxis.setMinMax(yRange);
            } else {
                yAxis.setMinMax(chartConfig.getYAxisConfig(i).getExtremes());
            }
        }
    }

    void createScroll(ScrollConfig scrollConfig, double scrollExtent1, double scrollExtent2) {
        isScrollEnabled = true;
        scrollPainter = new ScrollPainter(scrollConfig, scrollExtent1, scrollExtent2, xAxisList.get(0));
    }

    void createScroll(ScrollConfig scrollConfig, double scrollExtent) {
        createScroll(scrollConfig, scrollExtent, scrollExtent);
    }

    /**
     * @return true if scrollValue was changed and false if newValue = current scroll value
     */
    public boolean moveScroll(int mouseX, int mouseY) {
       return scrollPainter.moveScroll(mouseX, mouseY);
    }

    /**
     * @return true if scrollValue was changed and false if newValue = current scroll value
     */
    public boolean moveScroll(double newValue) {
       return scrollPainter.moveScroll(newValue);
    }

    public Range getScrollExtremes1() {
        return scrollPainter.getScrollExtremes1();
    }

    public Range getScrollExtremes2() {
        return scrollPainter.getScrollExtremes2();
    }

    boolean isMouseInsideScroll(int mouseX, int mouseY) {
        return scrollPainter.isMouseInsideScroll(mouseX, mouseY, graphArea);
    }

    boolean isMouseInsideChart(int mouseX, int mouseY) {
        return graphArea.contains(mouseX, mouseY);
    }


    // TODO: handling multiple xAxis!!!!
    // TODO: add separated base.tooltips
    public boolean hover(int mouseX, int mouseY) {
        if(!isMouseInsideChart(mouseX, mouseY)) {
            boolean isHoverChanged = false;
            for (int i = 0; i < traces.size(); i++) {
                isHoverChanged = traces.get(i).setHoverIndex(-1) || isHoverChanged;
                tooltipInfo = null;
            }
            return isHoverChanged;
        }
        int[] nearestIndexes = new int[traces.size()];
        Integer minDistance = null;
        // find min distance from base.traces points to mouseX
        for (int i = 0; i < traces.size(); i++) {
            nearestIndexes[i] = traces.get(i).findNearest(mouseX, mouseY);
            int x = (int) traces.get(i).getXPosition(nearestIndexes[i]);
            if(minDistance == null || Math.abs(minDistance) > Math.abs(x - mouseX)) {
                minDistance =  (x - mouseX);
            }
        }

        // hover base.traces points that have minDistance to mouseX
        boolean isHoverChanged = false;
        double hoverXValue = 0;
        if (minDistance != null) {
            ArrayList<TooltipItem> tooltipItems = new ArrayList<TooltipItem>();
            for (int i = 0; i < traces.size(); i++) {
                int x = (int) traces.get(i).getXPosition(nearestIndexes[i]);
                if((x - mouseX) == minDistance) {
                    isHoverChanged = traces.get(i).setHoverIndex(nearestIndexes[i]) || isHoverChanged;
                    tooltipItems.add(traces.get(i).getTooltipItem());
                    hoverXValue = traces.get(i).getXValue(nearestIndexes[i]);
                } else {
                    isHoverChanged = traces.get(i).setHoverIndex(-1) || isHoverChanged;
                }
            }
            if (isHoverChanged) {
                tooltipInfo = new TooltipInfo();
                tooltipInfo.setItems(tooltipItems);
                tooltipInfo.setX(mouseX + minDistance);
                tooltipInfo.setY(mouseY);
                tooltipInfo.setHeader(new TooltipItem(null, String.valueOf(hoverXValue), null));
            }
        }
        return isHoverChanged;
    }

    void calculateMarginsAndAreas(Graphics2D g2, Margin margin) {
        int titleHeight = titlePainter.getTitleHeight(g2, fullArea.width);
        int legendHeight = legendPainter.getLegendHeight(g2, fullArea.width);

        int left = -1;
        int right = -1;
        int bottom = -1;
        int top = -1;
        if(margin != null) {
            top = margin.top();
            bottom = margin.bottom();
            left = margin.left();
            right = margin.right();
        }
        setXAxisDomain();
        // set XAxis ranges
        int xStart = fullArea.x;
        int xEnd = fullArea.x + fullArea.width;
        xAxisList.get(0).setStartEnd(xStart, xEnd);
        xAxisList.get(1).setStartEnd(xStart, xEnd);
        if(top < 0) {
            top = titleHeight;
            if(chartConfig.getLegendConfig().isTop()) {
                top += legendHeight;
            }
            top += xAxisList.get(1).getThickness(g2);

        }
        if(bottom < 0) {
            bottom = 0;
            if(!chartConfig.getLegendConfig().isTop()) {
                bottom += legendHeight;
            }
            bottom += xAxisList.get(0).getThickness(g2);
        }

        setYAxisDomain();
        // set YAxis ranges
        Rectangle paintingArea = new Rectangle(fullArea.x, fullArea.y + top, fullArea.width, fullArea.height - top - bottom);
        for (int i = 0; i < yAxisList.size(); i++) {
            yAxisList.get(i).setStartEnd(chartConfig.getYAxisStartEnd(i, paintingArea));
        }
        if(left < 0) {
            for (int i = 0; i < yAxisList.size()/2; i++) {
                left = Math.max(left, yAxisList.get(i*2).getThickness(g2));
            }
        }
        if(right < 0) {
            for (int i = 0; i < yAxisList.size()/2; i++) {
                right = Math.max(right, yAxisList.get(i*2 + 1).getThickness(g2));
            }
        }

        this.margin = new Margin(top, right, bottom, left);
        graphArea = new Rectangle(fullArea.x + left, fullArea.y + top,
                fullArea.width - left - right, fullArea.height - top - bottom);

        // adjust XAxis ranges
        xStart = graphArea.x;
        xEnd = graphArea.x + graphArea.width;
        xAxisList.get(0).setStartEnd(xStart, xEnd);
        xAxisList.get(1).setStartEnd(xStart, xEnd);

        if (chartConfig.getLegendConfig().isTop()) {
            legendArea = new Rectangle(fullArea.x, graphArea.y - legendHeight, fullArea.width, legendHeight);
            titleArea = new Rectangle(fullArea.x,graphArea.y -legendHeight - titleHeight,fullArea.width, titleHeight);
        } else {
            legendArea = new Rectangle(fullArea.x, fullArea.y + fullArea.height - legendHeight, fullArea.width, legendHeight);
            titleArea = new Rectangle(fullArea.x,graphArea.y - titleHeight, fullArea.width, titleHeight);
        }
    }


    public void draw(Graphics2D g2d) {
        if(margin == null) {
            calculateMarginsAndAreas(g2d, chartConfig.getMargin());
        }

        g2d.setColor(chartConfig.getMarginColor());
        g2d.fill(fullArea);

        g2d.setColor(chartConfig.getBackground());
        g2d.fill(graphArea);

        /*
        * https://stackoverflow.com/questions/31536952/how-to-fix-text-quality-in-java-graphics
        */
        Map<?, ?> desktopHints =
                (Map<?, ?>) Toolkit.getDefaultToolkit().getDesktopProperty("awt.font.desktophints");
        if (desktopHints != null) {
            g2d.setRenderingHints(desktopHints);
        }


        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        //g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);


      /*  g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);  */

        titlePainter.draw(g2d, titleArea);
        legendPainter.draw(g2d, legendArea);


        int topPosition = graphArea.y;
        int bottomPosition = graphArea.y + graphArea.height;
        int leftPosition = graphArea.x;
        int rightPosition = graphArea.x + graphArea.width;

        for (int i = 0; i < xAxisList.size() / 2; i++) {
            xAxisList.get(i * 2).drawGrid(g2d, bottomPosition, graphArea.height);
            xAxisList.get(i * 2 + 1).drawGrid(g2d, topPosition, graphArea.height);
        }

        for (int i = 0; i < yAxisList.size() / 2; i++) {
            yAxisList.get(i * 2).drawGrid(g2d, leftPosition, graphArea.width);
            yAxisList.get(i * 2 + 1).drawGrid(g2d, rightPosition, graphArea.width);
        }

        for (int i = 0; i < xAxisList.size() / 2; i++) {
            xAxisList.get(i * 2).drawAxis(g2d, bottomPosition);
            xAxisList.get(i * 2 + 1).drawAxis(g2d, topPosition);
        }

        for (int i = 0; i < yAxisList.size() / 2; i++) {
            yAxisList.get(i * 2).drawAxis(g2d, leftPosition);
            yAxisList.get(i * 2 + 1).drawAxis(g2d, rightPosition);
        }

        Rectangle clip = g2d.getClipBounds();
        g2d.setClip(graphArea);

        for (Trace trace : traces) {
            trace.draw(g2d);
        }
        g2d.setClip(clip);

        if(isScrollEnabled) {
            scrollPainter.draw(g2d, graphArea);
        }

        if (tooltipInfo != null) {
            tooltipPainter.draw(g2d, fullArea, tooltipInfo);
            crosshairPainter.draw(g2d, graphArea, tooltipInfo.getX(), tooltipInfo.getY());
        }
    }
}
