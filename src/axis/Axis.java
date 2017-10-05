package axis;

import configuration.axis.AxisConfig;
import configuration.general.TextAnchor;
import data.Range;
import painters.Line;
import painters.Text;
import scales.Scale;
import scales.ScaleLinear;
import scales.Tick;
import scales.TickProvider;

import java.awt.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by galafit on 5/9/17.
 */
public class Axis {
    private Scale scale;
    private AxisConfig config;
    private List<Tick> ticks;
    private List<Double> minorTicks;
    private List<Line> tickLines;
    private List<Text> tickLabels;
    private Line axisLine;
    private Text axisName;
    private List<Line> gridLines;
    private List<Line> minorGridLines;
    private boolean isTicksOveralppingFixed = false;
    private TickProvider tickProvider;

    public Axis(AxisConfig config) {
        this.config = config;
        scale = new ScaleLinear();
    }

    public boolean isAutoScale() {
        return config.isAutoScale;
    }

    public void setAutoScale(boolean isAutoScale) {
        config.isAutoScale = isAutoScale;
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
    }

    public void setStartEnd(long start, long end) {
        scale.setRange(start, end);
    }
    public void setStartEnd(Range startEndRange) {
        scale.setRange(startEndRange.start(), startEndRange.end());
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
        if (!config.isVisible) {
            return 0;
        }

        int size = 0;
        if(config.axisLineConfig.isVisible()) {
            size += config.axisLineConfig.width  / 2;
        }

        if (config.ticksConfig.isTickMarksVisible()) {
            size += config.ticksConfig.tickMarkOutsideSize;
        }

        if(ticks == null) {
            ticks = getTickProvider().getTicks(1);
        }

        if(config.labelsConfig.isVisible) {
            int labelsSize = 0;
            FontMetrics fm = g2.getFontMetrics(config.labelsConfig.textStyle.getFont());
            if(config.isHorizontal()) {
                labelsSize = fm.getHeight();
            } else {
                labelsSize = getMaxTickLabelsWidth(fm, ticks);
            }
            size += config.labelsConfig.padding + labelsSize;
        }
        if (config.titleConfig.isVisible) {
            FontMetrics fm = g2.getFontMetrics(config.titleConfig.textStyle.getFont());
            size = size + config.titleConfig.padding + fm.getHeight();
        }
        return size;
    }

    public void drawGrid(Graphics2D g,  int axisOriginPoint, int length) {
        if(gridLines == null) {
            createGridLines(g, axisOriginPoint, length);
        }
        g.setColor(config.getMinorGridColor());
        g.setStroke(config.minorGridLineConfig.getStroke());
        for (Line minorGridLine : minorGridLines) {
            minorGridLine.draw(g);
        }
        g.setColor(config.getGridColor());
        g.setStroke(config.gridLineConfig.getStroke());
        for (Line gridLine : gridLines) {
            gridLine.draw(g);
        }
    }

    public void drawAxis(Graphics2D g,  int axisOriginPoint) {
        if(!config.isVisible) {
            return;
        }
        if(axisLine == null) {
            createAxisElements(g, axisOriginPoint);
        }
        g.setColor(config.getTicksColor());
        g.setStroke(new BasicStroke(config.ticksConfig.tickMarkWidth));
        for (Line tickLine : tickLines) {
            tickLine.draw(g);
        }

        g.setColor(config.getLabelsColor());
        g.setFont(config.labelsConfig.textStyle.getFont());
        g.setStroke(new BasicStroke());
        for (Text tickLabel : tickLabels) {
            tickLabel.draw(g);
        }

        if(config.axisLineConfig.isVisible()) {
            g.setColor(config.getAxisLineColor());
            g.setStroke(config.axisLineConfig.getStroke());
            axisLine.draw(g);
        }

        if(config.titleConfig.isVisible) {
            g.setStroke(new BasicStroke());
            g.setColor(config.getTitleColor());
            g.setFont(config.titleConfig.textStyle.getFont());
            axisName.draw(g);
        }
    }

    private TickProvider getTickProvider() {
        if(tickProvider != null) {
            return tickProvider;
        }
        TickProvider tickProvider =  scale.getTickProvider();
        tickProvider.setLabelFormatInfo(config.labelsConfig.formatInfo);
        if(config.ticksConfig.tickStep > 0) {
            tickProvider.setTickStep(config.ticksConfig.tickStep);
        } else if(config.ticksConfig.tickPixelInterval > 0) {
            int tickAmount = Math.abs(getEnd() - getStart())/config.ticksConfig.tickPixelInterval;
            tickProvider.setTickAmount(tickAmount);
        }
        this.tickProvider = tickProvider;
        return tickProvider;
    }


