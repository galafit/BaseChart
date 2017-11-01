package base.chart;

import base.DataSet;
import base.config.ChartConfig;
import base.config.ScrollConfig;
import base.config.general.Margin;
import base.Range;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by galafit on 3/10/17.
 */
public class BaseChartWithPreview  implements BaseMouseListener {
    private InteractiveChart chart;
    private SimpleChart preview;
    private ScrollConfig scrollConfig = new ScrollConfig();
    private Rectangle chartArea;
    private Rectangle previewArea;
    private Scroll scroll;
    private ArrayList<ChangeListener> changeListeners = new ArrayList<ChangeListener>();


    public BaseChartWithPreview(ChartConfig chartConfig, Rectangle area) {
        chart = new InteractiveChart(chartConfig, area);
        chart.addEventListener(new EventListener());
    }

    public BaseChartWithPreview(ChartConfig chartConfig, ChartConfig previewConfig, Rectangle area, long chartWidth) {
        int chartWeight = chartConfig.getSumWeight();
        int previewWeight = previewConfig.getSumWeight();

        int chartHeight = area.height * chartWeight / (chartWeight + previewWeight);
        int previewHeight = area.height * previewWeight / (chartWeight + previewWeight);
        chartArea = new Rectangle(area.x, area.y, area.width, chartHeight);
        previewArea = new Rectangle(area.x, area.y + chartHeight, area.width, previewHeight);

        chart = new InteractiveChart(chartConfig, chartArea);
        chart.addEventListener(new EventListener());
        preview = new SimpleChart(previewConfig, previewArea);

        Range minMax = Range.max(chart.getTracesXExtremes(), preview.getTracesXExtremes());
        preview.setBottomAxisExtremes(minMax);
        preview.setTopAxisExtremes(minMax);
        if(chartWidth > 0) {
            double extent = (minMax.end() - minMax.start()) * area.width / chartWidth;
            scroll = new Scroll(scrollConfig, extent, extent, preview.getBottomAxis().getScale());

        } else {
            double extentTop = (minMax.end() - minMax.start()) * area.width / chart.getPreferredTopAxisLength();
            double extentBottom = (minMax.end() - minMax.start()) * area.width / chart.getPreferredBottomAxisLength();
            scroll = new Scroll(scrollConfig, extentBottom, extentTop,  preview.getBottomAxis().getScale());
        }
        chart.setBottomAxisExtremes(getScrollExtremes(0));
        chart.setTopAxisExtremes(getScrollExtremes(1));
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
    public boolean moveScroll(int mouseX, int mouseY) {
        if(!previewArea.contains(mouseX, mouseY)) {
           return false;
        }
        boolean isScrollMoved = scroll.moveScroll(mouseX, mouseY);
        if(isScrollMoved) {
            chart.setBottomAxisExtremes(getScrollExtremes(0));
            chart.setTopAxisExtremes(getScrollExtremes(1));
        }
        return isScrollMoved;
    }

    /**
     * @return true if scrollValue was changed and false if newValue = current scroll value
     */
    public boolean moveScroll(double newValue) {
        boolean isScrollMoved = scroll.moveScroll(newValue);
        if(isScrollMoved) {
            chart.setBottomAxisExtremes(getScrollExtremes(0));
            chart.setTopAxisExtremes(getScrollExtremes(1));
        }
        return isScrollMoved;
    }


    public boolean isMouseInsideScroll(int mouseX, int mouseY) {
        if(preview == null) {
            return false;
        }
        return scroll.isMouseInsideScroll(mouseX, mouseY, preview.getGraphArea());
    }


    public Range getScrollExtremes(int xAxisIndex) {
        if(xAxisIndex == 0) {
            return scroll.getScrollExtremes1();
        }
        return scroll.getScrollExtremes2();
    }

    public void draw(Graphics2D g2d) {
         Margin chartMargin = chart.getMargin(g2d);
         Margin previewMargin = preview.getMargin(g2d);
         if(chartMargin.left() != previewMargin.left() || chartMargin.right() != previewMargin.right()) {
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
            scroll.draw(g2d, preview.getGraphArea());
        }
    }


    @Override
    public void mouseClicked(int mouseX, int mouseY) {
        if(chartArea.contains(mouseX, mouseY)) {
            chart.mouseClicked(mouseX, mouseY);
        } else if(preview != null && previewArea.contains(mouseX, mouseY) && !isMouseInsideScroll(mouseX, mouseY)) {
            moveScroll(mouseX, mouseY);
        }
        for (ChangeListener changeListener : changeListeners) {
            changeListener.update();
        }
    }

    @Override
    public void mouseDoubleClicked(int mouseX, int mouseY) {
        chart.mouseDoubleClicked(mouseX, mouseY);
    }

    @Override
    public void mouseMoved(int mouseX, int mouseY) {
        chart.mouseMoved(mouseX, mouseY);
    }

    @Override
    public void mouseWheelMoved(int mouseX, int mouseY, int wheelRotation) {
        chart.mouseWheelMoved(mouseX, mouseY, wheelRotation);
    }

    public void addChangeListener(ChangeListener changeListener) {
        changeListeners.add(changeListener);
    }

    class EventListener implements  ChartEventListener {
        @Override
        public void hoverChanged() {
            for (ChangeListener changeListener : changeListeners) {
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
    }
}
