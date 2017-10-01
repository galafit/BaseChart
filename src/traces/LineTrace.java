package traces;

import axis.Axis;
import configuration.traces.LineTraceConfig;
import configuration.traces.TraceConfig;
import data.Range;
import data.datasets.XYData;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

/**
 * Created by galafit on 20/9/17.
 */
public class LineTrace extends Trace {
    private LineTraceConfig traceConfig;
    private XYData data;
    private int hoverIndex = -1;


    public LineTrace(LineTraceConfig traceConfig) {
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
        double x = xAxis.scale(data.getX(0));
        double y = yAxis.scale(data.getY(0));

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
        g.setColor(traceConfig.getLineConfig().color);
        g.draw(path);
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