    private Line tickToGridLine(Double tickValue, int axisOriginPoint, int length) {
        if(config.isTop()) {
            int x = (int)scale(tickValue);
            int y1 = axisOriginPoint;
            int y2 = axisOriginPoint + length;
            return new Line(x, y1, x, y2);
        }
        if(config.isBottom()) {
            int x = (int)scale(tickValue);
            int y1 = axisOriginPoint;
            int y2 = axisOriginPoint - length;
            return new Line(x, y1, x, y2);
        }
        if(config.isLeft()) {
            int y = (int)scale(tickValue);
            int x1 = axisOriginPoint;
            int x2 = axisOriginPoint + length;
            return new Line(x1, y, x2, y);
        }
        // if config.isRight()
        int y = (int)scale(tickValue);
        int x1 = axisOriginPoint;
        int x2 = axisOriginPoint - length;
        return new Line(x1, y, x2, y);
    }

    private Line tickToMarkLine(Tick tick, int axisOriginPoint) {
        int axisWidth = config.axisLineConfig.width;
        if(config.isTop()) {
            int x = (int)scale(tick.getValue());
            int y1 = axisOriginPoint - axisWidth / 2 - config.ticksConfig.tickMarkOutsideSize;
            int y2 = axisOriginPoint + axisWidth / 2 + config.ticksConfig.tickMarkInsideSize ;
            return new Line(x, y1, x, y2);
        }
        if(config.isBottom()) {
            int x = (int)scale(tick.getValue());
            int y1 = axisOriginPoint + axisWidth / 2 + config.ticksConfig.tickMarkOutsideSize;
            int y2 = axisOriginPoint - axisWidth / 2 - config.ticksConfig.tickMarkInsideSize ;
            return new Line(x, y1, x, y2);
        }
        if(config.isLeft()) {
            int y = (int)scale(tick.getValue());
            int x1 = axisOriginPoint - axisWidth / 2 - config.ticksConfig.tickMarkOutsideSize;
            int x2 = axisOriginPoint + axisWidth / 2 + config.ticksConfig.tickMarkInsideSize ;
            return new Line(x1, y, x2, y);
        }
        // if config.isRight()
        int y = (int)scale(tick.getValue());
        int x1 = axisOriginPoint + axisWidth / 2 + config.ticksConfig.tickMarkOutsideSize;
        int x2 = axisOriginPoint - axisWidth / 2 - config.ticksConfig.tickMarkInsideSize ;
        return new Line(x1, y, x2, y);
    }

    private Text tickToLabel(Tick tick, int axisOriginPoint, FontMetrics fm) {
        int axisWidth = config.axisLineConfig.width;
        int labelPadding = config.labelsConfig.padding;
        if(config.isTop()) {
            int x = (int)scale(tick.getValue());
            int y = axisOriginPoint - axisWidth / 2 - config.ticksConfig.tickMarkOutsideSize - labelPadding;
            return new Text(tick.getLabel(), x, y, TextAnchor.MIDDLE, TextAnchor.START, fm);
        }
        if(config.isBottom()) {
            int x = (int)scale(tick.getValue());
            int y = axisOriginPoint + axisWidth / 2 + config.ticksConfig.tickMarkOutsideSize + labelPadding;
            return new Text(tick.getLabel(), x, y, TextAnchor.MIDDLE, TextAnchor.END, fm);

        }
        if(config.isLeft()) {
            int y = (int)scale(tick.getValue());
            int x = axisOriginPoint - axisWidth / 2 - config.ticksConfig.tickMarkInsideSize - labelPadding;
            return new Text(tick.getLabel(), x, y, TextAnchor.END, TextAnchor.MIDDLE, fm);
        }
        // if config.isRight()
        int y = (int)scale(tick.getValue());
        int x = axisOriginPoint + axisWidth / 2 + config.ticksConfig.tickMarkInsideSize + labelPadding;
        return new Text(tick.getLabel(), x, y, TextAnchor.START, TextAnchor.MIDDLE, fm);
    }

