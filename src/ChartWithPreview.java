import axis.Axis;
import data.Range;
import graphs.Graph;

import java.awt.*;
import java.text.MessageFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hdablin on 25.06.17.
 */
public class ChartWithPreview implements Drawable {
    private List<Chart> charts = new ArrayList<Chart>();
    private List<Chart> previews = new ArrayList<Chart>();
    private List<Integer> chartWeights = new ArrayList<Integer>();
    private List<Integer> previewWeights = new ArrayList<Integer>();
    private boolean isChartsSynchronized = true;
    private ScrollPainter scrollPainter;
    private Integer fullChartWidth;
    private final int SCREEN_WIDTH = 500;

    public ChartWithPreview() {
        scrollPainter = new ScrollPainter(new PreviewScrollModel());
    }

    public void update(){

        for (Chart preview : previews) {
            for (int i = 0; i < preview.getGraphsAmoiunt(); i++) {
                // CompressedData compressedData = (CompressedData)(preview.getGraph(i).getData());
                // compressedData.update();
            }
        }
        setAxisLength(getFullChartWidth());
    }

    public void setFullChartWidth(int fullChartWidth) {
        this.fullChartWidth = fullChartWidth;
    }

    private int getFullChartWidth(){
        if(fullChartWidth != null) {
            return fullChartWidth;
        }
        int width = 0;
        for (Chart chart : charts) {
            width = Math.max(width,chart.getMaxGraphSize());
        }
        return width;
    }



    private void setAxisLength(int length){
        int j = 0;
        for (Chart chart : charts) {
            j++;
            for (int i = 0; i < chart.getXAxisAmount(); i++) {
                Axis xAxis = chart.getXAxis(i);
                xAxis.setLength(length);
                //xAxis.setLength(chart.getMaxGraphSize());
            }
        }
    }

    public void addChart(Chart chart) {
        charts.add(chart);
        chartWeights.add(2);
        setAxisLength(getFullChartWidth());
    }

    public void addChartPanel() {
        addChartPanel(1);
    }

    public void addChartPanel(int weight) {
        if (weight <= 0) {
            String errorMessage = "Wrong weight: {0}. Expected > 0.";
            String formattedError = MessageFormat.format(errorMessage, weight);
            throw new IllegalArgumentException(formattedError);
        }
        chartWeights.add(weight);
        Chart chart = new Chart();
       // chart.getXAxis(0).setAutoScale(false);
       // chart.enableTicksAlignment(false);
        charts.add(chart);
    }

    public void addPreviewPanel(int weight) {
        if (weight <= 0) {
            String errorMessage = "Wrong weight: {0}. Expected > 0.";
            String formattedError = MessageFormat.format(errorMessage, weight);
            throw new IllegalArgumentException(formattedError);
        }
        previewWeights.add(weight);
        Chart preview = new Chart();
        preview.getXAxis(0).setAutoScale(false);
        preview.enableTicksAlignment(false);
        previews.add(preview);
    }

    public void addPreviewPanel() {
        addPreviewPanel(1);
    }

    public void addPreviewGraph(Graph graph, int previewIndex){
        previews.get(previewIndex).addGraph(graph);
    }


    private Range getMaxRange() {
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;

        for (Chart chart : charts) {
            max = Math.max(max, chart.getXAxis(0).getRawMax());
            min = Math.min(min, chart.getXAxis(0).getRawMin());
        }
        return new Range(min, max);
    }

    private void synchronizeRanges() {
        Range range = getMaxRange();
        for (Chart chart : charts) {
            chart.getXAxis(0).setRange(range.getStart(), range.getEnd());
        }
        for (Chart chart : previews) {
            chart.getXAxis(0).setRange(range.getStart(), range.getEnd());
        }
    }

    public boolean isMouseInsideCursor(int mouseX, int mouseY){
        return scrollPainter.isMouseInsideScroll(mouseX, mouseY);
    }

    public void moveCursorPosition(int shift){
        scrollPainter.moveScrollPosition(shift);
        setChartsAxisOrigins();
    }

    public void setCursorPosition(int mousePosition) {
        scrollPainter.setScrollPosition(mousePosition);
        setChartsAxisOrigins();
    }

