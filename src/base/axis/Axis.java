package base.axis;

import base.config.axis.AxisConfig;
import base.config.general.TextAnchor;
import base.Range;
import base.painters.Line;
import base.painters.Text;
import base.scales.*;

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

    protected final int DEFAULT_TICK_COUNT = 10;

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
    private Tick lastMinTick; // need it for continuous axis translate/scroll

    public Axis(AxisConfig config) {
        this.config = config;
        scale = new ScaleLinear();
        tickProvider = getTickProvider();
    }


    /**
     * Zoom affects only max value. Min value does not changed!!!
     * @param zoomFactor
     */
    public void zoom(double zoomFactor) {
        int start = getStart();
        int end = getEnd();
        double min = getMin();
        int shift = (int)((end - start) * (zoomFactor - 1) / 2);
        //int newStart = start - shift;
        int newEnd = end + 2 * shift;
        //setStartEnd(newStart, newEnd);
        setStartEnd(start, newEnd);
       // double minNew = invert(start);
        double maxNew = invert(end);
        setMinMax(min, maxNew);
        setStartEnd(start, end);
    }

    public void translate(int translation) {
        int start = getStart();
        int end = getEnd();
        double minNew = invert(start + translation);
        double maxNew = invert(end + translation);
        setMinMax(minNew, maxNew);
    }

    public void setMinMax(double min, double max) {
        setMinMax(new Range(min, max));
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
        if(min == max) {
            max = min + 1;
        }
        scale.setDomain(min, max);
        ticks = null;
        tickProvider = getTickProvider();
        isDirty = true;
    }

    public void setStartEnd(double start, double end) {
        scale.setRange(start, end);
        tickProvider = getTickProvider();
        ticks = null;
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
        if(config.getLabelsConfig().isVisible()) {
            int labelsSize = 0;
            FontMetrics fm = g2.getFontMetrics(config.getLabelsConfig().getTextStyle().getFont());
            if(config.isHorizontal()) { // horizontal axis
                labelsSize = fm.getHeight();
            } else { // vertical axis
                Tick minTick, maxTick;
                if(config.isMinMaxRoundingEnable()) {
                   minTick = tickProvider.getLowerTick(getMin());
                   maxTick = tickProvider.getUpperTick(getMax());
                } else {
                    minTick = tickProvider.getUpperTick(getMin());
                    maxTick = tickProvider.getLowerTick(getMax());
                }
                labelsSize = Math.max(fm.stringWidth(minTick.getLabel()), fm.stringWidth(maxTick.getLabel()));
            }
            size += config.getLabelsConfig().getPadding() + labelsSize;
        }
        if (config.getTitleConfig().isVisible()) {
            FontMetrics fm = g2.getFontMetrics(config.getTitleConfig().getTextStyle().getFont());
            size = size + config.getTitleConfig().getPadding() + fm.getHeight();
        }
        return size;
    }

    private boolean isHorizontal() {
        return config.isHorizontal();
    }


    private TickProvider getTickProvider() {
        if(config.getTicksConfig().getTickStep() > 0) {
            return  scale.getTickProvider(config.getTicksConfig().getTickStep(), null, config.getLabelsConfig().getFormatInfo());
        }

        int fontFactor = 3;
        if(isHorizontal()) {
            fontFactor = 4;
        }
        double tickPixelInterval = fontFactor * config.getLabelsConfig().getTextStyle().getFontSize();
        int tickCount = (int) (Math.abs(getStart() - getEnd()) / tickPixelInterval);
        tickCount = Math.max(tickCount, DEFAULT_TICK_COUNT);
        return scale.getTickProvider(tickCount, config.getLabelsConfig().getFormatInfo());
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

    private int getTicksDivider(Graphics2D g2, List<Tick> ticks) {
        if(ticks.size() < 2) {
            return 1;
        }
        FontMetrics fm = g2.getFontMetrics();
        double labelsSize;
        if(isHorizontal()) {
            labelsSize = Math.max(fm.stringWidth(ticks.get(0).getLabel()), fm.stringWidth(ticks.get(ticks.size() - 1).getLabel()));
        } else {
            labelsSize = fm.getHeight();
        }

        double tickPixelInterval = Math.abs(scale(ticks.get(0).getValue()) - scale(ticks.get(1).getValue()));
        // min space between labels = 1 symbols size (roughly)
        double labelSpace = 1 * config.getLabelsConfig().getTextStyle().getFontSize();
        double requiredSpace = labelsSize + labelSpace;
        int ticksDivider = (int) (requiredSpace / tickPixelInterval) + 1;
        return ticksDivider;
    }

    private List<Tick> getTicks(Graphics2D g2) {
        ArrayList<Tick> allTicks = new ArrayList<Tick>();
        int maxTicksAmount = 500; // if bigger it means that there is some error

        Tick tick = tickProvider.getLowerTick(getMin());
        for (int i = 0; i < maxTicksAmount; i++) {
            if(tick.getValue() < getMax()) {
                allTicks.add(tick);
                tick = tickProvider.getNextTick();
            } else {
                break;
            }
        }
        allTicks.add(tick);

        int tickDivider = getTicksDivider(g2, allTicks);


        int MIN_TICK_NUMBER = 3;
        // если есть округление и тиков мало то оставляем только первый и последний
        if(config.isMinMaxRoundingEnable() && (allTicks.size() - 1) / tickDivider < MIN_TICK_NUMBER) {
            ArrayList<Tick> resultantTicks = new ArrayList<Tick>(2);
            resultantTicks.add(allTicks.get(0));
            resultantTicks.add(allTicks.get(allTicks.size() - 1));
            scale.setDomain(resultantTicks.get(0).getValue(), resultantTicks.get(resultantTicks.size() - 1).getValue());
            return resultantTicks;
        }

        // если нет округления то добавляем вначале тики для их непрерывности при translate/scroll
        if(!config.isMinMaxRoundingEnable()) {
            int shift = 0;
            if(lastMinTick != null) {
                if(lastMinTick.getValue() < getMin()) {
                    tick = tickProvider.getLowerTick(lastMinTick.getValue());
                    while (tick.getValue() <= getMin()) {
                        shift++;
                        tick = tickProvider.getNextTick();
                    }
                }
                if(lastMinTick.getValue() > getMin()) {
                    tick = tickProvider.getLowerTick(getMin());
                    while (tick.getValue() <= lastMinTick.getValue()) {
                        shift++;
                        tick = tickProvider.getNextTick();
                    }
                }
                shift = ((shift - 1) % tickDivider);
            }
            tickProvider.getLowerTick(getMin());
            for (int i = 0; i < shift; i++) {
                allTicks.add(0, tickProvider.getPreviousTick());
            }
            lastMinTick = allTicks.get(0);
        }
        // оставляем только тики через tickDivider
        ArrayList<Tick> resultantTicks = new ArrayList<Tick>();
        for (int i = 0; i < allTicks.size(); i++) {
            if(i % tickDivider == 0) {
                resultantTicks.add(allTicks.get(i));
            }
        }

        tickProvider.getLowerTick(getMax());
        if((allTicks.size() - 1) % tickDivider > 0) {
            for (int i = 0; i <= tickDivider - (allTicks.size() -1) % tickDivider; i++) {
                tick = tickProvider.getNextTick();
            }
            resultantTicks.add(tick);
        }
        if(config.isMinMaxRoundingEnable()) {
            scale.setDomain(resultantTicks.get(0).getValue(), resultantTicks.get(resultantTicks.size() - 1).getValue());
        }
         return resultantTicks;
    }

    private void createAxisElements(Graphics2D g) {
        if(ticks == null) {
            ticks = getTicks(g);
        }
        // tick lines
        tickLines = new ArrayList<Line>();
        if(config.getTicksConfig().isTickMarksVisible()) {
            for (Tick tick : ticks) {
                if(tick.getValue() <= getMax() && tick.getValue() >= getMin()) {
                    tickLines.add(tickToMarkLine(tick));
                }
            }
        }

        // tick labels
        FontMetrics fm = g.getFontMetrics(config.getLabelsConfig().getTextStyle().getFont());
        tickLabels = new ArrayList<Text>();
        if(config.getLabelsConfig().isVisible()) {
            for (Tick tick : ticks) {
                if(tick.getValue() <= getMax() && tick.getValue() >= getMin()) {
                    tickLabels.add(tickToLabel(tick, fm));
                }
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
        if(ticks == null) {
            ticks = getTicks(g);
        }
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
                if(tick.getValue() <= getMax() && tick.getValue() >= getMin()) {
                    tickToGridLine(tick.getValue(), length).draw(g);
                }
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