    private void fixTicksOverlapping(FontMetrics fm) {
        if(!config.labelsConfig.isVisible) {
            isTicksOveralppingFixed = true;
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
        double labelSpace = 1 * config.labelsConfig.textStyle.fontSize;
        double requiredSpace = labelsSize + labelSpace;
        int ticksDivider = (int) (requiredSpace / tickPixelInterval) + 1;
        ticks = getTickProvider().getTicks(ticksDivider);
        minorTicks = getTickProvider().getMinorTicks(ticksDivider, config.minorGridCounter);
        isTicksOveralppingFixed = true;
    }


    private void createAxisElements(Graphics2D g, int axisOriginPoint) {
        if(ticks == null) {
            ticks = getTickProvider().getTicks(1);
        }
        FontMetrics fm = g.getFontMetrics(config.labelsConfig.textStyle.getFont());
        if(!isTicksOveralppingFixed) {
            fixTicksOverlapping(fm);
        }
        // tick labels
        tickLines = new ArrayList<Line>();
        if(config.ticksConfig.isTickMarksVisible()) {
            for (Tick tick : ticks) {
                tickLines.add(tickToMarkLine(tick, axisOriginPoint));
            }
        }

        // tick lines
        tickLabels = new ArrayList<Text>();
        if(config.labelsConfig.isVisible) {
            for (Tick tick : ticks) {
                tickLabels.add(tickToLabel(tick, axisOriginPoint, fm));
            }
        }

        // axis line
        if (config.isHorizontal()) {
            axisLine = new Line(getStart(), axisOriginPoint, getEnd(), axisOriginPoint);
        } else {
            axisLine = new Line(axisOriginPoint, getStart(), axisOriginPoint, getEnd());
        }

        // axis title
        int axisNameDistance = 0;
        if(config.axisLineConfig.isVisible()) {
            axisNameDistance += config.axisLineConfig.width / 2;
        }
        if (config.ticksConfig.isTickMarksVisible()) {
            axisNameDistance += config.ticksConfig.tickMarkOutsideSize;
        }
        if(config.labelsConfig.isVisible) {
            int labelsSize = 0;
            if(config.isHorizontal()) {
                labelsSize = fm.getAscent();
            } else {
                labelsSize = getMaxTickLabelsWidth(fm, ticks);
            }
            axisNameDistance += (labelsSize + config.labelsConfig.padding);
        }
        axisNameDistance += config.titleConfig.padding;
        fm = g.getFontMetrics(config.titleConfig.textStyle.getFont());
        if(config.isTop()) {
            int y = axisOriginPoint - axisNameDistance;
            int x = (getEnd() + getStart()) / 2;
            axisName = new Text(config.title, x, y, TextAnchor.MIDDLE, TextAnchor.START, fm);
        }
        if(config.isBottom()) {
            int y = axisOriginPoint + axisNameDistance;
            int x = (getEnd() + getStart()) / 2;
            axisName = new Text(config.title, x, y, TextAnchor.MIDDLE, TextAnchor.END, fm);
        }
        if(config.isLeft()) {
            int x = axisOriginPoint - axisNameDistance;
            int y = (getEnd() + getStart()) / 2;
            axisName = new Text(config.title, x, y, TextAnchor.START, TextAnchor.MIDDLE, -90, fm);
        }
        if(config.isRight()) {
            int x = axisOriginPoint + axisNameDistance;
            int y = (getEnd() + getStart()) / 2;
            axisName = new Text(config.title, x, y, TextAnchor.START, TextAnchor.MIDDLE, +90, fm);
        }
    }

    private void createGridLines(Graphics2D g, int axisOriginPoint, int length) {
        if(ticks == null) {
            ticks = getTickProvider().getTicks(1);
        }
        FontMetrics fm = g.getFontMetrics(config.labelsConfig.textStyle.getFont());
        if(!isTicksOveralppingFixed) {
            fixTicksOverlapping(fm);
        }
        gridLines = new ArrayList<Line>();
        minorGridLines = new ArrayList<Line>();
        if(config.gridLineConfig.isVisible()) {
            for (Tick tick : ticks) {
                gridLines.add(tickToGridLine(tick.getValue(), axisOriginPoint, length));
            }
        }
        if(config.minorGridLineConfig.isVisible()) {
            for (Double minorTick : minorTicks) {
                minorGridLines.add(tickToGridLine(minorTick, axisOriginPoint, length));
            }
        }
    }

    private int getMaxTickLabelsWidth(FontMetrics fm, List<Tick> ticks) {
        int maxSize = 0;
        for (Tick tick : ticks) {
            maxSize = Math.max(maxSize, fm.stringWidth(tick.getLabel()));
        }
        return maxSize;
    }

}

