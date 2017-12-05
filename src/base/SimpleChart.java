package base;

import base.axis.Axis;
import base.config.SimpleChartConfig;
import base.config.general.Margin;
import base.config.traces.TraceConfig;
import base.legend.LegendItem;
import base.painters.CrosshairPainter;
import base.painters.LegendPainter;
import base.painters.TitlePainter;
import base.painters.TooltipPainter;
import base.scales.Scale;
import base.tooltips.TooltipInfo;
import base.traces.Trace;
import base.traces.TraceRegister;


import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by hdablin on 24.03.17.
 */
public class SimpleChart  {
    private final Color GREY = new Color(150, 150, 150);
    private final Color BROWN = new Color(200, 102, 0);
    private final Color ORANGE = new Color(255, 153, 0);
    private Color[] traceColors = {Color.CYAN, Color.MAGENTA, Color.PINK, Color.RED, ORANGE};

    private List<Axis> xAxisList = new ArrayList<Axis>(2);
    private List<Axis> yAxisList = new ArrayList<Axis>();
    private List<Trace> traces = new ArrayList<Trace>();
    private boolean isTicksAlignmentEnable = false;

    private LegendPainter legendPainter;
    private TitlePainter titlePainter;
    private Rectangle fullArea;
    private Rectangle titleArea;
    private Rectangle chartArea;
    private Rectangle graphArea;
    private SimpleChartConfig chartConfig;
    private Margin margin;

    private boolean isDirty = true;
    // if true traces with a small number of points will be stretched over the whole graphArea
    private boolean isTracesStretched = false;

    private CrosshairPainter crosshairPainter;
    private TooltipPainter tooltipPainter;

    private int selectedTraceIndex = -1;
    private int hoverIndex = -1;


    public SimpleChart(SimpleChartConfig chartConfig, Rectangle area) {
        this.chartConfig = chartConfig;
        this.fullArea = area;
        for (int i = 0; i < chartConfig.getNumberOfXAxis(); i++) {
            xAxisList.add(new Axis(chartConfig.getXAxisConfig(i)));
        }
        for (int i = 0; i < chartConfig.getNumberOfYAxis(); i++) {
            yAxisList.add(new Axis(chartConfig.getYAxisConfig(i)));
        }
        for (int i = 0; i < chartConfig.getTraceAmount(); i++) {
            TraceConfig traceConfig = chartConfig.getTraceConfig(i);
            Trace trace = TraceRegister.getTrace(traceConfig, chartConfig.getTraceData(i));
            trace.setXAxis(xAxisList.get(chartConfig.getTraceXAxisIndex(i)));
            trace.setYAxis(yAxisList.get(chartConfig.getTraceYAxisIndex(i)));
            trace.setDefaultColor(traceColors[traces.size() % traceColors.length]);
            trace.setName(chartConfig.getTraceName(i));
            traces.add(trace);
        }
        tooltipPainter = new TooltipPainter(chartConfig.getTooltipConfig());
        crosshairPainter = new CrosshairPainter(chartConfig.getCrosshairConfig());
        titlePainter = new TitlePainter(chartConfig.getTitle(), chartConfig.getTitleTextStyle());
        legendPainter = new LegendPainter(getTracesInfo(), chartConfig.getLegendConfig());

        // set min and max for x axis
        for (int i = 0; i < xAxisList.size(); i++) {
            Range minMax = chartConfig.getXAxisMinMax(i);
            if(minMax != null) {
                xAxisList.get(i).setMinMax(minMax);

            } else {
                autoscaleXAxis(i);
            }
        }
        // set min and max for y axis
        for (int i = 0; i < yAxisList.size(); i++) {
            Range minMax = chartConfig.getYAxisMinMax(i);
            if(minMax != null) {
                yAxisList.get(i).setMinMax(minMax);

            } else {
                autoscaleYAxis(i);
            }
        }
    }

    public void setData(ArrayList<DataSet> data) {
        chartConfig.setData(data);
        for (int i = 0; i < chartConfig.getTraceAmount(); i++) {
            traces.get(i).setData(chartConfig.getTraceData(i));
        }
    }

    public int getNumberOfXAxis() {
        return xAxisList.size();
    }

    public int getNumberOfYAxis() {
        return yAxisList.size();
    }


    private TooltipInfo getTooltipInfo() {
        TooltipInfo tooltipInfo = null;
        if (selectedTraceIndex >= 0 && hoverIndex >= 0) {
            tooltipInfo = new TooltipInfo();
            tooltipInfo.addItems(traces.get(selectedTraceIndex).getInfo(hoverIndex));
            Point dataPosition = traces.get(selectedTraceIndex).getDataPosition(hoverIndex);
            tooltipInfo.setXY(dataPosition.x, dataPosition.y);
        }
        return tooltipInfo;
    }

