
import axis.Axis;
import axis.AxisType;
import configuration.AxisConfig;
import configuration.ChartConfig;
import configuration.Margin;
import configuration.Orientation;
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

    private ChartConfig chartConfig;

    public Chart() {
        this(new ChartConfig(ChartConfig.DEBUG_THEME));
    }

    public Chart(ChartConfig chartConfig) {
        this.chartConfig = chartConfig;

        for (int i = 0; i < chartConfig.getXAxisAmount(); i++) {
            xAxisList.add(new Axis(chartConfig.getXAxisConfig(i)));
        }
        for (int i = 0; i < chartConfig.getYAxisAmount(); i++) {
            yAxisList.add(new Axis(chartConfig.getYAxisConfig(i)));
        }
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
            double xValue = xAxis.invert(mouseX);
            long pointIndex = graph.getNearestPointIndex(xValue);
            //System.out.println(graph.getGraphName() + ": pointIndex=" + pointIndex);

            nearestPointsIndexes[i] = pointIndex;
            if (pointIndex >= 0) {
                int x = (int) Math.round(xAxis.scale(graph.getPoint(pointIndex).getX().doubleValue()));
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
                    int x = (int) Math.round(xAxis.scale(graph.getPoint(pointIndex).getX().doubleValue()));
                    if ((x - mouseX) == minDistance) {
                        hoverPointsCounter++;
                        hoverXValue = xAxis.invert(x);
                      //  hoverXValue = xAxis.roundValue(hoverXValue.doubleValue(), graphArea);
                        isHover = graph.setHoverPoint(pointIndex) || isHover;
                        TooltipItem tooltipItem = graph.getTooltipItem();
                        if (tooltipItem != null) {
                            tooltipItems.add(graph.getTooltipItem());
                        }

                        Range yValueRange = graph.getPointYRange(graph.getPoint(pointIndex).getY());
                        Range yPixelRange = new Range(yAxis.scale(yValueRange.start()), yAxis.scale(yValueRange.start()));
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
              //  axis.getTicksSettings().setTicksAmount(0);
            }

            for (Axis axis : axisList) {
               // maxSize = Math.max(maxSize, axis.getTicks(g).size());
            }

            for (Axis axis : axisList) {
               // axis.getTicksSettings().setTicksAmount(maxSize);
               // axis.setEndOnTick(true);
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
                XYList data = new XYList();
                for (int i = area.x; i <= area.width + area.x; i++) {
                    double value = xAxis.invert(i);
                    data.addPoint(value, function.apply(value));
                }
                graph.setData(data);
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
            int size = xAxisList.get(i).getWidth(g);
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
            int size = yAxisList.get(i).getWidth(g);
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
                    xAxis.setMinMax(preferredXRange.start(), preferredXRange.end());
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
                    yAxis.setMinMax(preferredYRange.start(), preferredYRange.end());
                }
            }
        }
    }

    Rectangle calculateGraphArea(Graphics2D g2d, Rectangle fullArea) {
        this.fullArea = fullArea;
        for (Axis axis : xAxisList) {
           axis.setStartEnd(fullArea.x, fullArea.x + fullArea.width);
        }
        for (Axis axis : yAxisList) {
            axis.setStartEnd(fullArea.y + fullArea.height, fullArea.y);
        }
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
            graph.setXRange(graphXAxis.getMin(), graphXAxis.getMax(), workArea);
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

        for (Axis axis : xAxisList) {
            axis.setStartEnd(graphArea.x, graphArea.x + graphArea.width);
        }
        for (Axis axis : yAxisList) {
            axis.setStartEnd(graphArea.y + graphArea.height, graphArea.y);
        }

        for (int i = 0; i < xAxisList.size(); i++) {
            xAxisList.get(i).drawGrid(g2d, xAxisPositions[i], graphArea.height);
        }

        for (int i = 0; i < yAxisList.size(); i++) {
            yAxisList.get(i).drawGrid(g2d, yAxisPositions[i], graphArea.width);
        }

        for (int i = 0; i < xAxisList.size(); i++) {
            xAxisList.get(i).drawAxis(g2d, xAxisPositions[i]);
        }

        for (int i = 0; i < yAxisList.size(); i++) {
            yAxisList.get(i).drawAxis(g2d, yAxisPositions[i]);
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
