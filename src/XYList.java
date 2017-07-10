import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hdablin on 05.04.17.
 */
public class XYList extends AbstractList<DataItem> implements DataList {
    private List<Point2d> items = new ArrayList<>();
    private double xMin = Double.MAX_VALUE;
    private double xMax = -Double.MAX_VALUE;
    private double yMin = Double.MAX_VALUE;
    private double yMax = -Double.MAX_VALUE;

    public void addItem(double x, double y){
        xMin = Math.min(xMin,x);
        xMax = Math.max(xMax,x);
        yMin = Math.min(yMin,y);
        yMax = Math.max(yMax,y);
        items.add(new Point2d(x,y));
    }

    @Override
    public Double getXmin() {
        if(size() > 0) {
            return xMin;
        }
        return null;
    }

    @Override
    public Double getXmax() {
        if(size() > 0) {
            return xMax;
        }
        return null;
    }

    @Override
    public Double getYmin() {
        if(size() > 0) {
            return yMin;
        }
        return null;
    }

    @Override
    public Double getYmax() {
        if(size() > 0) {
            return yMax;
        }
        return null;
    }

    @Override
    public int size(){
        return items.size();
    }

    @Override
    public DataItem get(int index){
        return items.get(index);
    }

}
