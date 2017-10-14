import base.Range;
import base.chart.BaseChartWithPreview;
import data.BaseDataSet;

import java.awt.*;

/**
 * Created by galafit on 6/10/17.
 */
public class Chart {
    Config config;
    BaseChartWithPreview chartWithPreview;
    BaseDataSet[] tracesData;


    public Chart(Config config, int width, int height) {
        this.config = config;
        Rectangle area = new Rectangle(0, 0, width, height);
        if(config.isPreviewEnable()) {
            tracesData = new BaseDataSet[config.getChartConfig().getTraceAmount()];
            for (int i = 0; i < config.getChartConfig().getTraceAmount(); i++) {
                tracesData[i] = (BaseDataSet) config.getChartConfig().getTraceData(i);
            }
            chartWithPreview = new BaseChartWithPreview(config.getChartConfig(), config.getPreviewConfig(), area, config.getChartWidth());
        } else {
            chartWithPreview = new BaseChartWithPreview(config.getChartConfig(), area);
        }
    }

    private void adjustData(Range xExtremes) {
        for (int i = 0; i < config.getChartConfig().getTraceAmount(); i++) {
          chartWithPreview.setTraceData(tracesData[i].getSubset(xExtremes.start(), xExtremes.end()), i);
           // System.out.println(i+" trace: "+ xyData.size());
        }
    }

    public boolean hover(int mouseX, int mouseY) {
        return chartWithPreview.hover(mouseX, mouseY);
    }

    public void moveScroll(int mouseX, int mouseY) {
        chartWithPreview.moveScroll(mouseX, mouseY);
        Range scrollExtremes = chartWithPreview.getScrollExtremes(0);
        adjustData(scrollExtremes);
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
}
