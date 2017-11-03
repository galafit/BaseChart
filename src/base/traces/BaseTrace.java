package base.traces;


import base.DataSet;
import base.Range;
import base.XYViewer;
import base.config.traces.BaseTraceConfig;
import base.legend.LegendItem;
import base.tooltips.InfoItem;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

/**
 * Created by galafit on 11/10/17.
 */
public abstract class BaseTrace extends Trace {
    BaseTraceConfig traceConfig;
    XYViewer xyData;

    @Override
    public void setData(DataSet dataSet) {
        super.setData(dataSet);
        xyData = new XYViewer();
        xyData.setData(dataSet);
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


    @Override
    public int getPreferredTraceLength() {
        int prefLength = xyData.size();
        if(traceConfig.getMarkConfig().getSize() > 0) {
            prefLength *= traceConfig.getMarkConfig().getSize();
        }
        return prefLength;
    }

    @Override
    public InfoItem[] getInfo(int dataIndex){
        if (dataIndex == -1){
            return new InfoItem[0];
        }
        InfoItem[] infoItems = new InfoItem[3];
        infoItems[0] = new InfoItem(getName(), "", getLineColor());
        infoItems[1] = new InfoItem("X: ", String.valueOf(xyData.getX(dataIndex)), null);
        infoItems[2] = new InfoItem("Y: ", String.valueOf(xyData.getY(dataIndex)), null);

        return infoItems;
    }

    @Override
    public LegendItem[] getLegendItems() {
        LegendItem[] items = {new LegendItem(getLineColor(), getName())};
        return items;
    }

    @Override
    public Range getYExtremes() {
        return xyData.getYExtremes();
    }


    @Override
    public Point getDataPosition(int dataIndex) {
        return new Point((int)getXAxis().scale(xyData.getX(dataIndex)), (int)getYAxis().scale(xyData.getY(dataIndex)));
    }

    @Override
    public void draw(Graphics2D g) {
        if (xyData == null || xyData.size() == 0) {
            return;
        }

        GeneralPath path = new GeneralPath();
        double x = getXAxis().scale(xyData.getX(0));
        double y = getYAxis().scale(xyData.getY(0));

        path.moveTo(x, y);
        g.setColor(getMarkColor());
        int pointRadius = traceConfig.getMarkConfig().getSize() / 2;
        g.draw(new Ellipse2D.Double(x - pointRadius,y - pointRadius, 2 * pointRadius,2 * pointRadius));
        for (int i = 1; i < xyData.size(); i++) {
            x = getXAxis().scale(xyData.getX(i));
            y = getYAxis().scale(xyData.getY(i));
            path.lineTo(x, y);
            g.draw(new Ellipse2D.Double(x - pointRadius,y - pointRadius, 2 * pointRadius,2 * pointRadius));
        }
        g.setColor(getLineColor());
        g.draw(path);
    }

}
