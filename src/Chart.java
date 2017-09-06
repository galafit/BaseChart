
import axis_old.Axis;
import axis_old.AxisType;
import axis_old.LinearAxis;
import configuration.ChartConfig;
import configuration.Margin;
import data.Range;
import data.XYList;
import functions.DoubleFunction;
import graphs.Graph;
import legend.LegendItem;
import painters.CrosshairPainter;
import painters.LegendPainter;
import painters.TitlePainter;
import tooltips.TooltipInfo;
import tooltips.TooltipItem;
import painters.TooltipPainter;


import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by hdablin on 24.03.17.
 */
public class Chart implements Drawable {
    private List<Graph> graphs = new ArrayList<Graph>();
    private List<Axis> xAxisList = new ArrayList<Axis>();
    private List<Axis> yAxisList = new ArrayList<Axis>();
    private int axisPadding = 0;

    private final Color GREY = new Color(150, 150, 150);
    private final Color BROWN = new Color(200, 102, 0);
    private final Color ORANGE = new Color(255, 153, 0);

    private Color[] colors = {GREY, BROWN, Color.GREEN, Color.YELLOW};
    private Color[] graphicColors = {Color.MAGENTA, Color.RED, ORANGE, Color.CYAN, Color.PINK};

    private boolean isTicksAlignmentEnable = false;
    private int[] xAxisPositions;
    private int[] yAxisPositions;
    private Rectangle fullArea;
    private Rectangle graphArea; // area to draw graphs
    private Rectangle chartArea; //area to draw graphs and axis_old
    private Rectangle titleArea;
    private TooltipPainter tooltipPainter;
    private boolean isTooltipSeparated = true;
    private TooltipInfo tooltipInfo;
    private Rectangle legendArea;
    private LegendPainter legendPainter;
    private TitlePainter titlePainter;
    private CrosshairPainter crosshairPainter;

    private ChartConfig chartConfig = new ChartConfig(ChartConfig.DEBUG_THEME);

    public Chart() {
        Axis x = new LinearAxis();
        x.setHorizontal(true);
        xAxisList.add(x);
        Axis y = new LinearAxis();
        y.setHorizontal(false);
        yAxisList.add(y);
    }


    public void update() {
        chartArea = null;
        for (Graph graph : graphs) {
            graph.update();
        }
    }


    // TODO: handling multiple xAxis!!!!
    // TODO: add separated tooltips
    public boolean hover(int mouseX, int mouseY) {
        boolean isHover = false;
        if (!graphArea.contains(new Point(mouseX, mouseY))) {
            tooltipInfo = null;
            for (Graph graph : graphs) {
                isHover = graph.setHoverPoint(-1) || isHover;
            }
            return isHover;
        }

        long[] nearestPointsIndexes = new long[graphs.size()];
        Integer minDistance = null;
        // find min distance from graphs points to mouseX
        for (int i = 0; i < graphs.size(); i++) {
            Graph graph = graphs.get(i);
            Axis xAxis = xAxisList.get(graph.getXAxisIndex());
            Axis yAxis = yAxisList.get(graph.getYAxisIndex());
            double xValue = xAxis.pointsToValue(mouseX, graphArea);
            long pointIndex = graph.getNearestPointIndex(xValue);
            //System.out.println(graph.getGraphName() + ": pointIndex=" + pointIndex);

            nearestPointsIndexes[i] = pointIndex;
            if (pointIndex >= 0) {
                int x = (int) Math.round(xAxis.valueToPoint(graph.getPoint(pointIndex).getX().doubleValue(), graphArea));
                //System.out.println(graph.getGraphName() + ": (x - mouseX)=" + (x - mouseX));
                if(minDistance == null || Math.abs(minDistance) > Math.abs(x - mouseX)) {
                    minDistance =  (x - mouseX);
                }
            }
        }
        //System.out.println("MinDistance = " + minDistance);
        // hover graphs points that have minDistance to mouseX
        if (minDistance != null) {
            ArrayList<TooltipItem> tooltipItems = new ArrayList<TooltipItem>();
            Range y_range = null;
            Number hoverXValue = null;
            int hoverPointsCounter = 0;
            for (int i = 0; i < graphs.size(); i++) {
                Graph graph = graphs.get(i);
                Axis xAxis = xAxisList.get(graph.getXAxisIndex());
                Axis yAxis = yAxisList.get(graph.getYAxisIndex());
                long pointIndex = nearestPointsIndexes[i];
                if (pointIndex >= 0) {
                    int x = (int) Math.round(xAxis.valueToPoint(graph.getPoint(pointIndex).getX().doubleValue(), graphArea));
                    if ((x - mouseX) == minDistance) {
                        hoverPointsCounter++;
                        hoverXValue = xAxis.pointsToValue(x, graphArea);
                        hoverXValue = xAxis.roundValue(hoverXValue.doubleValue(), graphArea);
                        isHover = graph.setHoverPoint(pointIndex) || isHover;
                        TooltipItem tooltipItem = graph.getTooltipItem();
                        if (tooltipItem != null) {
                            tooltipItems.add(graph.getTooltipItem());
                        }

                        Range yValueRange = graph.getPointYRange(graph.getPoint(pointIndex).getY());
                        Range yPixelRange = new Range(yAxis.valueToPoint(yValueRange.start(), graphArea), yAxis.valueToPoint(yValueRange.start(), graphArea));
                        y_range = Range.max(y_range, yPixelRange);
                    } else {
                        isHover = graph.setHoverPoint(-1) || isHover ;
                    }
                } else {
                    isHover = graph.setHoverPoint(-1) || isHover ;
                }
            }
            if (isHover) {
                if (hoverPointsCounter > 0) {
                    tooltipInfo = new TooltipInfo();
                    tooltipInfo.setItems(tooltipItems);
                    tooltipInfo.setX(mouseX + minDistance);
                    //tooltipInfo.setY((int)((y_range.end()  + y_range.start()) / 2));
                    tooltipInfo.setY(mouseY);
                    tooltipInfo.setHeader(new TooltipItem(null, hoverXValue.toString(), null));

                } else {
                    tooltipInfo = null;
                }
            }
        }
        return isHover;
    }


