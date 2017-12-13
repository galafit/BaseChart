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

    private boolean isAutoScaleDuringScroll = true;
    private int minPixPerDataItem = 5;
    private double currentGroupingInterval =
            0;

    private boolean isAutoScroll = true;
    private ScrollableChart scrollableChart;
    private Rectangle area;

    private List<DataSet> previewOriginalData;
    private List<DataSet> chartOriginalData;

    private ArrayList<DataSet> previewData;
    private ArrayList<DataSet> chartData;

    public ChartWithDataManager(ChartConfig config, Rectangle area) {
        this.area = area;
        this.config = config;

        SimpleChartConfig previewConfig = config.getPreviewConfig();
        SimpleChartConfig chartConfig = config.getChartConfig();

        // create list of x axis used by some traces
        List<Integer> usedXAxisIndexes = new ArrayList<>();
        for (int i = 0; i < chartConfig.getNumberOfTraces(); i++) {
            int xAxisIndex = chartConfig.getTraceXIndex(i);
            if (!usedXAxisIndexes.contains(xAxisIndex)) {
                usedXAxisIndexes.add(xAxisIndex);
            }
        }

        if (!config.isPreviewEnable() && previewConfig.getNumberOfTraces() > 0) {
            for (Integer xAxisIndex : usedXAxisIndexes) {
                config.addScroll(xAxisIndex, calculateChartExtent(xAxisIndex));
            }
        }
        chartOriginalData = chartConfig.getData();

        if (config.isPreviewEnable()) {
            previewOriginalData = previewConfig.getData();

            chartData = new ArrayList<DataSet>();
            for (DataSet data : chartOriginalData) {
                chartData.add(data);
            }

            previewData = new ArrayList<DataSet>();
            for (DataSet data : previewOriginalData) {
                previewData.add(data);
            }

            Range previewMinMax = config.getPreviewMinMax();
            if (previewMinMax == null) {
                previewMinMax = calculateInitialPreviewMinMax(config.getScrollsExtents());
            }
            config.setPreviewMinMax(previewMinMax);

            if (config.isCropEnable()) { // cropData
                for (int i = 0; i < config.getChartConfig().getNumberOfXAxis(); i++) {
                    Double scrollExtent = config.getScrollExtent(i);
                    if (scrollExtent != null) {
                        cropChartData(i, new Range(previewMinMax.start(), scrollExtent));
                    }
                }
            }

            chartConfig.setData(chartData);
            if (config.isGroupingEnable()) {
                groupPreviewData(previewMinMax);
            }
            previewConfig.setData(previewData);

        }

        scrollableChart = new ScrollableChart(config, area);
        for (Integer xAxisIndex : scrollableChart.getXAxisWithScroll()) {
            scrollableChart.addScrollListener(xAxisIndex, new ScrollListener() {
                @Override
                public void onScrollChanged(double scrollValue, double scrollExtent) {
                    if (config.isCropEnable()) {
                        cropChartData(xAxisIndex, new Range(scrollValue, scrollValue + scrollExtent));
                        scrollableChart.setChartData(chartData);
                        autoScaleChartY();
                    }
                }
            });
        }
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
        for (int traceIndex = 0; traceIndex < chartConfig.getNumberOfTraces(); traceIndex++) {
            int traceDataIndex = chartConfig.getTraceDataIndex(traceIndex);
            if (chartConfig.getTraceXIndex(traceIndex) == xAxisIndex) {
                BaseDataSet fullDataSet = (BaseDataSet) chartOriginalData.get(traceDataIndex);
                DataSet subset = fullDataSet.getSubset(scrollExtremes.start(), scrollExtremes.end());
                chartData.set(traceDataIndex, subset);
            }
        }
    }

    private void groupPreviewData(Range previewMinMax) {
        double bestGroupingInterval = minPixPerDataItem * previewMinMax.length() / area.width;
        // choose the first interval in the list >= bestGroupingInterval
        if (currentGroupingInterval < bestGroupingInterval) {
            for (Double interval : config.getGroupingIntervals()) {
                currentGroupingInterval = interval;
                if (interval >= bestGroupingInterval) {
                    break;
                }
            }
        }
        double groupingInterval = currentGroupingInterval;
        if (groupingInterval <= 0) {
            groupingInterval = bestGroupingInterval;
        }
        for (int i = 0; i < previewData.size(); i++) {
            previewData.set(i, ((BaseDataSet) previewData.get(i)).groupByInterval(groupingInterval));
        }

    }

    private void autoScaleChartY() {
        if (isAutoScaleDuringScroll) {
            for (int i = 0; i < scrollableChart.getChartNumberOfYAxis(); i++) {
                scrollableChart.autoScaleChartY(i);
            }
        }
    }

    private void autoScalePreviewY() {
        for (int i = 0; i < scrollableChart.getPreviewNumberOfYAxis(); i++) {
            scrollableChart.autoScalePreviewY(i);
        }

    }

    private Range calculateInitialPreviewMinMax(double[] scrollsExtents) {
        Range chartFullMinMax = null;
        for (DataSet chartData : chartOriginalData) {
            chartFullMinMax = Range.max(chartFullMinMax, chartData.getXExtremes());
        }

        // in the case if chart has a small number of data points
        // (number of data points < area.width)
        double previewExtent = 0;
        for (double scrollExtent : scrollsExtents) {
            previewExtent = Math.max(previewExtent, scrollExtent);
        }

        if (config.getGroupingIntervals().size() > 0) {
            double groupingInterval = config.getGroupingIntervals().get(0);
            previewExtent = Math.max(previewExtent, groupingInterval * area.width / minPixPerDataItem);
        }

        double min = chartFullMinMax.start();
        double maxLength = Math.max(previewExtent, chartFullMinMax.length());
        chartFullMinMax = new Range(min, min + maxLength);

        return chartFullMinMax;
    }


    private double calculateChartExtent(int xAxisIndex) {
        SimpleChartConfig chartConfig = config.getChartConfig();
        double dataIntervalMin = 0;
        for (int traceIndex = 0; traceIndex < chartConfig.getNumberOfTraces(); traceIndex++) {
            int traceDataIndex = chartConfig.getTraceDataIndex(traceIndex);
            if (chartConfig.getTraceXIndex(traceIndex) == xAxisIndex) {
                DataSet traceData = chartConfig.getData().get(traceDataIndex);
                if (traceData.size() > 1) {
                    double dataItemInterval = traceData.averageDataInterval();
                    if (dataItemInterval > 0) {
                        dataIntervalMin = (dataIntervalMin == 0) ? dataItemInterval : Math.min(dataIntervalMin, dataItemInterval);
                    }
                }
            }
        }
        double extent = dataIntervalMin * area.width / minPixPerDataItem;
        return extent;
    }

    private Range getChartDataMinMax() {
        Range minMax = null;
        SimpleChartConfig chartConfig = config.getChartConfig();
        for (int traceIndex = 0; traceIndex < chartConfig.getNumberOfTraces(); traceIndex++) {
            int traceDataIndex = chartConfig.getTraceDataIndex(traceIndex);
            DataSet traceData = chartOriginalData.get(traceDataIndex);
            minMax = Range.max(minMax, traceData.getXExtremes());
        }
        return minMax;
    }


    public boolean update() {
        Range previewMinMax = Range.max(scrollableChart.getPreviewMinMax(), getChartDataMinMax());
        groupPreviewData(previewMinMax);
        scrollableChart.setPreviewMinMax(previewMinMax);
        scrollableChart.setPreviewData(previewData);
        autoScalePreviewY();
        if (isAutoScroll) {
            return autoScroll();
        }
        return false;
    }

    private boolean autoScroll() {
        Range dataMinMax = getChartDataMinMax();
        double minExtent = 0;
        for (Integer xAxisIndex : scrollableChart.getXAxisWithScroll()) {
            minExtent = (minExtent == 0) ? scrollableChart.getScrollExtent(xAxisIndex) : Math.min(minExtent, scrollableChart.getScrollExtent(xAxisIndex));
        }
        return scrollableChart.setScrollsValue(dataMinMax.end() - minExtent);
    }
}
