package traces;

import axis.Axis;
import configuration.traces.AreaTraceConfig;
import configuration.traces.TraceConfig;
import data.Range;
import data.datasets.XYData;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

/**
 * Created by galafit on 20/9/17.
 */
public class AreaTrace extends Trace {
    private AreaTraceConfig traceConfig;
    private XYData data;
    private int hoverIndex = -1;


    public AreaTrace(AreaTraceConfig traceConfig) {
        this.traceConfig = traceConfig;
        this.data = traceConfig.getData();
    }

    @Override
    TraceConfig getTraceConfig() {
        return traceConfig;
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
    public void draw(Graphics2D g, Axis xAxis, Axis yAxis) {
        if (data == null || data.size() == 0) {
            return;
        }

        GeneralPath path = new GeneralPath();

        double x_0 = xAxis.scale(data.getX(0));
        double y_0 = yAxis.scale(data.getY(0));
        double x = x_0;
        double y = y_0;

        path.moveTo(x, y);
        g.setColor(traceConfig.getMarkConfig().color);
        int pointRadius = traceConfig.getMarkConfig().size / 2;
        g.draw(new Ellipse2D.Double(x - pointRadius,y - pointRadius, 2 * pointRadius,2 * pointRadius));
        for (int i = 1; i < data.size(); i++) {
            x = xAxis.scale(data.getX(i));
            y = yAxis.scale(data.getY(i));
            path.lineTo(x, y);
            g.draw(new Ellipse2D.Double(x - pointRadius,y - pointRadius, 2 * pointRadius,2 * pointRadius));
        }
        g.setColor(traceConfig.getLineConfig().color);
        g.setStroke(traceConfig.getLineConfig().getStroke());
        g.draw(path);

        path.lineTo(x, yAxis.getStart());
        path.lineTo(x_0, yAxis.getStart());
        path.lineTo(x_0, y_0);
        g.setColor(traceConfig.getFillColor());
        g.fill(path);

        drawHover(g, xAxis, yAxis);
    }


    public void drawHover(Graphics2D g, Axis xAxis, Axis yAxis) {
        if(hoverIndex >= 0) {
            double x = xAxis.scale(data.getX(hoverIndex));
            double y = yAxis.scale(data.getY(hoverIndex));
            double pointRadius = traceConfig.getHoverSize();
            g.setColor(Color.CYAN);
            g.draw(new Ellipse2D.Double(x - pointRadius,y - pointRadius, 2 * pointRadius,2 * pointRadius));
        }
    }
}
