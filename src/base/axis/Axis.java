package base.axis;

import base.config.axis.AxisConfig;
import base.config.general.TextAnchor;
import base.Range;
import base.painters.Line;
import base.painters.Text;
import base.scales.Scale;
import base.scales.ScaleLinear;
import base.scales.Tick;
import base.scales.TickProvider;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by galafit on 5/9/17.
 */
public class Axis {
    /** Dirty data in js - the data, that have been changed recently and
    ** DOM haven't been re-rendered according to this changes yet.
    ** So dirty checking is diff between next state and current state.
    **/
    // if the axis elements (ticks, labels, lines...) are not created or should be updated
    private boolean isDirty = true;
    private boolean isTicksOverlappingFixed = false;
    private Scale scale;
    private AxisConfig config;
    private List<Tick> ticks;
    private List<Double> minorTicks;
    private List<Line> tickLines;
    private List<Text> tickLabels;
    private Line axisLine;
    private Text axisName;
    private TickProvider tickProvider;
    private boolean isAutoscale;

    public Axis(AxisConfig config) {
        this.config = config;
        isAutoscale = config.isAutoScale();
        scale = new ScaleLinear();
    }

    public boolean isAutoScale() {
        return isAutoscale;
    }

    public void setAutoScale(boolean isAutoScale) {
        this.isAutoscale = isAutoScale;
    }

    public void setMinMax(Range minMaxRange) {
        if(minMaxRange == null) {
            return;
        }
        double min = minMaxRange.start();
        double max = minMaxRange.end();
        if (min > max){
            String errorMessage = "Error during setMinMax(). Expected Min < Max. Min = {0}, Max = {1}.";
            String formattedError = MessageFormat.format(errorMessage,min,max);
            throw new IllegalArgumentException(formattedError);
        }
        scale.setDomain(min, max);
        ticks = null;
        isTicksOverlappingFixed = false;
        tickProvider = null;
        isDirty = true;
    }

    public void setStartEnd(double start, double end) {
        scale.setRange(start, end);
        isTicksOverlappingFixed = false;
        isDirty = true;
    }

    public Scale getScale() {
        return scale;
    }

    public void setStartEnd(Range startEndRange) {
        setStartEnd(startEndRange.start(), startEndRange.end());
    }

    public double getMin() {
        return scale.getDomain()[0];
    }

    public double getMax() {
        return scale.getDomain()[scale.getDomain().length -1];
    }

    public int getStart() {
        return (int)scale.getRange()[0];
    }

    public int getEnd() {
        return (int)scale.getRange()[scale.getRange().length -1];
    }

    public double scale(double value) {
        return scale.scale(value);
    }

    public double invert(double value) {
        return scale.invert(value);
    }

    public int getThickness(Graphics2D g2) {
        if (!config.isVisible()) {
            return 0;
        }

        int size = 0;
        if(config.getAxisLineConfig().isVisible()) {
            size += config.getAxisLineConfig().getWidth() / 2;
        }

        if (config.getTicksConfig().isTickMarksVisible()) {
            size += config.getTicksConfig().getTickMarkOutsideSize();
        }

        if(ticks == null) {
            ticks = getTickProvider().getTicks(1);
        }

        if(config.getLabelsConfig().isVisible()) {
            int labelsSize = 0;
            FontMetrics fm = g2.getFontMetrics(config.getLabelsConfig().getTextStyle().getFont());
            if(config.isHorizontal()) {
                labelsSize = fm.getHeight();
            } else {
                labelsSize = getMaxTickLabelsWidth(fm, ticks);
            }
            size += config.getLabelsConfig().getPadding() + labelsSize;
        }
        if (config.getTitleConfig().isVisible()) {
            FontMetrics fm = g2.getFontMetrics(config.getTitleConfig().getTextStyle().getFont());
            size = size + config.getTitleConfig().getPadding() + fm.getHeight();
        }
        return size;
    }

    private TickProvider getTickProvider() {
        if(tickProvider != null) {
            return tickProvider;
        }
        TickProvider tickProvider =  scale.getTickProvider();
        tickProvider.setLabelFormatInfo(config.getLabelsConfig().getFormatInfo());
        if(config.getTicksConfig().getTickStep() > 0) {
            tickProvider.setTickStep(config.getTicksConfig().getTickStep());
        } else if(config.getTicksConfig().getTickPixelInterval() > 0) {
            int tickAmount = Math.abs(getEnd() - getStart())/ config.getTicksConfig().getTickPixelInterval();
            if(tickAmount < 3) {
                tickAmount = 3;
            }
            tickProvider.setTickAmount(tickAmount);
        }
        this.tickProvider = tickProvider;
        return tickProvider;
    }


