
import axis.Axis;
import axis.AxisType;
import axis.LinearAxis;
import data.Range;
import data.XYList;
import graphs.Graph;


import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.DoubleFunction;

/**
 * Created by hdablin on 24.03.17.
 */
public class Chart implements Drawable {
    private List<Graph> graphs = new ArrayList<>();
    private List<Axis> xAxisList = new ArrayList<>();
    private List<Axis> yAxisList = new ArrayList<>();
    private int chartPadding = 10;
    private int axisPadding = 10;

    private final Color GREY = new Color(150, 150, 150);
    private final Color BROWN = new Color(200, 102, 0);
    private final Color ORANGE = new Color(255, 153, 0);

    private Color[] colors = {GREY, BROWN, Color.GREEN, Color.YELLOW};
    private Color[] graphicColors = {Color.MAGENTA, Color.RED, ORANGE, Color.CYAN, Color.PINK};

    private boolean isTicksAlignmentEnable = false;
    private int[] xAxisPositions;
    private int[] yAxisPositions;
    private Rectangle graphArea; // area to draw graphs


    public Chart() {
        Axis x = new LinearAxis();
        x.setHorizontal(true);
        xAxisList.add(x);
        Axis y = new LinearAxis();
        y.setHorizontal(false);
        yAxisList.add(y);
    }

    public boolean hover(int mouseX, int mouseY) {
        boolean isHover = false;
        for (Graph graph : graphs) {
            Axis xAxis = xAxisList.get(graph.getXAxisIndex());
            Axis yAxis = yAxisList.get(graph.getYAxisIndex());
            isHover = isHover || graph.hover(mouseX, mouseY, xAxis, yAxis);
        }
        return isHover;
    }

    public String getTooltipText(){
        String tooltipText = "";
        for (Graph graph : graphs) {
             String graphToolTip = graph.getTooltipText();
             if (!graphToolTip.isEmpty()){
                 if(!tooltipText.isEmpty()) {
                     tooltipText = tooltipText + "\n\n" + graphToolTip;
                 } else {
                     tooltipText =  graphToolTip;
                 }
             }
        }
        return  tooltipText;
    }

    public double getPreferredPixelsPerUnit(int xAxisIndex) {
        double pixelsPerUnit = 0;
        for (Graph graph : graphs) {
            // skip graph with functions and take into account only graphs with real DataSets
            if(graph.getFunction() == null && graph.getXAxisIndex() == xAxisIndex) {
                pixelsPerUnit = Math.max(pixelsPerUnit, graph.getPreferredPixelsPerUnit());
            }
        }
        return pixelsPerUnit;
    }

    public Range getPreferredXRange(int xAxisIndex) {
        Range xRange = null;
        for (Graph graph : graphs) {
            // skip graph with functions and take into account only graphs with real DataSets
            if(graph.getFunction() == null && graph.getXAxisIndex() == xAxisIndex) {
                xRange = Range.max(xRange, graph.getXFullRange());
            }
        }
        return xRange;
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
        addGraph(graph,  0, 0);
    }

    public void update(){

    }