    public double getPreferredPixelsPerUnit(int xAxisIndex) {
        double pixelsPerUnit = 0;
        for (Graph graph : graphs) {
            // skip graph with functions and take into account only graphs with real DataSets
            if (graph.getFunction() == null && graph.getXAxisIndex() == xAxisIndex) {
                pixelsPerUnit = Math.max(pixelsPerUnit, graph.getPreferredPixelsPerUnit());
            }
        }
        return pixelsPerUnit;
    }


    // define whether ticks on different(opposite) xAxis or yAxis should be aligned/synchronized
    public void enableTicksAlignment(boolean isEnabled) {
        isTicksAlignmentEnable = isEnabled;
    }

    public Axis getXAxis(int xAxisIndex) {
        return xAxisList.get(xAxisIndex);
    }

    public Axis getYAxis(int yAxisIndex) {
        return yAxisList.get(yAxisIndex);
    }

    public int getXAxisAmount() {
        return xAxisList.size();
    }

    public int getYAxisAmount() {
        return yAxisList.size();
    }

    public void addYAxis(AxisType axisType, boolean isOpposite) {
        Axis axis = new LinearAxis();
        if (yAxisList.size() > 0) {
            axis.getGridSettings().setGridLineWidth(0);
            axis.getGridSettings().setMinorGridLineWidth(0);
        }
        axis.setOpposite(isOpposite);
        axis.setHorizontal(false);
        axis.getViewSettings().setAxisColor(colors[yAxisList.size() % colors.length]);
        yAxisList.add(axis);
    }

    public void addXAxis(AxisType axisType, boolean isOpposite) {
        Axis axis = new LinearAxis();
        if (xAxisList.size() > 0) {
            axis.getGridSettings().setGridLineWidth(0);
            axis.getGridSettings().setMinorGridLineWidth(0);
        }
        axis.setOpposite(isOpposite);
        axis.setHorizontal(true);
        axis.getViewSettings().setAxisColor(colors[xAxisList.size() % colors.length]);
        xAxisList.add(axis);
    }


    public void addGraph(Graph graph) {
        addGraph(graph, 0, 0);
    }


    public void addGraph(Graph graph, int xAxisIndex, int yAxisIndex) {
        graph.setAxisIndexes(xAxisIndex, yAxisIndex);
        graph.setLineColor(graphicColors[graphs.size() % graphicColors.length]);
        graphs.add(graph);

    }

    private void alignAxisTicks(List<Axis> axisList, Graphics2D g, Rectangle area) {
        int maxSize = 0;
        if (axisList.size() > 1) {
            for (Axis axis : axisList) {
                axis.getTicksSettings().setTicksAmount(0);
            }

            for (Axis axis : axisList) {
                maxSize = Math.max(maxSize, axis.getTicks(g, area).size());
            }

            for (Axis axis : axisList) {
                axis.getTicksSettings().setTicksAmount(maxSize);
                axis.setEndOnTick(true);
            }
        }
    }


