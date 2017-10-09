import base.Range;
import base.XYData;
import base.chart.BaseChartWithPreview;
import base.config.traces.TraceConfig;
import data.Data;
import data.DataSet;
import data.XYDataSet;

import java.awt.*;

/**
 * Created by galafit on 6/10/17.
 */
public class Chart {
    Config config;
    BaseChartWithPreview chartWithPreview;
    DataSet[] tracesData;


    public Chart(Config config, int width, int height) {
        this.config = config;
        Rectangle area = new Rectangle(0, 0, width, height);
        if(config.isPreviewEnable()) {
            tracesData = new DataSet[config.getChartConfig().getTraceAmount()];
            for (int i = 0; i < config.getChartConfig().getTraceAmount(); i++) {
                Data data = (Data) config.getChartConfig().getTraceConfig(i).getData();
                tracesData[i] = data.getDataSet();
                config.getChartConfig().getTraceConfig(i).setData(data.getCopy());
            }
            chartWithPreview = new BaseChartWithPreview(config.getChartConfig(), config.getPreviewConfig(), area, config.getChartWidth());
        } else {
            chartWithPreview = new BaseChartWithPreview(config.getChartConfig(), area);
        }
    }

    private void adjustData(Range xExtremes) {
        XYData xyData = null;
        for (int i = 0; i < config.getChartConfig().getTraceAmount(); i++) {
            Data data = (Data) config.getChartConfig().getTraceConfig(i).getData();
            data.setDataSet(tracesData[i].getSubset(xExtremes.start(), xExtremes.end()));
            xyData = (XYDataSet) data;
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