    Margin getMargin(Graphics2D g2) {
        if (margin == null) {
            calculateMarginsAndAreas(g2, chartConfig.getMargin());
        }
        return margin;
    }

    Rectangle getGraphArea(Graphics2D g2) {
        if(graphArea == null) {
            calculateMarginsAndAreas(g2, chartConfig.getMargin());
        }
        return graphArea;
    }

    void setMargin(Graphics2D g2, Margin margin) {
        calculateMarginsAndAreas(g2, margin);
    }

    public boolean isYAxisUsed(int yAxisIndex) {
        for (int i = 0; i < traces.size(); i++) {
            if(getTraceYAxisIndex(i) == yAxisIndex) {
                return true;
            }
        }
        return false;
    }


    int getPreferredTraceDataMarkSize(int traceIndex) {
        return traces.get(traceIndex).getPreferredDataMarkSize();
    }

    void setXAxisExtremes(int xAxisIndex, Range minMax) {
        xAxisList.get(xAxisIndex).setMinMax(minMax);
    }

    Scale getXAxisScale(int xAxisIndex) {
        return xAxisList.get(xAxisIndex).getScale();
    }

    private List<LegendItem> getTracesInfo() {
        ArrayList<LegendItem> legendItems = new ArrayList<LegendItem>(chartConfig.getTraceAmount());
        for (int i = 0; i < chartConfig.getTraceAmount(); i++) {
            LegendItem[] items = traces.get(i).getLegendItems();
            for (LegendItem item : items) {
                legendItems.add(item);
            }
        }
        return legendItems;
    }