    /**
     * We create XYList for every function. For every area point is calculated corresponding
     * value and function(value) and added to the list. So:
     * resultant XYLists contains «area.width» elements. Then those XYLists are added to
     * the corresponding Graphs.
     *
     * @param area
     */
    private void setFunctions(Rectangle area) {
        for (Graph graph : graphs) {
            DoubleFunction function = graph.getFunction();
            if (function != null) {
                Axis xAxis = xAxisList.get(graph.getXAxisIndex());
                boolean isEndOnTick = xAxis.isEndOnTick();
                double lowerPadding = xAxis.getLowerPadding();
                double upperPadding = xAxis.getUpperPadding();
                xAxis.setUpperPadding(0);
                xAxis.setLowerPadding(0);
                xAxis.setEndOnTick(false);
                XYList data = new XYList();
                for (int i = area.x; i <= area.width + area.x; i++) {
                    double value = xAxis.pointsToValue(i, area);
                    data.addPoint(value, function.apply(value));
                }
                graph.setData(data);
                // restore axis_old settings
                xAxis.setEndOnTick(isEndOnTick);
                xAxis.setLowerPadding(lowerPadding);
                xAxis.setUpperPadding(upperPadding);
            }

        }
    }

    //Calculate and set axis_old positions and graphArea
    private void setGraphAreaAndAxisPositions(Graphics2D g, Rectangle workArea) {
        xAxisPositions = new int[xAxisList.size()];
        yAxisPositions = new int[yAxisList.size()];

        int topIndent = 0;
        int bottomIndent = 0;
        int leftIndent = 0;
        int rightIndent = 0;

        //Calculate position and indents of xAxisList
        int xAxisAmount = xAxisList.size() - 1;
        for (int i = xAxisAmount; i >= 0; i--) {
            int size = xAxisList.get(i).getWidth(g, workArea);
            if (i != xAxisAmount) {
                size += axisPadding;
            }
            if (!xAxisList.get(i).isOpposite()) {
                bottomIndent += size;
                xAxisPositions[i] = workArea.y + workArea.height - bottomIndent;
            } else {
                topIndent += size;
                xAxisPositions[i] = workArea.y + topIndent;
            }
        }
        //Calculate position and indents of yAxisList
        int yAxisAmount = yAxisList.size() - 1;
        for (int i = yAxisAmount; i >= 0; i--) {
            int size = yAxisList.get(i).getWidth(g, workArea);
            if (i != yAxisAmount) {
                size += axisPadding;
            }
            if (!yAxisList.get(i).isOpposite()) {
                leftIndent += size;
                yAxisPositions[i] = workArea.x + leftIndent;
            } else {
                rightIndent += size;
                yAxisPositions[i] = workArea.x + workArea.width - rightIndent;
            }
        }
        Rectangle graphArea = new Rectangle(workArea.x + leftIndent, workArea.y + topIndent, workArea.width - leftIndent - rightIndent, workArea.height - topIndent - bottomIndent);
        setGraphArea(graphArea);
    }

    Rectangle getGraphArea() {
        return graphArea;
    }

    private void setGraphArea(Rectangle newGraphArea) {
        graphArea = newGraphArea;
    }

    public Range getPreferredXRange(int xAxisIndex) {
        Range xRange = null;
        for (Graph graph : graphs) {
            // skip graph with functions and take into account only graphs with real DataSets
            if (graph.getFunction() == null && graph.getXAxisIndex() == xAxisIndex) {
                xRange = Range.max(xRange, graph.getXFullRange());
            }
        }
        return xRange;
    }

    private void rangeXAxis() {
        for (int i = 0; i < xAxisList.size(); i++) {
            Axis xAxis = xAxisList.get(i);
            xAxis.update();
            if (xAxis.isAutoScale()) {
                Range preferredXRange = getPreferredXRange(i);
                if (preferredXRange != null) {
                    xAxis.setRange(preferredXRange.start(), preferredXRange.end());
                }
            }
        }
    }

    private void rangeYAxis() {
        for (int i = 0; i < yAxisList.size(); i++) {
            Axis yAxis = yAxisList.get(i);
            yAxis.update();
            if (yAxis.isAutoScale()) {
                Range preferredYRange = null;
                for (Graph graph : graphs) {
                    if (graph.getYAxisIndex() == i) {
                        preferredYRange = Range.max(preferredYRange, graph.getYRange());
                    }
                }
                if (preferredYRange != null) {
                    yAxis.setRange(preferredYRange.start(), preferredYRange.end());
                }
            }
        }
    }

