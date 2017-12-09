import base.*;
import base.config.SimpleChartConfig;
import data.BaseDataSet;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by galafit on 26/11/17.
 */
public class ChartWithDataManager {
    private ChartConfig config;

    private boolean isAutoscaleDuringScroll = true;
    private int minPixPerDataItem = 5;
    private int sumCompression = 1;

    private boolean isAutoScroll = true;
    private ScrollableChart scrollableChart;
    private Rectangle area;
    private ArrayList<DataSet> previewData;
    private ArrayList<DataSet> chartData;

    public ChartWithDataManager(ChartConfig config, Rectangle area) {
        this.area = area;
        this.config = config;


        SimpleChartConfig previewConfig = config.getPreviewConfig();
        SimpleChartConfig chartConfig = config.getChartConfig();
        chartData = new ArrayList<DataSet>();
        for (BaseDataSet data : config.getOriginalChartData()) {
            chartData.add(data);
        }

        previewData = new ArrayList<DataSet>();
        for (BaseDataSet data : config.getOriginalPreviewData()) {
            previewData.add(data);
        }

        // create list of x axis used by some traces
        List<Integer> usedXAxisIndexes = new ArrayList<>();
        for (int i = 0; i < chartConfig.getNumberOfTraces(); i++) {
            int xAxisIndex = chartConfig.getTraceXAxisIndex(i);
            if(!usedXAxisIndexes.contains(xAxisIndex)) {
                usedXAxisIndexes.add(xAxisIndex);
            }
        }

        if(config.getXAxisWithScroll().isEmpty() && previewData.size() > 0) {
            for (Integer xAxisIndex : usedXAxisIndexes) {
                config.addScroll(xAxisIndex, calculateChartExtent(xAxisIndex));
            }
        }


        Range previewMinMax = calculatePreviewInitialMinMax(config.getScrollsExtents());

        if(config.isCropEnable()) { // cropData
            for (Integer xAxisIndex : usedXAxisIndexes) {
                cropChartData(xAxisIndex, new Range(previewMinMax.start(), config.getScrollExtent(xAxisIndex)));
            }
        }

        chartConfig.setData(chartData);
        if(config.isGroupingEnable()) {
            groupData(previewData, config.getCompression());
        }


        previewConfig.setData(previewData);

        scrollableChart = new ScrollableChart(config.getScrollableChartConfig(), area);

        scrollableChart.setPreviewMinMax(previewMinMax);
        scrollableChart.addScrollsListener(new ScrollListener() {
            @Override
            public void onScrollChanged(int axisIndex, double scrollValue, double scrollExtent) {
                if(config.isCropEnable()) {
                    cropChartData(axisIndex, new Range(scrollValue, scrollValue + scrollExtent));
                    scrollableChart.setChartData(chartData);
                    autoscaleChartY();
                }
            }
        });

        if(isAutoScroll) {
            autoScroll();
        }
    }


    public ScrollableChart getChartWithPreview() {
        return scrollableChart;
    }

    /**
     * Prepare and crop chart data
     */
    private void cropChartData(int xAxisIndex, Range scrollExtremes) {
        SimpleChartConfig chartConfig = config.getChartConfig();
        for (int traceIndex = 0; traceIndex < chartConfig.getTraceAmount(); traceIndex++) {
            int traceDataIndex = chartConfig.getTraceDataIndex(traceIndex);
            DataSet subset;
            if(chartConfig.getTraceXAxisIndex(traceIndex) == xAxisIndex) {
                subset = config.getOriginalChartData().get(traceDataIndex).getSubset(scrollExtremes.start(), scrollExtremes.end());
                chartData.set(traceDataIndex, subset);
            }
        }
    }

    private void groupData(ArrayList<DataSet> previewData, int compression) {
        if (compression > 1) {
            for (int i = 0; i < previewData.size(); i++) {
                previewData.set(i, ((BaseDataSet) previewData.get(i)).group(compression));
            }
        }
        if (compression <= 0) { // autoCompression
            for (int i = 0; i < previewData.size(); i++) {
                compression = minPixPerDataItem * previewData.get(i).size() / area.width;
                compression++;
                if (compression > 1) {
                    previewData.set(i, ((BaseDataSet) previewData.get(i)).group(compression));
                }
                sumCompression = sumCompression * compression;

            }
        }
        config.getPreviewConfig().setData(previewData);
    }


    private void autoscaleChartY() {
        if(isAutoscaleDuringScroll) {
            for (int i = 0; i < scrollableChart.getChartNumberOfYAxis(); i++) {
                scrollableChart.autoscaleChartY(i);
            }
        }
    }

    private void autoscalePreviewY() {
        for (int i = 0; i < scrollableChart.getPreviewNumberOfYAxis(); i++) {
            scrollableChart.autoscalePreviewY(i);
        }
    }

