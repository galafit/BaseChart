package graphs;

import data.DataProcessorForNumbers;
import tooltips.TooltipInfo;
import tooltips.TooltipItem;

/**
 * Created by hdablin on 26.04.17.
 */
public class AreaGraph extends Graph<Number> {
    {
        graphPainter = new AreaGraphPainter();
        dataProcessor = new DataProcessorForNumbers();
    }
    public TooltipInfo getTooltipInfo(){
        return  null;
    }

    @Override
    public TooltipItem getTooltipItem() {
        return null;
    }
}
