package base.chart;

import base.DataSet;
import base.config.ChartConfig;
import base.config.ScrollConfig;
import base.config.general.Margin;
import base.Range;
import data.BaseDataSet;
import data.GroupedDataSet;

import java.awt.*;
import java.util.List;


/**
 * Created by galafit on 3/10/17.
 */
public class ChartWithPreview {
    private Config config;
    private BaseDataSet[] chartFullData;
    private BaseDataSet[] previewFullData;
    private BaseDataSet[] previewGroupedData;
    private boolean isAutoscaleDuringScroll = true;

    private SimpleChart chart;
    private SimpleChart preview;
    private ScrollConfig scrollConfig = new ScrollConfig();
    private Rectangle chartArea;
    private Rectangle previewArea;
    private Scroll scroll;

    private int minPixPerDataItem = 5;
    private double topExtent;
    private double bottomExtent;

    public ChartWithPreview(Config config, Rectangle area) {
        this.config = config;
        topExtent = config.getScrollExtent(1);
        bottomExtent = config.getScrollExtent(0);
        int chartWeight = config.getChartConfig().getSumWeight();
        int previewWeight = config.getPreviewConfig().getSumWeight();

        int chartHeight = area.height * chartWeight / (chartWeight + previewWeight);
        int previewHeight = area.height * previewWeight / (chartWeight + previewWeight);
        chartArea = new Rectangle(area.x, area.y, area.width, chartHeight);
        previewArea = new Rectangle(area.x, area.y + chartHeight, area.width, previewHeight);

        chartFullData = new BaseDataSet[config.getChartConfig().getTraceAmount()];
        for (int i = 0; i < config.getChartConfig().getTraceAmount(); i++) {
            chartFullData[i] = (BaseDataSet) config.getChartConfig().getTraceData(i);
        }
        previewFullData = new BaseDataSet[config.getPreviewConfig().getTraceAmount()];
        for (int i = 0; i < config.getPreviewConfig().getTraceAmount(); i++) {
            previewFullData[i] = (BaseDataSet) config.getPreviewConfig().getTraceData(i);
        }

        previewGroupedData = new BaseDataSet[previewFullData.length];
        for (int i = 0; i < previewFullData.length; i++) {
            previewGroupedData[i] = previewFullData[i];
        }
        groupData();
        preview = new SimpleChart(config.getPreviewConfig(), previewArea);
        Range previewMinMax = getPreviewMinMax();
        preview.setXAxisExtremes(0, previewMinMax);
        preview.setXAxisExtremes(1, previewMinMax);

        scroll = new Scroll(scrollConfig, getBottomExtent(), getTopExtent(), preview.getXAxisScale(0));
        scroll.addListener(new ScrollListener() {
            @Override
            public void onScrollChanged(double scrollValue, double scrollExtent0, double scrollExtent1) {
                cropData();
                chart.setXAxisExtremes(0, getScrollExtremes(0));
                chart.setXAxisExtremes(1, getScrollExtremes(1));
            }
        });
        cropData();
        chart = new SimpleChart(config.getChartConfig(), chartArea);
        chart.setXAxisExtremes(0, getScrollExtremes(0));
        chart.setXAxisExtremes(1, getScrollExtremes(1));
    }

    private void cropData() {
        if(config.isCropEnable()) {
            for (int i = 0; i < config.getChartConfig().getTraceAmount(); i++) {
                DataSet subset;
                if(config.getChartConfig().getTraceXAxisIndex(i) == 0) {
                    subset = chartFullData[i].getSubset(scroll.getValue(), scroll.getValue() + scroll.getScrollExtent0());
                } else {
                    subset = chartFullData[i].getSubset(scroll.getValue(), scroll.getValue() + scroll.getScrollExtent0());
                }
                if(chart == null) {
                    config.getChartConfig().setTraceData(subset, i);
                } else {
                    chart.setTraceData(subset, i);
                }
            }
            if(isAutoscaleDuringScroll && chart != null) {
                for (Integer yAxisIndex : getChartYIndexes()) {
                    autoscaleChartY(yAxisIndex);
                }
            }
        }
    }

    private void groupData() {
        if(config.isGroupingEnable()) {
            for (int i = 0; i < previewGroupedData.length; i++) {
                BaseDataSet groupDataSet = previewGroupedData[i];
                int compression = minPixPerDataItem * groupDataSet.size() /  previewArea.width;
                if(compression > 1) {
                    previewGroupedData[i] = new GroupedDataSet(previewGroupedData[i], compression);
                    if(preview == null) {
                        config.getPreviewConfig().setTraceData(previewGroupedData[i], i);
                    } else {
                        preview.setTraceData(previewGroupedData[i], i);
                    }
                }
            }
        }
    }

    private double calculateChartExtent(int xAxisIndex) {
        ChartConfig chartConfig = config.getChartConfig();
        double minDataItemInterval = 0;
        for(int i = 0; i < chartConfig.getTraceAmount(); i++) {
            if(chartConfig.getTraceXAxisIndex(i) == xAxisIndex) {
                DataSet traceData = chartFullData[i];
                if(traceData.size() > 1) {
                    double dataItemInterval = traceData.getXExtremes().length() / (traceData.size() -1);
                    minDataItemInterval = (minDataItemInterval == 0) ? dataItemInterval : Math.min(minDataItemInterval, dataItemInterval);
                }
            }
        }
        double extent = minDataItemInterval * chartArea.width / minPixPerDataItem;
        return extent;
    }