    private Range calculatePreviewInitialMinMax(double[] scrollsExtents) {
        Range chartMinMax = null;
        for (BaseDataSet chartData : config.getOriginalChartData()) {
            chartMinMax = Range.max(chartMinMax, chartData.getXExtremes());
        }
        double maxExtent = 0;
        for (double scrollExtent : scrollsExtents) {
           maxExtent = Math.max(maxExtent, scrollExtent);
        }
        double min = chartMinMax.start();
        double maxLength = Math.max(maxExtent, chartMinMax.length());
        chartMinMax = new Range(min, min + maxLength);

        Range previewMinMax = null;
        for (BaseDataSet previewData : config.getOriginalPreviewData()) {
            previewMinMax = Range.max(previewMinMax, previewData.getXExtremes());
        }
        min = previewMinMax.start();
        maxLength = Math.max(calculatePreviewExtent(), previewMinMax.length());
        previewMinMax = new Range(min, min + maxLength);
        previewMinMax = Range.max(previewMinMax, chartMinMax);
        return previewMinMax;
    }


    private double calculateChartExtent(int xAxisIndex) {
        SimpleChartConfig chartConfig = config.getChartConfig();
        List<BaseDataSet> chartData = config.getOriginalChartData();
        double minDataItemInterval = 0;
        for(int traceIndex = 0; traceIndex < chartConfig.getTraceAmount(); traceIndex++) {
            int traceDataIndex = chartConfig.getTraceDataIndex(traceIndex);
            if(chartConfig.getTraceXAxisIndex(traceIndex) == xAxisIndex) {
                DataSet traceData = chartData.get(traceDataIndex);
                if(traceData.size() > 1) {
                    double dataItemInterval = traceData.getXExtremes().length() / (traceData.size() -1);
                    minDataItemInterval = (minDataItemInterval == 0) ? dataItemInterval : Math.min(minDataItemInterval, dataItemInterval);
                }
            }
        }
        double extent = minDataItemInterval * area.width / minPixPerDataItem;
        return extent;
    }

    /**
     * we need this function in the case when preview has a small number of data points
     * (number of data points < previewArea.width)
     * @return preview minMax range for that special case
     */
    private double calculatePreviewExtent() {
        SimpleChartConfig previewConfig = config.getPreviewConfig();
        List<BaseDataSet> previewData = config.getOriginalPreviewData();
        double minDataItemInterval = 0;
        for(int traceIndex = 0; traceIndex < previewConfig.getTraceAmount(); traceIndex++) {
            int traceDataIndex = previewConfig.getTraceDataIndex(traceIndex);
            DataSet traceData = previewData.get(traceDataIndex);
            if(traceData.size() > 1) {
                double dataItemInterval = traceData.getXExtremes().length() / (traceData.size() -1);
                minDataItemInterval = (minDataItemInterval == 0) ? dataItemInterval : Math.min(minDataItemInterval, dataItemInterval);
            }
        }
        double extent = minDataItemInterval * area.width / minPixPerDataItem;
        return extent;
    }

    private Range getChartDataMinMax() {
        Range minMax = null;
        SimpleChartConfig chartConfig = config.getChartConfig();
        List<BaseDataSet> chartData = config.getOriginalChartData();
        for(int traceIndex = 0; traceIndex < chartConfig.getTraceAmount(); traceIndex++) {
            int traceDataIndex = chartConfig.getTraceDataIndex(traceIndex);
            DataSet traceData = chartData.get(traceDataIndex);
            minMax = Range.max(minMax, traceData.getXExtremes());
        }
        return minMax;
    }

    private Range getPreviewDataMinMax() {
        Range minMax = null;
        SimpleChartConfig previewConfig = config.getPreviewConfig();
        List<BaseDataSet> previewData = config.getOriginalPreviewData();
        for(int traceIndex = 0; traceIndex < previewConfig.getTraceAmount(); traceIndex++) {
            int traceDataIndex = previewConfig.getTraceDataIndex(traceIndex);
            DataSet traceData = previewData.get(traceDataIndex);
            minMax = Range.max(minMax, traceData.getXExtremes());
        }
        return minMax;
    }

    public boolean update() {
        if(config.getCompression() <= 0) { // autocompression every update
            groupData(previewData, config.getCompression());
        }

        Range dataMinMax = Range.max(getChartDataMinMax(), getPreviewDataMinMax());
        Range previewMinMax = Range.max(dataMinMax, scrollableChart.getPreviewMinMax());
        scrollableChart.setPreviewMinMax(previewMinMax);
        scrollableChart.setPreviewData(previewData);
        autoscalePreviewY();
        if(isAutoScroll) {
            return autoScroll();
        }
        return false;
    }

    private boolean autoScroll() {
        Range dataMinMax = getChartDataMinMax();
        return scrollableChart.moveScrollsTo(dataMinMax.end());
    }

}
