package configuration.general;

import java.awt.*;

/**
 * Created by galafit on 13/6/17.
 */
public enum LineStroke {
    SOLID {
        @Override
        public Stroke getStroke(int width) {
            return new BasicStroke(width);
        }
    },
    DASH_LONG {
        @Override
        public Stroke getStroke(int width) {
            float[] dash = {4f, 0f, 2f};
            return new BasicStroke(width, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_ROUND, 1.0f, dash, 2f);
        }
    },
    DASH_SHORT {
        @Override
        public Stroke getStroke(int width) {
            float[] dash = {2f, 0f, 2f};
            return new BasicStroke(width, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_ROUND, 1.0f, dash, 2f);

        }
    },
    DASH_DOT {
        @Override
        public Stroke getStroke(int width) {
            float[] dash = {4f, 4f, 1f};
            return new BasicStroke(width, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_ROUND, 1.0f, dash, 2f);

        }
    },
    DOT {
        @Override
        public Stroke getStroke(int width) {
            float[] dash = {1f, 1f, 1f};
            return new BasicStroke(width, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_ROUND, 1.0f, dash, 2f);
        }
    };

    public abstract Stroke getStroke(int width);
}
