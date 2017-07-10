import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hdablin on 26.06.17.
 */
public interface SliceDataList extends DataList{

    public void setRange(double startValue, double endValue);

    public void setRange(int startIndex, int window);

    public int getFullSize() ;

}
