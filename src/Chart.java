
import axis.Axis;
import configuration.ChartConfig;
import configuration.TraceConfig;
import data.Range;
import legend.LegendItem;
import painters.CrosshairPainter;
import painters.LegendPainter;
import painters.TitlePainter;
import tooltips.TooltipInfo;
import painters.TooltipPainter;
import traces.Trace;


import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by hdablin on 24.03.17.
 */
public class Chart implements Drawable {

    private List<Axis> xAxisList = new ArrayList<Axis>(2);
    private List<Axis> yAxisList = new ArrayList<Axis>();
    private List<Trace> traces = new ArrayList<Trace>();

    private final Color GREY = new Color(150, 150, 150);
    private final Color BROWN = new Color(200, 102, 0);
    private final Color ORANGE = new Color(255, 153, 0);

    private Color[] colors = {GREY, BROWN, Color.GREEN, Color.YELLOW};
    private Color[] graphicColors = {Color.MAGENTA, Color.RED, ORANGE, Color.CYAN, Color.PINK};

    private boolean isTicksAlignmentEnable = false;

    private TooltipPainter tooltipPainter;
    private boolean isTooltipSeparated = true;
    private TooltipInfo tooltipInfo;
    private LegendPainter legendPainter;
    private TitlePainter titlePainter;
    private CrosshairPainter crosshairPainter;
    private ChartConfig chartConfig;

    private Rectangle titleArea;
    private Rectangle legendArea;
    private Rectangle graphArea;


    public Chart(ChartConfig chartConfig) {
        this.chartConfig = chartConfig;
        xAxisList.add(new Axis(chartConfig.getBottomAxisConfig()));
        xAxisList.add(new Axis(chartConfig.getTopAxisConfig()));
        for (int i = 0; i < chartConfig.getStacksAmount(); i++) {
            yAxisList.add(new Axis(chartConfig.getLeftAxisConfig(i)));
            yAxisList.add(new Axis(chartConfig.getRightAxisConfig(i)));
        }

        tooltipPainter = new TooltipPainter(chartConfig.tooltipConfig);
        crosshairPainter = new CrosshairPainter(chartConfig.crosshairConfig);
        titlePainter = new TitlePainter(chartConfig.title, chartConfig.titleTextStyle);
        ArrayList<LegendItem> legendItems = new ArrayList<LegendItem>(chartConfig.getTraceAmount());
        for (int i = 0; i < chartConfig.getTraceAmount() ; i++) {
            TraceConfig traceConfig = chartConfig.getTraceConfig(i);
            if(traceConfig.color == null) {
                traceConfig.color = graphicColors[i % graphicColors.length];
            }
            if(traceConfig.name == null) {
                traceConfig.name = "Trace "+i;
            }
            legendItems.add(new LegendItem(traceConfig.color, traceConfig.name));
            Trace trace = traceConfig.getType().getTrace();
            trace.setConfig(traceConfig);
            traces.add(trace);
        }
        legendPainter = new LegendPainter(legendItems, chartConfig.legendConfig);
        setXAxisDomain();
        setYAxisDomain();
    }

    private void setXAxisDomain() {
        for (int i = 0; i < xAxisList.size(); i++) {
            Axis xAxis = xAxisList.get(i);
            if (xAxis.isAutoScale()) {
                Range xRange = null;
                for (Trace trace : traces) {
                    if(trace.getXAxisIndex() == i) {
                        xRange = Range.max(xRange, trace.getXRange());
                    }
                }
                xAxis.setMinMax(xRange);
            }
        }
    }

    private void setYAxisDomain() {
        for (int i = 0; i < yAxisList.size(); i++) {
            Axis yAxis = yAxisList.get(i);
            if (yAxis.isAutoScale()) {
                Range yRange = null;
                for (Trace trace : traces) {
                    if(trace.getYAxisIndex() == i) {
                        yRange = Range.max(yRange, trace.getYRange());
                    }
                }
                yAxis.setMinMax(yRange);
            }
        }
    }



