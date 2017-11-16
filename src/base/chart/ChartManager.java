package base.chart;
import base.Range;
import base.config.ChartConfig;
import data.BaseDataSet;
import data.GroupedDataSet;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by galafit on 16/11/17.
 */
public class ChartManager {
    private final ChartWithPreview chartWithPreview;
    private Config config;
    private BaseDataSet[] tracesData;
    private BaseDataSet[] previewData;
    private GroupedDataSet[] previewGroupedData;

    public ChartManager(Config config, Rectangle area) {
        this.config = config;
        if(config.isPreviewEnable()) {
            tracesData = new BaseDataSet[config.getChartConfig().getTraceAmount()];
            for (int i = 0; i < config.getChartConfig().getTraceAmount(); i++) {
                tracesData[i] = (BaseDataSet) config.getChartConfig().getTraceData(i);
            }
            previewData = new BaseDataSet[config.getPreviewConfig().getTraceAmount()];
            for (int i = 0; i < config.getPreviewConfig().getTraceAmount(); i++) {
                previewData[i] = (BaseDataSet) config.getPreviewConfig().getTraceData(i);
            }
            chartWithPreview = new ChartWithPreview(config.getChartConfig(), config.getPreviewConfig(), area);
            previewGroupedData = new GroupedDataSet[previewData.length];
            for (int i = 0; i < previewData.length; i++) {
                int compression = previewData[i].size() * 20 / area.width;
                if(compression > 1) {
                    previewGroupedData[i] = new GroupedDataSet(previewData[i], compression);
                    chartWithPreview.setPreviewTraceData(previewGroupedData[i], i);
                }
            }
            chartWithPreview.addScrollListener(new ScrollListener() {
                @Override
                public void onScrollMoved(double scrollValue, double scrollExtent0, double scrollExtent1) {
                    cropData(scrollValue, scrollExtent0, scrollExtent1);
                }
            });
            cropData(chartWithPreview.getScrollValue(), chartWithPreview.getScrollExtent0(), chartWithPreview.getScrollExtent1());

        } else {
            chartWithPreview = new ChartWithPreview(config.getChartConfig(), area);
        }
    }

    private void cropData(double scrollValue, double scrollExtent0, double scrollExtent1) {
        for (int i = 0; i < config.getChartConfig().getTraceAmount(); i++) {
            if(config.getChartConfig().getTraceXAxisIndex(i) == 0) {
                chartWithPreview.setTraceData(tracesData[i].getSubset(scrollValue, scrollValue + scrollExtent0), i);
            } else {
                chartWithPreview.setTraceData(tracesData[i].getSubset(scrollValue, scrollValue + scrollExtent1), i);
            }
        }
    }


    public void draw(Graphics2D g2d) {
        chartWithPreview.draw(g2d);
    }

    public int getChartSelectedTraceIndex() {
        return chartWithPreview.getChartSelectedTraceIndex();
    }

    public Range getChartYRange(int yAxisIndex) {
        return chartWithPreview.getChartYRange(yAxisIndex);
    }

    public int getChartTraceYIndex(int traceIndex) {
        return chartWithPreview.getChartTraceYIndex(traceIndex);
    }

    public int getChartTraceXIndex(int traceIndex) {
        return chartWithPreview.getChartTraceXIndex(traceIndex);
    }

    public int getChartYIndex(int x, int y) {
        return chartWithPreview.getChartYIndex(x, y);
    }

    public java.util.List<Integer> getChartStackXIndexes(int x, int y) {
        return chartWithPreview.getChartStackXIndexes(x, y);
    }

    public List<Integer> getChartYIndexes() {
        return chartWithPreview.getChartYIndexes();
    }

    public List<Integer> getChartXIndexes() {
        return chartWithPreview.getChartXIndexes();
    }

    public void zoomChartY(int yAxisIndex, double zoomFactor) {
        chartWithPreview.zoomChartY(yAxisIndex, zoomFactor);
    }

    public void zoomChartX(int xAxisIndex, double zoomFactor) {
        chartWithPreview.zoomChartX(xAxisIndex, zoomFactor);
    }

    public void translateChartY(int yAxisIndex, int dy) {
        chartWithPreview.translateChartY(yAxisIndex, dy);
    }

    public void translateChartX(int xAxisIndex, int dx) {
        chartWithPreview.translateChartX(xAxisIndex, dx);
    }

    public void autoscaleChartX(int xAxisIndex) {
        chartWithPreview.autoscaleChartX(xAxisIndex);
    }

    public void autoscaleChartY(int yAxisIndex) {
        chartWithPreview.autoscaleChartY(yAxisIndex);
    }

    public boolean chartHoverOff() {
        return chartWithPreview.chartHoverOff();
    }

    public boolean chartHoverOn(int x, int y) {
        return chartWithPreview.chartHoverOn(x, y);
    }

    public boolean isPointInsideChart(int x, int y) {
        return chartWithPreview.isPointInsideChart(x, y);
    }

    public boolean isPointInsideScroll(int x, int y) {
        return chartWithPreview.isPointInsideScroll(x, y);
    }

    public boolean isPointInsidePreview(int x, int y) {
        return chartWithPreview.isPointInsidePreview(x, y);
    }

    public boolean moveScrollTo(int x, int y) {
        return chartWithPreview.moveScrollTo(x, y);
    }

    public boolean translateScroll(int dx) {
        return chartWithPreview.translateScroll(dx);
    }

    public int getPreviewSelectedTraceIndex() {
        return chartWithPreview.getPreviewSelectedTraceIndex();
    }

    public Range getPreviewYRange(int yAxisIndex) {
        return chartWithPreview.getPreviewYRange(yAxisIndex);
    }

    public int getPreviewTraceYIndex(int traceIndex) {
        return chartWithPreview.getPreviewTraceYIndex(traceIndex);
    }

    public int getPreviewYIndex(int x, int y) {
        return chartWithPreview.getPreviewYIndex(x, y);
    }

    public List<Integer> getPreviewYIndexes() {
        return chartWithPreview.getPreviewYIndexes();
    }

    public void zoomPreviewY(int yAxisIndex, double zoomFactor) {
        chartWithPreview.zoomPreviewY(yAxisIndex, zoomFactor);
    }

    public void translatePreviewY(int yAxisIndex, int dy) {
        chartWithPreview.translatePreviewY(yAxisIndex, dy);
    }

    public void autoscalePreviewY(int yAxisIndex) {
        chartWithPreview.autoscalePreviewY(yAxisIndex);
    }
}