    private double calculatePreviewExtent() {
        ChartConfig previewConfig = config.getPreviewConfig();
        double minDataItemInterval = 0;
        for(int i = 0; i < previewConfig.getTraceAmount(); i++) {
            DataSet traceData = previewGroupedData[i];
            if(traceData.size() > 1) {
                double dataItemInterval = traceData.getXExtremes().length() / (traceData.size() -1);
                minDataItemInterval = (minDataItemInterval == 0) ? dataItemInterval : Math.min(minDataItemInterval, dataItemInterval);
            }
        }
        double extent = minDataItemInterval * chartArea.width / minPixPerDataItem;
        return extent;
    }


    private Range getPreviewMinMax() {
        Range chartMinMax = null;
        for (BaseDataSet traceData : chartFullData) {
            chartMinMax = Range.max(chartMinMax, traceData.getXExtremes());
        }
        double maxExtent = Math.max(getTopExtent(), getBottomExtent());
        double min = chartMinMax.start();
        double maxLength = Math.max(maxExtent, chartMinMax.length());
        chartMinMax = new Range(min, min + maxLength);


        Range previewMinMax = null;
        for (BaseDataSet traceData : chartFullData) {
            previewMinMax = Range.max(previewMinMax, traceData.getXExtremes());
        }
        min = previewMinMax.start();
        maxLength = Math.max(calculatePreviewExtent(), previewMinMax.length());
        previewMinMax = new Range(min, min + maxLength);
        previewMinMax = Range.max(previewMinMax, chartMinMax);
        return previewMinMax;
    }


    private double getBottomExtent() {
        if(bottomExtent == 0) {
            bottomExtent = calculateChartExtent(0);
        }
        return bottomExtent;
    }

    private double getTopExtent() {
        if(topExtent == 0) {
            topExtent = calculateChartExtent(1);
        }
        return topExtent;
    }


    public void setTraceData(DataSet data, int traceIndex) {
        chart.setTraceData(data, traceIndex);
    }

    public void setPreviewTraceData(DataSet data, int traceIndex) {
        preview.setTraceData(data, traceIndex);
    }

    /**
     * @return true if scrollValue was changed and false if newValue = current scroll value
     */
    public boolean moveScrollTo(double newValue) {
        return scroll.moveScrollTo(newValue);
    }

    private void translateChart(int dx) {
        double scrollTranslation = dx / scroll.getRation();
        scroll.translateScroll(scrollTranslation);
    }


    public Range getScrollExtremes(int xAxisIndex) {
        if (xAxisIndex == 0) {
            return scroll.getScrollExtremes1();
        }
        return scroll.getScrollExtremes2();
    }

    public void draw(Graphics2D g2d) {
        Margin chartMargin = chart.getMargin(g2d);
        Margin previewMargin = preview.getMargin(g2d);
        if (chartMargin.left() != previewMargin.left() || chartMargin.right() != previewMargin.right()) {
            int left = Math.max(chartMargin.left(), previewMargin.left());
            int right = Math.max(chartMargin.right(), previewMargin.right());
            chartMargin = new Margin(chartMargin.top(), right, chartMargin.bottom(), left);
            previewMargin = new Margin(previewMargin.top(), right, previewMargin.bottom(), left);
            chart.setMargin(g2d, chartMargin);
            preview.setMargin(g2d, previewMargin);
        }

        chart.draw(g2d);
        if (preview != null) {
            preview.draw(g2d);
            scroll.draw(g2d, preview.getGraphArea(g2d));
        }
    }

    /**
     * =======================Base methods to interact with chart==========================
     **/

    public int getChartSelectedTraceIndex() {
        return chart.getSelectedTraceIndex();
    }

    public Range getChartYRange(int yAxisIndex) {
        return chart.getYAxisRange(yAxisIndex);
    }

    public int getChartTraceYIndex(int traceIndex) {
        return chart.getTraceYAxisIndex(traceIndex);
    }

    public int getChartTraceXIndex(int traceIndex) {
        return chart.getTraceXAxisIndex(traceIndex);
    }

    public int getChartYIndex(int x, int y) {
        return chart.getYAxisIndex(x, y);
    }

    public List<Integer> getChartStackXIndexes(int x, int y) {
        return chart.getStackXAxisIndexes(x, y);
    }

    public List<Integer> getChartYIndexes() {
        return chart.getYAxisIndexes();
    }

    public List<Integer> getChartXIndexes() {
        return chart.getXAxisIndexes();
    }

    public void zoomChartY(int yAxisIndex, double zoomFactor) {
        chart.zoomY(yAxisIndex, zoomFactor);
    }

