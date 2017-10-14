package base.traces;


import base.DataSet;
import base.Range;
import base.config.traces.BaseTraceConfig;
import base.legend.LegendItem;
import base.tooltips.TooltipItem;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

/**
 * Created by galafit on 11/10/17.
 */
public abstract class BaseTrace extends Trace {
    BaseTraceConfig traceConfig;
    XYMapper data;

    @Override
    public void setData(DataSet dataSet) {
        data = traceConfig.getMapper();
        data.setDataSet(dataSet);
    }

    Color getLineColor() {
        Color lineColor = traceConfig.getLineConfig().getColor();
        if(lineColor == null) {
            lineColor = getDefaultColor();
        }
        return lineColor;
    }

    Color getMarkColor() {
        Color markColor = traceConfig.getMarkConfig().getColor();
        if(markColor == null) {
            markColor = getLineColor();
        }
        return markColor;
    }

    Color getHoverColor() {
        Color hoverColor = traceConfig.getHoverColor();
        if(hoverColor == null) {
            hoverColor = getMarkColor().brighter();
        }
        return hoverColor;
    }


    @Override
    public TooltipItem getTooltipItem(){
        if (getHoverIndex() == -1){
            return null;
        }
        String label = getName();
        return new TooltipItem(label, String.valueOf(data.getY(getHoverIndex())), getLineColor());
    }

    @Override
    public int findNearest(int mouseX, int mouseY) {
        double x = getXAxis().invert(mouseX);
        return data.findNearest(x);
    }

    @Override
    public double getXPosition(int dataIndex) {
        return getXAxis().scale(data.getX(dataIndex));
    }

    @Override
    public double getXValue(int dataIndex) {
        return data.getX(dataIndex);
    }

    @Override
    public LegendItem[] getLegendItems() {
        LegendItem[] items = {new LegendItem(getLineColor(), getName())};
        return items;
    }

    @Override
    public Range getXExtremes() {
        return data.getXExtremes();
    }

    @Override
    public Range getYExtremes() {
        return data.getYExtremes();
    }


    @Override
    public void draw(Graphics2D g) {
        if (data == null || data.size() == 0) {
            return;
        }

        GeneralPath path = new GeneralPath();
        double x = getXAxis().scale(data.getX(0));
        double y = getYAxis().scale(data.getY(0));

        path.moveTo(x, y);
        g.setColor(getMarkColor());
        int pointRadius = traceConfig.getMarkConfig().getSize() / 2;
        g.draw(new Ellipse2D.Double(x - pointRadius,y - pointRadius, 2 * pointRadius,2 * pointRadius));
        for (int i = 1; i < data.size(); i++) {
            x = getXAxis().scale(data.getX(i));
            y = getYAxis().scale(data.getY(i));
            path.lineTo(x, y);
            g.draw(new Ellipse2D.Double(x - pointRadius,y - pointRadius, 2 * pointRadius,2 * pointRadius));
        }
        g.setColor(getLineColor());
        g.draw(path);
        drawHover(g);
    }


    void drawHover(Graphics2D g) {
        if(getHoverIndex() >= 0) {
            double x = getXAxis().scale(data.getX(getHoverIndex()));
            double y = getYAxis().scale(data.getY(getHoverIndex()));
            double pointRadius = traceConfig.getHoverSize();
            g.setColor(Color.CYAN);
            g.draw(new Ellipse2D.Double(x - pointRadius,y - pointRadius, 2 * pointRadius,2 * pointRadius));
        }
    }


}
