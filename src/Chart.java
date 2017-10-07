import base.chart.BaseChartWithPreview;

import java.awt.*;

/**
 * Created by galafit on 6/10/17.
 */
public class Chart {
    Config config;
    BaseChartWithPreview chartWithPreview;

    public Chart(Config config, int width, int height) {
        this.config = config;
        Rectangle area = new Rectangle(0, 0, width, height);
        if(config.isPreviewEnable()) {
            chartWithPreview = new BaseChartWithPreview(config.getChartConfig(), config.getPreviewConfig(), area, config.getChartWidth());
        } else {
            chartWithPreview = new BaseChartWithPreview(config.getChartConfig(), area);
        }
    }

    public boolean hover(int mouseX, int mouseY) {
        return chartWithPreview.hover(mouseX, mouseY);
    }

    public void moveScroll(int mouseX, int mouseY) {
        chartWithPreview.moveScroll(mouseX, mouseY);
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
