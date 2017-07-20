package graphs;

import axis.Axis;
import data.ExtremesFunction;
import data.Range;
import graphs.Graph;

import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Created by hdablin on 26.04.17.
 */
public class AreaGraph extends DoubleGraph {
    {
        graphPainter = new AreaGraphPainter();
    }
}
