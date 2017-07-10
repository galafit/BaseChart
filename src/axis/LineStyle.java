package main.chart.axis;

import java.awt.*;

/**
 * Created by galafit on 13/6/17.
 */
public enum LineStyle {
    SOLID {
        @Override
        Stroke getStroke(int width) {
            return new BasicStroke(width);
        }
    },
    DASH_LONG {
        @Override
        Stroke getStroke(int width) {
            float[] dash = {4f, 0f, 2f};
            return new BasicStroke(width, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_ROUND, 1.0f, dash, 2f);
        }
    },
    DASH_SHORT {
        @Override
        Stroke getStroke(int width) {
            float[] dash = {2f, 0f, 2f};
            return new BasicStroke(width, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_ROUND, 1.0f, dash, 2f);

        }
    },
    DASH_DOT {
        @Override
        Stroke getStroke(int width) {
            float[] dash = {4f, 4f, 1f};
            return new BasicStroke(width, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_ROUND, 1.0f, dash, 2f);

        }
    },
    DOT {
        @Override
        Stroke getStroke(int width) {
            float[] dash = {1f, 1f, 1f};
            return new BasicStroke(width, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_ROUND, 1.0f, dash, 2f);
        }
    };

    abstract Stroke getStroke(int width);
}
