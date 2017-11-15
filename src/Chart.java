import base.Range;
import base.chart.ChartWithPreview;
import data.BaseDataSet;
import data.GroupedDataSet;

import java.awt.*;
import java.util.List;

/**
 * Created by galafit on 6/10/17.
 */
public class Chart {
    Config config;
    ChartWithPreview chart;
    BaseDataSet[] tracesData;
    BaseDataSet[] previewData;

    public Chart(Config config, int width, int height) {
        this.config = config;
        Rectangle area = new Rectangle(0, 0, width, height);
        if(config.isPreviewEnable()) {
            tracesData = new BaseDataSet[config.getChartConfig().getTraceAmount()];
            for (int i = 0; i < config.getChartConfig().getTraceAmount(); i++) {
                tracesData[i] = (BaseDataSet) config.getChartConfig().getTraceData(i);
            }
            previewData = new BaseDataSet[config.getPreviewConfig().getTraceAmount()];
            for (int i = 0; i < config.getPreviewConfig().getTraceAmount(); i++) {
                previewData[i] = (BaseDataSet) config.getPreviewConfig().getTraceData(i);
            }
            chart = new ChartWithPreview(config.getChartConfig(), config.getPreviewConfig(), area, config.getChartWidth());
            for (int i = 0; i < previewData.length; i++) {
                int compression = previewData[i].size() * 20 / width;
                if(compression > 1) {
                    GroupedDataSet groupedData = new GroupedDataSet(previewData[i], compression);
                    chart.setPreviewTraceData(groupedData, i);
                }
            }
           moveScroll(0);

        } else {
            chart = new ChartWithPreview(config.getChartConfig(), area);
        }
    }

    private void cropData(Range xExtremesBottom, Range xExtremesTop) {
        for (int i = 0; i < config.getChartConfig().getTraceAmount(); i++) {
            if(config.getChartConfig().getTraceXAxisIndex(i) == 0) {
                chart.setTraceData(tracesData[i].getSubset(xExtremesBottom.start(), xExtremesBottom.end()), i);
            } else {
                chart.setTraceData(tracesData[i].getSubset(xExtremesTop.start(), xExtremesTop.end()), i);
            }
        }
    }

    public void moveScroll(double scrollValue) {
        chart.moveScroll(scrollValue);
        Range scrollExtremesBottom = chart.getScrollExtremes(0);
        Range scrollExtremesTop = chart.getScrollExtremes(1);
       // cropData(scrollExtremesBottom, scrollExtremesTop);
    }

    public void draw(Graphics2D g2d) {
       chart.draw(g2d);
    }

    /**=======================Base methods to interact with chart==========================**/

    public int getChartSelectedTraceIndex() {
        return chart.getChartSelectedTraceIndex();
    }

    public Range getChartYRange(int yAxisIndex) {
        return chart.getChartYRange(yAxisIndex);
    }

    public int getChartTraceYIndex(int traceIndex) {
        return chart.getChartTraceYIndex(traceIndex);
    }

    public int getChartTraceXIndex(int traceIndex) {
        return chart.getChartTraceXIndex(traceIndex);
    }

    public int getChartYIndex(int x, int y) {
        return chart.getChartYIndex(x, y);
    }

    public List<Integer> getChartStackXIndexes(int x, int y) {
        return chart.getChartStackXIndexes(x, y);
    }

    public List<Integer> getChartYIndexes() {
        return chart.getChartYIndexes();
    }

    public List<Integer> getChartXIndexes() {
        return chart.getChartXIndexes();
    }

    public void zoomChartY(int yAxisIndex, double zoomFactor) {
        chart.zoomChartY(yAxisIndex, zoomFactor);
    }

    public void zoomChartX(int xAxisIndex, double zoomFactor) {
        chart.zoomChartX(xAxisIndex, zoomFactor);
    }

    public void translateChartY(int yAxisIndex, int dy) {
        chart.translateChartY(yAxisIndex, dy);
    }

    public void translateChartX(int xAxisIndex, int dx) {
        chart.translateChartX(xAxisIndex, dx);
    }

    public void autoscaleChartX(int xAxisIndex) {
        chart.autoscaleChartX(xAxisIndex);
    }

    public void autoscaleChartY(int yAxisIndex) {
        chart.autoscaleChartY(yAxisIndex);
    }

    public boolean chartHoverOff() {
        return chart.chartHoverOff();
    }

    public boolean chartHoverOn(int x, int y) {
        return chart.chartHoverOn(x, y);
    }
    public boolean isPointInsideChart(int x, int y) {
        return chart.isPointInsideChart(x, y);
    }

    /**=======================Base methods to interact with preview==========================**/
    public boolean isPointInsideScroll(int x, int y) {
       return chart.isPointInsideScroll(x, y);
    }


    public boolean isPointInsidePreview(int x, int y) {
       return chart.isPointInsidePreview(x, y);
    }

    public boolean moveScroll(int x, int y) {
        boolean isMoved =  chart.moveScroll(x, y);
        if(isMoved) {
            Range scrollExtremesBottom = chart.getScrollExtremes(0);
            Range scrollExtremesTop = chart.getScrollExtremes(1);
           // cropData(scrollExtremesBottom, scrollExtremesTop);
        }
      return isMoved;
    }

    public boolean translateScroll(int dx) {
        return chart.translateScroll(dx);
    }

    public int getPreviewSelectedTraceIndex() {
        return chart.getPreviewSelectedTraceIndex();
    }

    public Range getPreviewYRange(int yAxisIndex) {
        return chart.getPreviewYRange(yAxisIndex);
    }

    public int getPreviewTraceYIndex(int traceIndex) {
        return chart.getPreviewTraceYIndex(traceIndex);
    }

    public int getPreviewYIndex(int x, int y) {
        return chart.getPreviewYIndex(x, y);
    }

    public List<Integer> getPreviewYIndexes() {
        return chart.getPreviewYIndexes();
    }

    public void zoomPreviewY(int yAxisIndex, double zoomFactor) {
        chart.zoomPreviewY(yAxisIndex, zoomFactor);
    }

    public void translatePreviewY(int yAxisIndex, int dy) {
        chart.translatePreviewY(yAxisIndex, dy);
    }

    public void autoscalePreviewY(int yAxisIndex) {
        chart.autoscalePreviewY(yAxisIndex);
    }
}
