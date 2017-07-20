package graphs;

import data.DataProcessorForDoubles;

/**
 * Created by hdablin on 26.04.17.
 */
public class AreaGraph extends Graph<Double> {
    {
        graphPainter = new AreaGraphPainter();
        dataProcessor = new DataProcessorForDoubles();
    }
}