    private Line tickToGridLine(Double tickValue, int length) {
        if(config.isTop()) {
            int x = (int)scale(tickValue);
            int y1 = 0;
            int y2 = length;
            return new Line(x, y1, x, y2);
        }
        if(config.isBottom()) {
            int x = (int)scale(tickValue);
            int y1 = 0;
            int y2 = -length;
            return new Line(x, y1, x, y2);
        }
        if(config.isLeft()) {
            int y = (int)scale(tickValue);
            int x1 = 0;
            int x2 = length;
            return new Line(x1, y, x2, y);
        }
        // if config.isRight()
        int y = (int)scale(tickValue);
        int x1 = 0;
        int x2 = -length;
        return new Line(x1, y, x2, y);
    }

    private Line tickToMarkLine(Tick tick) {
        int axisWidth = config.getAxisLineConfig().getWidth();
        if(config.isTop()) {
            int x = (int)scale(tick.getValue());
            int y1 = -axisWidth / 2 - config.getTicksConfig().getTickMarkOutsideSize();
            int y2 = axisWidth / 2 + config.getTicksConfig().getTickMarkInsideSize();
            return new Line(x, y1, x, y2);
        }
        if(config.isBottom()) {
            int x = (int)scale(tick.getValue());
            int y1 = axisWidth / 2 + config.getTicksConfig().getTickMarkOutsideSize();
            int y2 = -axisWidth / 2 - config.getTicksConfig().getTickMarkInsideSize();
            return new Line(x, y1, x, y2);
        }
        if(config.isLeft()) {
            int y = (int)scale(tick.getValue());
            int x1 = -axisWidth / 2 - config.getTicksConfig().getTickMarkOutsideSize();
            int x2 = axisWidth / 2 + config.getTicksConfig().getTickMarkInsideSize();
            return new Line(x1, y, x2, y);
        }
        // if config.isRight()
        int y = (int)scale(tick.getValue());
        int x1 = axisWidth / 2 + config.getTicksConfig().getTickMarkOutsideSize();
        int x2 = -axisWidth / 2 - config.getTicksConfig().getTickMarkInsideSize();
        return new Line(x1, y, x2, y);
    }

    private Text tickToLabel(Tick tick, FontMetrics fm) {
        int axisWidth = config.getAxisLineConfig().getWidth();
        int labelPadding = config.getLabelsConfig().getPadding();
        if(config.isTop()) {
            int x = (int)scale(tick.getValue());
            int y = -axisWidth / 2 - config.getTicksConfig().getTickMarkOutsideSize() - labelPadding;
            return new Text(tick.getLabel(), x, y, TextAnchor.MIDDLE, TextAnchor.START, fm);
        }
        if(config.isBottom()) {
            int x = (int)scale(tick.getValue());
            int y = axisWidth / 2 + config.getTicksConfig().getTickMarkOutsideSize() + labelPadding;
            return new Text(tick.getLabel(), x, y, TextAnchor.MIDDLE, TextAnchor.END, fm);

        }
        if(config.isLeft()) {
            int y = (int)scale(tick.getValue());
            int x = -axisWidth / 2 - config.getTicksConfig().getTickMarkInsideSize() - labelPadding;
            return new Text(tick.getLabel(), x, y, TextAnchor.END, TextAnchor.MIDDLE, fm);
        }
        // if config.isRight()
        int y = (int)scale(tick.getValue());
        int x = axisWidth / 2 + config.getTicksConfig().getTickMarkInsideSize() + labelPadding;
        return new Text(tick.getLabel(), x, y, TextAnchor.START, TextAnchor.MIDDLE, fm);
    }

    private void fixTicksOverlapping(FontMetrics fm) {
        if(ticks.size() < 2) {
            return;
        }
        if(!config.getLabelsConfig().isVisible()) {
            isTicksOverlappingFixed = true;
            return;
        }
        int labelsSize;
        if(config.isHorizontal()) {
            labelsSize = getMaxTickLabelsWidth(fm, ticks);
        } else {
            labelsSize = fm.getHeight();
        }
        double tickPixelInterval = Math.abs(scale(ticks.get(1).getValue()) - scale(ticks.get(0).getValue()));
        // min space between labels = 1 symbols size (roughly)
        double labelSpace = 1 * config.getLabelsConfig().getTextStyle().getFontSize();
        double requiredSpace = labelsSize + labelSpace;
        int ticksDivider = (int) (requiredSpace / tickPixelInterval) + 1;
        ticks = getTickProvider().getTicks(ticksDivider);
        minorTicks = getTickProvider().getMinorTicks(ticksDivider, config.getMinorGridCounter());
        isTicksOverlappingFixed = true;
    }

    private void normalizeTicks(Graphics2D g) {
        if(ticks == null) {
            ticks = getTickProvider().getTicks(1);
        }
        FontMetrics fm = g.getFontMetrics(config.getLabelsConfig().getTextStyle().getFont());
        if(!isTicksOverlappingFixed) {
            fixTicksOverlapping(fm);
        }
    }


