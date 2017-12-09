
import base.config.ScrollableChartConfig;
import base.config.traces.TraceConfig;
import data.Data;

/**
 * Created by galafit on 6/10/17.
 */
public class ChartConfig extends ScrollableChartConfig {
    public static final int DARK_THEME = 1;
    public static final int LIGHT_THEME = 2;

    private boolean isDataCropEnable = true;
    private boolean isGroupingEnable = true;
    private int compression = 0;


    public int getCompression() {
        return compression;
    }

    public void setCompression(int compression) {
        this.compression = compression;
    }

    public boolean isCropEnable() {
        return isDataCropEnable;
    }

    public void setCropEnable(boolean isCropEnable) {
        this.isDataCropEnable = isCropEnable;
    }

    public boolean isGroupingEnable() {
        return isGroupingEnable;
    }

    public void setGroupingEnable(boolean isGroupingEnable) {
        this.isGroupingEnable = isGroupingEnable;
    }

    /*********************************************
     *              CHART CONFIG
     *********************************************/

    /**
     * add trace to the last chart stack
     */
    public void addTrace(TraceConfig traceConfig, Data traceData, String name, boolean isXAxisOpposite, boolean isYAxisOpposite) {
        getChartConfig().addTrace(traceConfig, traceData.getDataSet(), name,  isXAxisOpposite, isYAxisOpposite);
    }

    /**
     * add trace to the last chart stack
     */
    public void addTrace(TraceConfig traceConfig, Data data, String name) {
        addTrace(traceConfig, data, name, false, false);
    }

    /**
     * add trace to the last chart stack
     */
    public void addTrace(TraceConfig traceConfig, Data data) {
        addTrace(traceConfig, data, null, false, false);
    }


    /*********************************************
     *              PREVIEW CONFIG
     *********************************************/


    /**
     * add trace to the last preview stack
     */
    public void addPreviewTrace(TraceConfig traceConfig, Data traceData, String name,  boolean isXAxisOpposite, boolean isYAxisOpposite) {
        getPreviewConfig().addTrace(traceConfig, traceData.getDataSet(), name,  isXAxisOpposite, isYAxisOpposite);
    }

    /**
     * add trace to the last preview stack
     */
    public void addPreviewTrace(TraceConfig traceConfig, Data data, String name) {
        addPreviewTrace(traceConfig, data, name, false, false);
    }

    /**
     * add trace to the last preview stack
     */
    public void addPreviewTrace(TraceConfig traceConfig, Data data) {
        addPreviewTrace(traceConfig, data, null, false, false);
    }
}
