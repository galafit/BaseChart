package base.traces;


import base.*;
import base.config.traces.BaseTraceConfig;
import base.tooltip.InfoItem;

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

    BColor getLineColor() {
        BColor lineColor = traceConfig.getColor();
        if(lineColor == null) {
            lineColor = getDefaultColor();
        }
        return lineColor;
    }

    BColor getMarkColor() {
        BColor markColor = traceConfig.getMarkConfig().getColor();
        if(markColor == null) {
            markColor = getLineColor();
        }
        return markColor;
    }

    @Override
    public InfoItem[] getInfo(int dataIndex){
        if (dataIndex == -1){
            return new InfoItem[0];
        }
        InfoItem[] infoItems = new InfoItem[3];
        infoItems[0] = new InfoItem(getName(), "", getLineColor());
        //infoItems[1] = new InfoItem("X: ", String.valueOf(xyData.getX(dataIndex)), null);
        //infoItems[2] = new InfoItem("Y: ", String.valueOf(xyData.getY(dataIndex)), null);
        infoItems[1] = new InfoItem("X: ", getXAxis().formatDomainValue(xyData.getX(dataIndex)), null);
        infoItems[2] = new InfoItem("Y: ", getYAxis().formatDomainValue(xyData.getY(dataIndex)), null);

        return infoItems;
    }

    @Override
    public Range getYExtremes() {
        return xyData.getYExtremes();
    }


    @Override
    public BPoint getDataPosition(int dataIndex) {
        return new BPoint((int)getXAxis().scale(xyData.getX(dataIndex)), (int)getYAxis().scale(xyData.getY(dataIndex)));
    }

    @Override
    public void draw(BCanvas canvas) {
        if (xyData == null || xyData.size() == 0) {
            return;
        }

        BPath path = new BPath(xyData.size());
        int x =  (int) getXAxis().scale(xyData.getX(0));
        int y = (int) getYAxis().scale(xyData.getY(0));

        path.moveTo(x, y);
        canvas.setColor(getMarkColor());
        int pointRadius = traceConfig.getMarkConfig().getSize() / 2;
        canvas.drawOval(x - pointRadius,y - pointRadius, 2 * pointRadius,2 * pointRadius);
        for (int i = 1; i < xyData.size(); i++) {
            x = (int) getXAxis().scale(xyData.getX(i));
            y = (int) getYAxis().scale(xyData.getY(i));
            path.lineTo(x, y);
            canvas.drawOval(x - pointRadius,y - pointRadius, 2 * pointRadius,2 * pointRadius);
        }
        canvas.setColor(getLineColor());
        canvas.drawPath(path);
    }

}