    public void zoomChartX(int xAxisIndex, double zoomFactor) {
        chart.zoomX(xAxisIndex, zoomFactor);
        if (preview != null) {
            chart.zoomX(xAxisIndex, zoomFactor);
            Range minMax = Range.max(chart.getXAxisMinMax(0), chart.getXAxisMinMax(1));
            minMax = Range.max(minMax, preview.getXAxisMinMax(0));
            preview.setXAxisExtremes(0, minMax);
            preview.setXAxisExtremes(1, minMax);
            if(xAxisIndex == 0) {
                scroll.setScrollExtent0(chart.getXAxisMinMax(xAxisIndex).length());
            }
            if(xAxisIndex == 1) {
                scroll.setScrollExtent1(chart.getXAxisMinMax(xAxisIndex).length());
            }
        }
    }

    public void translateChartY(int yAxisIndex, int dy) {
        chart.translateY(yAxisIndex, dy);
    }

    public void translateChartX(int xAxisIndex, int dx) {
        if (preview == null) {
            chart.translateX(xAxisIndex, dx);
        } else {
            translateChart(dx);
        }
    }

    public void autoscaleChartX(int xAxisIndex) {
        if (preview == null) {
            chart.autoscaleXAxis(xAxisIndex);
        } else {
          /*  scroll.setScrollExtent0(getBottomExtent());
            scroll.setScrollExtent1(getTopExtent());
          */
        }

    }

    public void autoscaleChartY(int yAxisIndex) {
        chart.autoscaleYAxis(yAxisIndex);
    }

    public boolean chartHoverOff() {
        return chart.hoverOff();
    }

    public boolean chartHoverOn(int x, int y) {
        return chart.hoverOn(x, y);
    }

    public boolean isPointInsideChart(int x, int y) {
        return chartArea.contains(x, y);
    }

    /**
     * =======================Base methods to interact with preview==========================
     **/
    public boolean isPointInsideScroll(int x, int y) {
        if (preview != null && previewArea.contains(x, y) && scroll.isPointInsideScroll(x)) {
            return true;
        }
        return false;
    }


    public boolean isPointInsidePreview(int x, int y) {
        if (preview == null) {
            return false;
        }
        return previewArea.contains(x, y);
    }

    /**
     * @return true if scrollValue was changed and false if newValue = current scroll value
     */
    public boolean moveScrollTo(int x, int y) {
        if (!previewArea.contains(x, y)) {
            return false;
        }
        return scroll.moveScrollTo(x, y);
    }

    public boolean translateScroll(int dx) {
        return scroll.translateScroll(dx);
    }

    public int getPreviewSelectedTraceIndex() {
        return preview.getSelectedTraceIndex();
    }

    public Range getPreviewYRange(int yAxisIndex) {
        return preview.getYAxisRange(yAxisIndex);
    }

    public int getPreviewTraceYIndex(int traceIndex) {
        return preview.getTraceYAxisIndex(traceIndex);
    }

    public int getPreviewYIndex(int x, int y) {
        return preview.getYAxisIndex(x, y);
    }

    public List<Integer> getPreviewYIndexes() {
        return preview.getYAxisIndexes();
    }

    public void zoomPreviewY(int yAxisIndex, double zoomFactor) {
        preview.zoomY(yAxisIndex, zoomFactor);
    }

    public void translatePreviewY(int yAxisIndex, int dy) {
        preview.translateY(yAxisIndex, dy);
    }

    public void autoscalePreviewY(int yAxisIndex) {
        preview.autoscaleYAxis(yAxisIndex);
    }




/*    @Override
    public void onClick(int mouseX, int mouseY) {
        if(chartArea.contains(mouseX, mouseY)) {
            chart.onClick(mouseX, mouseY);
        } else if(preview != null && previewArea.contains(mouseX, mouseY) && !isPointInsideScroll(mouseX, mouseY)) {
            moveScrollTo(mouseX, mouseY);
        }
        for (ScrollListener changeListener : chartEventListeners) {
           // changeListener.update();
        }
    }

    @Override
    public void onDoubleClick(int mouseX, int mouseY) {
        chart.onDoubleClick(mouseX, mouseY);
    }

    @Override
    public void mouseMoved(int mouseX, int mouseY) {
        chart.mouseMoved(mouseX, mouseY);
    }

    @Override
    public void onScroll(int mouseX, int mouseY, int wheelRotation) {
        chart.mouseWheelMoved(mouseX, mouseY, wheelRotation);
    }


    class EventListener implements  ScrollListener {
        @Override
        public void hoverChanged() {
            for (ScrollListener changeListener : chartEventListeners) {
                changeListener.update();
            }
        }

        @Override
        public void xAxisActionPerformed(int xAxisIndex, int actionDirection) {
            System.out.println(xAxisIndex+" X AxisAction "+ actionDirection);
        }

        @Override
        public void yAxisActionPerformed(int yAxisIndex, int actionDirection) {
            System.out.println(yAxisIndex+" Y AxisAction "+ actionDirection);
        }

        @Override
        public void yAxisResetActionPerformed(int yAxisIndex) {
            System.out.println(yAxisIndex+" Y AxisReset ");
        }

        @Override
        public void xAxisResetActionPerformed(int xAxisIndex) {
            System.out.println(xAxisIndex+" X AxisReset ");
        }
    }*/
}