    private void createAxisElements(Graphics2D g) {
        normalizeTicks(g);
        // tick lines
        tickLines = new ArrayList<Line>();
        if(config.getTicksConfig().isTickMarksVisible()) {
            for (Tick tick : ticks) {
                tickLines.add(tickToMarkLine(tick));
            }
        }

        // tick labels
        FontMetrics fm = g.getFontMetrics(config.getLabelsConfig().getTextStyle().getFont());
        tickLabels = new ArrayList<Text>();
        if(config.getLabelsConfig().isVisible()) {
            for (Tick tick : ticks) {
                tickLabels.add(tickToLabel(tick, fm));
            }
        }

        // axis line
        if (config.isHorizontal()) {
            axisLine = new Line(getStart(), 0, getEnd(), 0);
        } else {
            axisLine = new Line(0, getStart(), 0, getEnd());
        }

        // axis title
        int axisNameDistance = 0;
        if(config.getAxisLineConfig().isVisible()) {
            axisNameDistance += config.getAxisLineConfig().getWidth() / 2;
        }
        if (config.getTicksConfig().isTickMarksVisible()) {
            axisNameDistance += config.getTicksConfig().getTickMarkOutsideSize();
        }
        if(config.getLabelsConfig().isVisible()) {
            int labelsSize = 0;
            if(config.isHorizontal()) {
                labelsSize = fm.getAscent();
            } else {
                labelsSize = getMaxTickLabelsWidth(fm, ticks);
            }
            axisNameDistance += (labelsSize + config.getLabelsConfig().getPadding());
        }
        axisNameDistance += config.getTitleConfig().getPadding();
        fm = g.getFontMetrics(config.getTitleConfig().getTextStyle().getFont());
        if(config.isTop()) {
            int y = -axisNameDistance;
            int x = (getEnd() + getStart()) / 2;
            axisName = new Text(config.getTitle(), x, y, TextAnchor.MIDDLE, TextAnchor.START, fm);
        }
        if(config.isBottom()) {
            int y = axisNameDistance;
            int x = (getEnd() + getStart()) / 2;
            axisName = new Text(config.getTitle(), x, y, TextAnchor.MIDDLE, TextAnchor.END, fm);
        }
        if(config.isLeft()) {
            int x = -axisNameDistance;
            int y = (getEnd() + getStart()) / 2;
            axisName = new Text(config.getTitle(), x, y, TextAnchor.START, TextAnchor.MIDDLE, -90, fm);
        }
        if(config.isRight()) {
            int x = axisNameDistance;
            int y = (getEnd() + getStart()) / 2;
            axisName = new Text(config.getTitle(), x, y, TextAnchor.START, TextAnchor.MIDDLE, +90, fm);
        }
        isDirty = false;
    }

    private int getMaxTickLabelsWidth(FontMetrics fm, List<Tick> ticks) {
        int maxSize = 0;
        for (Tick tick : ticks) {
            maxSize = Math.max(maxSize, fm.stringWidth(tick.getLabel()));
        }
        return maxSize;
    }

    public void drawGrid(Graphics2D g, int axisOriginPoint, int length) {
        if(isDirty) {
            createAxisElements(g);
        }
        AffineTransform initialTransform = g.getTransform();
        if(config.isHorizontal()) {
            g.translate(0, axisOriginPoint);
        } else {
            g.translate(axisOriginPoint, 0);
        }
        normalizeTicks(g);
        g.setColor(config.getMinorGridColor());
        g.setStroke(config.getMinorGridLineConfig().getStroke());
        if(config.getMinorGridLineConfig().isVisible()) {
            for (Double minorTick : minorTicks) {
                tickToGridLine(minorTick, length).draw(g);
            }
        }

        g.setColor(config.getGridColor());
        g.setStroke(config.getGridLineConfig().getStroke());
        if(config.getGridLineConfig().isVisible()) {
            for (Tick tick : ticks) {
                tickToGridLine(tick.getValue(), length).draw(g);
            }
        }
        g.setTransform(initialTransform);
    }

    public void drawAxis(Graphics2D g,  int axisOriginPoint) {
        if(!config.isVisible()) {
            return;
        }
        if(isDirty) {
            createAxisElements(g);
        }
        AffineTransform initialTransform = g.getTransform();
        if(config.isHorizontal()) {
            g.translate(0, axisOriginPoint);
        } else {
            g.translate(axisOriginPoint, 0);
        }
        g.setColor(config.getTicksColor());
        g.setStroke(new BasicStroke(config.getTicksConfig().getTickMarkWidth()));
        for (Line tickLine : tickLines) {
            tickLine.draw(g);
        }

        g.setColor(config.getLabelsColor());
        g.setFont(config.getLabelsConfig().getTextStyle().getFont());
        g.setStroke(new BasicStroke());
        for (Text tickLabel : tickLabels) {
            tickLabel.draw(g);
        }

        if(config.getAxisLineConfig().isVisible()) {
            g.setColor(config.getAxisLineColor());
            g.setStroke(config.getAxisLineConfig().getStroke());
            axisLine.draw(g);
        }

        if(config.getTitleConfig().isVisible()) {
            g.setStroke(new BasicStroke());
            g.setColor(config.getTitleColor());
            g.setFont(config.getTitleConfig().getTextStyle().getFont());
            axisName.draw(g);
        }
        g.setTransform(initialTransform);
    }

}

