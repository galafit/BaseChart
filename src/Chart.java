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


    public void moveScroll(int mouseX, int mouseY) {
        chart.moveScroll(mouseX, mouseY);
        Range scrollExtremesBottom = chart.getScrollExtremes(0);
        Range scrollExtremesTop = chart.getScrollExtremes(1);
        cropData(scrollExtremesBottom, scrollExtremesTop);
    }

    public void moveScroll(double scrollValue) {
        chart.moveScroll(scrollValue);
        Range scrollExtremesBottom = chart.getScrollExtremes(0);
        Range scrollExtremesTop = chart.getScrollExtremes(1);
        cropData(scrollExtremesBottom, scrollExtremesTop);
    }

    public void draw(Graphics2D g2d) {
       chart.draw(g2d);
    }

    /**=======================Base methods to interact==========================**/

    public int getSelectedTraceIndex() {
        return chart.getSelectedTraceIndex();
    }

    public int getTraceYAxisIndex(int traceIndex) {
        return chart.getTraceYAxisIndex(traceIndex);
    }

    public int getTraceXAxisIndex(int traceIndex) {
        return chart.getTraceXAxisIndex(traceIndex);
    }


    public List<Integer> getStackYAxisUsedIndexes(int x, int y) {
        return chart.getStackYAxisUsedIndexes(x, y);
    }

    public List<Integer> getStackXAxisUsedIndexes(int x, int y) {
        return chart.getStackXAxisUsedIndexes(x, y);
    }

    public List<Integer> getYAxisUsedIndexes() {
        return chart.getYAxisUsedIndexes();
    }

    public List<Integer> getXAxisUsedIndexes() {
        return chart.getXAxisUsedIndexes();
    }

    public void zoomY(int yAxisIndex, double zoomFactor) {
        chart.zoomY(yAxisIndex, zoomFactor);
    }

    public void zoomX(int xAxisIndex, double zoomFactor) {
        chart.zoomX(xAxisIndex, zoomFactor);
    }

    public void translateY(int yAxisIndex, int dy) {
        chart.translateY(yAxisIndex, dy);
    }

    public void translateX(int xAxisIndex, int dx) {
        chart.translateX(xAxisIndex, dx);
    }

    public void zoomY(int yAxisIndex, int dy) {
        chart.zoomY(yAxisIndex, dy);
    }

    public void zoomX(int xAxisIndex, int dx) {
        chart.zoomX(xAxisIndex, dx);
    }

    public void autoscaleXAxis(int xAxisIndex) {
        chart.autoscaleXAxis(xAxisIndex);
    }

    public void autoscaleYAxis(int yAxisIndex) {
        chart.autoscaleYAxis(yAxisIndex);
    }

    public boolean hoverOff() {
        return chart.hoverOff();
    }

    public boolean hoverOn(int x, int y) {
        return chart.hoverOn(x, y);
    }

}
