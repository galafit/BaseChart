import base.Range;
import base.chart.BaseChartWithPreview;
import base.chart.BaseMouseListener;
import base.chart.ChangeListener;
import base.chart.ChartEventListener;
import data.BaseDataSet;
import data.GroupedDataSet;

import java.awt.*;

/**
 * Created by galafit on 6/10/17.
 */
public class Chart implements BaseMouseListener {
    Config config;
    BaseChartWithPreview chartWithPreview;
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
            chartWithPreview = new BaseChartWithPreview(config.getChartConfig(), config.getPreviewConfig(), area, config.getChartWidth());
            for (int i = 0; i < previewData.length; i++) {
                int compression = previewData[i].size() * 20 / width;
                if(compression > 1) {
                    GroupedDataSet groupedData = new GroupedDataSet(previewData[i], compression);
                    chartWithPreview.setPreviewTraceData(groupedData, i);
                }
            }
           moveScroll(0);

        } else {
            chartWithPreview = new BaseChartWithPreview(config.getChartConfig(), area);
        }
    }

    private void cropData(Range xExtremesBottom, Range xExtremesTop) {
        for (int i = 0; i < config.getChartConfig().getTraceAmount(); i++) {
            if(config.getChartConfig().getTraceXAxisIndex(i) == 0) {
                chartWithPreview.setTraceData(tracesData[i].getSubset(xExtremesBottom.start(), xExtremesBottom.end()), i);
            } else {
                chartWithPreview.setTraceData(tracesData[i].getSubset(xExtremesTop.start(), xExtremesTop.end()), i);
            }
        }
    }


    public void moveScroll(int mouseX, int mouseY) {
        chartWithPreview.moveScroll(mouseX, mouseY);
        Range scrollExtremesBottom = chartWithPreview.getScrollExtremes(0);
        Range scrollExtremesTop = chartWithPreview.getScrollExtremes(1);
        cropData(scrollExtremesBottom, scrollExtremesTop);
    }

    public void moveScroll(double scrollValue) {
        chartWithPreview.moveScroll(scrollValue);
        Range scrollExtremesBottom = chartWithPreview.getScrollExtremes(0);
        Range scrollExtremesTop = chartWithPreview.getScrollExtremes(1);
        cropData(scrollExtremesBottom, scrollExtremesTop);
    }


    public boolean isMouseInsideScroll(int mouseX, int mouseY) {
       return chartWithPreview.isMouseInsideScroll(mouseX, mouseY);
    }

    public boolean isMouseInsidePreview(int mouseX, int mouseY) {
       return chartWithPreview.isMouseInsidePreview(mouseX, mouseY);
    }

    public void draw(Graphics2D g2d) {
       chartWithPreview.draw(g2d);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {
        chartWithPreview.mouseClicked(mouseX, mouseY);
    }


    @Override
    public void mouseDoubleClicked(int mouseX, int mouseY) {
        chartWithPreview.mouseDoubleClicked(mouseX, mouseY);
    }

    @Override
    public void mouseMoved(int mouseX, int mouseY) {
        chartWithPreview.mouseMoved(mouseX, mouseY);
    }

    @Override
    public void mouseWheelMoved(int mouseX, int mouseY, int wheelRotation) {
        chartWithPreview.mouseWheelMoved(mouseX, mouseY, wheelRotation);
    }

    public void addChangeListener(ChangeListener changeListener) {
        chartWithPreview.addChangeListener(changeListener);
    }
}
