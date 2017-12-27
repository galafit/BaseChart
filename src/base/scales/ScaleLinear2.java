package base.scales;

import base.config.axis.LabelFormatInfo;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by galafit on 20/12/17.
 */
public class ScaleLinear2 extends Scale2 {
    @Override
    public double scale(double value) {
        return range[0] + (value - domain[0]) * (range[range.length - 1] - range[0]) / (domain[domain.length - 1] - domain[0]);
    }

    @Override
    public double invert(double value) {
        return domain[0] + (value - range[0]) * (domain[domain.length - 1] - domain[0]) / (range[range.length - 1] - range[0]);
    }

    /**
     * On the base of ticks amount calculate round Tick Step
     * that is  multiples of 2, 5 or 10.
     * FirstDigit is in {1,2,5,10};
     */
    @Override
    public double getTickStep(int tickCount)  {
        if(tickCount <= 1) {
            String errMsg = MessageFormat.format("Invalid ticks tickCount: {0}. Expected >= 2", tickCount);
            throw new IllegalArgumentException(errMsg);
        }
        double max = domain[domain.length - 1];
        double min = domain[0];
        double step = (max - min) / (tickCount - 1);
        NormalizedNumber normalizedStep = new NormalizedNumber(step);

        int power = normalizedStep.getPower();
        int firstDigit = (int)Math.round(normalizedStep.getMantissa());
        switch (firstDigit) {
            case 3:
                firstDigit = 2;
                break;
            case 4:
                firstDigit = 5;
                break;
            case 6:
                firstDigit = 5;
                break;
            case 7:
                firstDigit = 5;
                break;
            case 8:
                firstDigit = 1;
                power++;
                break;
            case 9:
                firstDigit = 1;
                power++;
                break;
        }
        return new NormalizedNumber(firstDigit, power).getValue();
    }

    @Override
    public List<Tick> getTicks(double tickStep, int tickDivider, boolean isRoundingEnable, LabelFormatInfo labelFormatInfo) {
        NormalizedNumber step = new NormalizedNumber(tickStep);
        DecimalFormat tickFormat = getNumberFormat(step.getPower(), labelFormatInfo);
        ArrayList<Tick> ticks = new ArrayList<Tick>();
        int maxTicksAmount = 500; // if bigger it means that there is some error
        double min = domain[0];
        double max = domain[domain.length - 1];

        if(! isRoundingEnable) {
            double tickValue = getUpperTick(min, tickStep * tickDivider);
            for (int i = 0; i < maxTicksAmount; i++) {
                if(tickValue <= max) {
                    ticks.add(new Tick(tickValue, tickFormat.format(tickValue)));
                } else {
                    break;
                }
                tickValue += tickStep * tickDivider;
            }
            return ticks;
        }

        if(isRoundingEnable) {
            double roundMin = getLowerTick(min, tickStep);
            double roundMax = getUpperTick(max, tickStep);
            double bigStepCounter = (roundMax - roundMin) / (tickStep * tickDivider);

            if(bigStepCounter < 3) {
                ticks.add(new Tick(roundMin, tickFormat.format(roundMin)));
                ticks.add(new Tick(roundMax, tickFormat.format(roundMax)));
            } else {
                double tickValue = roundMin;
                if(bigStepCounter - (int) bigStepCounter > 0) {
                    bigStepCounter++;
                }
                roundMax = roundMin + (int)bigStepCounter * tickDivider * tickStep;
                for (int i = 0; i < maxTicksAmount; i++) {
                    if(tickValue <= roundMax) {
                        ticks.add(new Tick(tickValue, tickFormat.format(tickValue)));
                    } else {
                        break;
                    }
                    tickValue += tickStep * tickDivider;
                }
            }
        }
        return ticks;
    }

    private double getUpperTick(double value, double tickStep) {
        return Math.ceil(value / tickStep) * tickStep;
    }

    private double getLowerTick(double value, double tickStep) {
        return Math.floor(value / tickStep) * tickStep;
    }

    // TODO: use metric shortcuts - k, M, G... from formatInfo
    private DecimalFormat getNumberFormat(int power, LabelFormatInfo labelFormatInfo) {
        DecimalFormat dfNeg4 = new DecimalFormat("0.0000");
        DecimalFormat dfNeg3 = new DecimalFormat("0.000");
        DecimalFormat dfNeg2 = new DecimalFormat("0.00");
        DecimalFormat dfNeg1 = new DecimalFormat("0.0");
        DecimalFormat df0 = new DecimalFormat("#,##0");

        DecimalFormat df = new DecimalFormat("#.######E0");

        if (power == -4) {
            df = dfNeg4;
        }
        if (power == -3) {
            df = dfNeg3;
        }
        if (power == -2) {
            df = dfNeg2;
        }
        if (power == -1) {
            df = dfNeg1;
        }
        if (power >= 0 && power <= 6) {
            df = df0;
        }

        String formatPattern = df.toPattern();
        if (labelFormatInfo != null && labelFormatInfo.prefix != null) {
            formatPattern = labelFormatInfo.prefix + " " + formatPattern;
        }
        if (labelFormatInfo != null && labelFormatInfo.suffix != null) {
            formatPattern = formatPattern + " " + labelFormatInfo.suffix;

        }
        df = new DecimalFormat(formatPattern);
        return df;
    }
}