    public void addGraph(Graph graph,  int xAxisIndex, int yAxisIndex) {
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
     * @param area
     */
    private void setFunctions(Rectangle area) {
        for (Graph graph : graphs) {
            DoubleFunction function = graph.getFunction();
            if(function != null) {
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
                // restore axis settings
                xAxis.setEndOnTick(isEndOnTick);
                xAxis.setLowerPadding(lowerPadding);
                xAxis.setUpperPadding(upperPadding);
            }

        }
    }

    //Calculate and set axis positions and graphArea
    private void setGraphAreaAndAxisPositions(Graphics2D g, Rectangle fullArea) {
        xAxisPositions = new int[xAxisList.size()];
        yAxisPositions = new int[yAxisList.size()];

        int topIndent = chartPadding;
        int bottomIndent = chartPadding;
        int leftIndent = chartPadding;
        int rightIndent = chartPadding;

        //Calculate position and indents of xAxisList
        int xAxisAmount = xAxisList.size() - 1;
        for (int i = xAxisAmount; i >= 0; i--) {
            int size = xAxisList.get(i).getWidth(g, fullArea);
            if (i != xAxisAmount) {
                size += axisPadding;
            }
            if (!xAxisList.get(i).isOpposite()) {
                bottomIndent += size;
                xAxisPositions[i] = fullArea.y + fullArea.height - bottomIndent;
            } else {
                topIndent += size;
                xAxisPositions[i] = fullArea.y + topIndent;
            }
        }
        //Calculate position and indents of yAxisList
        int yAxisAmount = yAxisList.size() - 1;
        for (int i = yAxisAmount; i >= 0; i--) {
            int size = yAxisList.get(i).getWidth(g, fullArea);
            if (i != yAxisAmount) {
                size += axisPadding;
            }
            if (!yAxisList.get(i).isOpposite()) {
                leftIndent += size;
                yAxisPositions[i] = fullArea.x + leftIndent;
            } else {
                rightIndent += size;
                yAxisPositions[i] = fullArea.x + fullArea.width - rightIndent;
            }
        }
        Rectangle graphArea = new Rectangle(fullArea.x + leftIndent, fullArea.y + topIndent, fullArea.width - leftIndent - rightIndent, fullArea.height - topIndent - bottomIndent);
        setGraphArea(graphArea);
    }

    Rectangle getGraphArea() {
        return graphArea;
    }

    private void setGraphArea(Rectangle newGraphArea) {
        graphArea = newGraphArea;
    }

    private void rangeXAxis(Rectangle area){
        for (int i = 0; i < xAxisList.size(); i++) {
            Axis xAxis = xAxisList.get(i);
            if(xAxis.isAutoScale()) {
                Range preferredXRange = getPreferredXRange(i);
                if(preferredXRange != null) {
                    xAxis.setRange(preferredXRange.start(), preferredXRange.end());
                }
            }
        }
    }

    private void rangeYAxis(){
        for (int i = 0; i < yAxisList.size(); i++) {
            Axis yAxis = yAxisList.get(i);
            if(yAxis.isAutoScale()) {
                Range preferredYRange = null;
                for (Graph graph : graphs) {
                    if(graph.getYAxisIndex() == i) {
                        preferredYRange = Range.max(preferredYRange, graph.getYRange());
                    }
                }
                if(preferredYRange != null) {
                    yAxis.resetRange();
                    yAxis.setRange(preferredYRange.start(), preferredYRange.end());
                }
            }
        }
    }

    Rectangle calculateGraphArea(Graphics2D g2d, Rectangle fullArea) {
        rangeXAxis(fullArea);
        setFunctions(fullArea);
        for (Graph graph : graphs) {
            Axis graphXAxis = xAxisList.get(graph.getXAxisIndex());
            graph.setXRange(graphXAxis.getMin(), graphXAxis.getMax(fullArea), fullArea);
        }
        rangeYAxis();

        setGraphAreaAndAxisPositions(g2d, fullArea);

        if (isTicksAlignmentEnable) {
            alignAxisTicks(xAxisList, g2d, graphArea);
            alignAxisTicks(yAxisList, g2d, graphArea);
            setGraphAreaAndAxisPositions(g2d, fullArea);
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
        for (int i = 0; i < xAxisList.size(); i++) {
            xAxisList.get(i).draw(g2d, graphArea, xAxisPositions[i]);
        }

        for (int i = 0; i < yAxisList.size(); i++) {
            yAxisList.get(i).draw(g2d, graphArea, yAxisPositions[i]);
        }

        Rectangle clip = g2d.getClipBounds();
        g2d.setClip(graphArea);

        for (Graph graph : graphs) {
            graph.draw(g2d, graphArea, xAxisList.get(graph.getXAxisIndex()),yAxisList.get(graph.getYAxisIndex()) );
        }
        g2d.setClip(clip);
    }


    public void draw(Graphics2D g2d, Rectangle fullArea) {
        calculateGraphArea(g2d, fullArea);
        draw(g2d);
    }

    public void drawHover(Graphics2D g2d, Rectangle fullArea){
        for (Graph graph : graphs) {
            graph.drawHover(g2d, graphArea, xAxisList.get(graph.getXAxisIndex()),yAxisList.get(graph.getYAxisIndex()) );
        }
    }
}
