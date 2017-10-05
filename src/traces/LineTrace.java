package traces;

import configuration.traces.LineTraceConfig;
import data.Range;
import data.XYData;
import legend.LegendItem;
import tooltips.TooltipItem;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

/**
 * Created by galafit on 20/9/17.
 */
public class LineTrace extends Trace {
    private LineTraceConfig traceConfig;
    private XYData data;

    public LineTrace(LineTraceConfig traceConfig) {
        this.traceConfig = traceConfig;
        this.data = traceConfig.getData();
    }

    @Override
    public TooltipItem getTooltipItem(){
        if (getHoverIndex() == -1){
            return null;
        }
        String label = traceConfig.getName();
        return new TooltipItem(label, String.valueOf(data.getY(getHoverIndex())), traceConfig.getColor());
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
    public double getX(int dataIndex) {
        return data.getX(dataIndex);
    }

    @Override
    public LegendItem[] getLegendItems() {
        LegendItem[] items = {new LegendItem(traceConfig.getColor(), traceConfig.getName())};
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
        g.setColor(traceConfig.getMarkConfig().color);
        int pointRadius = traceConfig.getMarkConfig().size / 2;
        g.draw(new Ellipse2D.Double(x - pointRadius,y - pointRadius, 2 * pointRadius,2 * pointRadius));
        for (int i = 1; i < data.size(); i++) {
            x = getXAxis().scale(data.getX(i));
            y = getYAxis().scale(data.getY(i));
            path.lineTo(x, y);
            g.draw(new Ellipse2D.Double(x - pointRadius,y - pointRadius, 2 * pointRadius,2 * pointRadius));
        }
        g.setColor(traceConfig.getLineConfig().color);
        g.setColor(traceConfig.getLineConfig().color);
        g.draw(path);
        drawHover(g);
    }


    public void drawHover(Graphics2D g) {
        if(getHoverIndex() >= 0) {
            double x = getXAxis().scale(data.getX(getHoverIndex()));
            double y = getYAxis().scale(data.getY(getHoverIndex()));
            double pointRadius = traceConfig.getHoverSize();
            g.setColor(Color.CYAN);
            g.draw(new Ellipse2D.Double(x - pointRadius,y - pointRadius, 2 * pointRadius,2 * pointRadius));
        }
    }

}
