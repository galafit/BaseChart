package com.biorecorder.basechart.chart.traces;

import com.biorecorder.basechart.chart.*;
import com.biorecorder.basechart.chart.config.traces.LineTraceConfig;

/**
 * Created by galafit on 20/9/17.
 */
public class AreaTrace {
    LineTraceConfig traceConfig;
    XYViewer xyData;



    public void draw(BCanvas canvas) {
        if (xyData == null || xyData.size() == 0) {
            return;
        }

        BPath path = new BPath();
/*
        int x_0 = (int) getXAxis().scale(xyData.getX(0));
        int y_0 = (int) getYAxis().scale(xyData.getY(0));
        int x = x_0;
        int y = y_0;

        path.moveTo(x, y);
        canvas.setColor(getMarkColor());
        int pointRadius = traceConfig.getMarkConfig().getSize() / 2;
        canvas.drawOval(x - pointRadius, y - pointRadius, 2 * pointRadius,2 * pointRadius);
        for (int i = 1; i < xyData.size(); i++) {
            x = (int) getXAxis().scale(xyData.getX(i));
            y = (int) getYAxis().scale(xyData.getY(i));
            path.lineTo(x, y);
            canvas.drawOval(x - pointRadius,y - pointRadius, 2 * pointRadius,2 * pointRadius);
        }
        canvas.setColor(getLineColor());
        canvas.setStroke(traceConfig.getLineStroke());
        canvas.drawPath(path);

        path.lineTo(x, getYAxis().getStart());
        path.lineTo(x_0, getYAxis().getStart());
        path.lineTo(x_0, y_0);
        canvas.setColor(getFillColor());
        canvas.fillPath(path);
        */
    }

}
