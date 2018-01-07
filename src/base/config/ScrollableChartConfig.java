package base.config;

import base.BColor;
import base.Range;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * Created by galafit on 2/12/17.
 */
public class ScrollableChartConfig {
    public static final int DARK_THEME = 1;
    public static final int LIGHT_THEME = 2;

    private SimpleChartConfig chartConfig;
    private SimpleChartConfig previewConfig;
    private ScrollConfig scrollConfig = new ScrollConfig();
    private Map<Integer, Float> scrollExtents = new Hashtable<Integer, Float>(2);
    private Range previewMinMax;

    public ScrollableChartConfig() {
        this(1);
    }

    public ScrollableChartConfig(int theme) {
        AxisConfig leftAxisConfig = new AxisConfig(AxisOrientation.LEFT);
        AxisConfig rightAxisConfig = new AxisConfig(AxisOrientation.RIGHT);
        leftAxisConfig.setMinMaxRoundingEnable(true);
        leftAxisConfig.setLabelInside(true);
        leftAxisConfig.setTickMarkInsideSize(3);
        leftAxisConfig.setTickMarkOutsideSize(0);

        rightAxisConfig.setLabelInside(true);
        rightAxisConfig.setTickMarkInsideSize(3);
        rightAxisConfig.setTickMarkOutsideSize(0);
        rightAxisConfig.setMinMaxRoundingEnable(true);
        chartConfig = new SimpleChartConfig(leftAxisConfig, rightAxisConfig);
        previewConfig = new SimpleChartConfig(leftAxisConfig, rightAxisConfig);
        chartConfig.setLeftAxisPrimary(false);
        previewConfig.setLeftAxisPrimary(false);
        chartConfig.setBottomAxisPrimary(false);

        if(theme == DARK_THEME) {
            BColor bgColor = new BColor(20, 20, 30);
            BColor marginColor = new BColor(20, 20, 20);
            BColor textColor = new BColor(200, 200, 200);
            chartConfig.setBackground(bgColor);
            chartConfig.setMarginColor(marginColor);
            chartConfig.getLegendConfig().setBackgroundColor(bgColor);
            chartConfig.getLegendConfig().setTextColor(textColor);
            chartConfig.setTitleColor(textColor);

            previewConfig.setBackground(bgColor);
            previewConfig.setMarginColor(marginColor);
            previewConfig.getLegendConfig().setBackgroundColor(bgColor);
            previewConfig.getLegendConfig().setTextColor(textColor);
            previewConfig.setTitleColor(textColor);
        }
    }



    public SimpleChartConfig getChartConfig() {
        return chartConfig;
    }

    public SimpleChartConfig getPreviewConfig() {
        return previewConfig;
    }

    public boolean isPreviewEnable() {
        return ! scrollExtents.isEmpty();
    }

    public void setPreviewMinMax(Range minMax) {
      previewMinMax = minMax;
        for (int i = 0; i < previewConfig.getNumberOfXAxis(); i++) {
            previewConfig.setXMinMax(i, minMax);
        }
    }

    public Range getPreviewMinMax() {
        return previewMinMax;
    }

    public ScrollConfig getScrollConfig() {
        return scrollConfig;
    }

    public Float getScrollExtent(int xAxisIndex) {
        return scrollExtents.get(xAxisIndex);
    }

    public float[] getScrollsExtents() {
        float[] extents = new float[scrollExtents.keySet().size()];
        int i = 0;
        for (Integer xAxisIndex : scrollExtents.keySet()) {
            extents[i] = scrollExtents.get(xAxisIndex);
            i++;
        }
        return extents;
    }

    public void addScroll(int xAxisIndex, float extent) {
        scrollExtents.put(xAxisIndex, extent);
    }

    public Set<Integer> getXAxisWithScroll() {
        return scrollExtents.keySet();
    }

    public void addPreviewStack(int weight) {
        previewConfig.addStack(weight);
    }

    public void addPreviewStack() {
        addPreviewStack(SimpleChartConfig.DEFAULT_WEIGHT);
    }

    public void addChartStack(int weight) {
        chartConfig.addStack(weight);

    }

    public void addChartStack() {
        addChartStack(SimpleChartConfig.DEFAULT_WEIGHT);
    }
}
