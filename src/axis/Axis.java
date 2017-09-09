package axis;

import configuration.AxisConfig;
import configuration.Orientation;

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
    private int width;

    public Axis(AxisConfig config) {
        this.config = config;
        scale = new ScaleLinear();
    }

    public boolean isOpposite() {
        if(config.getOrientation() == Orientation.TOP || config.getOrientation() == Orientation.RIGHT) {
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

    public void setConfig(AxisConfig config) {
        this.config = config;
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
            size += config.linesConfig.axisLineWidth;
        }

        List<Tick> ticks = getTicks(g2);

        if (config.ticksConfig.isTickMarksVisible()) {
            size += config.ticksConfig.tickMarkOutsideSize;
        }
        if(config.ticksConfig.isLabelsVisible) {
            int labelsSize = 0;
            if(config.isHorizontal()) {
                labelsSize = getStringHeight(g2, config.ticksConfig.textStyle.getFont());
            } else {
                labelsSize = getMaxTickLabelsWidth(g2, config.ticksConfig.textStyle.getFont(), ticks);
            }
            size += config.ticksConfig.padding + labelsSize;
        }

        if (config.isNameVisible) {
            size = size + config.namePadding + getStringHeight(g2, config.nameTextStyle.getFont());
        }
        width = size;

        return size;
    }

    public void update() {
        ticks = null;
    }


    public void draw(Graphics2D g,  int axisOriginPoint) {

        if(config.background != null) {
            int length = Math.abs(getEnd() - getStart());
            g.setColor(config.background);
            if(!config.isHorizontal()) {
                if(isOpposite()) {
                    g.fillRect(axisOriginPoint, getEnd(), width, length);
                } else {
                    g.fillRect(axisOriginPoint - width, getEnd(), width, length);
                }
            } else {
                if(isOpposite()) {
                    g.fillRect(getStart(), axisOriginPoint - width,  length, width);
                } else {
                    g.fillRect(getStart(), axisOriginPoint, length, width);
                }
            }
        }


        if (config.isAxisVisible) {
            if(ticks == null) {
                ticks = getTicks(g);
            }

            if (config.linesConfig.isMinorGridVisible()) {
                drawMinorGrid(g, ticks);
            }

            if (config.linesConfig.isGridVisible()) {
                drawGrid(g, ticks);
            }

            if (config.ticksConfig.isTickMarksVisible()) {
                drawTicks(g, axisOriginPoint, ticks);
            }

            if (config.ticksConfig.isLabelsVisible) {
                drawLabels(g, axisOriginPoint, ticks);
            }

            if (config.linesConfig.isAxisLineVisible()) {
                drawAxisLine(g, axisOriginPoint);
            }

            if(config.isNameVisible) {
                drawName(g,  axisOriginPoint, ticks);
            }
        }
    }


    private List<Tick> createTicksList(TickProvider tickProvider) {
        List<Tick> ticks = new ArrayList<Tick>();
        int maxTicksAmount = 500;
        for (int i = 0; i < maxTicksAmount; i++) {
            Tick tick = tickProvider.getNext();
            ticks.add(tick);
            if(tick.getValue() >= getMax()) {
                break;
            }
        }
        return ticks;
    }

    public List<Tick> getTicks(Graphics2D g) {
        TickProvider tickProvider =  scale.getTickProvider();
        tickProvider.setTickFormatInfo(config.ticksConfig.formatInfo);
        List<Tick> ticks = createTicksList(tickProvider);
        int labelsSize;
        if(ticks.size() > 1) {
            if(config.isHorizontal()) {
                labelsSize = getMaxTickLabelsWidth(g, config.ticksConfig.textStyle.getFont(), ticks);
            } else {
                labelsSize = getStringHeight(g, config.ticksConfig.textStyle.getFont());
            }
            double tickPixelInterval = Math.abs(scale(ticks.get(1).getValue()) - scale(ticks.get(0).getValue()));
            // min space between labels = 1 symbols size (roughly)
            double labelSpace = 1 * config.ticksConfig.textStyle.fontSize;
            double requiredSpace = labelsSize + labelSpace;


            int n = (int) (requiredSpace / tickPixelInterval) + 1;

            if(ticks.size() / n > 2) {
                List<Tick> newTicks = new ArrayList<Tick>();
                for (int i = 0; i < ticks.size(); i++) {
                    if(i%n == 0) {
                        newTicks.add(ticks.get(i));
                    }

                }
                if(ticks.size() % n != 0) {
                    newTicks.add(ticks.get(ticks.size() - 1));
                }
                ticks = newTicks;
            } else {
                List<Tick> newTicks = new ArrayList<Tick>();
                newTicks.add(ticks.get(0));
                newTicks.add(ticks.get(ticks.size() - 1));
                if(!config.isRoundingEnabled) {
                    if(getMin() != ticks.get(0).getValue()) {
                        newTicks.add(ticks.get(1));
                    }
                    if(getMax() != ticks.get(ticks.size() - 1).getValue()) {
                        newTicks.add(ticks.get(ticks.size() - 2));
                    }

                }

                ticks = newTicks;
            }
        }

        return ticks;

    }

    private void drawAxisLine(Graphics2D g, int axisOriginPoint) {
        g.setColor(config.linesConfig.axisLineColor);
        Stroke defaultStroke = g.getStroke();
        g.setStroke(new BasicStroke(config.linesConfig.axisLineWidth));
        if (config.isHorizontal()) {
            if(config.getOrientation() == Orientation.TOP) {
                axisOriginPoint -= config.linesConfig.axisLineWidth/2;
            }
            else {
                axisOriginPoint += config.linesConfig.axisLineWidth/2;
            }
            g.drawLine(getStart(), axisOriginPoint, getEnd(), axisOriginPoint);
        } else {
            if(config.getOrientation() == Orientation.RIGHT) {
                axisOriginPoint += config.linesConfig.axisLineWidth/2;
            }
            else {
                axisOriginPoint -= config.linesConfig.axisLineWidth/2;
            }
            g.drawLine(axisOriginPoint, getStart(), axisOriginPoint, getEnd());
        }
        g.setStroke(defaultStroke);
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
            if(config.isHorizontal()) {
                labelsSize = getStringHeight(g, config.ticksConfig.textStyle.getFont());
            } else {
                labelsSize = getMaxTickLabelsWidth(g, config.ticksConfig.textStyle.getFont(), ticks);
            }
            namePosition += labelsSize;
        }
        namePosition += config.namePadding;
        int nameWidth = getStringWidth(g,config.nameTextStyle.getFont(), config.name);
        if (config.isHorizontal()) {
            if (config.getOrientation() == Orientation.TOP) {
                g.drawString(config.name, (getStart() + getEnd()) / 2 - nameWidth / 2, axisOriginPoint - namePosition);
            } else {
                namePosition += getStringAscent(g, config.nameTextStyle.getFont());
                g.drawString(config.name, (getStart() + getEnd()) / 2 - nameWidth / 2, axisOriginPoint + namePosition);
            }
        } else {
            if (config.getOrientation() == Orientation.LEFT) {
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

    private void drawTicks(Graphics2D g, int axisOriginPoint, List<Tick> ticks) {
        g.setColor(config.linesConfig.axisLineColor);
        for (Tick tick : ticks) {
            if (getMin() <= tick.getValue() && tick.getValue() <= getMax()) {
                int tickPoint = (int) scale(tick.getValue());
                drawTick(g, axisOriginPoint, tickPoint);
            }
        }
    }

    private void drawLabels(Graphics2D g, int axisOriginPoint, List<Tick> ticks) {
        g.setColor(config.linesConfig.axisLineColor);
        Font labelFont = config.ticksConfig.textStyle.getFont();
        g.setFont(labelFont);
        for (Tick tick : ticks) {
            if (getMin() <= tick.getValue() && tick.getValue() <= getMax()) {
                int tickPoint = (int) scale(tick.getValue());
                drawLabel(g, axisOriginPoint, tickPoint, tick.getLabel());
            }
        }
    }

    private void drawGrid(Graphics2D g,  List<Tick> ticks) {
        g.setColor(config.linesConfig.gridColor);
        g.setStroke(config.linesConfig.gridLineStyle.getStroke(config.linesConfig.gridLineWidth));
        for (Tick tick : ticks) {
            if (getMin() <= tick.getValue() && tick.getValue() <= getMax()) {
                if (config.isHorizontal()) {
                  //  g.drawLine((int) invert(tick.getValue()), area.y + 1, (int) invert(tick.getValue()), area.y  + area.height - 1);
                } else {
                  //  g.drawLine(area.x + 1, (int) invert(tick.getValue()), area.x + area.width - 1, (int) invert(tick.getValue(), area));
                }
            }
        }
        g.setStroke(new BasicStroke());

    }

    private void drawMinorGrid(Graphics2D g, List<Tick> ticks) {
        if (ticks.size() > 1) {
            g.setColor(config.linesConfig.minorGridColor);
            g.setStroke(config.linesConfig.minorGridLineStyle.getStroke(config.linesConfig.gridLineWidth));

            double tickInterval = ticks.get(1).getValue() - ticks.get(0).getValue();
            double minorTickInterval = tickInterval / config.linesConfig.minorGridCounter;

            double minorTickValue = ticks.get(0).getValue();
            while (minorTickValue < getMax()) {
                if (getMin() < minorTickValue) {
                    if (config.isHorizontal()) {
                      //  g.drawLine((int)invert(minorTickValue), area.y + 1, (int)invert(minorTickValue), area.y + area.height - 1);
                    } else {
                      //  g.drawLine(area.x + 1, (int)invert(minorTickValue), area.x + area.width - 1, (int)invert(minorTickValue));
                    }
                }
                minorTickValue += minorTickInterval;
            }
            g.setStroke(new BasicStroke());
        }
    }

    private void drawTick(Graphics2D g, int axisOriginPoint, int tickPoint) {
        Stroke defaultStroke = g.getStroke();
        int tickWidth = config.ticksConfig.tickMarkWidth;
        int tickEnd;
        int axisLineWidth = config.linesConfig.axisLineWidth;
        int tickSize = config.ticksConfig.tickMarkOutsideSize;
        if (config.isHorizontal()) {
            if(config.getOrientation() == Orientation.TOP) {
                axisOriginPoint -= axisLineWidth;
                tickEnd = axisOriginPoint - tickSize;
            } else {
                axisOriginPoint += axisLineWidth;
                tickEnd = axisOriginPoint + tickSize  ;
            }
            g.drawLine(tickPoint, axisOriginPoint, tickPoint, tickEnd);
        } else {
            if(config.getOrientation() == Orientation.RIGHT) {
                axisOriginPoint += axisLineWidth;
                tickEnd = axisOriginPoint + tickSize;
            } else {
                axisOriginPoint -= axisLineWidth;
                tickEnd = axisOriginPoint - tickSize;
            }
            g.drawLine(axisOriginPoint, tickPoint, tickEnd, tickPoint);
        }
        g.setStroke(defaultStroke);
    }

    private void drawLabel(Graphics2D g, int axisOriginPoint, int tickPoint, String label) {
        //HORIZONTAL position
        Font font = config.ticksConfig.textStyle.getFont();
        int labelPosition = 0;
        if(config.linesConfig.isAxisLineVisible()) {
            labelPosition += config.linesConfig.axisLineWidth;
        }
        if(config.ticksConfig.isTickMarksVisible()) {
            labelPosition += config.ticksConfig.tickMarkOutsideSize;
        }
        labelPosition += config.ticksConfig.padding;
        if (config.isHorizontal()) {
            // TOP axis_old position
            if (config.getOrientation() == Orientation.TOP) {
                g.drawString(label, tickPoint - getStringWidth(g, font, label) / 2, axisOriginPoint - labelPosition);
            } else { //BOTTOM axis_old position
                labelPosition += getStringAscent(g, font);
                g.drawString(label, tickPoint - getStringWidth(g, font, label) / 2, axisOriginPoint + labelPosition);
            }

        } else { //VERTICAL position
            //RIGTH axis_old position

            int stringHeight = getStringHeight(g,font);
            int shift = stringHeight/2 + 1;
            int labelY = tickPoint - shift + getStringAscent(g, font);
            if (config.getOrientation() == Orientation.RIGHT) {
                g.drawString(label, axisOriginPoint + labelPosition, labelY);
            } else { //LEFT axis_old position
                labelPosition += getStringWidth(g, font, label) + 1;
                g.drawString(label, axisOriginPoint - labelPosition, labelY);
            }
        }
    }

    private int getMaxTickLabelsWidth(Graphics2D g, Font font, List<Tick> ticks) {
        int maxSize = 0;
        for (Tick tick : ticks) {
            maxSize = Math.max(maxSize, getStringWidth(g, font, tick.getLabel()));
        }
        return maxSize;
    }

    private int getStringWidth(Graphics2D g2, Font font, String string) {
        return  g2.getFontMetrics(font).stringWidth(string);
    }

    private int getStringHeight(Graphics2D g2, Font font) {
        return g2.getFontMetrics(font).getHeight();
    }

    private int getStringAscent(Graphics2D g2, Font font) {
        return g2.getFontMetrics(font).getAscent();
    }
}

