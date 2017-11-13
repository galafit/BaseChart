package base.chart;

import base.DataSet;
import base.axis.Axis;
import base.config.ChartConfig;
import base.config.general.Margin;
import base.config.traces.TraceConfig;
import base.Range;
import base.legend.LegendItem;
import base.tooltips.InfoItem;
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
    private ChartConfig chartConfig;

    private Rectangle fullArea;
    private Rectangle graphArea;
    private Margin margin;

    public SimpleChart(ChartConfig chartConfig) {
        this.chartConfig = chartConfig;
        xAxisList.add(new Axis(chartConfig.getBottomAxisConfig()));
        xAxisList.add(new Axis(chartConfig.getTopAxisConfig()));
        for (int i = 0; i < chartConfig.getStacksAmount(); i++) {
            yAxisList.add(new Axis(chartConfig.getLeftAxisConfig(i)));
            yAxisList.add(new Axis(chartConfig.getRightAxisConfig(i)));
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
        autoscaleXAxis();
        autoscaleYAxis();
    }

    public void setArea(Rectangle area) {
        fullArea = area;
    }

    Rectangle getFullArea() {
        return fullArea;
    }

    Rectangle getGraphArea() {
        return graphArea;
    }

    Margin getMargin(Graphics2D g2) {
        if (margin == null) {
            calculateMarginsAndAreas(g2, chartConfig.getMargin());
        }
        return margin;
    }

    void setMargin(Graphics2D g2, Margin margin) {
        calculateMarginsAndAreas(g2, margin);
    }

    int getTraceYAxisIndex(int traceIndex) {
        return chartConfig.getTraceYAxisIndex(traceIndex);
    }

    int getTraceXAxisIndex(int traceIndex) {
        return chartConfig.getTraceXAxisIndex(traceIndex);
    }

    int[] getStackXAxisIndexes(int stackIndex) {
        boolean isBottom = false;
        boolean isTop = false;
        for (int i = 0; i < traces.size(); i++) {
            int traceStackIndex = getTraceYAxisIndex(i) / 2;
            if(traceStackIndex == stackIndex) {
                if(getTraceXAxisIndex(i) == 0) {
                    isBottom = true;
                } else {
                    isTop = true;
                }
            }
        }
        if(isBottom && isTop) {
            int[] xAxisIndexes = {0, 1};
            return xAxisIndexes;
        }
        if(isBottom) {
            int[] xAxisIndexes = {0};
            return xAxisIndexes;
        }
        if(isTop) {
            int[] xAxisIndexes = {1};
            return xAxisIndexes;
        }
        int[] xAxisIndexes = new int[0];
        return xAxisIndexes;
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

    Range getYAxisRange(int yAxisIndex) {
        return new Range(yAxisList.get(yAxisIndex).getStart(), yAxisList.get(yAxisIndex).getEnd(), true);
    }

    int getStackIndex(int y) {
        for (int i = 0; i < yAxisList.size() / 2; i++) {
            Axis yAxis = yAxisList.get(2 * i);
            // for yAxis Start > End
            if (yAxis.getEnd() <= y && yAxis.getStart() >= y) {
                return i;
            }
        }
        return - 1;
    }

    /**
     * true if there are traces using given Y axis
     */
    public boolean isYAxisUsed(int yAxisIndex) {
        return isYAxisUsed(yAxisList.get(yAxisIndex));
    }

    /**
     * true if there are traces using given X axis
     */
    public boolean isXAxisUsed(int xAxisIndex) {
        return isXAxisUsed(xAxisList.get(xAxisIndex));
    }

    private boolean isYAxisUsed(Axis yAxis) {
        for (Trace trace : traces) {
            if(trace.getYAxis() == yAxis) {
                return true;
            }
        }
        return false;
    }

    private boolean isXAxisUsed(Axis xAxis) {
        for (Trace trace : traces) {
            if(trace.getXAxis() == xAxis) {
                return true;
            }
        }
        return false;
    }

    int getYAxisIndex(int x, int y) {
        Rectangle leftArea = new Rectangle(graphArea.x, graphArea.y, graphArea.width / 2, graphArea.height);
        Rectangle rightArea = new Rectangle(graphArea.x + graphArea.width / 2, graphArea.y, graphArea.width / 2, graphArea.height);

        if (leftArea.contains(x, y)) {
            for (int i = 0; i < yAxisList.size() / 2; i++) {
                Axis yAxis = yAxisList.get(2 * i);
                // for yAxis Start > End
                if (yAxis.getEnd() <= y && yAxis.getStart() >= y) {
                    if(isYAxisUsed(yAxis)) {
                        return 2 * i;
                    }
                    else {
                        return 2 * i + 1;
                    }
                }
            }
        }
        if (rightArea.contains(x, y)) {
            for (int i = 0; i < yAxisList.size() / 2; i++) {
                Axis yAxis = yAxisList.get(2 * i + 1);
                // for yAxis Start > End
                if (yAxis.getEnd() <= y && yAxis.getStart() >= y) {
                    if(isYAxisUsed(yAxis)) {
                        return 2 * i + 1;
                    }
                    else {
                        return 2 * i;
                    }
                }
            }
        }
        return - 1;
    }


    int getPreferredTopAxisLength() {
        Axis topAxis = xAxisList.get(1);
        int prefLength = 0;
        for (Trace trace : traces) {
            if (trace.getXAxis() == topAxis) {
                prefLength = Math.max(prefLength, trace.getPreferredTraceLength());
            }
        }
        return prefLength;
    }

    int getPreferredBottomAxisLength() {
        Axis bottomAxis = xAxisList.get(0);
        int prefLength = 0;
        for (Trace trace : traces) {
            if (trace.getXAxis() == bottomAxis) {
                prefLength = Math.max(prefLength, trace.getPreferredTraceLength());
            }
        }
        return prefLength;
    }


    Range getTracesXExtremes() {
        Range xRange = null;
        for (Trace trace : traces) {
            xRange = Range.max(xRange, trace.getData().getXExtremes());
        }
        return xRange;
    }

    void setTopAxisExtremes(Range minMax) {
       // xAxisList.get(1).setAutoScale(false);
        xAxisList.get(1).setMinMax(minMax);
    }

    void setBottomAxisExtremes(Range minMax) {
       // xAxisList.get(0).setAutoScale(false);
        xAxisList.get(0).setMinMax(minMax);
    }


    public void setTraceData(DataSet data, int traceIndex) {
        Trace trace = traces.get(traceIndex);
        trace.setData(data);
        Axis yAxis = trace.getYAxis();
        Axis xAxis = trace.getXAxis();
        if (xAxis.isAutoScale()) {
            autoscaleXAxis(getTraceXAxisIndex(traceIndex));
        }
        if (yAxis.isAutoScale()) {
            autoscaleYAxis(getTraceYAxisIndex(traceIndex));
        }
    }


    public void autoscale(int traceIndex) {
        autoscaleXAxis(getTraceXAxisIndex(traceIndex));
        autoscaleYAxis(getTraceYAxisIndex(traceIndex));
    }

    public void autoscaleXAxis(int xAxisIndex) {
        Range xRange = null;
        for (int i = 0; i < traces.size(); i++) {
            if(getTraceXAxisIndex(i) == xAxisIndex) {
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

    public void autoscaleXAxis() {
        for (int i = 0; i < xAxisList.size(); i++) {
            Axis xAxis = xAxisList.get(i);
            if (xAxis.isAutoScale()) {
                autoscaleXAxis(i);
            } else {
                xAxis.setMinMax(chartConfig.getXAxisConfig(i).getExtremes());
            }
        }
    }

    public void autoscaleYAxis() {
        for (int i = 0; i < yAxisList.size(); i++) {
            Axis yAxis = yAxisList.get(i);
            if (yAxis.isAutoScale()) {
                autoscaleYAxis(i);
            } else {
                yAxis.setMinMax(chartConfig.getYAxisConfig(i).getExtremes());
            }
        }
    }

    public int getTraceAmount() {
        return traces.size();
    }

    public List<LegendItem> getTracesInfo() {
        ArrayList<LegendItem> legendItems = new ArrayList<LegendItem>(chartConfig.getTraceAmount());
        for (int i = 0; i < chartConfig.getTraceAmount(); i++) {
            LegendItem[] items = traces.get(i).getLegendItems();
            for (LegendItem item : items) {
                legendItems.add(item);
            }
        }
        return legendItems;
    }

    public DataSet getData(int traceIndex) {
        return traces.get(traceIndex).getData();
    }

    public InfoItem[] getDataInfo(int traceIndex, int dataIndex) {
        return traces.get(traceIndex).getInfo(dataIndex);
    }

    public  Point getDataPosition(int traceIndex, int dataIndex) {
        return traces.get(traceIndex).getDataPosition(dataIndex);
    }

    public double xPositionToValue(int traceIndex, int mouseX) {
        return traces.get(traceIndex).getXAxis().invert(mouseX);
    }

    void calculateMarginsAndAreas(Graphics2D g2, Margin margin) {
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
            top += xAxisList.get(1).getThickness(g2);

        }
        if (bottom < 0) {
            bottom += xAxisList.get(0).getThickness(g2);
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

    Axis getBottomAxis() {
        return xAxisList.get(0);
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
    }
}
