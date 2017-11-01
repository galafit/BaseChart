package base.chart;

import base.DataSet;
import base.Range;
import base.config.ChartConfig;
import base.config.general.Margin;
import base.painters.CrosshairPainter;
import base.painters.TooltipPainter;
import base.tooltips.InfoItem;
import base.tooltips.TooltipInfo;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by galafit on 27/10/17.
 */
public class InteractiveChart implements BaseMouseListener {
    private SimpleChart chart;
    private CrosshairPainter crosshairPainter;
    private TooltipPainter tooltipPainter;
    private boolean isTooltipSeparated = true;
    private TooltipInfo tooltipInfo;

    private ArrayList<ChartEventListener> eventsListeners = new ArrayList<ChartEventListener>();

    public InteractiveChart(ChartConfig chartConfig, Rectangle area) {
        chart = new SimpleChart(chartConfig, area);
        tooltipPainter = new TooltipPainter(chartConfig.getTooltipConfig());
        crosshairPainter = new CrosshairPainter(chartConfig.getCrosshairConfig());
    }

    private TooltipInfo getTooltipInfo() {
        TooltipInfo tooltipInfo = null;
        int number = 0;
        Point xy = null;
        for (int i = 0; i < chart.getTraceAmount(); i++) {
            int hoverIndex = chart.getTraceHoverIndex(i);
            if(hoverIndex >= 0) {
                if(tooltipInfo == null) {
                   tooltipInfo = new TooltipInfo();
                }
                if(number > 0) {
                    tooltipInfo.addItems(new InfoItem("", "", null));
                }
                tooltipInfo.addItems(chart.getDataPointInfo(i, hoverIndex));
                xy = chart.getDataPointPosition(i, hoverIndex);
                tooltipInfo.setXY(xy.x, xy.y);
                number++;
            }
        }
        return tooltipInfo;
    }

    public void setTraceData(DataSet data, int traceIndex) {
        chart.setTraceData(data, traceIndex);
    }

    Margin getMargin(Graphics2D g2) {
       return chart.getMargin(g2);
    }

    void setMargin(Graphics2D g2, Margin margin) {
        chart.setMargin(g2, margin);
    }


    public void draw(Graphics2D g2d) {
        chart.draw(g2d);
        if (tooltipInfo != null) {
            tooltipPainter.draw(g2d, chart.getFullArea(), tooltipInfo);
            crosshairPainter.draw(g2d, chart.getGraphArea(), tooltipInfo.getX(), tooltipInfo.getY());
        }
    }

    public void addEventListener(ChartEventListener eventListener) {
        eventsListeners.add(eventListener);
    }


    @Override
    public void mouseMoved(int mouseX, int mouseY) {
        if (chart.hover(mouseX, mouseY)) {
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
