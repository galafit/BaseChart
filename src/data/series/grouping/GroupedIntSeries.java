package data.series.grouping;

import data.series.IntSeries;
import data.series.LongSeries;

/**
 * Data grouping or data binning (banding) with aggregation.
 * Serves to reduce large number of data.
 * <p>
 * Binning is a way to groupByNumber a number of more or less continuous values
 * into a smaller number of buckets (bins).  Each groupByNumber/bucket/bin defines
 * an numerical interval and usually is characterized by a name and two boundaries -
 * the start or lower boundary and the stop or upper one.
 * <p>
 * On the chart every bin is represented by one value (point).
 * It may be the number of element in the bin (for histogram)
 * or the midpoint of the bin interval (avg) and so on.
 * Some aggregation function specifies how we will calculate the "value" of each bin
 * (sum, average, count, min, max, first, last, center...)
 * <p>
 * The most common "default" methods to divide data into bins:
 * <ol>
 *  <li>Equal intervals [equal width binning] - each bin has equal range value or lengths. </li>
 *  <li>Equal frequencies [equal height binning, quantiles] - each bin has equal number of elements or data points.
 *  Percentile ranks - % of the total data to groupByNumber into bins, or  the number of points in bins are specified. </li>
 *  <li>Custom Edges - edge values of each bin are specified. The edge value is always the lower boundary of the bin.</li>
 *  <li>Custom Elements [list] - the elements for each bin are specified manually.</li>
 * </ol>
 * <p>
 * <a href="https://msdn.microsoft.com/library/en-us/Dn913065.aspx">MSDN: Group Data into Bins</a>,
 * <a href="https://gerardnico.com/wiki/data_mining/discretization">Discretizing and binning</a>,
 * <a href="https://docs.rapidminer.com/studio/operators/cleansing/binning/discretize_by_bins.html">discretize by bins</a>,
 * <a href="http://www.ncgia.ucsb.edu/cctp/units/unit47/html/comp_class.html">Data Classification</a>,
 * <a href="https://www.ibm.com/support/knowledgecenter/en/SSLVMB_24.0.0/spss/base/idh_webhelp_scatter_options_palette.html">Binning (Grouping) Data Values</a>,
 * <a href="http://www.jdatalab.com/data_science_and_data_mining/2017/01/30/data-binning-plot.html">Data Binning and Plotting</a>,
 * <a href="https://docs.tibco.com/pub/sfire-bauthor/7.6.0/doc/html/en-US/GUID-D82F7907-B3B4-45F6-AFDA-C3179361F455.html">Binning functions</a>,
 * <a href="https://devnet.logianalytics.com/rdPage.aspx?rdReport=Article&dnDocID=6029">Data Binning</a>,
 * <a href="http://www.cs.wustl.edu/~zhang/teaching/cs514/Spring11/Data-prep.pdf">Data Preprocessing</a>
 */
public abstract class GroupedIntSeries implements IntSeries {
    protected IntSeries inputSeries;
    protected LongSeries groupsStartIndexes;

    public GroupedIntSeries(IntSeries inputSeries) {
        this.inputSeries = inputSeries;
    }

    /**
     * Gets resultant number of groups/bins
     */
    @Override
    public long size() {
        // last point we do not count because it will change on adding data
        return groupsStartIndexes.size() - 1;
    }

    /**
     * Gets the (aggregated) value corresponding to the bin
     * @param groupIndex index of the groupByNumber/bin
     * @return aggregated value corresponding to the bin
     */
    @Override
    public int get(long groupIndex) {
        return getGroupedValue(groupIndex);
    }

    protected abstract int getGroupedValue(long groupIndex);


    /**
     * Gets start or lower boundary of the groupByNumber/bin
     */
  //  public abstract long getStartBoundary(long groupIndex);

    /**
     * Gets start or upper boundary of the groupByNumber/bin
     */
   // public abstract long getStopBoundary(long groupIndex);

    /**
     * Serves to synchronize groups(bins) of x and y data series
     * @return series containing start index of each bin
     */
   /* public LongSeries getGroupsStartIndexes() {
        return groupsStartIndexes;
    }*/


    /**
     * Gets the name of the groupByNumber/bin.
     * By default the name is: "[LowerBoundary - UpperBoundary)"
     * @param groupIndex index of the groupByNumber/bin
     * @return the name of the groupByNumber/bin
     */
   /* public String getGroupName(long groupIndex) {
        return new String("["+getStartBoundary(groupIndex)+" - "+getStopBoundary(groupIndex)+")");
    }*/


}
