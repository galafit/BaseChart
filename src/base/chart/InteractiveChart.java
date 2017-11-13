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
import java.util.ArrayList;

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

    private int selectedTraceIndex = 0;
    private int hoverIndex = -1;
    private int pastX;
    private int pastY;
    private int selectedYAxisIndex;
    private int[] selectedXAxisIndexes;


    private GestureListener mouseListener = new GestureListenerImpl();
    private ArrayList<ChartEventListener> chartEventListeners = new ArrayList<ChartEventListener>();

    // во сколько раз растягивается или сжимается ось при автозуме
    private double defaultZoomFactor = 2;
    private boolean isLongPress = false;

    public InteractiveChart(ChartConfig chartConfig, Rectangle area) {
        fullArea = area;
        this.chartConfig = chartConfig;
        chart = new SimpleChart(chartConfig);
        tooltipPainter = new TooltipPainter(chartConfig.getTooltipConfig());
        crosshairPainter = new CrosshairPainter(chartConfig.getCrosshairConfig());
        titlePainter = new TitlePainter(chartConfig.getTitle(), chartConfig.getTitleTextStyle());
        legendPainter = new LegendPainter(chart.getTracesInfo(), chartConfig.getLegendConfig());
    }

    public void zoomY(int yAxisIndex, double zoomFactor) {
        chart.zoomY(yAxisIndex, zoomFactor);
    }

    public void zoomX(int xAxisIndex, double zoomFactor) {
        chart.zoomX(xAxisIndex, zoomFactor);
    }

    public void translateY(int yAxisIndex, int translation) {
        chart.translateY(yAxisIndex, translation);
    }

    public void translateX(int xAxisIndex, int translation) {
        chart.translateX(xAxisIndex, translation);
    }

    private boolean hoverOff() {
        if (hoverIndex >= 0) {
            hoverIndex = -1;
            return true;
        }
        return false;
    }

    private boolean hoverOn(int mouseX, int mouseY) {
        if (selectedTraceIndex < 0) {
            return false;
        }
        int nearestIndex = chart.getData(selectedTraceIndex).findNearestData(chart.xPositionToValue(selectedTraceIndex, mouseX));
        if (hoverIndex != nearestIndex) {
            hoverIndex = nearestIndex;
            return true;
        }
        return false;
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

    private void fireChangeEvent() {
        for (ChartEventListener chartEventListener : chartEventListeners) {
            chartEventListener.update();
        }
    }

    public void addChartListener(ChartEventListener listener) {
        chartEventListeners.add(listener);
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

    void setTopAxisExtremes(Range minMax) {
        chart.setTopAxisExtremes(minMax);
    }

    void setBottomAxisExtremes(Range minMax) {
        chart.setBottomAxisExtremes(minMax);
    }

    public GestureListener getMouseListener() {
        return mouseListener;
    }

    class GestureListenerImpl implements GestureListener {

        public void mouseMoved(int mouseX, int mouseY) {
            if (hoverOn(mouseX, mouseY)) {
                tooltipInfo = getTooltipInfo();
                fireChangeEvent();
            }
        }

        @Override
        public void onClick(int x, int y) {
        }

        @Override
        public void onDoubleClick(int x, int y) {

        }

        @Override
        public void onPinchZoom(double xZoomFactor, double yZoomFactor) {

        }

        @Override
        public void onRelease(int x, int y) {
            if (isLongPress) {
                if (hoverOff()) {
                    fireChangeEvent();
                }
            }
            isLongPress = false;
            //selectedYAxisIndex = -1;
            //selectedXAxisIndexes = new int[0];

        }

        @Override
        public void onPress(int x, int y, boolean isLong) {
            isLongPress = isLong;
            if (isLong) {
                if (hoverOn(x, y)) {
                    fireChangeEvent();
                }
            } else {
                pastX = x;
                pastY = y;
                if (selectedTraceIndex >= 0) {
                    selectedYAxisIndex = chart.getTraceYAxisIndex(selectedTraceIndex);
                    selectedXAxisIndexes = new int[1];
                    selectedXAxisIndexes[0] = chart.getTraceXAxisIndex(selectedTraceIndex);

                } else {
                    selectedYAxisIndex = chart.getYAxisIndex(x, y);
                    selectedXAxisIndexes = chart.getStackXAxisIndexes(selectedYAxisIndex / 2);
                }
            }
        }

        @Override
        public void onDrag(int x, int y, boolean isModified) {
            if (isLongPress) { // longPressDrag we show tooltip
                if (hoverOn(x, y)) {
                    fireChangeEvent();
                }
            } else {
                if (isModified) { // drag with some key pressed (shift, control, alt... ) we zoom y axis
                    if (selectedYAxisIndex >= 0) {
                        Range axisRange = chart.getYAxisRange(selectedYAxisIndex);
                        double dy1 = y - axisRange.start();
                        double dy2 = pastY - axisRange.start();
                        double zoomFactor =  Math.abs(dy1/dy2);
                        if(zoomFactor > 10) {
                            zoomFactor = 10;
                        }
                        if(zoomFactor < 0.1) {
                            zoomFactor = 0.1;
                        }
                        chart.zoomY(selectedYAxisIndex, zoomFactor);
                        fireChangeEvent();
                    }

                } else { // normal drag we translate x and y axis
                    int dx = pastX - x;
                    int dy = pastY - y;
                    if (selectedYAxisIndex >= 0) {
                        chart.translateY(selectedYAxisIndex, dy);

                    }
                    for (int xAxisIndex : selectedXAxisIndexes) {
                        chart.translateX(xAxisIndex, dx);
                    }
                    fireChangeEvent();
                }
            }
            pastX = x;
            pastY = y;

        }

        @Override
        public void onScroll(int translation, boolean isModified) {

        }
    }

}
