import main.chart.axis.Axis;

import java.awt.*;
import java.util.List;

/**
 * Created by hdablin on 26.04.17.
 */
public abstract class Graph {
    protected DataList dataItemList;
    protected Axis xAxis;
    protected Axis yAxis;
    protected Color color = Color.GRAY;

    public void setData(DataList data){
        dataItemList = data;
    }

    public DataList getDataItemList() {
        return dataItemList;
    }

    protected void rangeXaxis(){
        if (xAxis.isAutoScale()){
            Double xMin = getXmin();
            Double xMax = getXmax();
            if(xMin != null && !xMin.isNaN() && xMax != null && !xMax.isNaN()) {
                xAxis.setRange(xMin, xMax);
            }
        }
    }

    protected void setDataRange(Rectangle area){
        double xMin = xAxis.pointsToValue(area.x, area);
        double xMax = xAxis.pointsToValue(area.x + area.width, area);
        if (dataItemList instanceof SliceDataList){
            SliceDataList sliceDataList = (SliceDataList) dataItemList;
            sliceDataList.setRange(xMin, xMax);
        }
    }

    public int getDataSize(){
        if (dataItemList != null){
            if (dataItemList instanceof SliceDataList){
                SliceDataList sliceDataList = (SliceDataList) dataItemList;
                return sliceDataList.getFullSize();
            }
            return dataItemList.size();
        }
        return 0;
    }


    protected void rangeYaxis(){
        if (yAxis.isAutoScale()){
            Double yMin = getYmin();
            Double yMax = getYmax();
            if(yMin != null && !yMin.isNaN() && yMax != null && !yMax.isNaN()) {
                yAxis.setRange(yMin, yMax);
            }
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Double getXmin(){
        if (dataItemList != null) {
            return dataItemList.getXmin();
        }
        return null;
    }

    public Double getXmax(){
        if (dataItemList != null) {
        return dataItemList.getXmax();
        }
        return null;
    }

    public Double getYmin(){
        if (dataItemList != null) {
            return dataItemList.getYmin();
        }
        return null;
    }

    public Double getYmax(){
        if (dataItemList != null) {
            return dataItemList.getYmax();
        }
        return null;
    }

    public Axis getXAxis() {
        return xAxis;
    }

    public Axis getYAxis() {
        return yAxis;
    }

    public void setAxis(Axis xAxis, Axis yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    public abstract void draw(Graphics2D g, Rectangle area);
}
