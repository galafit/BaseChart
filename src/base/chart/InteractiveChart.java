package base.chart;

import base.DataSet;
import base.Range;
import base.config.ChartConfig;
import base.config.general.Margin;
import base.painters.CrosshairPainter;
import base.painters.LegendPainter;
import base.painters.TitlePainter;
import base.painters.TooltipPainter;
import base.scales.Scale;
import base.tooltips.TooltipInfo;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by galafit on 27/10/17.
 */
public class InteractiveChart implements BaseMouseListener {
    private SimpleChart chart;

    private LegendPainter legendPainter;
    private TitlePainter titlePainter;
    private Rectangle titleArea;
    private Rectangle legendArea;
    private Rectangle fullArea;
    private ChartConfig chartConfig;
    private boolean isDirty = true;


    private CrosshairPainter crosshairPainter;
    private TooltipPainter tooltipPainter;
    private TooltipInfo tooltipInfo;
    private int selectedTraceIndex = 0;
    private int hoverIndex = -1;

    private ArrayList<ChartEventListener> eventsListeners = new ArrayList<ChartEventListener>();

    public InteractiveChart(ChartConfig chartConfig, Rectangle area) {
        fullArea = area;
        this.chartConfig = chartConfig;
        chart = new SimpleChart(chartConfig);
        tooltipPainter = new TooltipPainter(chartConfig.getTooltipConfig());
        crosshairPainter = new CrosshairPainter(chartConfig.getCrosshairConfig());
        titlePainter = new TitlePainter(chartConfig.getTitle(), chartConfig.getTitleTextStyle());
        legendPainter = new LegendPainter(chart.getTracesInfo(), chartConfig.getLegendConfig());
    }

    private void hoverOff() {
        hoverIndex = -1;
    }

    private boolean hoverOn(int mouseX, int mouseY) {
        if(selectedTraceIndex < 0) {
            return false;
        }
        int nearestIndex = chart.getData(selectedTraceIndex).findNearestData(chart.xPositionToValue(selectedTraceIndex, mouseX));
        if(hoverIndex != nearestIndex) {
            hoverIndex = nearestIndex;
            return true;
        }
        return false;
    }

    private TooltipInfo getTooltipInfo() {
        TooltipInfo tooltipInfo = null;
        if(selectedTraceIndex >= 0 && hoverIndex >= 0) {
            tooltipInfo = new TooltipInfo();
            tooltipInfo.addItems(chart.getDataInfo(selectedTraceIndex, hoverIndex));
            Point dataPosition = chart.getDataPosition(selectedTraceIndex, hoverIndex);
            tooltipInfo.setXY(dataPosition.x, dataPosition.y);
        }
        return tooltipInfo;
    }

    public void setTraceData(DataSet data, int traceIndex) {
        chart.setTraceData(data, traceIndex);
    }

    Margin getMargin(Graphics2D g2) {
        if(isDirty) {
            calculateAreas(g2);
        }
       return chart.getMargin(g2);
    }

    void setMargin(Graphics2D g2, Margin margin) {
        if(isDirty) {
            calculateAreas(g2);
        }
        chart.setMargin(g2, margin);
    }

    Rectangle getGraphArea() {
        return chart.getGraphArea();
    }

    Scale getBottomScale() {
        return chart.getBottomAxis().getScale();
    }

    public void setArea(Rectangle area, Graphics2D g2) {
        fullArea = area;
        isDirty = true;
    }

    private void calculateAreas(Graphics2D g2) {
        int titleHeight = titlePainter.getTitleHeight(g2, fullArea.width);
        int legendHeight = legendPainter.getLegendHeight(g2, fullArea.width);

        if (chartConfig.getLegendConfig().isTop()) {

        }
        titleArea = new Rectangle(fullArea.x, fullArea.y, fullArea.width, titleHeight);
        Rectangle chartArea;
        if (chartConfig.getLegendConfig().isTop()) {
            legendArea = new Rectangle(fullArea.x, fullArea.y + titleHeight, fullArea.width, legendHeight);
             chartArea = new Rectangle(fullArea.x, fullArea.y + titleHeight + legendHeight, fullArea.width, fullArea.height - titleHeight - legendHeight);
        } else {
            legendArea = new Rectangle(fullArea.x, fullArea.y + fullArea.height - legendHeight, fullArea.width, legendHeight);
            chartArea = new Rectangle(fullArea.x, fullArea.y + titleHeight, fullArea.width, fullArea.height - titleHeight - legendHeight);
        }
        chart.setArea(chartArea);
        isDirty = false;
    }

    public void draw(Graphics2D g2) {
        if(isDirty) {
            calculateAreas(g2);
        }

        g2.setColor(chartConfig.getMarginColor());
        g2.fill(fullArea);

        chart.draw(g2);
        if (tooltipInfo != null) {
            tooltipPainter.draw(g2, chart.getFullArea(), tooltipInfo);
            crosshairPainter.draw(g2, chart.getGraphArea(), tooltipInfo.getX(), tooltipInfo.getY());
        }
        titlePainter.draw(g2, titleArea);
        legendPainter.draw(g2, legendArea);
    }

    public void addEventListener(ChartEventListener eventListener) {
        eventsListeners.add(eventListener);
    }

    @Override
    public void mouseMoved(int mouseX, int mouseY) {
        if (hoverOn(mouseX, mouseY)) {
            tooltipInfo = getTooltipInfo();
            for (ChartEventListener listener : eventsListeners) {
                listener.hoverChanged();
            }
        }
    }