    void calculateMarginsAndAreas(Graphics2D g2, Margin margin) {
        int titleHeight = titlePainter.getTitleHeight(g2, fullArea.width);
        titleArea = new Rectangle(fullArea.x, fullArea.y, fullArea.width, titleHeight);
        chartArea = new Rectangle(fullArea.x, fullArea.y + titleHeight, fullArea.width, fullArea.height - titleHeight);
        int left = -1;
        int right = -1;
        int bottom = -1;
        int top = -1;
        if (margin != null) {
            top = margin.top();
            bottom = margin.bottom();
            left = margin.left();
            right = margin.right();
        }

        // set XAxis ranges
        int xStart = fullArea.x;
        int xEnd = fullArea.x + fullArea.width;
        xAxisList.get(0).setStartEnd(xStart, xEnd);
        xAxisList.get(1).setStartEnd(xStart, xEnd);
        if (top < 0) {
            top = titleHeight + xAxisList.get(1).getThickness(g2);

        }
        if (bottom < 0) {
            bottom = xAxisList.get(0).getThickness(g2);
        }

        // set YAxis ranges
        Rectangle paintingArea = new Rectangle(fullArea.x, fullArea.y + top, fullArea.width, fullArea.height - top - bottom);
        for (int i = 0; i < yAxisList.size(); i++) {
            yAxisList.get(i).setStartEnd(chartConfig.getYAxisStartEnd(i, paintingArea));
        }
        if (left < 0) {
            for (int i = 0; i < yAxisList.size() / 2; i++) {
                left = Math.max(left, yAxisList.get(i * 2).getThickness(g2));
            }
        }
        if (right < 0) {
            for (int i = 0; i < yAxisList.size() / 2; i++) {
                right = Math.max(right, yAxisList.get(i * 2 + 1).getThickness(g2));
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
    }


    public void draw(Graphics2D g2d) {
        if (margin == null) {
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

        titlePainter.draw(g2d, titleArea);
//        legendPainter.draw(g2d, graphArea);
        TooltipInfo tooltipInfo = getTooltipInfo();
        if (tooltipInfo != null) {
            tooltipPainter.draw(g2d, chartArea, tooltipInfo);
            crosshairPainter.draw(g2d, graphArea, tooltipInfo.getX(), tooltipInfo.getY());
        }
    }

    /**=======================Base methods to interact==========================**/


    public Range getXAxisMinMax(int xAxisIndex) {
        return new Range(xAxisList.get(xAxisIndex).getMin(), xAxisList.get(xAxisIndex).getMax());
    }

    public int getSelectedTraceIndex() {
        return selectedTraceIndex;
    }

    public Range getYAxisRange(int yAxisIndex) {
        return new Range(yAxisList.get(yAxisIndex).getStart(), yAxisList.get(yAxisIndex).getEnd(), true);
    }

    /**
     * Find and return X axis used by the traces belonging to the stack containing point (x, y)
     */
    public List<Integer> getStackXAxisIndexes(int x, int y) {
        int stackIndex = -1;
        List<Integer> axisList = new ArrayList<>(2);
        for (int i = 0; i < yAxisList.size() / 2; i++) {
            Axis yAxis = yAxisList.get(2 * i);
            // for yAxis Start > End
            if (yAxis.getEnd() <= y && yAxis.getStart() >= y) {
                stackIndex = i;
                break;
            }
        }
        if(stackIndex >= 0) {
            for (int i = 0; i < traces.size(); i++) {
                int traceStackIndex = getTraceYAxisIndex(i) / 2;
                if(traceStackIndex == stackIndex) {
                    int xAxisIndex = getTraceXAxisIndex(i);
                    if(!axisList.contains(xAxisIndex)) {
                        axisList.add(xAxisIndex);
                    }
                }
            }
        }
        return axisList;
    }

    /**
     * return only used by some trace axes
     */
    public List<Integer> getUsedXAxisIndexes() {
       return chartConfig.getUsedXAxisIndexes();
    }

    /**
     * return only used by some trace axes
     */
    public List<Integer> getUsedYAxisIndexes() {
        return chartConfig.getUsedYAxisIndexes();
    }

    public int getYAxisIndex(int x, int y) {
        int stackIndex = -1;
        for (int i = 0; i < yAxisList.size() / 2; i++) {
            Axis yAxis = yAxisList.get(2 * i);
            // for yAxis Start > End
            if (yAxis.getEnd() <= y && yAxis.getStart() >= y) {
                stackIndex = i;
                break;
            }
        }
        if(stackIndex >= 0) {
            if(x <= fullArea.x + fullArea.width / 2) {
                int yAxisIndex = 2 * stackIndex;
                if(!isYAxisUsed(yAxisIndex)) {
                    yAxisIndex = 2 * stackIndex + 1;
                }
                return yAxisIndex;
            } else {
                int yAxisIndex = 2 * stackIndex + 1;
                if(!isYAxisUsed(yAxisIndex)) {
                    yAxisIndex = 2 * stackIndex;
                }
                return yAxisIndex;
            }
        }
        return -1;
    }

    public void zoomY(int yAxisIndex, double zoomFactor) {
        yAxisList.get(yAxisIndex).zoom(zoomFactor);
    }

    public void zoomX(int xAxisIndex, double zoomFactor) {
        xAxisList.get(xAxisIndex).zoom(zoomFactor);
    }

    public void translateY(int yAxisIndex, int translation) {
        yAxisList.get(yAxisIndex).translate(translation);
    }

    public void translateX(int xAxisIndex, int translation) {
        xAxisList.get(xAxisIndex).translate(translation);
    }

    public void autoscaleXAxis(int xAxisIndex) {
        Range xRange = null;
        for (int i = 0; i < traces.size(); i++) {
            if(getTraceXAxisIndex(i) == xAxisIndex) {
                DataSet traceData = traces.get(i).getData();
                xRange = traceData.getXExtremes();
                // if trace has a small number of points we prevent it from stretching over the whole graphArea
                if(!isTracesStretched && xRange != null && xRange.length() > 0) {
                    int preferredTraceLength = (traceData.size() - 1) * getPreferredTraceDataMarkSize(i);
                    if(preferredTraceLength < fullArea.width) {
                        double preferredMax = xRange.start() + xRange.length() * fullArea.width / preferredTraceLength;
                        xRange = new Range(xRange.start(), preferredMax);
                    }
                }
                xRange = Range.max(xRange, traces.get(i).getData().getXExtremes());
            }
        }
        xAxisList.get(xAxisIndex).setMinMax(xRange);
    }

    public void autoscaleYAxis(int yAxisIndex) {
        Range yRange = null;
        for (int i = 0; i < traces.size(); i++) {
            if(getTraceYAxisIndex(i) == yAxisIndex) {
                yRange = Range.max(yRange, traces.get(i).getYExtremes());
            }
        }
        yAxisList.get(yAxisIndex).setMinMax(yRange);
    }

    public int getTraceYAxisIndex(int traceIndex) {
        return chartConfig.getTraceYAxisIndex(traceIndex);
    }

    public int getTraceXAxisIndex(int traceIndex) {
        return chartConfig.getTraceXAxisIndex(traceIndex);
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
        double xValue = traces.get(selectedTraceIndex).getXAxis().invert(x);
        int nearestIndex = traces.get(selectedTraceIndex).getData().findNearestData(xValue);
        if (hoverIndex != nearestIndex) {
            hoverIndex = nearestIndex;
            return true;
        }
        return false;
    }

}
