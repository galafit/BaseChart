import java.util.List;

/**
 * Created by hdablin on 05.04.17.
 */
public interface DataList extends List<DataItem> {
    public Double getXmin();

    public Double getXmax();

    public Double getYmin();

    public Double getYmax();
}