    int getPreferredTopAxisLength() {
        return chart.getPreferredTopAxisLength();
    }

    int getPreferredBottomAxisLength() {
        return chart.getPreferredBottomAxisLength();
    }

    Range getTracesXExtremes() {
        return chart.getTracesXExtremes();
    }

    void setTopAxisExtremes(Range minMax) {
       chart.setTopAxisExtremes(minMax);
    }

    void setBottomAxisExtremes(Range minMax) {
        chart.setBottomAxisExtremes(minMax);
    }

    @Override
    public void mouseDoubleClicked(int mouseX, int mouseY) {
     /*   if (titleArea.contains(mouseX, mouseY) || legendArea.contains(mouseX, mouseY)) {
            return;
        }
        Rectangle topAxisArea = new Rectangle(graphArea.x, graphArea.y - margin.top(), graphArea.width, margin.top());
        if (topAxisArea.contains(mouseX, mouseY)) {
            for (ChartEventListener listener : eventsListeners) {
                listener.xAxisResetActionPerformed(1);
            }
        }
        Rectangle bottomAxisArea = new Rectangle(graphArea.x, graphArea.y + graphArea.height, graphArea.width, margin.bottom());
        if (bottomAxisArea.contains(mouseX, mouseY)) {
            for (ChartEventListener listener : eventsListeners) {
                listener.xAxisResetActionPerformed(0);
            }
        }

        Rectangle leftArea = new Rectangle(graphArea.x, graphArea.y, graphArea.width / 2, graphArea.height);
        Rectangle rightArea = new Rectangle(graphArea.x + graphArea.width / 2, graphArea.y, graphArea.width / 2, graphArea.height);
        if (leftArea.contains(mouseX, mouseY)) {
            for (int i = 0; i < yAxisList.size() / 2; i++) {
                Axis yAxis = yAxisList.get(2 * i);
                // for yAxis Start > End
                if (yAxis.getEnd() <= mouseY && yAxis.getStart() >= mouseY) {
                    for (ChartEventListener listener : eventsListeners) {
                        listener.yAxisResetActionPerformed(2*i);
                    }
                    break;
                }
            }
        }
        if (rightArea.contains(mouseX, mouseY)) {
            for (int i = 0; i < yAxisList.size() / 2; i++) {
                Axis yAxis = yAxisList.get(2 * i + 1);
                // for yAxis Start > End
                if (yAxis.getEnd() <= mouseY && yAxis.getStart() >= mouseY) {
                    for (ChartEventListener listener : eventsListeners) {
                        listener.yAxisResetActionPerformed(2*i + 1);
                    }
                    break;
                }
            }
        }*/


    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {
      /*  if (titleArea.contains(mouseX, mouseY) || legendArea.contains(mouseX, mouseY)) {
            return;
        }
        Rectangle topAxisStartArea = new Rectangle(graphArea.x, graphArea.y - margin.top(), graphArea.width / 2, margin.top());
        if (topAxisStartArea.contains(mouseX, mouseY)) {
            for (ChartEventListener listener : eventsListeners) {
                listener.xAxisActionPerformed(1, -1);
            }
        }
        Rectangle topAxisEndArea = new Rectangle(graphArea.x + graphArea.width / 2, graphArea.y - margin.top(), graphArea.width / 2, margin.top());
        if (topAxisEndArea.contains(mouseX, mouseY)) {
            for (ChartEventListener listener : eventsListeners) {
                listener.xAxisActionPerformed(1, 1);
            }
        }
        Rectangle bottomAxisStartArea = new Rectangle(graphArea.x, graphArea.y + graphArea.height, graphArea.width / 2, margin.bottom());
        if (bottomAxisStartArea.contains(mouseX, mouseY)) {
            for (ChartEventListener listener : eventsListeners) {
                listener.xAxisActionPerformed(0, -1);
            }
        }
        Rectangle bottomAxisEndArea = new Rectangle(graphArea.x + graphArea.width / 2, graphArea.y + graphArea.height, graphArea.width / 2, margin.bottom());
        if (bottomAxisEndArea.contains(mouseX, mouseY)) {
            for (ChartEventListener listener : eventsListeners) {
                listener.xAxisActionPerformed(0, 1);
            }
        }*/
    }

    @Override
    public void mouseWheelMoved(int mouseX, int mouseY, int wheelRotation) {
      /*  Rectangle leftArea = new Rectangle(graphArea.x, graphArea.y, graphArea.width / 2, graphArea.height);
        Rectangle rightArea = new Rectangle(graphArea.x + graphArea.width / 2, graphArea.y, graphArea.width / 2, graphArea.height);

        if (leftArea.contains(mouseX, mouseY)) {
            for (int i = 0; i < yAxisList.size() / 2; i++) {
                Axis yAxis = yAxisList.get(2 * i);
                // for yAxis Start > End
                if (yAxis.getEnd() <= mouseY && yAxis.getStart() >= mouseY) {
                    for (ChartEventListener listener : eventsListeners) {
                        listener.yAxisActionPerformed(2*i, wheelRotation);
                    }
                    break;
                }
            }
        }
        if (rightArea.contains(mouseX, mouseY)) {
            for (int i = 0; i < yAxisList.size() / 2; i++) {
                Axis yAxis = yAxisList.get(2 * i + 1);
                // for yAxis Start > End
                if (yAxis.getEnd() <= mouseY && yAxis.getStart() >= mouseY) {
                    for (ChartEventListener listener : eventsListeners) {
                        listener.yAxisActionPerformed(2*i + 1, wheelRotation);
                    }
                    break;
                }
            }
        }*/
    }
}