    // TODO: handling multiple xAxis!!!!
    // TODO: add separated tooltips
    public boolean hover(int mouseX, int mouseY) {
        return false;
    }
 /*   public boolean hover(int mouseX, int mouseY) {
        boolean isHover = false;
        if (true ) {
            tooltipInfo = null;
            for (Graph graph : traces) {
                isHover = graph.setHoverPoint(-1) || isHover;
            }
            return isHover;
        }

        long[] nearestPointsIndexes = new long[traces.size()];
        Integer minDistance = null;
        // find min distance from traces points to mouseX
        for (int i = 0; i < traces.size(); i++) {
            Graph graph = traces.getString(i);
            Axis xAxis = xAxisList.getString(graph.getXAxisIndex());
            Axis yAxis = yAxisList.getString(graph.getYAxisIndex());
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
        // hover traces points that have minDistance to mouseX
        if (minDistance != null) {
            ArrayList<TooltipItem> tooltipItems = new ArrayList<TooltipItem>();
            Range y_range = null;
            Number hoverXValue = null;
            int hoverPointsCounter = 0;
            for (int i = 0; i < traces.size(); i++) {
                Graph graph = traces.getString(i);
                Axis xAxis = xAxisList.getString(graph.getXAxisIndex());
                Axis yAxis = yAxisList.getString(graph.getYAxisIndex());
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
    }*/


    private void calculateMarginsAndAreas(Graphics2D g2) {
        Rectangle fullArea = chartConfig.getArea();
        int titleHeight = titlePainter.getTitleHeight(g2, fullArea.width);
        titleArea = new Rectangle(fullArea.x,fullArea.y,fullArea.width, titleHeight);

        int left, right, top, bottom;
        if(chartConfig.margin != null) {
            left = chartConfig.margin.left();
            right = chartConfig.margin.right();
            top = chartConfig.margin.top();
            bottom = chartConfig.margin.bottom();

            int legendWidth = fullArea.width - right - left;
            int legendHeight = legendPainter.getLegendHeight(g2, legendWidth);
            int legendX = fullArea.x + left;
            if (chartConfig.legendConfig.isTop()) {
                legendArea = new Rectangle(legendX, fullArea.y + titleHeight, legendWidth, legendHeight);
            } else {
                legendArea = new Rectangle(legendX, fullArea.y + fullArea.height - legendHeight, legendWidth, legendHeight);
            }
        } else {
            // set YAxis ranges and calculate margins
            for (int i = 0; i < yAxisList.size(); i++) {
                yAxisList.get(i).setStartEnd(chartConfig.getYAxisStartEnd(i, fullArea));
            }
            left = 0;
            right = 0;
            for (int i = 0; i < yAxisList.size()/2; i++) {
                left = Math.max(left, yAxisList.get(i*2).getThickness(g2));
                right = Math.max(right, yAxisList.get(i*2 + 1).getThickness(g2));
            }

            // set XAxis ranges and calculate margins
            int xStart = fullArea.x;
            int xEnd = fullArea.x + fullArea.width;
            xAxisList.get(0).setStartEnd(xStart, xEnd);
            xAxisList.get(1).setStartEnd(xStart, xEnd);


            int legendWidth = fullArea.width - right - left;
            int legendHeight = legendPainter.getLegendHeight(g2, legendWidth);
            int legendX = fullArea.x + left;
            if (chartConfig.legendConfig.isTop()) {
                legendArea = new Rectangle(legendX, fullArea.y + titleHeight, legendWidth, legendHeight);
            } else {
                legendArea = new Rectangle(legendX, fullArea.y + fullArea.height - legendHeight, legendWidth, legendHeight);
            }

            bottom = 0;
            top = titleArea.height;
            if(chartConfig.legendConfig.isTop()) {
                top += legendArea.height;
            } else {
                bottom += legendArea.height;
            }

            bottom += xAxisList.get(0).getThickness(g2);
            top += xAxisList.get(1).getThickness(g2);
        }
        graphArea = new Rectangle(fullArea.x + left, fullArea.y + top,
                fullArea.width - left - right, fullArea.height - top - bottom);

        int xStart = graphArea.x;
        int xEnd = graphArea.x + graphArea.width;
        xAxisList.get(0).setStartEnd(xStart, xEnd);
        xAxisList.get(1).setStartEnd(xStart, xEnd);

        for (int i = 0; i < yAxisList.size(); i++) {
            yAxisList.get(i).setStartEnd(chartConfig.getYAxisStartEnd(i, graphArea));
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        if(graphArea == null) {
            calculateMarginsAndAreas(g2d);
        }
        Rectangle fullArea = chartConfig.getArea();
        g2d.setColor(chartConfig.marginColor);
        g2d.fill(fullArea);

        g2d.setColor(chartConfig.background);
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
            trace.draw(g2d, xAxisList.get(trace.getXAxisIndex()), yAxisList.get(trace.getYAxisIndex()));
        }
        g2d.setClip(clip);

        if (tooltipInfo != null) {
            tooltipPainter.draw(g2d, fullArea, tooltipInfo);
            crosshairPainter.draw(g2d, graphArea, tooltipInfo.getX(), tooltipInfo.getY());
        }
    }
}
