package graphs;

import data_old.DataProcessorForNumbers;

/**
 * Created by hdablin on 26.04.17.
 */
public class AreaGraph extends Graph<Number> {
    {
        graphPainter = new AreaGraphPainter();
        dataProcessor = new DataProcessorForNumbers();
    }
}
