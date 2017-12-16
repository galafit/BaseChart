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
    private int minPixelsPerDataItem = 5;
    private double currentPreviewGroupingInterval = 0;
    private int chartGroupingStep = 2;

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
                    if (config.isGroupingEnable()) {
                        groupChartData(xAxisIndex, scrollExtent);
                    }
                    if (config.isCropEnable()) {
                        cropChartData(xAxisIndex, new Range(scrollValue, scrollValue + scrollExtent));
                    }
                    scrollableChart.setChartData(chartData);
                    autoScaleChartY();
                }
            });
        }
        if (isAutoScroll) {
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
                BaseDataSet traceDataSet = (BaseDataSet) chartData.get(traceDataIndex);
                DataSet subset = traceDataSet.getSubset(scrollExtremes.start(), scrollExtremes.end());
                chartData.set(traceDataIndex, subset);
            }
        }
    }

    private void groupChartData(int xAxisIndex, double scrollExtent) {
        double bestGroupingInterval = minPixelsPerDataItem * scrollExtent / area.width;
        boolean isCachingEnable = true;
        SimpleChartConfig chartConfig = config.getChartConfig();
        for (int traceIndex = 0; traceIndex < chartConfig.getNumberOfTraces(); traceIndex++) {
            int traceDataIndex = chartConfig.getTraceDataIndex(traceIndex);
            if (chartConfig.getTraceXIndex(traceIndex) == xAxisIndex) {
                BaseDataSet traceData = (BaseDataSet) chartData.get(traceIndex);
                double dataInterval = traceData.getAverageDataInterval();
                if (dataInterval > 0) {
                    double numberOfDataItemsToGroup = (bestGroupingInterval / dataInterval);
                    if (numberOfDataItemsToGroup >= chartGroupingStep) {
                        chartData.set(traceIndex, traceData.groupByNumber((int) numberOfDataItemsToGroup, isCachingEnable));
                    }
                    if (numberOfDataItemsToGroup <= 1.0 / chartGroupingStep) {
                        BaseDataSet traceOriginalData = (BaseDataSet) chartOriginalData.get(traceDataIndex);
                        double originalDataInterval = traceOriginalData.getAverageDataInterval();
                        numberOfDataItemsToGroup = (bestGroupingInterval / originalDataInterval);
                        chartData.set(traceIndex, traceOriginalData.groupByNumber((int) numberOfDataItemsToGroup, isCachingEnable));
                    }
                }
            }
        }
    }

    private void groupPreviewData(Range previewMinMax) {
        double bestGroupingInterval = minPixelsPerDataItem * previewMinMax.length() / area.width;
        // choose the first interval in the list >= bestGroupingInterval
        if (currentPreviewGroupingInterval < bestGroupingInterval) {
            for (Double interval : config.getPreviewGroupingIntervals()) {
                currentPreviewGroupingInterval = interval;
                if (interval >= bestGroupingInterval) {
                    break;
                }
            }
        }
        double groupingInterval = currentPreviewGroupingInterval;
        if (groupingInterval <= 0) {
            groupingInterval = bestGroupingInterval;
        }
        boolean isCachingEnable = true;
        for (int i = 0; i < previewData.size(); i++) {
            double dataInterval = previewData.get(i).getAverageDataInterval();
            if (dataInterval > 0) {
                int numberOfDataItemsToGroup = (int) (groupingInterval / dataInterval);
                previewData.set(i, ((BaseDataSet) previewData.get(i)).groupByNumber(numberOfDataItemsToGroup, isCachingEnable));
            }
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

        if (config.getPreviewGroupingIntervals().size() > 0) {
            double groupingInterval = config.getPreviewGroupingIntervals().get(0);
            previewExtent = Math.max(previewExtent, groupingInterval * area.width / minPixelsPerDataItem);
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
                    double dataItemInterval = traceData.getAverageDataInterval();
                    if (dataItemInterval > 0) {
                        dataIntervalMin = (dataIntervalMin == 0) ? dataItemInterval : Math.min(dataIntervalMin, dataItemInterval);
                    }
                }
            }
        }
        double extent = dataIntervalMin * area.width / minPixelsPerDataItem;
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


    public  void update() {
        boolean isAllScrollsAtTheEnd = isAllScrollsAtTheEnd();
        Range previewMinMax = Range.max(scrollableChart.getPreviewXMinMax(), getChartDataMinMax());
        groupPreviewData(previewMinMax);
        scrollableChart.setPreviewMinMax(previewMinMax);
        scrollableChart.setPreviewData(previewData);
        autoScalePreviewY();
        if (isAutoScroll && isAllScrollsAtTheEnd) {
            autoScroll();
        }
    }

    private boolean isAllScrollsAtTheEnd() {
        double previewMax = scrollableChart.getPreviewXMinMax().end();
        for (Integer xAxisIndex : scrollableChart.getXAxisWithScroll()) {
            double scrollEnd = scrollableChart.getScrollValue(xAxisIndex) + scrollableChart.getScrollExtent(xAxisIndex);
            if(previewMax - scrollEnd > 0) {
                return false;
            }
        }
        return true;
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
