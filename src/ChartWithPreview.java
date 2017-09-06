import axis_old.Axis;
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
/**
 * Created by hdablin on 25.06.17.
 */
public class ChartWithPreview implements Drawable {
    private List<Chart> charts = new ArrayList<Chart>();
    private List<Chart> previews = new ArrayList<Chart>();
    private List<Integer> chartWeights = new ArrayList<Integer>();
    private List<Integer> previewWeights = new ArrayList<Integer>();
    private boolean isXAxisSynchronized = true;
    private double xAxisPixelsPerUnit = 0;
    private Scroll scroll = new Scroll();
    private boolean isFirstDraw = true;
    private Rectangle fullArea;

    public void update() {
        fullArea = null;
    }

    public boolean hover(int mouseX, int mouseY) {
        boolean isHover = false;
        for (Chart chart : charts) {
            isHover = isHover || chart.hover(mouseX, mouseY);
        }
        return isHover;
    }

    private double getXAxisMaxScale() {
        double preferredScale = 0;
        for (Chart chart : charts) {
            preferredScale = Math.max(preferredScale, chart.getPreferredPixelsPerUnit(0));
        }
        return preferredScale;
    }

    public void setXAxisScale(double pixelsPerUnit, int chartIndex) {
        charts.get(chartIndex).getXAxis(0).setPixelsPerUnit(pixelsPerUnit);

    }

    public void setXAxisScale(double pixelsPerUnit) {
        xAxisPixelsPerUnit = pixelsPerUnit;
        for (Chart chart : charts) {
            chart.getXAxis(0).setPixelsPerUnit(pixelsPerUnit);
        }
    }

    private void adjustXAxisScale() {
        if (xAxisPixelsPerUnit != 0) { // set the given axisData for all charts
            for (Chart chart : charts) {
                chart.getXAxis(0).setPixelsPerUnit(xAxisPixelsPerUnit);
            }
            scroll.setPointsPerUnit(xAxisPixelsPerUnit);
        } else {
            double maxScale = getXAxisMaxScale();
            scroll.setPointsPerUnit(maxScale);
            if (isXAxisSynchronized) { // set max axisData for all charts
                for (Chart chart : charts) {
                    chart.getXAxis(0).setPixelsPerUnit(maxScale);
                }
            } else {
                for (Chart chart : charts) { // set preferred axisData for every chart
                    double scale = chart.getPreferredPixelsPerUnit(0);
                    chart.getXAxis(0).setPixelsPerUnit(scale);
                }
            }
        }
    }

    private void adjustMinMaxRange() {
        Range minMaxRange = null;

        for (Chart chart : charts) {
            minMaxRange = Range.max(minMaxRange, chart.getPreferredXRange(0));
        }
        for (Chart preview : previews) {
            minMaxRange = Range.max(minMaxRange, preview.getPreferredXRange(0));
        }

        for (Chart preview : previews) {
            preview.getXAxis(0).setRange(minMaxRange.start(), minMaxRange.end());
        }

        scroll.getScrollModel().setMin(minMaxRange.start());
        scroll.getScrollModel().setMax(minMaxRange.end());
    }

    public void addChart() {
        addChart(2);
    }

    public void addChart(int weight) {
        if (weight <= 0) {
            String errorMessage = "Wrong weight: {0}. Expected > 0.";
            String formattedError = MessageFormat.format(errorMessage, weight);
            throw new IllegalArgumentException(formattedError);
        }
        chartWeights.add(weight);
        Chart chart = new Chart();
        chart.getXAxis(0).setAutoScale(false);
        charts.add(chart);
    }

    public void addPreview(int weight) {
        if (weight <= 0) {
            String errorMessage = "Wrong weight: {0}. Expected > 0.";
            String formattedError = MessageFormat.format(errorMessage, weight);
            throw new IllegalArgumentException(formattedError);
        }
        previewWeights.add(weight);
        Chart preview = new Chart();
        preview.getXAxis(0).setAutoScale(false);
        previews.add(preview);
    }

    public void addPreview() {
        addPreview(1);
    }

    public void addGraph(Graph graph, int chartIndex) {
        charts.get(chartIndex).addGraph(graph);
    }

    public void addPreviewGraph(Graph graph, int previewIndex) {
        previews.get(previewIndex).addGraph(graph);
    }


    public boolean isMouseInsideCursor(int mouseX, int mouseY) {
        return scroll.isMouseInsideScroll(mouseX, mouseY);
    }

    public void moveCursorPosition(int shift) {
        scroll.moveScroll(shift);
        setChartsAxisStart();
        update();
    }

    public void setCursorPosition(int mousePosition) {
        scroll.setScrollPosition(mousePosition);
        setChartsAxisStart();
        update();
    }

    private void setChartsAxisStart() {
        for (Chart chart : charts) {
            Axis xAxis = chart.getXAxis(0);
            xAxis.setRange(scroll.getScrollModel().getViewportPosition(), null);
        }
    }


    public void draw(Graphics2D g2d, Rectangle fullArea) {
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

        if(this.fullArea == null || !this.fullArea.equals(fullArea)) {
           this.fullArea = fullArea;

            adjustXAxisScale();
            adjustMinMaxRange();
            if(isFirstDraw) {
                scroll.getScrollModel().setViewportPosition(scroll.getScrollModel().getMin());
                setChartsAxisStart();
                isFirstDraw = false;
            }

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
            }
        }

        g2d.setColor(Color.BLACK);
        g2d.fill(fullArea);

        for (int i = 0; i < chartsAndPreviews.size(); i++) {
            chartsAndPreviews.get(i).draw(g2d);
        }
        scroll.draw(g2d, getPreviewArea());
    }



    private Rectangle getPreviewArea() {
        Rectangle firstArea = previews.get(0).getGraphArea();
        Rectangle lastArea = previews.get(previews.size() - 1).getGraphArea();
        return new Rectangle(firstArea.x, firstArea.y, firstArea.width, (int) lastArea.getMaxY() - firstArea.y);
    }

    public boolean isMouseInPreviewArea(int mouseX, int mouseY) {
        return getPreviewArea().contains(mouseX, mouseY);
    }

    private int getPaintingAreaWidth() {
        return charts.get(0).getGraphArea().width;
    }

    private int getPaintingAreaX() {
        return charts.get(0).getGraphArea().x;
    }
}