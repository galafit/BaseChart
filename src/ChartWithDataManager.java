import base.*;
import base.config.ChartConfig;
import data.BaseDataSet;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by galafit on 26/11/17.
 */
public class ChartWithDataManager {
    private Config config;

    private boolean isAutoscaleDuringScroll = true;
    private int minPixPerDataItem = 5;
    private int sumCompression = 1;

    private boolean isAutoScroll = true;
    private ChartWithPreview chartWithPreview;
    private Rectangle area;
    private ArrayList<DataSet> previewData;

    public ChartWithDataManager(Config config, Rectangle area) {
        this.area = area;
        this.config = config;
        if(isDataManagingEnable()) {

        }
        double scrollExtent0 = config.getScrollExtent(0);
        double scrollExtent1 = config.getScrollExtent(1);
        if(scrollExtent0 <= 0) {
            scrollExtent0 = calculateChartExtent(0);
        }

        if(scrollExtent1 <= 0) {
            scrollExtent1 = calculateChartExtent(1);
        }
        Range previewMinMax = calculatePreviewInitialMinMax(scrollExtent0, scrollExtent1);

        ChartConfig chartConfig = config.getChartConfig();
        chartConfig.setData(createChartData(new Range(previewMinMax.start(), scrollExtent0), new Range(previewMinMax.start(), scrollExtent1)));

        ChartConfig previewConfig = config.getPreviewConfig();
        previewData = new ArrayList<DataSet>();
        for (BaseDataSet data : config.getPreviewData()) {
            previewData.add(data);
        }
        if(isGroupingEnable()) {
            groupData(previewData, config.getCompression());
        }
        previewConfig.setData(previewData);

        chartWithPreview = new ChartWithPreview(chartConfig, previewConfig, config.getScrollConfig(), scrollExtent0, scrollExtent1, area);

        chartWithPreview.setPreviewMinMax(previewMinMax);
        chartWithPreview.addScrollListener(new ScrollListener() {
            @Override
            public void onScrollChanged(double scrollValue, double scrollExtent0, double scrollExtent1) {
                chartWithPreview.setChartData(createChartData(new Range(scrollValue, scrollValue + scrollExtent0), new Range(scrollValue, scrollValue + scrollExtent1)));
                autoscaleChartY();
            }
        });

        if(isAutoScroll) {
            autoScroll();
        }
    }


    public ChartWithPreview getChartWithPreview() {
        return chartWithPreview;
    }

    private boolean isDataManagingEnable() {
        return false;
    }


    private boolean isPreviewEnable() {
        if(config.getPreviewConfig() != null && config.getPreviewConfig().getTraceAmount() > 0) {
            return true;
        }
        return false;
    }

    /**
     * Prepare and crop chart data
     */
    private ArrayList<DataSet> createChartData(Range scrollExtremes0, Range scrollExtremes1) {
        ArrayList<DataSet> chartData = new ArrayList<DataSet>();
        for (BaseDataSet data : config.getChartData()) {
            chartData.add(data);
        }
        if(isCropEnable()) { // cropData
            ChartConfig chartConfig = config.getChartConfig();
            for (int traceIndex = 0; traceIndex < chartConfig.getTraceAmount(); traceIndex++) {
                int traceDataIndex = chartConfig.getTraceDataIndex(traceIndex);
                DataSet subset;
                if(chartConfig.getTraceXAxisIndex(traceIndex) == 0) {
                    subset = config.getChartData().get(traceDataIndex).getSubset(scrollExtremes0.start(), scrollExtremes0.end());
                } else {
                    subset = config.getChartData().get(traceDataIndex).getSubset(scrollExtremes1.start(), scrollExtremes1.end());
                }
                chartData.set(traceDataIndex, subset);
            }
        }
        return chartData;
    }

    private void autoscaleChartY() {
        if(isAutoscaleDuringScroll) {
            for (Integer yAxisIndex : chartWithPreview.getChartYIndexes()) {
                chartWithPreview.autoscaleChartY(yAxisIndex);
            }
        }
    }

    private void autoscalePreviewY() {
        for (Integer yAxisIndex : chartWithPreview.getPreviewYIndexes()) {
            chartWithPreview.autoscalePreviewY(yAxisIndex);
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

    private boolean isCropEnable() {
        return config.isCropEnable();
    }

    private boolean isGroupingEnable() {
        return config.isGroupingEnable();
    }

    private Range calculatePreviewInitialMinMax(double scrollExtent0, double scrollExtent1) {
        Range chartMinMax = null;
        for (BaseDataSet chartData : config.getChartData()) {
            chartMinMax = Range.max(chartMinMax, chartData.getXExtremes());
        }
        double maxExtent = Math.max(scrollExtent0, scrollExtent1);
        double min = chartMinMax.start();
        double maxLength = Math.max(maxExtent, chartMinMax.length());
        chartMinMax = new Range(min, min + maxLength);


        Range previewMinMax = null;
        for (BaseDataSet previewData : config.getPreviewData()) {
            previewMinMax = Range.max(previewMinMax, previewData.getXExtremes());
        }
        min = previewMinMax.start();
        maxLength = Math.max(calculatePreviewExtent(), previewMinMax.length());
        previewMinMax = new Range(min, min + maxLength);
        previewMinMax = Range.max(previewMinMax, chartMinMax);
        return previewMinMax;
    }


    private double calculateChartExtent(int xAxisIndex) {
        ChartConfig chartConfig = config.getChartConfig();
        List<BaseDataSet> chartData = config.getChartData();
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
        ChartConfig previewConfig = config.getPreviewConfig();
        List<BaseDataSet> previewData = config.getPreviewData();
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
        ChartConfig chartConfig = config.getChartConfig();
        List<BaseDataSet> chartData = config.getChartData();
        for(int traceIndex = 0; traceIndex < chartConfig.getTraceAmount(); traceIndex++) {
            int traceDataIndex = chartConfig.getTraceDataIndex(traceIndex);
            DataSet traceData = chartData.get(traceDataIndex);
            minMax = Range.max(minMax, traceData.getXExtremes());
        }
        return minMax;
    }

    private Range getPreviewDataMinMax() {
        Range minMax = null;
        ChartConfig previewConfig = config.getPreviewConfig();
        List<BaseDataSet> previewData = config.getPreviewData();
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
        Range previewMinMax = Range.max(dataMinMax, chartWithPreview.getPreviewMinMax());
        chartWithPreview.setPreviewMinMax(previewMinMax);
        chartWithPreview.setPreviewData(previewData);
        autoscalePreviewY();
        if(isAutoScroll) {
            return autoScroll();
        }
        return false;
    }

    private boolean autoScroll() {
        Range dataMinMax = getChartDataMinMax();
        return chartWithPreview.moveScrollTo(dataMinMax.end());
    }

}