    private double getPreviewXValue(double scrollPosition) {
        Axis xAxis = previews.get(0).getXAxis(0);
        Rectangle previewArea = previews.get(0).getGraphArea();
        return xAxis.pointsToValue(previewArea.getX() + scrollPosition, previewArea);
    }


    private void setChartsAxisOrigins() {
        for (Chart chart : charts) {
            for (int i = 0; i < chart.getXAxisAmount(); i++) {
                Axis xAxis = chart.getXAxis(i);
                xAxis.setStartValue(getPreviewXValue(scrollPainter.getScrollPosition()));
            }
        }
    }


    public void draw(Graphics2D g2d, Rectangle fullArea) {
        if (isChartsSynchronized) {
            synchronizeRanges();
        }
        List<Chart> chartsAndPreviews = new AbstractList<Chart>() {
            @Override
            public Chart get(int index) {

                if (index < charts.size()) {
                    return charts.get(index);
                }
                return previews.get(index - charts.size());
            }

            @Override
            public int size() {
                return charts.size() + previews.size();
            }
        };

        List<Integer> allWeights = new AbstractList<Integer>() {
            @Override
            public Integer get(int index) {

                if (index < chartWeights.size()) {
                    return chartWeights.get(index);
                }
                return previewWeights.get(index - chartWeights.size());
            }

            @Override
            public int size() {
                return chartWeights.size() + previewWeights.size();
            }

        };

        int weightSum = 0;
        for (int j = 0; j < allWeights.size(); j++) {
            weightSum += allWeights.get(j);
        }

        int oneWeightHeight = (chartsAndPreviews.size() == 0) ? fullArea.height : fullArea.height / weightSum;
        int chartY = fullArea.y;
        List<Rectangle> chartGraphAreas = new ArrayList<Rectangle>(chartsAndPreviews.size());


        for (int i = 0; i < chartsAndPreviews.size(); i++) {
            int chartHeight = oneWeightHeight * allWeights.get(i);
            Rectangle chartRectangle = new Rectangle(fullArea.x, chartY, fullArea.width, chartHeight);
            chartY = chartY + chartHeight;
            chartGraphAreas.add(chartsAndPreviews.get(i).calculateGraphArea(g2d, chartRectangle));
        }

        int maxX = Integer.MIN_VALUE;
        int minEnd = Integer.MAX_VALUE;
        for (Rectangle area : chartGraphAreas) {
            maxX = Math.max(maxX, area.x);
            minEnd = Math.min(minEnd, area.x + area.width);
        }

        for (int i = 0; i < chartsAndPreviews.size(); i++) {
            chartsAndPreviews.get(i).reduceGraphArea(g2d, maxX, minEnd - maxX);
            chartsAndPreviews.get(i).draw(g2d);
        }
        scrollPainter.draw(g2d, getPreviewArea());
    }

    private Rectangle getPreviewArea(){
        Rectangle firstArea = previews.get(0).getGraphArea();
        Rectangle lastArea = previews.get(previews.size() - 1).getGraphArea();
        return new Rectangle(firstArea.x,firstArea.y,firstArea.width,(int)lastArea.getMaxY() - firstArea.y);
    }

    public boolean isMouseInPreviewArea(int mouseX, int mouseY){
        return getPreviewArea().contains(mouseX, mouseY);
    }

    private int getPaintingAreaWidth() {
        return charts.get(0).getGraphArea().width;
    }

    private int getPaintingAreaX() {
        return charts.get(0).getGraphArea().x;
    }

    class PreviewScrollModel implements ScrollModel {
        long viewportPosition;

        @Override
        public long getMin() {
            return 0;
        }

        @Override
        public long getMax() {
            return Math.max(getFullChartWidth(),getPaintingAreaWidth());
        }


        @Override
        public long getViewportWidth() {
            return getPaintingAreaWidth();
        }

        @Override
        public long getViewportPosition() {
            return viewportPosition;
        }

        @Override
        public void setViewportPosition(long newPosition) {
            if (newPosition > getMax() - getViewportWidth()) {
                newPosition = getMax() - getViewportWidth();
            }
            if (newPosition < getMin()){
                newPosition = getMin();
            }
            viewportPosition = newPosition;

        }
    }

}