    Rectangle calculateGraphArea(Graphics2D g2d, Rectangle fullArea) {
        this.fullArea = fullArea;
        Margin chartMargin = chartConfig.chartMargin;
        Rectangle workArea = new Rectangle(fullArea.x + chartMargin.left(),
                fullArea.y + chartMargin.top(),
                fullArea.width - chartMargin.left() - chartMargin.right(),
                fullArea.height - chartMargin.top() - chartMargin.bottom());
        rangeXAxis();
        setFunctions(workArea);
        List<LegendItem> legendItems = new ArrayList<LegendItem>();
        for (Graph graph : graphs) {
            Axis graphXAxis = xAxisList.get(graph.getXAxisIndex());
            graph.setXRange(graphXAxis.getMin(), graphXAxis.getMax(workArea), workArea);
            if(graph.getGraphName() != null) {
                legendItems.add(new LegendItem(graph.getColor(), graph.getGraphName()));
            }
        }

        tooltipPainter = new TooltipPainter(chartConfig.tooltipConfig);

        crosshairPainter = new CrosshairPainter(chartConfig.crosshairConfig);

        titlePainter = new TitlePainter(chartConfig.title, chartConfig.titleTextStyle);
        int titleHeight = titlePainter.getTitleHeight(g2d, workArea.width);
        titleArea = new Rectangle(workArea.x,workArea.y,workArea.width, titleHeight);

        legendPainter = new LegendPainter(legendItems, chartConfig.legendConfig);
        int legendHeight = legendPainter.getLegendHeight(g2d, workArea.width);
        if (legendPainter.isTop()) {
            legendArea = new Rectangle(workArea.x, workArea.y + titleHeight, workArea.width, legendHeight);
            this.chartArea = new Rectangle(workArea.x,workArea.y + titleHeight + legendHeight, workArea.width, workArea.height - legendHeight - titleHeight);
        } else {
            legendArea = new Rectangle(workArea.x, workArea.y + workArea.height - legendHeight, workArea.width, legendHeight);
            this.chartArea = new Rectangle(workArea.x, workArea.y + titleHeight, workArea.width, workArea.height - legendHeight - titleHeight);
        }


        rangeYAxis();
        setGraphAreaAndAxisPositions(g2d, this.chartArea);
        if (isTicksAlignmentEnable) {
            alignAxisTicks(xAxisList, g2d, graphArea);
            alignAxisTicks(yAxisList, g2d, graphArea);
            setGraphAreaAndAxisPositions(g2d, this.chartArea);
        }
        return graphArea;
    }

    // used in multi-pane charts to align its graph areas
    void reduceGraphArea(Graphics2D g2d, int graphAreaX, int graphAreaWidth) {
        int shiftLeft, shiftRight;
        Rectangle graphArea = this.graphArea;
        shiftLeft = graphAreaX - graphArea.x;
        shiftRight = graphArea.x + graphArea.width - graphAreaX - graphAreaWidth;

        for (int i = 0; i < yAxisList.size(); i++) {
            if (yAxisList.get(i).isOpposite()) {
                yAxisPositions[i] -= shiftRight;
            } else {
                yAxisPositions[i] += shiftLeft;
            }
        }
        graphArea.x = graphAreaX;
        graphArea.width = graphAreaWidth;
        setGraphArea(graphArea);
    }


    void draw(Graphics2D g2d) {
        g2d.setColor(chartConfig.background);
        g2d.setColor(Color.BLACK);
        g2d.fill(fullArea);
        Margin chartMargin = chartConfig.chartMargin;
        Rectangle workArea = new Rectangle(fullArea.x + chartMargin.left(),
                fullArea.y + chartMargin.top(),
                fullArea.width - chartMargin.left() - chartMargin.right(),
                fullArea.height - chartMargin.top() - chartMargin.bottom());
        g2d.setColor(chartConfig.background);
        g2d.fillRect(workArea.x, workArea.y, workArea.width, workArea.height);

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

        legendPainter.draw(g2d, legendArea);
        titlePainter.draw(g2d, titleArea);

        for (int i = 0; i < xAxisList.size(); i++) {
            xAxisList.get(i).draw(g2d, graphArea, xAxisPositions[i]);
        }

        for (int i = 0; i < yAxisList.size(); i++) {
            yAxisList.get(i).draw(g2d, graphArea, yAxisPositions[i]);
        }

        Rectangle clip = g2d.getClipBounds();
        g2d.setClip(graphArea);

        for (Graph graph : graphs) {
            graph.draw(g2d, graphArea, xAxisList.get(graph.getXAxisIndex()), yAxisList.get(graph.getYAxisIndex()));
        }
        g2d.setClip(clip);

        if (tooltipInfo != null) {
            tooltipPainter.draw(g2d, fullArea, tooltipInfo);
            crosshairPainter.draw(g2d, graphArea, tooltipInfo.getX(), tooltipInfo.getY());
        }
    }


    public void draw(Graphics2D g2d, Rectangle fullArea) {
        if (this.fullArea == null || !this.fullArea.equals(fullArea)) {
            calculateGraphArea(g2d, fullArea);
        }
        draw(g2d);
    }
}
