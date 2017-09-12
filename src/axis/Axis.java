package axis;

import configuration.AxisConfig;
import configuration.TextAnchor;
import painters.Line;
import painters.Text;

import java.awt.*;
import java.awt.geom.AffineTransform;
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

    public boolean isOpposite() {
        if(config.isTop() || config.isRight()) {
            return true;
        }
        return false;
    }

    public boolean isAutoScale() {
        return config.isAutoScale;
    }

    public void setAutoScale(boolean isAutoScale) {
        config.isAutoScale = isAutoScale;
    }

    public void update() {
        ticks = null;
    }

    public void setMinMax(double min, double max) {
        if (min > max){
            String errorMessage = "Error during setMinMax(). Expected Min < Max. Min = {0}, Max = {1}.";
            String formattedError = MessageFormat.format(errorMessage,min,max);
            throw new IllegalArgumentException(formattedError);
        }
        scale.setDomain(min, max);
    }

    public void setStartEnd(int start, int end) {
        scale.setRange(start, end);
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

    public int getWidth(Graphics2D g2) {
        if (!config.isAxisVisible) {
            return 0;
        }

        int size = 0;
        if(config.linesConfig.isAxisLineVisible()) {
            size += config.linesConfig.axisLineWidth / 2;
        }

        if (config.ticksConfig.isTickMarksVisible()) {
            size += config.ticksConfig.tickMarkOutsideSize;
        }

        List<Tick> ticks = getTicks();
        if(config.ticksConfig.isLabelsVisible) {
            int labelsSize = 0;
            FontMetrics fm = g2.getFontMetrics(config.ticksConfig.textStyle.getFont());
            if(config.isHorizontal()) {
                labelsSize = fm.getHeight();
            } else {
                labelsSize = getMaxTickLabelsWidth(fm, ticks);
            }
            size += config.ticksConfig.padding + labelsSize;
        }
        if (config.isNameVisible) {
            FontMetrics fm = g2.getFontMetrics(config.nameTextStyle.getFont());
            size = size + config.namePadding + fm.getHeight();
        }
        return size;
    }

    public void drawGrid(Graphics2D g,  int axisOriginPoint, int length) {
        if(gridLines == null) {
            createGridLines(g, axisOriginPoint, length);
        }
        g.setColor(config.linesConfig.minorGridColor);
        g.setStroke(config.linesConfig.getMinorGridLineStroke());
        for (Line minorGridLine : minorGridLines) {
            minorGridLine.draw(g);
        }
        g.setColor(config.linesConfig.gridColor);
        g.setStroke(config.linesConfig.getGridLineStroke());
        for (Line gridLine : gridLines) {
            gridLine.draw(g);
        }
    }

    public void drawAxis(Graphics2D g,  int axisOriginPoint) {
        if(!config.isAxisVisible) {
            return;
        }
        if(axisLine == null) {
            createAxisElements(g, axisOriginPoint);
        }
        g.setColor(config.linesConfig.axisLineColor);
        g.setStroke(new BasicStroke(config.ticksConfig.tickMarkWidth));
        for (Line tickLine : tickLines) {
            tickLine.draw(g);
        }

        g.setFont(config.ticksConfig.textStyle.getFont());
        g.setStroke(new BasicStroke());
        for (Text tickLabel : tickLabels) {
            tickLabel.draw(g);
        }

        if (config.linesConfig.isAxisLineVisible()) {
            if(config.linesConfig.isAxisLineVisible()) {
                g.setColor(config.linesConfig.axisLineColor);
                g.setStroke(config.linesConfig.getAxisLineStroke());
                axisLine.draw(g);
            }

        }
        if(config.isNameVisible) {
            drawName(g,  axisOriginPoint, ticks);
        }
    }

    private TickProvider getTickProvider() {
        if(tickProvider != null) {
            return tickProvider;
        }
        TickProvider tickProvider =  scale.getTickProvider();
        tickProvider.setTickFormatInfo(config.ticksConfig.formatInfo);
        if(config.ticksConfig.tickStep > 0) {
            tickProvider.setTickStep(config.ticksConfig.tickStep);
        } else if(config.ticksConfig.tickPixelInterval > 0) {
            int tickAmount = Math.abs(getEnd() - getStart())/config.ticksConfig.tickPixelInterval;
            tickProvider.setTickAmount(tickAmount);
        }
        this.tickProvider = tickProvider;
        return tickProvider;
    }

    public List<Tick> getTicks() {
        if(ticks == null) {
            ticks = getTickProvider().getTicks(1);
        }
        return ticks;
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
        if(config.isTop()) {
            int x = (int)scale(tick.getValue());
            int y1 = axisOriginPoint - config.linesConfig.axisLineWidth / 2 - config.ticksConfig.tickMarkOutsideSize;
            int y2 = axisOriginPoint + config.linesConfig.axisLineWidth / 2 + config.ticksConfig.tickMarkInsideSize ;
            return new Line(x, y1, x, y2);
        }
        if(config.isBottom()) {
            int x = (int)scale(tick.getValue());
            int y1 = axisOriginPoint + config.linesConfig.axisLineWidth / 2 + config.ticksConfig.tickMarkOutsideSize;
            int y2 = axisOriginPoint - config.linesConfig.axisLineWidth / 2 - config.ticksConfig.tickMarkInsideSize ;
            return new Line(x, y1, x, y2);
        }
        if(config.isLeft()) {
            int y = (int)scale(tick.getValue());
            int x1 = axisOriginPoint - config.linesConfig.axisLineWidth / 2 - config.ticksConfig.tickMarkOutsideSize;
            int x2 = axisOriginPoint + config.linesConfig.axisLineWidth / 2 + config.ticksConfig.tickMarkInsideSize ;
            return new Line(x1, y, x2, y);
        }
        // if config.isRight()
        int y = (int)scale(tick.getValue());
        int x1 = axisOriginPoint + config.linesConfig.axisLineWidth / 2 + config.ticksConfig.tickMarkOutsideSize;
        int x2 = axisOriginPoint - config.linesConfig.axisLineWidth / 2 - config.ticksConfig.tickMarkInsideSize ;
        return new Line(x1, y, x2, y);
    }

    private Text tickToLabel(Tick tick, int axisOriginPoint, FontMetrics fm) {
        if(config.isTop()) {
            int x = (int)scale(tick.getValue());
            int y = axisOriginPoint - config.linesConfig.axisLineWidth / 2 - config.ticksConfig.tickMarkOutsideSize - config.ticksConfig.padding;
            return new Text(tick.getLabel(), x, y, TextAnchor.MIDDLE, TextAnchor.START, fm);
        }
        if(config.isBottom()) {
            int x = (int)scale(tick.getValue());
            int y = axisOriginPoint + config.linesConfig.axisLineWidth / 2 + config.ticksConfig.tickMarkOutsideSize + config.ticksConfig.padding;
            return new Text(tick.getLabel(), x, y, TextAnchor.MIDDLE, TextAnchor.END, fm);

        }
        if(config.isLeft()) {
            int y = (int)scale(tick.getValue());
            int x = axisOriginPoint - config.linesConfig.axisLineWidth / 2 - config.ticksConfig.tickMarkInsideSize - config.ticksConfig.padding;
            return new Text(tick.getLabel(), x, y, TextAnchor.END, TextAnchor.MIDDLE, fm);
        }
        // if config.isRight()
        int y = (int)scale(tick.getValue());
        int x = axisOriginPoint + config.linesConfig.axisLineWidth / 2 + config.ticksConfig.tickMarkInsideSize + config.ticksConfig.padding;
        return new Text(tick.getLabel(), x, y, TextAnchor.START, TextAnchor.MIDDLE, fm);
    }

    private void fixTicksOverlapping(FontMetrics fm) {
        if(!config.ticksConfig.isLabelsVisible) {
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
        double labelSpace = 1 * config.ticksConfig.textStyle.fontSize;
        double requiredSpace = labelsSize + labelSpace;
        int ticksDivider = (int) (requiredSpace / tickPixelInterval) + 1;
        ticks = getTickProvider().getTicks(ticksDivider);
        minorTicks = getTickProvider().getMinorTicks(ticksDivider, config.linesConfig.minorGridCounter);
        isTicksOveralppingFixed = true;
    }


    private void createAxisElements(Graphics2D g, int axisOriginPoint) {
        FontMetrics fm = g.getFontMetrics(config.ticksConfig.textStyle.getFont());
        if(!isTicksOveralppingFixed) {
            fixTicksOverlapping(fm);
        }
        // tick labels
        tickLines = new ArrayList<Line>();
        if(config.ticksConfig.isTickMarksVisible()) {
            for (Tick tick : getTicks()) {
                tickLines.add(tickToMarkLine(tick, axisOriginPoint));
            }
        }

        // tick lines
        tickLabels = new ArrayList<Text>();
        if(config.ticksConfig.isLabelsVisible) {
            for (Tick tick : getTicks()) {
                tickLabels.add(tickToLabel(tick, axisOriginPoint, fm));
            }
        }

        // axis line
        if (config.isHorizontal()) {
            axisLine = new Line(getStart(), axisOriginPoint, getEnd(), axisOriginPoint);
        } else {
            axisLine = new Line(axisOriginPoint, getStart(), axisOriginPoint, getEnd());
        }

        // axis name
        int namePosition = 0;
        if(config.linesConfig.isAxisLineVisible()) {
            namePosition += config.linesConfig.axisLineWidth / 2;
        }
        if (config.ticksConfig.isTickMarksVisible()) {
            namePosition += config.ticksConfig.tickMarkOutsideSize;
        }
        if(config.ticksConfig.isLabelsVisible) {
            int labelsSize = 0;
            if(config.isHorizontal()) {
                labelsSize = fm.getHeight();
            } else {
                labelsSize = getMaxTickLabelsWidth(fm, ticks);
            }
            namePosition += labelsSize;
        }
        namePosition += config.namePadding;
        fm = g.getFontMetrics(config.nameTextStyle.getFont());
        if(config.isTop()) {
            int x = axisOriginPoint - namePosition;
            int y = (getEnd() - getStart()) / 2;
            axisName = new Text(config.name, x, y, TextAnchor.START, TextAnchor.MIDDLE, fm);
        }
        if(config.isBottom()) {
            int x = axisOriginPoint + namePosition;
            int y = (getEnd() - getStart()) / 2;
            axisName = new Text(config.name, x, y, TextAnchor.END, TextAnchor.MIDDLE, fm);
        }

    }

    private void createGridLines(Graphics2D g, int axisOriginPoint, int length) {
        FontMetrics fm = g.getFontMetrics(config.ticksConfig.textStyle.getFont());
        if(!isTicksOveralppingFixed) {
            fixTicksOverlapping(fm);
        }
        gridLines = new ArrayList<Line>();
        minorGridLines = new ArrayList<Line>();
        if(config.linesConfig.isGridVisible()) {
            for (Tick tick : ticks) {
                gridLines.add(tickToGridLine(tick.getValue(), axisOriginPoint, length));
            }
        }
        if(config.linesConfig.isMinorGridVisible()) {
            for (Double minorTick : minorTicks) {
                minorGridLines.add(tickToGridLine(minorTick, axisOriginPoint, length));
            }
        }
    }

    private void drawName(Graphics2D g,  int axisOriginPoint, List<Tick> ticks) {
        g.setColor(config.linesConfig.axisLineColor);
        g.setFont(config.nameTextStyle.getFont());

        int namePosition = 0;
        if(config.linesConfig.isAxisLineVisible()) {
            namePosition += config.linesConfig.axisLineWidth;
        }
        if (config.ticksConfig.isTickMarksVisible()) {
            namePosition += config.ticksConfig.tickMarkOutsideSize;
        }
        if(config.ticksConfig.isLabelsVisible) {
           // namePosition += config.ticksConfig.padding;
            int labelsSize = 0;
            FontMetrics fm = g.getFontMetrics(config.ticksConfig.textStyle.getFont());
            if(config.isHorizontal()) {
                labelsSize = fm.getHeight();
            } else {
                labelsSize = getMaxTickLabelsWidth(fm, ticks);
            }
            namePosition += labelsSize;
        }
        namePosition += config.namePadding;
        FontMetrics fm = g.getFontMetrics(config.nameTextStyle.getFont());
        int nameWidth = fm.stringWidth(config.name);
        if (config.isHorizontal()) {
            if (config.isTop()) {
                g.drawString(config.name, (getStart() + getEnd()) / 2 - nameWidth / 2, axisOriginPoint - namePosition);
            } else {
                namePosition += fm.getAscent();
                g.drawString(config.name, (getStart() + getEnd()) / 2 - nameWidth / 2, axisOriginPoint + namePosition);
            }
        } else {
            if (config.isLeft()) {
                AffineTransform transform = new AffineTransform();
                transform.rotate(Math.toRadians(-90));
                AffineTransform defaultTransform = g.getTransform();
                g.setTransform(transform);
                int nameX = axisOriginPoint - namePosition;
                int nameY = (getStart() + getEnd()) / 2 + nameWidth / 2;
                g.drawString(config.name, -nameY, nameX);
                g.setTransform(defaultTransform);
            } else {
                AffineTransform transform = new AffineTransform();
                transform.rotate(Math.toRadians(+90));
                AffineTransform defaultTransform = g.getTransform();
                g.setTransform(transform);
                int nameX = axisOriginPoint + namePosition;
                int nameY = (getStart() + getEnd()) / 2 - nameWidth / 2;
                g.drawString(config.name, nameY,  - nameX);
                g.setTransform(defaultTransform);
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

